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
import co.crowde.toni.model.response.list.ProductFavoriteModel;
import co.crowde.toni.view.activity.catalog.CatalogProductActivity;

public class ReportProductFavoriteAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private Activity activity ;
    private List<ProductFavoriteModel> productFavoriteModels = new ArrayList<>();

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTabProductNumber, tvTabProductName, tvTabProductUnit,
                tvTabProductSold;

        public ViewHolder(final View itemView) {
            super(itemView);
            tvTabProductNumber = itemView.findViewById(R.id.tv_product_number);
            tvTabProductName = itemView.findViewById(R.id.tv_product_name);
            tvTabProductUnit = itemView.findViewById(R.id.tv_product_unit);
            tvTabProductSold = itemView.findViewById(R.id.tv_product_sold);

        }
    }

    public ReportProductFavoriteAdapter(Context context,
                                 List<ProductFavoriteModel> ProductModelList,
                                 Activity activity) {
        this.context = context;
        this.productFavoriteModels.clear();
        this.productFavoriteModels.addAll(ProductModelList);
        this.activity = activity;
    }

    public void replaceItemFiltered(List<ProductFavoriteModel> ProductModelList) {
        this.productFavoriteModels.clear();
        this.productFavoriteModels.addAll(ProductModelList);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_produk_laris, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress_bar, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final ProductFavoriteModel model = productFavoriteModels.get(position);
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

                viewHolder.tvTabProductNumber.setText(String.valueOf(position+1));
                viewHolder.tvTabProductName.setText(nama + " ("+varian+")");
                viewHolder.tvTabProductUnit.setText(model.getUnit());
                viewHolder.tvTabProductSold.setText(String.valueOf(model.getCount()));
            }
        } else if (holder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) holder, position);
        }

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
        return productFavoriteModels!=null? productFavoriteModels.size():0;
    }

    @Override
    public int getItemViewType(int position) {
        return productFavoriteModels.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }
}
