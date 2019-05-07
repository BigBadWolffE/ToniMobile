package co.crowde.toni.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.model.CatalogModel;
import co.crowde.toni.model.ProductModel;
import co.crowde.toni.view.main.CatalogProduct;

public class CatalogProductAdapter
        extends RecyclerView.Adapter<CatalogProductAdapter.ViewHolder>
        implements Filterable {

    private Context context;
    private Activity activity ;
    private List<CatalogModel> productModels;
    private List<CatalogModel> productModelsFiltered;
    ArrayList<CatalogModel> arrayList = new ArrayList<>();

    private int lastPosition = -1;

    public class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkProduct;
        TextView tvTabProductName, tvTabProductUnit,
                tvTabProductCategory, tvTabProductSupplier;
        LinearLayout layoutProductInventory;

        public ViewHolder(final View itemView) {
            super(itemView);
            tvTabProductName = itemView.findViewById(R.id.tvTabProductName);
            tvTabProductUnit = itemView.findViewById(R.id.tvTabProductUnit);
            tvTabProductCategory = itemView.findViewById(R.id.tvTabProductCategory);
            tvTabProductSupplier = itemView.findViewById(R.id.tvTabProductSupplier);
            checkProduct = itemView.findViewById(R.id.checkProduct);

            layoutProductInventory = itemView.findViewById(R.id.layoutProductInventory);

        }
    }

    public CatalogProductAdapter(Context context,
                                   List<CatalogModel> ProductModelList,
                                   Activity activity) {
        this.productModels = ProductModelList;
        this.productModelsFiltered = ProductModelList;
        this.context = context;
        this.activity = activity;
//        this.listener = listener;
    }

    @Override
    public CatalogProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_catalog_item, parent, false);

        CatalogProductAdapter.ViewHolder mViewHolder = new CatalogProductAdapter.ViewHolder(view);

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(final CatalogProductAdapter.ViewHolder holder, final int position) {
        final CatalogModel model = productModelsFiltered.get(position);
        CatalogProductAdapter.ViewHolder viewHolder = (CatalogProductAdapter.ViewHolder) holder;

        String product = model.getProductName();
        String nama;
        String varian;
        if(product.contains("_")){
            nama = StringUtils.substringBeforeLast(product, "_");
            varian = StringUtils.substringAfterLast(product, "_");
        } else {
            nama = product;
            varian = "-";
        }

        viewHolder.tvTabProductName.setText(nama);
        viewHolder.tvTabProductUnit.setText(varian);
        viewHolder.tvTabProductCategory.setText(String.valueOf(model.getCategoryName()));
        viewHolder.tvTabProductSupplier.setText(model.getSupplierName());
        viewHolder.checkProduct.setOnCheckedChangeListener(null);

        //if true, your checkbox will be selected, else unselected
        viewHolder.checkProduct.setChecked(model.isChecked());

        viewHolder.checkProduct.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    model.setChecked(true);
                    arrayList.add(model);
                    CatalogProduct.setEnabledButton(activity, getCount());

                } else {
                    model.setChecked(false);
                    arrayList.remove(model);
                    CatalogProduct.setEnabledButton(activity, getCount());
                }
            }
        });
        setAnimation(holder.itemView, position);

    }

    @Override
    public int getItemCount() {
        return productModelsFiltered!=null? productModelsFiltered.size():0;
    }

    public int getCount() {
        return arrayList.size();
    }

    public ArrayList<CatalogModel> getSelectList(){
        ArrayList<CatalogModel> list = new ArrayList<>();
        for(int i=0;i<arrayList.size();i++){
            if(arrayList.get(i).isChecked())
                list.add(arrayList.get(i));
        }
        return list;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    productModelsFiltered = productModels;
                } else {
                    List<CatalogModel> filteredList = new ArrayList<>();

                    for (CatalogModel row : productModels) {
                        if (row.getProductName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    productModelsFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = productModelsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productModelsFiltered = (ArrayList<CatalogModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

}
