package co.crowde.toni.view.activity.cart;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;

import co.crowde.toni.R;
import co.crowde.toni.database.Cart;
import co.crowde.toni.helper.OnSwipeTouchListener;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.view.fragment.cart.CartListItemFragment;
import co.crowde.toni.view.fragment.cart.CartPaymentFragment;
import co.crowde.toni.view.fragment.modul.DashboardFragment;

public class CartListActivity extends AppCompatActivity
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
            }
        });

        enabledButton(CartListActivity.this);

        CartListItemFragment cartList = new CartListItemFragment();
        FragmentTransaction cartListTransaction = getSupportFragmentManager()
                .beginTransaction();
        cartListTransaction.replace(R.id.layoutCart, cartList);
        cartListTransaction.commit();
        showOrHide(CartListActivity.this);
        layoutCart.setOnTouchListener(new OnSwipeTouchListener(CartListActivity.this) {
            public void onSwipeTop() {
                if(SavePref.readCustomerId(CartListActivity.this)!=null
                        && SavePref.readCustomer(CartListActivity.this)!=null){
                    if(!show){
                        CartPaymentFragment cartPayment = new CartPaymentFragment();
                        FragmentTransaction cartPaymentTransaction = getSupportFragmentManager()
                                .beginTransaction();
                        cartPaymentTransaction.setCustomAnimations(
                                R.anim.anim_slide_in_down, R.anim.anim_slide_out_up);
                        cartPaymentTransaction.replace(R.id.layoutCart, cartPayment);
                        cartPaymentTransaction.commit();
                        show=true;
                        showOrHide(CartListActivity.this);
                    }
                }
            }
            public void onSwipeRight() {
            }
            public void onSwipeLeft() {
            }
            public void onSwipeBottom() {
                if(SavePref.readCustomerId(CartListActivity.this)!=null
                        && SavePref.readCustomer(CartListActivity.this)!=null){
                    if(show){
                        CartListItemFragment cartList = new CartListItemFragment();
                        FragmentTransaction cartListTransaction = getSupportFragmentManager()
                                .beginTransaction();
                        cartListTransaction.setCustomAnimations(
                                R.anim.anim_slide_in_up, R.anim.anim_slide_out_down);
                        cartListTransaction.replace(R.id.layoutCart, cartList);
                        cartListTransaction.commit();
                        show=false;
                        showOrHide(CartListActivity.this);
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
                CartListItemFragment cartList = new CartListItemFragment();
                FragmentTransaction cartListTransaction = getSupportFragmentManager()
                        .beginTransaction();
                cartListTransaction.setCustomAnimations(
                        R.anim.anim_slide_in_up, R.anim.anim_slide_out_down);
                cartListTransaction.replace(R.id.layoutCart, cartList);
                cartListTransaction.commit();
                show=false;
                showOrHide(CartListActivity.this);
                break;

            case R.id.cvCartPayment:
                CartPaymentFragment cartPayment = new CartPaymentFragment();
                FragmentTransaction cartPaymentTransaction = getSupportFragmentManager()
                        .beginTransaction();
                cartPaymentTransaction.setCustomAnimations(
                        R.anim.anim_slide_in_down, R.anim.anim_slide_out_up);
                cartPaymentTransaction.replace(R.id.layoutCart, cartPayment);
                cartPaymentTransaction.commit();
                show=true;
                showOrHide(CartListActivity.this);
                break;

            case R.id.imgDeleteCart:
                new AlertDialog.Builder(this)
                        .setTitle("Hapus Keranjang Belanja")
                        .setMessage("Apakah Anda ingin menghapus Keranjang Belanja?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                resetCart(CartListActivity.this);
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
        CartListItemFragment.cartModels.clear();
        CartListItemFragment.cartAdapter.notifyDataSetChanged();
        DashboardFragment.productDashboardAdapter.notifyDataSetChanged();
        DashboardFragment.setTotal(activity, dbCart);
        CartListItemFragment.setTotalAmount(activity);
        SavePref.saveCustomer(activity, null);
        SavePref.saveCustomerId(activity, null );
    }

    @Override
    public void onBackPressed() {
        SavePref.saveCustomer(CartListActivity.this, null);
        SavePref.saveCustomerId(CartListActivity.this, null);
        show=false;
        super.onBackPressed();
    }
}
