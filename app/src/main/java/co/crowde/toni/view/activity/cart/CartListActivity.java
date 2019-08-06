package co.crowde.toni.view.activity.cart;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.adapter.CartAdapter;
import co.crowde.toni.constant.Const;
import co.crowde.toni.database.Cart;
import co.crowde.toni.helper.OnSwipeTouchListener;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.listener.ItemClickListener;
import co.crowde.toni.model.CartModel;
import co.crowde.toni.model.CustomerModel;
import co.crowde.toni.utils.SetHeader;
import co.crowde.toni.utils.analytics.AnalyticsToniUtils;
import co.crowde.toni.view.activity.customer.SelectCustomerActivity;
import co.crowde.toni.view.activity.filter.DashboardFilterActivity;
import co.crowde.toni.view.dialog.message.product.StockInsufficientDialog;
import co.crowde.toni.view.fragment.cart.CartListItemFragment;
import co.crowde.toni.view.fragment.cart.CartPaymentFragment;
import co.crowde.toni.view.fragment.modul.DashboardFragment;
import co.crowde.toni.view.fragment.reset_password.ResetPassPhoneFragment;
import co.crowde.toni.view.fragment.transaction.DashboardReportFragment;

public class CartListActivity extends AppCompatActivity
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

    public static CardView cvBtnCustomer;
    public static ConstraintLayout cvBtnPayment;

    public static Toolbar toolbar;
    public static ImageView imgDelete;

    AppBarLayout appBarLayout;

    public static CustomerModel customerModel;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);

        appBarLayout = findViewById(R.id.appBar);
        SetHeader.isLolipop(CartListActivity.this, appBarLayout);
        toolbar = findViewById(R.id.toolbarCart);
        imgDelete = findViewById(R.id.imgDeleteCart);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));

        imgDelete.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cvBtnCustomer = findViewById(R.id.cvBtnCustomer);
        tvCustomer = findViewById(R.id.tvCustomer);
        imgCheck = findViewById(R.id.imgCheck);
        tvAmountTotal = findViewById(R.id.tvAmountTotal);
        cvBtnPayment = findViewById(R.id.cv_btn_payment);
        cvBtnPayment.setEnabled(false);

        formatNumber = new DecimalFormat("###,###,###,###,###,###");

        itemDecorator = new DividerItemDecoration(getBaseContext(),
                DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getBaseContext(),
                R.drawable.divider_line_item));
        rcCart = findViewById(R.id.rcCartList);

        itemCartListener(this);
        layoutCart = new LinearLayoutManager(getApplicationContext());
        rcCart.addItemDecoration(itemDecorator);
        rcCart.setLayoutManager(layoutCart);
        rcCart.setAdapter(cartAdapter);
        cvBtnCustomer.setOnClickListener(this);
        cvBtnPayment.setOnClickListener(this);

        setTotalAmount(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgDeleteCart:
                new AlertDialog.Builder(this)
                        .setTitle("Hapus Keranjang Belanja")
                        .setMessage("Apakah Anda ingin menghapus Keranjang Belanja?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                AnalyticsToniUtils.getEvent(Const.CATEGORY_TRANSACTION, Const.MODUL_CART, Const.LABEL_CART_REMOVE_ALL_PRODUCT);
                                resetCart(CartListActivity.this);
                                finish();
                            }
                        }).create().show();
                break;
            case R.id.cvBtnCustomer:
                Intent select = new Intent(CartListActivity.this, SelectCustomerActivity.class);
                startActivity(select);
                break;

            case R.id.cv_btn_payment:
                Intent payment = new Intent(CartListActivity.this, CartPaymentActivity.class);
                payment.putExtra(CustomerModel.class.getSimpleName(), customerModel);
                payment.putExtra("total_amount", ""+DashboardFragment.totalAmount);
                startActivityForResult(payment, 123);
                break;
        }
    }

    public static void resetCart(Activity activity){
        dbCart = new Cart(activity);
        dbCart.deleteAllItem();
        cartModels.clear();
        cartAdapter.notifyDataSetChanged();
        DashboardFragment.productDashboardAdapter.notifyDataSetChanged();
        DashboardFragment.setTotal(activity, dbCart);
        setTotalAmount(activity);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
                AnalyticsToniUtils.getEvent(Const.CATEGORY_TRANSACTION,Const.MODUL_CART,Const.LABEL_CART_REMOVE_PRODUCT);

                dbCart = new Cart(activity);
                dbCart.deleteItem(cartModels.get(position));
                cartModels.remove(position);
                cartAdapter.notifyItemRemoved(position);
                cartAdapter.notifyItemRangeChanged(position, cartModels.size());
                DashboardFragment.productDashboardAdapter.notifyDataSetChanged();
                DashboardFragment.ifCartEmpty(activity);
                DashboardFragment.setTotal(activity, dbCart);
                setTotalAmount(activity);
            }

            @Override
            public void onIncreaseItem(View v, int position) {
                AnalyticsToniUtils.getEvent(Const.CATEGORY_TRANSACTION,Const.MODUL_CART,Const.LABEL_CART_CHANGE_QTY_PLUS_MIN);

                if(cartModels.get(position).getQuantity()<cartModels.get(position).getStok()){
                    cartModels.get(position).setQuantity(cartModels.get(position).getQuantity()+1);
                    cartModels.get(position).setAmount(cartModels.get(position).getQuantity()*cartModels.get(position).getSellingPrice());
                    dbCart.updateItem(cartModels.get(position));
                    cartAdapter.notifyDataSetChanged();
                    DashboardFragment.ifCartEmpty(activity);
                    setTotalAmount(activity);
                } else {
                    StockInsufficientDialog.showDialog(activity);
                }

            }

            @Override
            public void onDecreaseItem(View v, int position) {
                AnalyticsToniUtils.getEvent(Const.CATEGORY_TRANSACTION,Const.MODUL_CART,Const.LABEL_CART_CHANGE_QTY_PLUS_MIN);

                if(cartModels.get(position).getQuantity()>1){
                    cartModels.get(position).setQuantity(cartModels.get(position).getQuantity()-1);
                    cartModels.get(position).setAmount(cartModels.get(position).getQuantity()*cartModels.get(position).getSellingPrice());
                    dbCart.updateItem(cartModels.get(position));
                    cartAdapter.notifyDataSetChanged();
                    DashboardFragment.ifCartEmpty(activity);
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
                        AnalyticsToniUtils.getEvent(Const.CATEGORY_TRANSACTION,Const.MODUL_CART,Const.LABEL_CART_CHANGE_QTY_DROPDOWN);

                        if(Integer.parseInt(item.getTitle().toString())<=cartModels.get(position).getStok()){
                            cartModels.get(position).setQuantity(Integer.parseInt(item.getTitle().toString()));
                            cartModels.get(position).setAmount(cartModels.get(position).getQuantity()*cartModels.get(position).getSellingPrice());
                            dbCart.updateItem(cartModels.get(position));
                            cartAdapter.notifyDataSetChanged();
                            DashboardFragment.ifCartEmpty(activity);
                            setTotalAmount(activity);
                        } else {
                            StockInsufficientDialog.showDialog(activity);
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
                +String.valueOf(formatNumber.format(DashboardFragment.totalAmount))+",-");

    }

    public static void setCustomer(Activity activity, CustomerModel model){
        customerModel = model;
        if(model!=null){
            tvCustomer.setText(model.getCustomerName()+"\n"
                    +model.getPhone());
            imgCheck.setImageDrawable(
                    activity.getResources().getDrawable(R.drawable.ic_check_box_orange_24dp));
        } else {
            tvCustomer.setText(
                    activity.getResources().getString(R.string.pilih_pelanggan));
            imgCheck.setImageDrawable(
                    activity.getResources().getDrawable(R.drawable.ic_check_box_outline_blank_white_24dp));
        }
        enabledPayment(activity, model);
    }

    public static void enabledPayment(Activity activity, CustomerModel model) {
        if(model!=null){
            cvBtnPayment.setEnabled(true);
            cvBtnPayment.setBackgroundColor(activity.getResources().getColor(R.color.colorThemeOrange));
        } else {
            cvBtnPayment.setEnabled(false);
            cvBtnPayment.setBackgroundColor(activity.getResources().getColor(R.color.colorThemeGrey));
        }
    }
}

//        CartListItemFragment cartList = new CartListItemFragment();
//        FragmentTransaction cartListTransaction = getSupportFragmentManager()
//                .beginTransaction();
//        cartListTransaction.replace(R.id.layoutCart, cartList);
//        cartListTransaction.commit();
//        showOrHide(CartListActivity.this);
//        layoutCart.setOnTouchListener(new OnSwipeTouchListener(CartListActivity.this) {
//            public void onSwipeTop() {
//                if(SavePref.readCustomerId(CartListActivity.this)!=null
//                        && SavePref.readCustomer(CartListActivity.this)!=null){
//                    if(!show){
//                        CartPaymentFragment cartPayment = new CartPaymentFragment();
//                        FragmentTransaction cartPaymentTransaction = getSupportFragmentManager()
//                                .beginTransaction();
//                        cartPaymentTransaction.setCustomAnimations(
//                                R.anim.anim_slide_in_down, R.anim.anim_slide_out_up);
//                        cartPaymentTransaction.replace(R.id.layoutCart, cartPayment);
//                        cartPaymentTransaction.commit();
//                        show=true;
//                        showOrHide(CartListActivity.this);
//                    }
//                }
//            }
//            public void onSwipeRight() {
//            }
//            public void onSwipeLeft() {
//            }
//            public void onSwipeBottom() {
//                if(SavePref.readCustomerId(CartListActivity.this)!=null
//                        && SavePref.readCustomer(CartListActivity.this)!=null){
//                    if(show){
//                        CartListItemFragment cartList = new CartListItemFragment();
//                        FragmentTransaction cartListTransaction = getSupportFragmentManager()
//                                .beginTransaction();
//                        cartListTransaction.setCustomAnimations(
//                                R.anim.anim_slide_in_up, R.anim.anim_slide_out_down);
//                        cartListTransaction.replace(R.id.layoutCart, cartList);
//                        cartListTransaction.commit();
//                        show=false;
//                        showOrHide(CartListActivity.this);
//                    }
//                }
//            }
//
//        });
