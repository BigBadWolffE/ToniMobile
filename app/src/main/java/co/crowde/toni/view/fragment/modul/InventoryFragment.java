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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.adapter.ProductInventoryAdapter;
import co.crowde.toni.helper.CloseSoftKeyboard;
import co.crowde.toni.network.ProductRequest;
import co.crowde.toni.model.ProductModel;
import co.crowde.toni.view.activity.catalog.CatalogProductActivity;
import co.crowde.toni.view.activity.filter.InventoryFilterActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class InventoryFragment extends Fragment {

    public static TextView tvEmptyField;
    public static EditText etSearch;
    public static ImageView imgBtnFilter;
    public static FloatingActionButton btnAddProduct;
    public static RecyclerView rcProduct;
    public static ProductInventoryAdapter inventoryAdapter;
    public static List<ProductModel> productModels = new ArrayList<>();

    static DividerItemDecoration itemDecorator;

    public static ProgressDialog progressDialog;

    Drawable close;
    Drawable search;

    public InventoryFragment() {
        // Required empty public constructor
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);

        progressDialog = new ProgressDialog(getActivity());

        tvEmptyField = view.findViewById(R.id.tv_empty_field);
        etSearch = view.findViewById(R.id.etSearchProduct);
        imgBtnFilter = view.findViewById(R.id.imgBtnFilter);
        rcProduct = view.findViewById(R.id.rcProduct);
        btnAddProduct = view.findViewById(R.id.btnAddProduct);

        //Get Product List
        itemDecorator = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(),
                R.drawable.divider_line_item));

        initAdapter(getActivity());
        requestFilter(getActivity());
        initScrollListener(getActivity());

        //Edittext Watcher
        searchProduct(getActivity(), getContext());

        //Filter Product
        imgBtnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent filter = new Intent(getActivity(), InventoryFilterActivity.class);
                startActivity(filter);

            }
        });

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CatalogProductActivity.showCatalog(getActivity(), getContext());
                Intent catalog = new Intent(getActivity(), CatalogProductActivity.class);
                startActivity(catalog);
            }
        });

        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void searchProduct(final Activity activity, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            close = context.getDrawable(R.drawable.ic_close_black_24dp);
            search = context.getDrawable(R.drawable.ic_search_black_24dp);
        } else {
            close = activity.getResources().getDrawable(R.drawable.ic_close_black_24dp);
            search = activity.getResources().getDrawable(R.drawable.ic_search_black_24dp);
        }
        etSearch.setCompoundDrawablesWithIntrinsicBounds(null, null, search, null);
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
                    ProductRequest.getInventoryList(activity);
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
                ProductRequest.getInventoryList(activity);
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

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (etSearch.getRight() -
                            etSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (etSearch.length() > 0) {
                            progressDialog = new ProgressDialog(getActivity());
                            progressDialog.setMessage("Harap tunggu...");
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show();
                            etSearch.setText("");
                            event.setAction(MotionEvent.ACTION_CANCEL);
                            ProductRequest.productName = "";
                            ProductRequest.page = 1;
                            ProductRequest.getInventoryList(activity);
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
//            inventoryAdapter.getFilter().filter(s);

            if (etSearch.getText().length() > 0) {
                etSearch.setCompoundDrawablesWithIntrinsicBounds(
                        null, null, close, null);

            } else {
                etSearch.setCompoundDrawablesWithIntrinsicBounds(
                        null, null, search, null);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public static void showListField(Activity activity){
        if(InventoryFragment.productModels.size()!=0){
            tvEmptyField.setVisibility(View.GONE);
        } else {
            tvEmptyField.setVisibility(View.VISIBLE);
        }
    }

    public static void initAdapter(Activity activity) {
        inventoryAdapter = new ProductInventoryAdapter(activity, productModels, activity);

        rcProduct.setLayoutManager(new LinearLayoutManager(activity));
        rcProduct.addItemDecoration(itemDecorator);
        rcProduct.setAdapter(inventoryAdapter);
    }

    public static void updateDataProduct(List<ProductModel> productModelResponse, int page) {
        if (productModels.size() != 0) {
            productModels.remove(productModels.size() - 1);
            int scrollPosition = productModels.size();
            inventoryAdapter.notifyItemRemoved(scrollPosition);
        }

        if (page == 1)
            productModels.clear();
        productModels.addAll(productModelResponse);
        inventoryAdapter.replaceItemFiltered(productModels);
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
        inventoryAdapter.notifyItemInserted(productModels.size() - 1);

        ProductRequest.page = ProductRequest.page + 1;
        ProductRequest.getInventoryList(activity);
    }

    public static void requestFilter(final Activity activity) {
        progressDialog.setMessage("Harap tunggu...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (InventoryFilterActivity.category.size() > 0) {
            StringBuilder buffer = new StringBuilder();
            for (String each : InventoryFilterActivity.category)
                buffer.append(",").append(each);
            ProductRequest.categoryId = buffer.deleteCharAt(0).toString();
        } else {
            ProductRequest.categoryId = "";
        }

        if (InventoryFilterActivity.statusList.size() > 0) {
            StringBuilder buffer1 = new StringBuilder();
            for (String each : InventoryFilterActivity.statusList)
                buffer1.append(",").append(each);
            ProductRequest.status = buffer1.deleteCharAt(0).toString();
        } else {
            ProductRequest.status = "";
        }
        ProductRequest.page = 1;
        ProductRequest.supplierId = "";
        ProductRequest.productName = etSearch.getText().toString();
        productModels.clear();

//        inventoryAdapter.replaceItemFiltered(productModels);
//        inventoryAdapter.notifyDataSetChanged();
        ProductRequest.getInventoryList(activity);

    }

}
