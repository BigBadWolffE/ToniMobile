package co.crowde.toni.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.helper.DecimalFormatRupiah;
import co.crowde.toni.model.ProductModel;
import co.crowde.toni.model.TransactionProductModel;
import co.crowde.toni.model.response.list.ProductFavoriteModel;
import co.crowde.toni.view.activity.transaction.DetailTransactionActivity;

public class TransactionProductListAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private Activity activity ;
    private List<TransactionProductModel> transactionProductModels = new ArrayList<>();

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTabProductQty, tvTabProductName, tvTabProductUnit,
                tvTabProductPrice;

        public ViewHolder(final View itemView) {
            super(itemView);
            tvTabProductName = itemView.findViewById(R.id.tv_product_name);
            tvTabProductQty = itemView.findViewById(R.id.tv_product_qty);
            tvTabProductUnit = itemView.findViewById(R.id.tv_product_unit);
            tvTabProductPrice = itemView.findViewById(R.id.tv_product_price);

        }
    }

    public TransactionProductListAdapter(Context context,
                                        List<TransactionProductModel> ProductModelList,
                                        Activity activity) {
        this.context = context;
        this.transactionProductModels.clear();
        this.transactionProductModels.addAll(ProductModelList);
        this.activity = activity;
    }

    public void replaceItemFiltered(List<TransactionProductModel> ProductModelList) {
        this.transactionProductModels.clear();
        this.transactionProductModels.addAll(ProductModelList);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_transaction_product_list, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress_bar, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final TransactionProductModel model = transactionProductModels.get(position);
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

                viewHolder.tvTabProductName.setText(nama + " ("+varian+")");
                viewHolder.tvTabProductQty.setText(String.valueOf(model.getQuantity()));
                viewHolder.tvTabProductUnit.setText(model.getUnit());
                DecimalFormatRupiah.changeFormat(activity);
                if(model.getAmount()!=(model.getQuantity()*model.getSellingPrice())){
                    viewHolder.tvTabProductPrice.setText(
                            "Rp. "+DecimalFormatRupiah.formatNumber.format(model.getQuantity()*model.getSellingPrice())+"\n"
                                    + "(-"+DecimalFormatRupiah.formatNumber.format((model.getQuantity()*model.getSellingPrice())-model.getAmount())+")");
                } else {
                    viewHolder.tvTabProductPrice.setText("Rp. "+
                            DecimalFormatRupiah.formatNumber.format(model.getAmount())+",-");
                }

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
        return transactionProductModels!=null? transactionProductModels.size():0;
    }

    @Override
    public int getItemViewType(int position) {
        return transactionProductModels.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

}
