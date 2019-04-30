package co.crowde.toni.view.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;
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

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.adapter.ProductDashboardAdapter;
import co.crowde.toni.controller.main.ProductController;
import co.crowde.toni.controller.network.ProductRequest;
import co.crowde.toni.helper.CloseSoftKeyboard;
import co.crowde.toni.model.ProductModel;
import co.crowde.toni.view.main.MainActivity;
import co.crowde.toni.view.popup.FilterProductDashboardPopup;


/**
 * A simple {@link Fragment} subclass.
 */
public class Dashboard extends Fragment {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static TextView tvDashboard;
    public static EditText etSearch;
    public static ImageView imgBtnFilter;
    public static RecyclerView rcProduct;
    public static RecyclerView.LayoutManager rcLayoutDashboard;
    public static ProductDashboardAdapter productDashboardAdapter;
    public static List<ProductModel> productModels;
    public static List<ProductModel> productModelsFiltered;

    static Drawable close;
    static Drawable search;


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

        //Get Product List
        productList(getActivity());

        //Edittext Watcher
        searchProduct(getActivity(), getContext());

        //Filter Product
        imgBtnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterProductDashboardPopup.showFilterCategory(getActivity());

            }
        });

//        CloseSoftKeyboard.hideSoftKeyboard(view, getActivity());

        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void searchProduct(Activity activity, Context context){
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

                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(event.getRawX() >= (etSearch.getRight() -
                            etSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        etSearch.setText("");
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public static void productList(Activity activity){
        if(ProductController.categoryList.isEmpty()){
            ProductRequest.getProductList(activity);
            productModels = new ArrayList<>();
            productDashboardAdapter = new ProductDashboardAdapter(activity,
                    productModels, activity);
            rcProduct.setLayoutManager(new GridLayoutManager(activity, 2));
            rcProduct.setAdapter(productDashboardAdapter);
            imgBtnFilter.setBackground(
                    activity.getResources().getDrawable(R.color.colorWhite));
            imgBtnFilter.setImageResource(R.drawable.ic_filter_list_black_24dp);

        } else {
            productDashboardAdapter = new ProductDashboardAdapter(activity,
                    productModelsFiltered, activity);
            rcProduct.setLayoutManager(new GridLayoutManager(activity, 2));
            rcProduct.setAdapter(Dashboard.productDashboardAdapter);
            imgBtnFilter.setBackground(
                    activity.getResources().getDrawable(R.drawable.bg_rec_radius_5dp_green));
            imgBtnFilter.setImageResource(R.drawable.ic_filter_list_white_24dp);
        }
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


}
