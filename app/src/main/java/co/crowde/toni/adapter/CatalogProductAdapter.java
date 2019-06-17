package co.crowde.toni.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.model.CatalogModel;
import co.crowde.toni.view.activity.catalog.CatalogProduct;

public class CatalogProductAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        {

    private Context context;
    private Activity activity ;
    private List<CatalogModel> productModels;
    private List<CatalogModel> productModelsFiltered;
    ArrayList<CatalogModel> arrayList = new ArrayList<>();

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_catalog_item, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress_bar, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final CatalogModel model = productModelsFiltered.get(position);
            ViewHolder viewHolder = (ViewHolder) holder;

            if (model != null) {
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


            }
        } else if (holder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) holder, position);
        }

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

    private class LoadingViewHolder extends ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    @Override
    public int getItemCount() {
        return productModelsFiltered!=null? productModelsFiltered.size():0;
    }

    @Override
    public int getItemViewType(int position) {
        return productModelsFiltered.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }


}
