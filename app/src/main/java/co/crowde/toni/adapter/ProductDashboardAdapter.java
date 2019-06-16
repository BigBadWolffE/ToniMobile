package co.crowde.toni.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.controller.cart.CartController;
import co.crowde.toni.database.Cart;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.CartModel;
import co.crowde.toni.network.API;
import co.crowde.toni.listener.ProductListener;
import co.crowde.toni.model.ProductModel;
import co.crowde.toni.view.dialog.product.ProductDetailDashboardPopup;
import co.crowde.toni.view.fragment.cart.CartListItem;
import co.crowde.toni.view.fragment.modul.Dashboard;

public class ProductDashboardAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private Activity activity;
    private List<ProductModel> productModels;
    private List<ProductModel> productModelsFiltered;
    ProductListener listener;
    private Cart dbCart;
    int countProduct;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvProductQty,
                tvProductName, tvProductUnit, tvProductCount;
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
        this.context = context;
        this.productModels = ProductModelList;
        this.productModelsFiltered = ProductModelList;
        this.activity = activity;
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
                final CartModel cart = Dashboard.dbCart.getItem(model.getProductId());
                countProduct = 0;
                if (cart != null)
                    countProduct = cart.getQuantity();
//                    countProduct = model.getCountItem();
//                else
//                    countProduct = cart.getQuantity();
                viewHolder.cvProductQty.setVisibility(countProduct > 0 ? View.VISIBLE : View.GONE);
                viewHolder.tvProductQty.setText(countProduct + "");

                Picasso.with(activity).load(API.Host + model.getPicture())
                        .into(viewHolder.imgProductItem);

                viewHolder.imgBtnPlusQty.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CartController.addFromPlus(activity, model);
                    }
                });

                viewHolder.imgBtnMinQty.setVisibility(countProduct > 0 ? View.VISIBLE : View.GONE);
                viewHolder.imgBtnMinQty.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CartController.minQtyCart(activity, model);
                    }
                });

                viewHolder.cvProductItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(activity, ""+nama, Toast.LENGTH_SHORT).show();
                        ProductDetailDashboardPopup.showPopup(activity, model);
                    }
                });

//        dbCart = new Cart(activity);
//        if(dbCart.getItemCount()>0){
//            CartModel cartModel = dbCart.getItem(model.getProductId());
//            holder.tvProductQty.setText(String.valueOf(cartModel.getQuantity()));
//        } else {
//            holder.tvProductQty.setText("0");
//        }

//        holder.tvProductQty.setText(Qty);

//        holder.imgBtnPlusQty.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.onIncreaseItem(v, position);
//            }
//        });
//
//        holder.imgBtnMinQty.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.onDecreaseItem(v, position);
//            }
//        });
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
