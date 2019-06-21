package co.crowde.toni.view.activity.catalog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.adapter.CatalogProductAdapter;
import co.crowde.toni.network.CatalogRequest;
import co.crowde.toni.helper.CloseSoftKeyboard;
import co.crowde.toni.model.CatalogModel;
import co.crowde.toni.view.activity.filter.CatalogFilterActivity;
import co.crowde.toni.view.dialog.message.catalog.AddCatalogDialog;
import co.crowde.toni.view.fragment.modul.DashboardFragment;

public class CatalogProductActivity extends AppCompatActivity {

    public static TextView tvEmptyField;
    public static ImageView imgBtnBack,
            imgBtnFilter;
    public static EditText etSearchProduct;
    public static RecyclerView rcProduct;
    public static CatalogProductAdapter catalogProductAdapter;
    public static List<CatalogModel> productModels = new ArrayList<>();
    public static CardView cvBtnAddProduct;
    public static Toolbar toolbar;

    public static Dialog dialog;

    static DividerItemDecoration itemDecorator;

    public static ProgressDialog progressDialog;

    Drawable close;
    Drawable search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
//        if (tabletSize) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        } else {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }
        setContentView(R.layout.activity_catalog_product);

        progressDialog = new ProgressDialog(this);

        tvEmptyField = findViewById(R.id.tv_empty_field);
        imgBtnBack = findViewById(R.id.imgBtnBack);
        imgBtnFilter = findViewById(R.id.imgBtnFilter);
        etSearchProduct = findViewById(R.id.etSearchProduct);
        rcProduct = findViewById(R.id.rcProduct);
        cvBtnAddProduct = findViewById(R.id.cvBtnAddProduct);
        toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_close_black_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Get Product List
        itemDecorator = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(this,
                R.drawable.divider_line_item));

        initAdapter(CatalogProductActivity.this);
        requestFilter(CatalogProductActivity.this);
        initScrollListener(CatalogProductActivity.this);

        //Edittext Watcher
        searchProduct(this, this);

        //Filter Product
        imgBtnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FilterInventoryPopup.showFilterInventory(getActivity());
                Intent filter = new Intent(CatalogProductActivity.this, CatalogFilterActivity.class);
                startActivity(filter);

            }
        });

        hideKeyboard(this);
        
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
        etSearchProduct.setCompoundDrawablesWithIntrinsicBounds(null,null,search,null);
        etSearchProduct.addTextChangedListener(searchWatcher);
        etSearchProduct.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    progressDialog = new ProgressDialog(activity);
                    progressDialog.setMessage("Harap tunggu...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    CatalogRequest.productName = etSearchProduct.getText().toString();
                    CatalogRequest.page = 1;
                    productModels.clear();
                    CatalogRequest.getCatalogList(activity);
                }

            }
        });
        etSearchProduct.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                progressDialog = new ProgressDialog(activity);
                progressDialog.setMessage("Harap tunggu...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                CatalogRequest.productName = etSearchProduct.getText().toString();
                CatalogRequest.page = 1;
                productModels.clear();
                CatalogRequest.getCatalogList(activity);
                return false;
            }
        });

        etSearchProduct.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (etSearchProduct.getRight() -
                            etSearchProduct.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if(etSearchProduct.length()>0){
                            progressDialog = new ProgressDialog(activity);
                            progressDialog.setMessage("Harap tunggu...");
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show();
                            etSearchProduct.setText("");
                            event.setAction(MotionEvent.ACTION_CANCEL);
                            CatalogRequest.productName ="";
                            CatalogRequest.page = 1;
                            CatalogRequest.getCatalogList(activity);
                        }

                        return false;
                    }
                }
                return false;
            }
        });

        etSearchProduct.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

            if (etSearchProduct.getText().length() > 0) {
                etSearchProduct.setCompoundDrawablesWithIntrinsicBounds(
                        null, null, close, null);

            } else {
                etSearchProduct.setCompoundDrawablesWithIntrinsicBounds(
                        null,null,search,null);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public static void showListField(Activity activity){
        if(CatalogProductActivity.productModels.size()!=0){
            tvEmptyField.setVisibility(View.GONE);
        } else {
            tvEmptyField.setVisibility(View.VISIBLE);
        }
    }


    public static void setEnabledButton(final Activity activity, int count){
        if(count>0){
            cvBtnAddProduct.setEnabled(true);
            cvBtnAddProduct.setCardBackgroundColor(activity.getResources().getColor(R.color.colorThemeOrange));

            cvBtnAddProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddCatalogDialog.showDialog(activity);
                }
            });

        } else {
            cvBtnAddProduct.setEnabled(false);
            cvBtnAddProduct.setCardBackgroundColor(activity.getResources().getColor(R.color.colorThemeGrey));
        }
    }

    public static void hideKeyboard(final Activity activity){
        etSearchProduct.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                CloseSoftKeyboard.hideSoftKeyboard(v, activity);
            }
        });

    }

    public static void initAdapter(Activity activity) {
        catalogProductAdapter = new CatalogProductAdapter(activity, productModels, activity);

        rcProduct.setLayoutManager(new LinearLayoutManager(activity));
        rcProduct.addItemDecoration(itemDecorator);
        rcProduct.setAdapter(catalogProductAdapter);
    }

    public static void updateDataProduct(List<CatalogModel> productModelResponse, int page) {
        if (productModels.size() != 0) {
            productModels.remove(productModels.size() - 1);
            int scrollPosition = productModels.size();
            catalogProductAdapter.notifyItemRemoved(scrollPosition);
        }

        if (page == 1)
            productModels.clear();
        productModels.addAll(productModelResponse);
        catalogProductAdapter.replaceItemFiltered(productModels);
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
        catalogProductAdapter.notifyItemInserted(productModels.size() - 1);

        CatalogRequest.page = CatalogRequest.page + 1;
        CatalogRequest.getCatalogList(activity);
    }

    public static void requestFilter(final Activity activity) {
        progressDialog.setMessage("Harap tunggu...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (CatalogFilterActivity.category.size() > 0) {
            StringBuilder buffer = new StringBuilder();
            for (String each : CatalogFilterActivity.category)
                buffer.append(",").append(each);
            CatalogRequest.categoryId = buffer.deleteCharAt(0).toString();
        } else {
            CatalogRequest.categoryId = "";
        }

        if (CatalogFilterActivity.supplier.size() > 0) {
            StringBuilder buffer1 = new StringBuilder();
            for (String each : CatalogFilterActivity.supplier)
                buffer1.append(",").append(each);
            CatalogRequest.supplierId = buffer1.deleteCharAt(0).toString();
        } else {
            CatalogRequest.supplierId = "";
        }
        CatalogRequest.page = 1;
        CatalogRequest.productName = etSearchProduct.getText().toString();
        productModels.clear();

//        inventoryAdapter.replaceItemFiltered(productModels);
//        inventoryAdapter.notifyDataSetChanged();
        CatalogRequest.getCatalogList(activity);

    }

}
