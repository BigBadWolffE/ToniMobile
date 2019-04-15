package co.crowde.toni.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.media.MediaPlayer;
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

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.controller.network.API;
import co.crowde.toni.model.ProductModel;

public class ProductDashboardAdapter
        extends RecyclerView.Adapter<ProductDashboardAdapter.ViewHolder>
         implements Filterable{

    private Context context;
    private Activity activity ;
    private List<ProductModel> productModels;
    private List<ProductModel> productModelsFiltered;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvProductQty,
                tvProductName, tvProductUnit;
        ImageView imgProductItem,
                imgBtnMinQty, imgBtnPlusQty;
        CardView cvProductItem, cvProductQty;

        public ViewHolder(final View itemView) {
            super(itemView);
            tvProductQty = itemView.findViewById(R.id.tvProductQty);
            imgBtnMinQty = itemView.findViewById(R.id.imgBtnMinQty);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductUnit = itemView.findViewById(R.id.tvProductUnit);
            imgBtnPlusQty = itemView.findViewById(R.id.imgBtnPlusQty);
            imgProductItem = itemView.findViewById(R.id.imgProductItem);
            cvProductItem = itemView.findViewById(R.id.cvProductItem);
            cvProductQty = itemView.findViewById(R.id.cvProductQty);

        }
    }

    public ProductDashboardAdapter(Context context,
                                   List<ProductModel> ProductModelList,
                                   Activity activity) {
        this.productModels = ProductModelList;
        this.productModelsFiltered = ProductModelList;
        this.context = context;
        this.activity = activity;
//        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_product_dashboard_item, parent, false);

        ViewHolder mViewHolder = new ViewHolder(view);

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProductModel model = productModelsFiltered.get(position);
        ProductDashboardAdapter.ViewHolder viewHolder = (ProductDashboardAdapter.ViewHolder) holder;

        holder.tvProductName.setText(model.getProductName());
        holder.tvProductUnit.setText(model.getUnit());

        Picasso.with(activity).load(API.Host+model.getPicture())
                .into(viewHolder.imgProductItem);

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

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
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
