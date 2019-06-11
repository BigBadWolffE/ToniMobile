package co.crowde.toni.view.activity.cart;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.database.Cart;
import co.crowde.toni.helper.OnSwipeTouchListener;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.CartModel;
import co.crowde.toni.view.fragment.cart.CartListItem;
import co.crowde.toni.view.fragment.cart.CartPayment;
import co.crowde.toni.view.fragment.modul.Dashboard;

public class CartList extends AppCompatActivity
implements View.OnClickListener{

    public static CardView cvCartList, cvCartPayment;
    public static Toolbar toolbar;
    public static FrameLayout layoutCart;
    public static ImageView imgDelete;

    static Cart dbCart;


    public static boolean show;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
//        if (tabletSize) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        } else {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }

        setContentView(R.layout.activity_cart_list);

        show  = false;

        cvCartList = findViewById(R.id.cvCartList);
        cvCartPayment = findViewById(R.id.cvCartPayment);
        toolbar = findViewById(R.id.toolbarCart);
        layoutCart = findViewById(R.id.layoutCart);
        imgDelete = findViewById(R.id.imgDeleteCart);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));

        cvCartList.setOnClickListener(this);
        cvCartPayment.setOnClickListener(this);
        imgDelete.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                show=false;
            }
        });

        enabledButton(CartList.this);

        CartListItem cartList = new CartListItem();
        FragmentTransaction cartListTransaction = getSupportFragmentManager()
                .beginTransaction();
        cartListTransaction.replace(R.id.layoutCart, cartList);
        cartListTransaction.commit();
        showOrHide(CartList.this);
        layoutCart.setOnTouchListener(new OnSwipeTouchListener(CartList.this) {
            public void onSwipeTop() {
                if(SavePref.readCustomerId(CartList.this)!=null
                        && SavePref.readCustomer(CartList.this)!=null){
                    if(!show){
                        CartPayment cartPayment = new CartPayment();
                        FragmentTransaction cartPaymentTransaction = getSupportFragmentManager()
                                .beginTransaction();
                        cartPaymentTransaction.setCustomAnimations(
                                R.anim.anim_slide_in_down, R.anim.anim_slide_out_up);
                        cartPaymentTransaction.replace(R.id.layoutCart, cartPayment);
                        cartPaymentTransaction.commit();
                        show=true;
                        showOrHide(CartList.this);
                    }
                }
            }
            public void onSwipeRight() {
            }
            public void onSwipeLeft() {
            }
            public void onSwipeBottom() {
                if(SavePref.readCustomerId(CartList.this)!=null
                        && SavePref.readCustomer(CartList.this)!=null){
                    if(show){
                        CartListItem cartList = new CartListItem();
                        FragmentTransaction cartListTransaction = getSupportFragmentManager()
                                .beginTransaction();
                        cartListTransaction.setCustomAnimations(
                                R.anim.anim_slide_in_up, R.anim.anim_slide_out_down);
                        cartListTransaction.replace(R.id.layoutCart, cartList);
                        cartListTransaction.commit();
                        show=false;
                        showOrHide(CartList.this);
                    }
                }
            }

        });

    }

    public static void showOrHide(Activity activity){
        if(show){
            imgDelete.setVisibility(View.GONE);
            cvCartPayment.setVisibility(View.GONE);
            cvCartList.setVisibility(View.VISIBLE);
        } else {
            dbCart = new Cart(activity);
            if(dbCart.getItemCount()>0){
                imgDelete.setVisibility(View.VISIBLE);
            } else {
                imgDelete.setVisibility(View.GONE);
            }
            cvCartList.setVisibility(View.GONE);
            cvCartPayment.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        show=false;
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.cvCartList:
                CartListItem cartList = new CartListItem();
                FragmentTransaction cartListTransaction = getSupportFragmentManager()
                        .beginTransaction();
                cartListTransaction.setCustomAnimations(
                        R.anim.anim_slide_in_up, R.anim.anim_slide_out_down);
                cartListTransaction.replace(R.id.layoutCart, cartList);
                cartListTransaction.commit();
                show=false;
                showOrHide(CartList.this);
                break;

            case R.id.cvCartPayment:
                CartPayment cartPayment = new CartPayment();
                FragmentTransaction cartPaymentTransaction = getSupportFragmentManager()
                        .beginTransaction();
                cartPaymentTransaction.setCustomAnimations(
                        R.anim.anim_slide_in_down, R.anim.anim_slide_out_up);
                cartPaymentTransaction.replace(R.id.layoutCart, cartPayment);
                cartPaymentTransaction.commit();
                show=true;
                showOrHide(CartList.this);
                break;

            case R.id.imgDeleteCart:
                new AlertDialog.Builder(this)
                        .setTitle("Hapus Keranjang Belanja")
                        .setMessage("Apakah Anda ingin menghapus Keranjang Belanja?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                resetCart(CartList.this);
                                finish();
                            }
                        }).create().show();

                break;
        }

    }

    public static void enabledButton(Activity activity){
        if(SavePref.readCustomerId(activity)!=null
                && SavePref.readCustomer(activity)!=null){
            cvCartPayment.setEnabled(true);
            cvCartPayment.setCardBackgroundColor(
                    activity.getResources().getColor(R.color.colorThemeOrange));

        } else {
            cvCartPayment.setEnabled(false);
            cvCartPayment.setCardBackgroundColor(
                    activity.getResources().getColor(R.color.colorThemeGrey));
        }
    }

    public static void resetCart(Activity activity){
        dbCart = new Cart(activity);
        dbCart.deleteAllItem();
        CartListItem.cartModels.clear();
        CartListItem.cartAdapter.notifyDataSetChanged();
        Dashboard.productDashboardAdapter.notifyDataSetChanged();
        Dashboard.setTotal(activity, dbCart);
        CartListItem.setTotalAmount(activity);
        SavePref.saveCustomer(activity, null);
        SavePref.saveCustomerId(activity, null );
    }
}
