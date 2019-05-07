package co.crowde.toni.view.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.adapter.CartAdapter;
import co.crowde.toni.adapter.ProductDashboardAdapter;
import co.crowde.toni.controller.main.ProductController;
import co.crowde.toni.controller.network.CategoryRequest;
import co.crowde.toni.controller.network.ProductRequest;
import co.crowde.toni.database.Cart;
import co.crowde.toni.helper.CloseSoftKeyboard;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.listener.ProductListener;
import co.crowde.toni.model.CartModel;
import co.crowde.toni.model.ProductModel;
import co.crowde.toni.view.main.CartList;
import co.crowde.toni.view.main.MainActivity;
import co.crowde.toni.view.main.SelectCustomer;
import co.crowde.toni.view.popup.FilterProductDashboardPopup;
import co.crowde.toni.view.popup.ProductDetailDashboardPopup;


/**
 * A simple {@link Fragment} subclass.
 */
public class Dashboard extends Fragment {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static EditText etSearch;
    public static ImageView imgBtnFilter;
    public static RecyclerView rcProduct;
    public static ProductDashboardAdapter productDashboardAdapter;
    public static List<ProductModel> productModels;
    public static List<ProductModel> productModelsFiltered;

    public  static CardView cvBtnCart;

    static Drawable close;
    static Drawable search;

    //Database Keranjang
    public static Cart dbCart;
    public static List<CartModel> cartModels = new ArrayList<>();
    public static int totalItem;
    public static int totalAmount;
    public static ProductListener listener;

    public static CartModel cartModel_;
    public static CartModel cartModel;

    static int qty = 0;
    static int jumlah =0;

    public static TextView tvQty, tvAmount;

    private static DecimalFormat formatNumber;

    public Dashboard() {
        // Required empty public constructor
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View  view =  inflater.inflate(R.layout.fragment_dashboard, container, false);

        etSearch = view.findViewById(R.id.etSearchProduct);
        imgBtnFilter = view.findViewById(R.id.imgBtnFilter);
        rcProduct = view.findViewById(R.id.rcProduct);
        cvBtnCart = view.findViewById(R.id.cvBtnCart);
        tvQty = view.findViewById(R.id.tvQty);
        tvAmount = view.findViewById(R.id.tvAmount);

        formatNumber = new DecimalFormat("###,###,###,###,###,###");

        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            productListLanscape(getActivity());
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            productListPotrait(getActivity());
            showFloatingCart();
        }
        //Edittext Watcher
        searchProduct(getActivity(), getContext());

        //Filter Product
        imgBtnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterProductDashboardPopup.showFilterCategory(getActivity());

            }
        });

        ifCartEmpty(getActivity());

//        CloseSoftKeyboard.hideSoftKeyboard(view, getActivity());

        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void searchProduct(final Activity activity, final Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            close = context.getDrawable(R.drawable.ic_close_black_24dp);
            search = context.getDrawable(R.drawable.ic_search_black_24dp);
        } else {
            close = activity.getResources().getDrawable(R.drawable.ic_close_black_24dp);
            search = activity.getResources().getDrawable(R.drawable.ic_search_black_24dp);
        }
        etSearch.setCompoundDrawablesWithIntrinsicBounds(null,null,search,null);
        etSearch.addTextChangedListener(searchWatcher);
        etSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (etSearch.getRight() -
                            etSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        etSearch.setText("");
                        event.setAction(MotionEvent.ACTION_CANCEL);
                        return false;
                    }
                }
                return false;
            }
        });
    }

    public TextWatcher searchWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            productDashboardAdapter.getFilter().filter(s);

            if (etSearch.getText().length() > 0) {
                etSearch.setCompoundDrawablesWithIntrinsicBounds(
                        null, null, close, null);

            } else {
                etSearch.setCompoundDrawablesWithIntrinsicBounds(
                        null,null,search,null);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public static void productListPotrait(final Activity activity){
        if(ProductController.categoryList.isEmpty()){
            ProductRequest.getProductList(activity);
            productModels = new ArrayList<>();
            productDashboardAdapter = new ProductDashboardAdapter(activity,
                    productModels, listener);
            productDashboardAdapter = new ProductDashboardAdapter(activity,
                    productModels, new ProductListener() {
                @Override
                public void onItemClick(View v, int position) {
                    ProductDetailDashboardPopup.showPopup(activity, productModels.get(position));
                }

                @Override
                public void onIncreaseItem(View v, int position) {

                }

                @Override
                public void onDecreaseItem(View v, int position) {

                }

                @Override
                public void onChangeQty(View v, int position) {
                }
            });
            rcProduct.setLayoutManager(new GridLayoutManager(activity, 2));
            rcProduct.setAdapter(productDashboardAdapter);
            imgBtnFilter.setBackground(
                    activity.getResources().getDrawable(R.color.colorWhite));
            imgBtnFilter.setImageResource(R.drawable.ic_tune_black_24dp);

        } else {
            productDashboardAdapter = new ProductDashboardAdapter(activity,
                    productModelsFiltered, listener);
            productDashboardAdapter = new ProductDashboardAdapter(activity,
                    productModelsFiltered, new ProductListener() {
                @Override
                public void onItemClick(View v, int position) {
                    ProductDetailDashboardPopup.showPopup(activity, productModelsFiltered.get(position));
                }

                @Override
                public void onIncreaseItem(View v, int position) {

                }

                @Override
                public void onDecreaseItem(View v, int position) {

                }

                @Override
                public void onChangeQty(View v, int position) {

                }
            });
            rcProduct.setLayoutManager(new GridLayoutManager(activity, 2));
            rcProduct.setAdapter(Dashboard.productDashboardAdapter);
            imgBtnFilter.setBackground(
                    activity.getResources().getDrawable(R.drawable.bg_rec_radius_5dp_green));
            imgBtnFilter.setImageResource(R.drawable.ic_tune_white_24dp);
        }
    }
    private void showFloatingCart() {
        //Show Cart List
        cvBtnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cart = new Intent(getActivity(), CartList.class);
                startActivity(cart);
            }
        });
    }


    public static void productListLanscape(Activity activity){
        if(ProductController.categoryList.isEmpty()){
            ProductRequest.getProductList(activity);
            productModels = new ArrayList<>();
            productDashboardAdapter = new ProductDashboardAdapter(activity,
                    productModels, listener);
            rcProduct.setLayoutManager(new GridLayoutManager(activity, 3));
            rcProduct.setAdapter(productDashboardAdapter);
            imgBtnFilter.setBackground(
                    activity.getResources().getDrawable(R.color.colorWhite));
            imgBtnFilter.setImageResource(R.drawable.ic_tune_black_24dp);

        } else {
            productDashboardAdapter = new ProductDashboardAdapter(activity,
                    productModelsFiltered, listener);
            rcProduct.setLayoutManager(new GridLayoutManager(activity, 3));
            rcProduct.setAdapter(Dashboard.productDashboardAdapter);
            imgBtnFilter.setBackground(
                    activity.getResources().getDrawable(R.drawable.bg_rec_radius_5dp_green));
            imgBtnFilter.setImageResource(R.drawable.ic_tune_white_24dp);
        }
    }

    public static void ifCartEmpty(Activity activity){
        dbCart = new Cart(activity);
        cartModels.clear();
        cartModels.addAll(dbCart.getAllItem());
        if(dbCart.getItemCount()>0){
            cvBtnCart.setVisibility(View.VISIBLE);
            setTotal(activity, dbCart);

        } else {
            cvBtnCart.setVisibility(View.GONE);
        }

    }

    public static void setTotal(Activity activity,Cart db) {
        if(db.getItemCount()>0){
            totalItem =0;
            for(int i=0;i<dbCart.getItemCount(); i++){
                totalItem = totalItem +(cartModels.get(i).getQuantity());
            }
            totalAmount=0;
            for(int i=0;i<dbCart.getItemCount(); i++){
                totalAmount = totalAmount +(cartModels.get(i).getAmount());
            }

            tvQty.setText(String.valueOf(db.getItemCount())
                    +" Produk");
            tvAmount.setText("TOTAL : Rp. "
                    +String.valueOf(formatNumber.format(totalAmount))+",-");
        } else {
            ifCartEmpty(activity);
        }
    }


}

