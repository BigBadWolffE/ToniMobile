package co.crowde.toni.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.model.ProductModel;

public class ProductInventoryAdapter
        extends RecyclerView.Adapter<ProductInventoryAdapter.ViewHolder>
        implements Filterable {

    private Context context;
    private Activity activity ;
    private List<ProductModel> productModels;
    private List<ProductModel> productModelsFiltered;

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

        viewHolder.tvTabProductName.setText(model.getProductName());
        viewHolder.tvTabProductUnit.setText(model.getUnit());
        viewHolder.tvTabProductStock.setText(String.valueOf(model.getStock()));
        viewHolder.tvTabProductStatus.setText(model.getStatus());

        switch (model.getStatus()) {
            case "Habis":
                viewHolder.tvTabProductStatus.setTextColor(
                        activity.getResources().getColor(R.color.status_kosong));
                break;
            case "Mulai habis":
                viewHolder.tvTabProductStatus.setTextColor(
                        activity.getResources().getColor(R.color.status_persediaan_isi));
                break;
            case "Tersedia":
                viewHolder.tvTabProductStatus.setTextColor(
                        activity.getResources().getColor(R.color.status_tersedia));
                break;
            default:
                viewHolder.tvTabProductStatus.setTextColor(
                        activity.getResources().getColor(R.color.status_kosong));
                break;
        }

        viewHolder.layoutProductInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, model.getProductName(), Toast.LENGTH_SHORT).show();
//                ProductDetailDashboardPopup.showProductDetail(activity, model);
            }
        });

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

}
