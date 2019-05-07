package co.crowde.toni.view.main;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import co.crowde.toni.R;
import co.crowde.toni.helper.OnSwipeTouchListener;
import co.crowde.toni.view.fragment.CartListItem;
import co.crowde.toni.view.fragment.CartPayment;

public class CartList extends AppCompatActivity
implements View.OnClickListener{

    public static CardView cvCartList, cvCartPayment;
    public static Toolbar toolbar;
    public static FrameLayout layoutCart;

    public static boolean show = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);

        cvCartList = findViewById(R.id.cvCartList);
        cvCartPayment = findViewById(R.id.cvCartPayment);
        toolbar = findViewById(R.id.toolbarCart);
        layoutCart = findViewById(R.id.layoutCart);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));

        cvCartList.setOnClickListener(this);
        cvCartPayment.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                show=false;
            }
        });

        CartListItem cartList = new CartListItem();
        FragmentTransaction cartListTransaction = getSupportFragmentManager()
                .beginTransaction();
        cartListTransaction.replace(R.id.layoutCart, cartList);
        cartListTransaction.commit();
        showOrHide();
        layoutCart.setOnTouchListener(new OnSwipeTouchListener(CartList.this) {
            public void onSwipeTop() {
                if(!show){
                    CartPayment cartPayment = new CartPayment();
                    FragmentTransaction cartPaymentTransaction = getSupportFragmentManager()
                            .beginTransaction();
                    cartPaymentTransaction.setCustomAnimations(
                            R.anim.anim_slide_in_down, R.anim.anim_slide_out_up);
                    cartPaymentTransaction.replace(R.id.layoutCart, cartPayment);
                    cartPaymentTransaction.commit();
                    show=true;
                    showOrHide();
                } else {

                }
            }
            public void onSwipeRight() {
                Toast.makeText(CartList.this, "Right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
                Toast.makeText(CartList.this, "Left", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeBottom() {
                if(show){
                    CartListItem cartList = new CartListItem();
                    FragmentTransaction cartListTransaction = getSupportFragmentManager()
                            .beginTransaction();
                    cartListTransaction.setCustomAnimations(
                            R.anim.anim_slide_in_up, R.anim.anim_slide_out_down);
                    cartListTransaction.replace(R.id.layoutCart, cartList);
                    cartListTransaction.commit();
                    show=false;
                    showOrHide();
                }
            }

        });

    }

    public static void showOrHide(){
        if(show){
            cvCartPayment.setVisibility(View.GONE);
            cvCartList.setVisibility(View.VISIBLE);
        } else {
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
                showOrHide();
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
                showOrHide();
                break;
        }

    }
}
