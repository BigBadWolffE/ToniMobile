package co.crowde.toni.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.database.Cart;
import co.crowde.toni.listener.ItemClickListener;
import co.crowde.toni.model.CartModel;
import co.crowde.toni.model.ProductModel;
import co.crowde.toni.view.popup.InventoryDetailedPopup;

public class CartAdapter
        extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context context;
    private List<CartModel> cartModels;
    private Cart dbCart;
    ItemClickListener listener;

    private int lastPosition = -1;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvVarian, tvStock, tvPrice,
                tvDelete, tvAmount;
        ImageView imgDecrease, imgIncrease;
        ConstraintLayout layout;
        EditText etQty;

        public ViewHolder(final View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvVarian = itemView.findViewById(R.id.tvVarian);
            tvStock = itemView.findViewById(R.id.tvStock);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDelete = itemView.findViewById(R.id.tvDelete);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            imgDecrease = itemView.findViewById(R.id.imgDecrease);
            imgIncrease = itemView.findViewById(R.id.imgIncrease);
            etQty = itemView.findViewById(R.id.etQty);

            layout = itemView.findViewById(R.id.layout);

        }
    }

    public CartAdapter(Context context,
                       List<CartModel> cartModels,
                       ItemClickListener listener) {
        this.context = context;
        this.cartModels = cartModels;
        this.listener = listener;
    }

    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_cart_item, parent, false);

        CartAdapter.ViewHolder mViewHolder = new CartAdapter.ViewHolder(view);

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(final CartAdapter.ViewHolder holder, final int position) {
        final CartModel model = cartModels.get(position);
        CartAdapter.ViewHolder viewHolder = (CartAdapter.ViewHolder) holder;
        DecimalFormat formatNumber = new DecimalFormat("###,###,###,###,###,###");

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

        holder.tvName.setText(nama);
        holder.tvVarian.setText("Kemasan "+varian);
        holder.tvStock.setText("Stok : "+model.getStok()+" "+model.getUnit());
        holder.tvPrice.setText("Rp. "+String.valueOf(formatNumber.format(model.getSellingPrice()))+",-");
        holder.etQty.setText(String.valueOf(model.getQuantity()));
        holder.tvAmount.setText("Rp. "+String.valueOf(formatNumber.format(model.getAmount()))+",-");

        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeleteItemClick(v, position);
            }
        });

        holder.imgDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDecreaseItem(v, position);
            }
        });

        holder.imgIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onIncreaseItem(v, position);
            }
        });

        holder.etQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onChangeQty(v, position);
            }
        });

        setAnimation(holder.itemView, position);

    }

    @Override
    public int getItemCount() {
        return cartModels.size();
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
