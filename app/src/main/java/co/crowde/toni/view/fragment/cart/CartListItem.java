package co.crowde.toni.view.fragment.cart;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.adapter.CartAdapter;
import co.crowde.toni.database.Cart;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.listener.ItemClickListener;
import co.crowde.toni.model.CartModel;
import co.crowde.toni.model.CustomerModel;
import co.crowde.toni.view.activity.cart.CartList;
import co.crowde.toni.view.fragment.modul.Dashboard;
import co.crowde.toni.view.activity.customer.SelectCustomer;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartListItem extends Fragment
implements View.OnClickListener{

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    //Database Cart
    public static TextView tvAmountTotal, tvCustomer;
    public static ImageView imgCheck;
    public static List<CartModel> cartModels = new ArrayList<>();
    static RecyclerView.LayoutManager layoutCart;
    static RecyclerView rcCart;
    public static CartAdapter cartAdapter;
    static Cart dbCart;
    static int totalItem;
    static int totalBelanjaan;
    static ItemClickListener listener;

    static DividerItemDecoration itemDecorator;

    private static DecimalFormat formatNumber;

    public CartListItem() {
        // Required empty public constructor
    }

    public static CardView cvBtnCustomer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        View  view =  inflater.inflate(R.layout.fragment_cart_list, container, false);

        cvBtnCustomer = view.findViewById(R.id.cvBtnCustomer);
        tvCustomer = view.findViewById(R.id.tvCustomer);
        imgCheck = view.findViewById(R.id.imgCheck);
        tvAmountTotal = view.findViewById(R.id.tvAmountTotal);
        setCustomer(getActivity());

        formatNumber = new DecimalFormat("###,###,###,###,###,###");

        itemDecorator = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(),
                R.drawable.divider_line_item));
        rcCart = view.findViewById(R.id.rcCartList);
        itemCartListener(getActivity());
        layoutCart = new LinearLayoutManager(getActivity().getApplicationContext());
        rcCart.addItemDecoration(itemDecorator);
        rcCart.setLayoutManager(layoutCart);
        rcCart.setAdapter(cartAdapter);
        cvBtnCustomer.setOnClickListener(this);

        setTotalAmount(getActivity());

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.cvBtnCustomer:
                Intent select = new Intent(getActivity(), SelectCustomer.class);
                startActivity(select);
                break;

        }

    }

    private static void itemCartListener(final Activity activity) {
        dbCart = new Cart(activity);
        cartModels.clear();
        cartModels.addAll(dbCart.getAllItem());
        cartAdapter = new CartAdapter(activity, cartModels, activity, listener);
        cartAdapter = new CartAdapter(activity, cartModels, activity, new ItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

            }

            @Override
            public void onDeleteItemClick(View v, int position) {
                dbCart = new Cart(activity);
                dbCart.deleteItem(cartModels.get(position));
                cartModels.remove(position);
                cartAdapter.notifyItemRemoved(position);
                cartAdapter.notifyItemRangeChanged(position, cartModels.size());
                Dashboard.productDashboardAdapter.notifyDataSetChanged();
                Dashboard.ifCartEmpty(activity);
                Dashboard.setTotal(activity, dbCart);
                setTotalAmount(activity);
                CartList.showOrHide(activity);
            }

            @Override
            public void onIncreaseItem(View v, int position) {
                if(cartModels.get(position).getQuantity()<cartModels.get(position).getStok()){
                    cartModels.get(position).setQuantity(cartModels.get(position).getQuantity()+1);
                    cartModels.get(position).setAmount(cartModels.get(position).getQuantity()*cartModels.get(position).getSellingPrice());
                    dbCart.updateItem(cartModels.get(position));
                    cartAdapter.notifyDataSetChanged();
                    Dashboard.ifCartEmpty(activity);
                    setTotalAmount(activity);
                } else {
                    Toast.makeText(activity, "Stok tidak mencukupi permintaan pelanggan", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onDecreaseItem(View v, int position) {
                if(cartModels.get(position).getQuantity()>1){
                    cartModels.get(position).setQuantity(cartModels.get(position).getQuantity()-1);
                    cartModels.get(position).setAmount(cartModels.get(position).getQuantity()*cartModels.get(position).getSellingPrice());
                    dbCart.updateItem(cartModels.get(position));
                    cartAdapter.notifyDataSetChanged();
                    Dashboard.ifCartEmpty(activity);
                    setTotalAmount(activity);
                }
            }

            @Override
            public void onChangeQty(View v, final int position, TextView tvQty) {
                final PopupMenu popup = new PopupMenu(activity, tvQty);
                //Inflating the Popup using xml file
                for(int i =1; i<100;i++){
                    popup.getMenu().add(String.valueOf(i));
                }

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if(Integer.parseInt(item.getTitle().toString())<=cartModels.get(position).getStok()){
                            cartModels.get(position).setQuantity(Integer.parseInt(item.getTitle().toString()));
                            cartModels.get(position).setAmount(cartModels.get(position).getQuantity()*cartModels.get(position).getSellingPrice());
                            dbCart.updateItem(cartModels.get(position));
                            cartAdapter.notifyDataSetChanged();
                            Dashboard.ifCartEmpty(activity);
                            setTotalAmount(activity);
                        } else {
                            Toast.makeText(activity, "Stok tidak mencukupi permintaan pelanggan", Toast.LENGTH_SHORT).show();
                        }


                        return true;
                    }
                });

                popup.show(); //showing popup menu

            }
        });
    }

    public static void setTotalAmount(Activity activity){
        tvAmountTotal.setText("Rp. "
                +String.valueOf(formatNumber.format(Dashboard.totalAmount))+",-");

    }

    public static void setCustomer(Activity activity){
        if(SavePref.readCustomerId(activity)!=null
                && SavePref.readCustomer(activity)!=null){
            CustomerModel model = new Gson().fromJson(SavePref.readCustomer(activity), CustomerModel.class);

            CartListItem.tvCustomer.setText(model.getCustomerName()+"\n"
                    +model.getPhone());
            CartListItem.imgCheck.setImageDrawable(
                    activity.getResources().getDrawable(R.drawable.ic_check_box_white_24dp));
        } else {
            CartListItem.tvCustomer.setText(
                    activity.getResources().getString(R.string.pilih_pelanggan));
            CartListItem.imgCheck.setImageDrawable(
                    activity.getResources().getDrawable(R.drawable.ic_check_box_outline_blank_white_24dp));
        }
    }
}