package co.crowde.toni.view.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.adapter.ProductDashboardAdapter;
import co.crowde.toni.controller.network.ProductRequest;
import co.crowde.toni.helper.CloseSoftKeyboard;
import co.crowde.toni.model.ProductModel;
import co.crowde.toni.view.main.MainActivity;
import co.crowde.toni.view.popup.FilterProductDashboardPopup;


/**
 * A simple {@link Fragment} subclass.
 */
public class Dashboard extends Fragment {

    public static TextView tvDashboard;
    public static EditText etSearch;
    public static ImageView imgBtnFilter;
    public static RecyclerView rcProduct;
    public static RecyclerView.LayoutManager rcLayoutDashboard;
    public static ProductDashboardAdapter productDashboardAdapter;
    public static List<ProductModel> productModels;
    public static List<ProductModel> productModelsFiltered;

    Drawable close;
    Drawable search;


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
        ProductRequest.getProductList(getActivity());
        productModels = new ArrayList<>();
        productDashboardAdapter = new ProductDashboardAdapter(getActivity(),
                productModels, getActivity());
        rcProduct.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rcProduct.setAdapter(productDashboardAdapter);

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
                FilterProductDashboardPopup.showFilterCategory(getActivity());

            }
        });

//        CloseSoftKeyboard.hideSoftKeyboard(view, getActivity());

        return view;
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
