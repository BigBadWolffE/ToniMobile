package co.crowde.toni.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.database.Cart;
import co.crowde.toni.listener.ItemClickListener;
import co.crowde.toni.model.CartModel;
import co.crowde.toni.view.fragment.modul.DashboardFragment;

public class CartAdapter
        extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context context;
    private List<CartModel> cartModels;
    private Cart dbCart;
    private Activity activity;
    ItemClickListener listener;


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvVarian, tvStock, tvPrice,
                tvDelete, tvAmount, tvQty, tvDiscount, tvTotal, tvEditDiscount;
        ImageView imgDecrease, imgIncrease;
        ConstraintLayout layout;

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
            tvQty = itemView.findViewById(R.id.tvQty);
            tvDiscount = itemView.findViewById(R.id.tvDiscount);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            tvEditDiscount = itemView.findViewById(R.id.tv_edit_discount);

            layout = itemView.findViewById(R.id.layout);

        }
    }

    public CartAdapter(Context context,
                       List<CartModel> cartModels,
                       Activity activity,
                       ItemClickListener listener) {
        this.context = context;
        this.cartModels = cartModels;
        this.activity = activity;
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

        dbCart = new Cart(context);
        dbCart.getItem(model.getProductId());

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
        holder.tvPrice.setText("Rp. "+formatNumber.format(model.getSellingPrice()));
        holder.tvQty.setText(String.valueOf(model.getQuantity()));

        holder.tvDiscount.setText("- Rp. "+formatNumber.format(model.getDiscount()));

        holder.tvTotal.setText("TOTAL: Rp. "+formatNumber.format(model.getAmount()));

        if(model.getAmount()!=(model.getQuantity()*model.getSellingPrice())){
            holder.tvDiscount.setVisibility(View.VISIBLE);
            holder.tvEditDiscount.setText("UBAH DISKON");
        } else {
            holder.tvDiscount.setVisibility(View.GONE);
            holder.tvEditDiscount.setText("TAMBAH DISKON");
        }
//        holder.tvEditDiscount.setText(model.getDiscount() > 0 ? "UBAH DISKON" : "TAMBAH DISKON");

        holder.tvEditDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDiscount(v, position);
            }
        });

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
                DashboardFragment.productDashboardAdapter.notifyDataSetChanged();
            }
        });

        holder.imgIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onIncreaseItem(v, position);
                DashboardFragment.productDashboardAdapter.notifyDataSetChanged();
            }
        });

        holder.tvQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onChangeQty(v, position, holder.tvQty);
                DashboardFragment.productDashboardAdapter.notifyDataSetChanged();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return cartModels.size();
    }
}
