package co.crowde.toni.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.constant.Const;
import co.crowde.toni.controller.cart.CartController;
import co.crowde.toni.database.Cart;
import co.crowde.toni.model.CartModel;
import co.crowde.toni.network.API;
import co.crowde.toni.model.ProductModel;
import co.crowde.toni.utils.analytics.AnalyticsToniUtils;
import co.crowde.toni.view.activity.product.ProductDashboardDetailActivity;
import co.crowde.toni.view.dialog.popup.product.ProductDetailDashboardPopup;
import co.crowde.toni.view.fragment.modul.DashboardFragment;

public class ProductDashboardAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private Activity activity;
    private List<ProductModel> productModelsFiltered = new ArrayList<>();
    private Cart dbCart;
    int countProduct;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvProductQty,
                tvProductName, tvProductUnit;
        ImageView imgProductItem,
                imgBtnMinQty, imgBtnPlusQty;

        public ViewHolder(final View itemView) {
            super(itemView);
            tvProductQty = itemView.findViewById(R.id.tvProductQty);
            imgBtnMinQty = itemView.findViewById(R.id.imgDecrease);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductUnit = itemView.findViewById(R.id.tvProductVarian);
            imgBtnPlusQty = itemView.findViewById(R.id.imgIncrease);
            imgProductItem = itemView.findViewById(R.id.imgProductItem);

        }
    }

    public ProductDashboardAdapter(Context context,
                                   List<ProductModel> ProductModelList,
                                   Activity activity) {
        this.context = context;
        this.productModelsFiltered.clear();
        this.productModelsFiltered.addAll(ProductModelList);
//        this.productModelsFiltered = ProductModelList;
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product_dashboard_item, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress_bar, parent, false);
            return new LoadingViewHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            dbCart = new Cart(context);

            final ProductModel model = productModelsFiltered.get(position);
            final ViewHolder viewHolder = (ViewHolder) holder;

            if (model != null) {
                String product = model.getProductName();
                final String nama;
                String varian;
                if (product.contains("_")) {
                    nama = StringUtils.substringBeforeLast(product, "_");
                    varian = StringUtils.substringAfterLast(product, "_");
                } else {
                    nama = product;
                    varian = model.getUnit();
                }

                viewHolder.tvProductName.setText(nama);
                viewHolder.tvProductUnit.setText(varian);

                // TODO : init count of product
                final CartModel cart = DashboardFragment.dbCart.getItem(model.getProductId());
                countProduct = 0;
                if (cart != null)
                    countProduct = cart.getQuantity();
//                    countProduct = model.getCountItem();
//                else
//                    countProduct = cart.getQuantity();
                viewHolder.tvProductQty.setVisibility(countProduct > 0 ? View.VISIBLE : View.GONE);
                viewHolder.tvProductQty.setText(countProduct + "");

                Picasso.with(activity).load(API.Host + model.getPicture())
                        .into(viewHolder.imgProductItem);

                viewHolder.imgBtnPlusQty.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CartController.addFromPlus(activity, model);
                        AnalyticsToniUtils.getEvent(Const.CATEGORY_TRANSACTION,Const.MODUL_CART,Const.LABEL_CART_CHANGE_QTY_DASHBOARD);
                    }
                });

                viewHolder.imgBtnMinQty.setBackground(countProduct > 0 ?
                        activity.getResources().getDrawable(R.drawable.bg_green_dark_radius_2dp) :
                        activity.getResources().getDrawable(R.drawable.bg_grey_cccccc_2dp));
//                        View.VISIBLE : View.GONE);
                viewHolder.imgBtnMinQty.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CartController.minQtyCart(activity, model);
                        AnalyticsToniUtils.getEvent(Const.CATEGORY_TRANSACTION,Const.MODUL_CART,Const.LABEL_CART_CHANGE_QTY_DASHBOARD);
                    }
                });

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent detail = new Intent(activity, ProductDashboardDetailActivity.class);
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
