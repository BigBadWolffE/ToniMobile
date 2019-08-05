package co.crowde.toni.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.model.ProductModel;
import co.crowde.toni.utils.print.Utils;
import co.crowde.toni.view.activity.product.InventoryDetailActivity;
import co.crowde.toni.view.activity.product.ProductDashboardDetailActivity;
import co.crowde.toni.view.dialog.popup.product.InventoryDetailPopup;

public class ProductInventoryAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private Activity activity;
    private List<ProductModel> productModelsFiltered = new ArrayList<>();

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

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
        this.context = context;
        this.productModelsFiltered.clear();
        this.productModelsFiltered.addAll(ProductModelList);
        this.activity = activity;
    }

    public void replaceItemFiltered(List<ProductModel> ProductModelList) {
        this.productModelsFiltered.clear();
        this.productModelsFiltered.addAll(ProductModelList);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product_inventory_item, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress_bar, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final ProductModel model = productModelsFiltered.get(position);
            ViewHolder viewHolder = (ViewHolder) holder;

            if (model != null) {
                String product = model.getProductName();
                String nama;
                String varian;
                if (product.contains("_")) {
                    nama = StringUtils.substringBeforeLast(product, "_");
                    varian = StringUtils.substringAfterLast(product, "_");
                } else {
                    nama = product;
                    varian = model.getUnit();
                }
                String text = "<font color=#52575C><b>" + nama + "</b></font>" + (!Utils.calculateDateBetweenTwoDays(model.getCreatedAt())?
                        "" : "<font color=#F7931D> (Baru!)</font>");
                viewHolder.tvTabProductName.setText(Html.fromHtml(text));
//                viewHolder.tvTabProductName.setText(nama);
                viewHolder.tvTabProductUnit.setText("(" + varian + ")");
                viewHolder.tvTabProductStock.setText(String.valueOf(model.getStock()));
                viewHolder.tvTabProductStatus.setText(model.getStatus());

                if(model.getStatus()!=null){
                    switch (model.getStatus()) {
                        case "Habis":
                            viewHolder.tvTabProductStatus.setTextColor(
                                    activity.getResources().getColor(R.color.color_theme_red));
                            break;
                        case "Mulai habis":
                            viewHolder.tvTabProductStatus.setTextColor(
                                    activity.getResources().getColor(R.color.color_theme_yellow));
                            break;
                        case "Tersedia":
                            viewHolder.tvTabProductStatus.setTextColor(
                                    activity.getResources().getColor(R.color.color_theme_green));
                            break;

                        default:
                            viewHolder.tvTabProductStatus.setTextColor(
                                    activity.getResources().getColor(R.color.status_kosong));
                            break;
                    }
                }


                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        InventoryDetailPopup.showPopup(activity, model);
                        Intent detail = new Intent(activity, InventoryDetailActivity.class);
                        detail.putExtra(ProductModel.class.getSimpleName(), model);
                        activity.startActivityForResult(detail, 123);
                    }
                });

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
        return productModelsFiltered != null ? productModelsFiltered.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return productModelsFiltered.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

}
