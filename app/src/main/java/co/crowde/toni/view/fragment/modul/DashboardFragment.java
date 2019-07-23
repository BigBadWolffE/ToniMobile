package co.crowde.toni.view.fragment.modul;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.adapter.ProductDashboardAdapter;
import co.crowde.toni.constant.Const;
import co.crowde.toni.helper.CloseSoftKeyboard;
import co.crowde.toni.listener.ItemClickListener;
import co.crowde.toni.network.ProductRequest;
import co.crowde.toni.database.Cart;
import co.crowde.toni.model.CartModel;
import co.crowde.toni.model.ProductModel;
import co.crowde.toni.utils.analytics.AnalyticsToniUtils;
import co.crowde.toni.view.activity.filter.DashboardFilterActivity;
import co.crowde.toni.view.activity.cart.CartListActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static TextView tvEmptyField;
    public static EditText etSearch;
    public static ImageView imgBtnFilter;
    public static RecyclerView rcProduct;
    public static ProductDashboardAdapter productDashboardAdapter;
    public static List<ProductModel> productModels = new ArrayList<>();

    public  static CardView cvBtnCart;

    static Drawable close;
    static Drawable search;
    static Drawable filter, filtered;

    //Database Keranjang
    public static Cart dbCart;
    public static List<CartModel> cartModels = new ArrayList<>();
    public static int totalItem;
    public static int totalAmount;
    public static ItemClickListener listener;

    public static CartModel cartModel_;
    public static CartModel cartModel;

    public static TextView tvQty, tvAmount;

    private static DecimalFormat formatNumber;

    public static ProgressDialog progressDialog;

    public static String categoryId = "";
    public static String productName = "";
    public static String status = "";

    public DashboardFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View  view =  inflater.inflate(R.layout.fragment_dashboard, container, false);

        progressDialog = new ProgressDialog(getActivity());

        tvEmptyField = view.findViewById(R.id.tv_empty_field);
        etSearch = view.findViewById(R.id.etSearchProduct);
        imgBtnFilter = view.findViewById(R.id.imgBtnFilter);
        rcProduct = view.findViewById(R.id.rcProduct);
        cvBtnCart = view.findViewById(R.id.cvBtnCart);
        tvQty = view.findViewById(R.id.tvQty);
        tvAmount = view.findViewById(R.id.tvAmount);

        formatNumber = new DecimalFormat("###,###,###,###,###,###");

        initAdapter(getActivity());
        requestFilter(getActivity());
        initScrollListener(getActivity());
        showFloatingCart();

        //Edittext Watcher
        searchProduct(getActivity(), getContext());

        //Filter Product
        imgBtnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent filter = new Intent(getActivity(), DashboardFilterActivity.class);
                startActivity(filter);
            }
        });

        ifCartEmpty(getActivity());

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
        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    requestFilter(activity);
                }

            }
        });
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                requestFilter(activity);
                return false;
            }
        });

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
                        if(etSearch.length()>0){
                            etSearch.setText("");
                            event.setAction(MotionEvent.ACTION_CANCEL);
                            requestFilter(activity);
                        }

                        return false;
                    }
                }
                return false;
            }
        });

        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                CloseSoftKeyboard.hideSoftKeyboard(v, activity);
            }
        });
    }

    public TextWatcher searchWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
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

    public static void showListField(Activity activity){
        if(DashboardFragment.productModels.size()!=0){
            tvEmptyField.setVisibility(View.GONE);
        } else {
            tvEmptyField.setVisibility(View.VISIBLE);
        }
    }

    private void showFloatingCart() {
        //Show Cart List
        cvBtnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cart = new Intent(getActivity(), CartListActivity.class);
                startActivity(cart);
            }
        });
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

            tvQty.setText(String.valueOf(totalItem)
                    +" Produk");
            tvAmount.setText("TOTAL : Rp. "
                    +String.valueOf(formatNumber.format(totalAmount))+",-");
        } else {
            totalItem =0;
            totalAmount=0;
            ifCartEmpty(activity);
        }
    }

    public static void initAdapter(Activity activity) {
        productDashboardAdapter = new ProductDashboardAdapter(activity, productModels, activity);

        rcProduct.setLayoutManager(new GridLayoutManager(activity, 2));
        rcProduct.setAdapter(productDashboardAdapter);
    }

    public static void updateDataProduct(List<ProductModel> productModelResponse, int page) {
        if (productModels.size() != 0){
            productModels.remove(productModels.size() - 1);
            int scrollPosition = productModels.size();
            productDashboardAdapter.notifyItemRemoved(scrollPosition);
        }

        if (page == 1)
            productModels.clear();
        productModels.addAll(productModelResponse);
        productDashboardAdapter.replaceItemFiltered(productModels);
//        inventoryAdapter.notifyDataSetChanged();
        isLoading = false;

        if (page == 1)
            if (productModels.size() > 0)
                rcProduct.scrollToPosition(0);
    }

    static boolean isLoading = false;

    public static void initScrollListener(final Activity activity) {
        rcProduct.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == productModels.size() - 1) {
                        //bottom of list!
                        loadMoreProduct(activity);
                        isLoading = true;
                    }
                }
            }
        });
    }

    public static void loadMoreProduct(Activity activity) {
        productModels.add(null);
        productDashboardAdapter.notifyItemInserted(productModels.size() - 1);

        ProductRequest.page = ProductRequest.page + 1;
        ProductRequest.getProductList(activity);
    }

    public static void requestFilter(final Activity activity){
        progressDialog.setMessage("Harap tunggu...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if(categoryId.equals("")){
            ProductRequest.categoryId = "";
        } else {
            ProductRequest.categoryId = categoryId;
        }

        if(status.equals("")){
            ProductRequest.status = "";
        } else {
            ProductRequest.status = status;
        }

        isFiltered(activity, activity.getBaseContext());

        ProductRequest.page=1;
        ProductRequest.supplierId="";
        ProductRequest.productName = etSearch.getText().toString();
        productModels.clear();

        if(etSearch.getText().toString().length()>0){
            AnalyticsToniUtils.getEvent(Const.CATEGORY_TRANSACTION,Const.MODUL_PRODUCT,Const.LABEL_PRODUCT_SEARCH_DASHBOARD);
        }

        if(ProductRequest.categoryId.length()>1 || ProductRequest.status.length()>1){
            AnalyticsToniUtils.getEvent(Const.CATEGORY_TRANSACTION,Const.MODUL_PRODUCT,Const.LABEL_PRODUCT_FILTER_DASHBOARD);
        }

        ProductRequest.getProductList(activity);

    }

    @SuppressLint("ClickableViewAccessibility")
    public static void isFiltered(final Activity activity, final Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            filter = context.getDrawable(R.drawable.ic_tune_black_24dp);
            filtered = context.getDrawable(R.drawable.ic_tune_white_24dp);
        } else {
            filter = activity.getResources().getDrawable(R.drawable.ic_tune_black_24dp);
            filtered = activity.getResources().getDrawable(R.drawable.ic_tune_white_24dp);
        }

        if(!categoryId.equals("")|| !status.equals("")){
            imgBtnFilter.setImageDrawable(filtered);
            imgBtnFilter.setBackground(activity.getResources().getDrawable(R.drawable.bg_rec_radius_5dp_green));
        } else {
            imgBtnFilter.setImageDrawable(filter);
            imgBtnFilter.setBackgroundColor(activity.getResources().getColor(R.color.colorWhite));
        }

    }


}

