package co.crowde.toni.view.fragment;


import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import co.crowde.toni.controller.network.ProductRequest;
import co.crowde.toni.model.ProductModel;
import co.crowde.toni.view.popup.FilterInventoryPopup;
import co.crowde.toni.view.popup.FilterProductDashboardPopup;

/**
 * A simple {@link Fragment} subclass.
 */
public class Inventory extends Fragment {

    public static EditText etSearch;
    public static ImageView imgBtnFilter;
    public static RecyclerView rcProduct;
    public static RecyclerView.LayoutManager rcLayoutDashboard;
    public static ProductInventoryAdapter inventoryAdapter;
    public static List<ProductModel> productModels;
    public static List<ProductModel> productModelsFiltered;

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

        //Get Product List
        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(),
                R.drawable.divider_line_item));

        ProductRequest.getInventoryList(getActivity());
        productModels = new ArrayList<>();
        inventoryAdapter = new ProductInventoryAdapter(getActivity(),
                productModels, getActivity());
        rcProduct.addItemDecoration(itemDecorator);
        rcProduct.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcProduct.setAdapter(inventoryAdapter);

        //Edittext Watcher
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

        //Filter Product
        imgBtnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterInventoryPopup.showFilterInventory(getActivity());

            }
        });

        return view;
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
