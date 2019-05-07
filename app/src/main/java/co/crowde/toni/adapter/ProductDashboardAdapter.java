package co.crowde.toni.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.media.MediaPlayer;
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

import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.controller.network.API;
import co.crowde.toni.database.Cart;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.listener.ProductListener;
import co.crowde.toni.model.CartModel;
import co.crowde.toni.model.ProductModel;
import co.crowde.toni.view.fragment.Dashboard;
import co.crowde.toni.view.popup.ProductDetailDashboardPopup;

public class ProductDashboardAdapter
        extends RecyclerView.Adapter<ProductDashboardAdapter.ViewHolder>
         implements Filterable{

    private Context context;
    private Activity activity ;
    private List<ProductModel> productModels;
    private List<ProductModel> productModelsFiltered;
    ProductListener listener;

    static Cart dbCart;
    static CartModel cartModel;

    private int lastPosition = -1;

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
                                   ProductListener listener) {
        this.context = context;
        this.productModels = ProductModelList;
        this.productModelsFiltered = ProductModelList;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_product_dashboard_item, parent, false);

        ViewHolder mViewHolder = new ViewHolder(view);

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ProductModel model = productModelsFiltered.get(position);
//        int Qty = dbCart.getQty(model.getProductId());
        ProductDashboardAdapter.ViewHolder viewHolder = (ProductDashboardAdapter.ViewHolder) holder;

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

        holder.tvProductName.setText(nama);
        holder.tvProductUnit.setText(varian);

        Picasso.with(activity).load(API.Host+model.getPicture())
                .into(viewHolder.imgProductItem);

//        dbCart = new Cart(activity);
//        if(dbCart.getItemCount()>0){
//            CartModel cartModel = dbCart.getItem(model.getProductId());
//            holder.tvProductQty.setText(String.valueOf(cartModel.getQuantity()));
//        } else {
//            holder.tvProductQty.setText("0");
//        }

//        holder.tvProductQty.setText(Qty);

        holder.cvProductItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, position);
            }
        });

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

        setAnimation(holder.itemView, position);

    }

    @Override
    public int getItemCount() {
        return productModelsFiltered!=null? productModelsFiltered.size():0;
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
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
