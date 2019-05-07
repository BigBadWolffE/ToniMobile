package co.crowde.toni.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.model.ProductModel;
import co.crowde.toni.view.popup.InventoryDetailedPopup;

public class ProductInventoryAdapter
        extends RecyclerView.Adapter<ProductInventoryAdapter.ViewHolder>
        implements Filterable {

    private Context context;
    private Activity activity ;
    private List<ProductModel> productModels;
    private List<ProductModel> productModelsFiltered;

    private int lastPosition = -1;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTabProductName, tvTabProductUnit,
                tvTabProductStock, tvTabProductStatus;
        LinearLayout layoutProductInventory;

        public ViewHolder(final View itemView) {
            super(itemView);
            tvTabProductName = itemView.findViewById(R.id.tvTabProductName);
            tvTabProductUnit = itemView.findViewById(R.id.tvTabProductUnit);
            tvTabProductStock = itemView.findViewById(R.id.tvTabProductStock);
            tvTabProductStatus = itemView.findViewById(R.id.tvTabProductStatus);

            layoutProductInventory = itemView.findViewById(R.id.layoutProductInventory);

        }
    }

    public ProductInventoryAdapter(Context context,
                                   List<ProductModel> ProductModelList,
                                   Activity activity) {
        this.productModels = ProductModelList;
        this.productModelsFiltered = ProductModelList;
        this.context = context;
        this.activity = activity;
//        this.listener = listener;
    }

    @Override
    public ProductInventoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_product_inventory_item, parent, false);

        ProductInventoryAdapter.ViewHolder mViewHolder = new ProductInventoryAdapter.ViewHolder(view);

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(final ProductInventoryAdapter.ViewHolder holder, final int position) {
        final ProductModel model = productModelsFiltered.get(position);
        ProductInventoryAdapter.ViewHolder viewHolder = (ProductInventoryAdapter.ViewHolder) holder;

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

        holder.tvTabProductName.setText(nama);
        holder.tvTabProductUnit.setText(varian);
        holder.tvTabProductStock.setText(String.valueOf(model.getStock()));
        holder.tvTabProductStatus.setText(model.getStatus());

        switch (model.getStatus()) {
            case "Habis":
                holder.tvTabProductStatus.setTextColor(
                        activity.getResources().getColor(R.color.status_kosong));
                break;
            case "Mulai habis":
                holder.tvTabProductStatus.setTextColor(
                        activity.getResources().getColor(R.color.status_persediaan_isi));
                break;
            case "Tersedia":
                holder.tvTabProductStatus.setTextColor(
                        activity.getResources().getColor(R.color.status_tersedia));
                break;
            default:
                holder.tvTabProductStatus.setTextColor(
                        activity.getResources().getColor(R.color.status_kosong));
                break;
        }

        holder.layoutProductInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(activity, model.getProductName(), Toast.LENGTH_SHORT).show();
                InventoryDetailedPopup.showPopup(activity, model);
            }
        });

        setAnimation(holder.itemView, position);

    }

    @Override
    public int getItemCount() {
        return productModelsFiltered!=null? productModelsFiltered.size():0;
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
                    List<ProductModel> filteredList = new ArrayList<>();

                    for (ProductModel row : productModels) {
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
                productModelsFiltered = (ArrayList<ProductModel>) filterResults.values;
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
