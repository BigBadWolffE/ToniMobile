package co.crowde.toni.view.fragment.modul;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.adapter.ProductDashboardAdapter;
import co.crowde.toni.helper.CloseSoftKeyboard;
import co.crowde.toni.listener.ItemClickListener;
import co.crowde.toni.network.ProductRequest;
import co.crowde.toni.database.Cart;
import co.crowde.toni.listener.ProductListener;
import co.crowde.toni.model.CartModel;
import co.crowde.toni.model.ProductModel;
import co.crowde.toni.view.activity.filter.DashboardFilter;
import co.crowde.toni.view.activity.cart.CartList;


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

    public  static CardView cvBtnCart;

    static Drawable close;
    static Drawable search;

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

//        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
//        if (tabletSize) {
//            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//            productListLanscape(getActivity());
//        } else {
//            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//            productListPotrait(getActivity());
//            showFloatingCart();
//        }


        progressDialog = new ProgressDialog(getActivity());
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
                Intent filter = new Intent(getActivity(), DashboardFilter.class);
                startActivity(filter);
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
        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Harap tunggu...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    ProductRequest.productName = etSearch.getText().toString();
                    ProductRequest.page = 1;
                    productModels.clear();
                    ProductRequest.getProductList(activity);
                }

            }
        });
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Harap tunggu...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                ProductRequest.productName = etSearch.getText().toString();
                ProductRequest.page = 1;
                productModels.clear();
                ProductRequest.getProductList(activity);
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
                            progressDialog = new ProgressDialog(getActivity());
                            progressDialog.setMessage("Harap tunggu...");
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show();
                            etSearch.setText("");
                            event.setAction(MotionEvent.ACTION_CANCEL);
                            ProductRequest.productName ="";
                            ProductRequest.page = 1;
                            ProductRequest.getProductList(activity);
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
//            productDashboardAdapter.getFilter().filter(s);
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
        productModels = new ArrayList<>();
        productDashboardAdapter = new ProductDashboardAdapter(activity, productModels, activity);

        rcProduct.setLayoutManager(new GridLayoutManager(activity, 2));
        rcProduct.setAdapter(productDashboardAdapter);
    }

    public static void updateDataProduct(List<ProductModel> productModelResponse) {
        if (productModels.size() != 0){
            productModels.remove(productModels.size() - 1);
            int scrollPosition = productModels.size();
            productDashboardAdapter.notifyItemRemoved(scrollPosition);
        }

        productModels.addAll(productModelResponse);
        productDashboardAdapter.notifyDataSetChanged();
        isLoading = false;
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

//        if(DashboardFilter.category.size()>0){
//            StringBuilder buffer = new StringBuilder();
//            for (String each : DashboardFilter.category)
//                buffer.append(",").append(each);
//            ProductRequest.categoryId = buffer.deleteCharAt(0).toString();
//        } else {
//            ProductRequest.categoryId = "";
//        }

//        if(DashboardFilter.statusList.size()>0){
//            StringBuilder buffer1 = new StringBuilder();
//            for (String each : DashboardFilter.statusList)
//                buffer1.append(",").append(each);
//            ProductRequest.status= buffer1.deleteCharAt(0).toString();
//        } else {
//            ProductRequest.status = "";
//        }

        ProductRequest.categoryId = "";
        ProductRequest.status = "";
        ProductRequest.page=1;
        ProductRequest.supplierId="";
        ProductRequest.productName=etSearch.getText().toString();
        ProductRequest.productName = etSearch.getText().toString();
        ProductRequest.getProductList(activity);

    }


}

