package co.crowde.toni.view.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.adapter.ProductDashboardAdapter;
import co.crowde.toni.adapter.ProductInventoryAdapter;
import co.crowde.toni.controller.main.ProductController;
import co.crowde.toni.controller.network.ProductRequest;
import co.crowde.toni.model.ProductModel;
import co.crowde.toni.view.main.CatalogProduct;
import co.crowde.toni.view.popup.FilterInventoryPopup;
import co.crowde.toni.view.popup.FilterProductDashboardPopup;

/**
 * A simple {@link Fragment} subclass.
 */
public class Inventory extends Fragment {

    public static EditText etSearch;
    public static ImageView imgBtnFilter;
    public static FloatingActionButton btnAddProduct;
    public static RecyclerView rcProduct;
    public static RecyclerView.LayoutManager rcLayoutDashboard;
    public static ProductInventoryAdapter inventoryAdapter;
    public static List<ProductModel> productModels;
    public static List<ProductModel> productModelsFiltered;

    static DividerItemDecoration itemDecorator;

    Drawable close;
    Drawable search;

    public Inventory() {
        // Required empty public constructor
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);

        etSearch = view.findViewById(R.id.etSearchProduct);
        imgBtnFilter = view.findViewById(R.id.imgBtnFilter);
        rcProduct = view.findViewById(R.id.rcProduct);
        btnAddProduct = view.findViewById(R.id.btnAddProduct);

        //Get Product List
        itemDecorator = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(),
                R.drawable.divider_line_item));
        productList(getActivity());

        //Edittext Watcher
        searchProduct(getActivity());

        //Filter Product
        imgBtnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterInventoryPopup.showFilterInventory(getActivity());

            }
        });

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CatalogProduct.showCatalog(getActivity(), getContext());
            }
        });

        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void searchProduct(Activity activity){
        close = getContext().getResources().getDrawable( R.drawable.ic_close_black_24dp );
        search = getContext().getResources().getDrawable( R.drawable.ic_search_black_24dp );
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
        if(ProductController.categoryInventory.isEmpty()){
            ProductRequest.getInventoryList(activity);
            productModels = new ArrayList<>();
            inventoryAdapter = new ProductInventoryAdapter(activity,
                    productModels, activity);
            rcProduct.addItemDecoration(itemDecorator);
            rcProduct.setLayoutManager(new LinearLayoutManager(activity));
            rcProduct.setAdapter(inventoryAdapter);
            imgBtnFilter.setBackground(
                    activity.getResources().getDrawable(R.color.colorWhite));
            imgBtnFilter.setImageDrawable(
                    activity.getResources().getDrawable(R.drawable.ic_filter_list_black_24dp));

        } else {
            inventoryAdapter = new ProductInventoryAdapter(activity,
                    productModelsFiltered, activity);
            rcProduct.addItemDecoration(itemDecorator);
            rcProduct.setLayoutManager(new LinearLayoutManager(activity));
            rcProduct.setAdapter(inventoryAdapter);
            imgBtnFilter.setBackground(
                    activity.getResources().getDrawable(R.drawable.bg_rec_radius_5dp_green));
            imgBtnFilter.setImageDrawable(
                    activity.getResources().getDrawable(R.drawable.ic_filter_list_white_24dp));
        }
    }

    public TextWatcher searchWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            inventoryAdapter.getFilter().filter(s);

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
