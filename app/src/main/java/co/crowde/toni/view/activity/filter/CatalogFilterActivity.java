package co.crowde.toni.view.activity.filter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.adapter.chips.CategoryChipsFilterAdapter;
import co.crowde.toni.helper.CloseSoftKeyboard;
import co.crowde.toni.listener.ChipsFilterListener;
import co.crowde.toni.model.CategoryModel;
import co.crowde.toni.model.response.list.SupplierModel;
import co.crowde.toni.network.CategoryRequest;
import co.crowde.toni.network.SupplierRequest;
import co.crowde.toni.utils.SetHeader;
import co.crowde.toni.view.activity.catalog.CatalogProductActivity;
import co.crowde.toni.view.fragment.modul.InventoryFragment;

public class CatalogFilterActivity extends AppCompatActivity implements View.OnClickListener {

//    public static TextView tvSupplierLabel, tvCategoryLabel;
//    public static ChipGroup chipSupplier, chipHistorySupplier, chipCategory, chipHistoryCategory;
//    public static AutoCompleteTextView etSupplier, etCategory;
//    public static Toolbar toolbarFilter;
//
//    public static ProgressDialog progressDialog;
//
//    public static List<SupplierModel> suppliers = new ArrayList<>();
//    public static ArrayList<SupplierModel> supplierModels = new ArrayList<>();
//    public static ArrayList<String> supplier = new ArrayList<>();
//
//    public static List<CategoryModel> categories = new ArrayList<>();
//    public static ArrayList<CategoryModel> categoryModels = new ArrayList<>();
//    public static ArrayList<String> category = new ArrayList<>();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_catalog_filter);
//
//        toolbarFilter = findViewById(R.id.toolbarFilter);
//        tvSupplierLabel = findViewById(R.id.tvSupplierLabel);
//        chipSupplier = findViewById(R.id.chipSupplier);
//        etSupplier = findViewById(R.id.etSupplier);
//        chipHistorySupplier = findViewById(R.id.chipHistorySupplier);
//        tvCategoryLabel = findViewById(R.id.tvCategoryLabel);
//        chipCategory = findViewById(R.id.chipCategory);
//        chipHistoryCategory = findViewById(R.id.chipHistoryCategory);
//        etCategory = findViewById(R.id.etCategory);
//
//        progressDialog = new ProgressDialog(this);
//
//        setSupportActionBar(toolbarFilter);
//
//        toolbarFilter.setNavigationIcon(getResources().getDrawable(R.drawable.ic_close_black_24dp));
//        toolbarFilter.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//
//        etSupplier.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                CloseSoftKeyboard.hideSoftKeyboard(v, CatalogFilterActivity.this);
//            }
//        });
//
//        etCategory.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                CloseSoftKeyboard.hideSoftKeyboard(v, CatalogFilterActivity.this);
//            }
//        });
//
//        supplierModels = new ArrayList<>();
//        categoryModels = new ArrayList<>();
//        loadData(this);
//
//        autoTextViewCompleteSupplier(this);
//        autoTextViewCompleteCategory(this);
//
//    }
//
//    private void autoTextViewCompleteSupplier(final Activity activity) {
//        final ArrayAdapter<SupplierModel> adapter =
//                new ArrayAdapter<SupplierModel>(this, android.R.layout.simple_list_item_1, supplierModels);
//        etSupplier.setAdapter(adapter);
//
//        if(suppliers.size()>0){
//            for (int i=0; i<suppliers.size();i++){
//                final Chip chip = new Chip(activity);
//                chip.setId(i);
//                chip.setTag(i);
//
//                chip.setText(suppliers.get(i).getSupplierName());
//                chip.setCloseIconVisible(true);
//                chip.setCheckable(false);
//
//                chip.setCloseIconTintResource(R.color.colorWhite);
//                chip.setChipBackgroundColorResource(R.color.colorThemeGreen);
//                chip.setTextColor(getResources().getColor(R.color.colorWhite));
//                chip.setOnCloseIconClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        int tag = (int) v.getTag();
//                        chipSupplier.removeView(v);
//                        supplier.remove(tag);
//                        suppliers.remove(tag);
//                    }
//                });
//                chipSupplier.addView(chip);
//            }
//
//        }
//
//        etSupplier.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                if(suppliers.size()>0){
//                    for(int i=0; i<suppliers.size();i++){
//                        if(etCategory.getText().toString().equals(suppliers.get(i).getSupplierName())){
//                            Toast.makeText(activity, "Kategori sudah terpilih.", Toast.LENGTH_SHORT).show();
//                        } else {
//                            String str = new Gson().toJson(etCategory.getAdapter().getItem(position));
//                            try {
//                                JSONObject obj = new JSONObject(str);
//                                suppliers.add(new SupplierModel(obj.getString("supplierId"), obj.getString("supplierName")));
//                                supplier.add(obj.getString("supplierId"));
//
//                                final Chip chip = new Chip(activity);
//                                chip.setId(i);
//                                chip.setTag(i);
//
//                                chip.setText(suppliers.get(i).getSupplierName());
//                                chip.setCloseIconVisible(true);
//                                chip.setCheckable(false);
//
//                                chip.setCloseIconTintResource(R.color.colorWhite);
//                                chip.setChipBackgroundColorResource(R.color.colorThemeGreen);
//                                chip.setTextColor(getResources().getColor(R.color.colorWhite));
//                                chip.setOnCloseIconClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        int tag = (int) v.getTag();
//                                        chipSupplier.removeView(v);
//                                        supplier.remove(tag);
//                                        suppliers.remove(tag);
//                                    }
//                                });
//                                chipSupplier.addView(chip);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                } else {
//                    String str = new Gson().toJson(etSupplier.getAdapter().getItem(position));
//                    try {
//                        JSONObject obj = new JSONObject(str);
//                        suppliers.add(new SupplierModel(obj.getString("supplierId"), obj.getString("supplierName")));
//                        supplier.add(obj.getString("supplierId"));
//
//                        for (int i=0; i<suppliers.size();i++){
//                            final Chip chip = new Chip(activity);
//                            chip.setId(i);
//                            chip.setTag(i);
//
//                            chip.setText(suppliers.get(i).getSupplierName());
//                            chip.setCloseIconVisible(true);
//                            chip.setCheckable(false);
//
//                            chip.setCloseIconTintResource(R.color.colorWhite);
//                            chip.setChipBackgroundColorResource(R.color.colorThemeGreen);
//                            chip.setTextColor(getResources().getColor(R.color.colorWhite));
//                            chip.setOnCloseIconClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    int tag = (int) v.getTag();
//                                    chipSupplier.removeView(v);
//                                    supplier.remove(tag);
//                                    suppliers.remove(tag);
//                                }
//                            });
//                            chipSupplier.addView(chip);
//                        }
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                etSupplier.setText("");
//            }
//        });
//    }
//
//    private void autoTextViewCompleteCategory(final Activity activity) {
//        final ArrayAdapter<CategoryModel> adapter =
//                new ArrayAdapter<CategoryModel>(this, android.R.layout.simple_list_item_1, categoryModels);
//        etCategory.setAdapter(adapter);
//
//        if(categories.size()>0){
//            for (int i=0; i<categories.size();i++){
//                final Chip chip = new Chip(activity);
//                chip.setId(i);
//                chip.setTag(i);
//
//                chip.setText(categories.get(i).getCategoryName());
//                chip.setCloseIconVisible(true);
//                chip.setCheckable(false);
//
//                chip.setCloseIconTintResource(R.color.colorWhite);
//                chip.setChipBackgroundColorResource(R.color.colorThemeGreen);
//                chip.setTextColor(getResources().getColor(R.color.colorWhite));
//                chip.setOnCloseIconClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        int tag = (int) v.getTag();
//                        chipCategory.removeView(v);
//                        category.remove(tag);
//                        categories.remove(tag);
//                    }
//                });
//                chipCategory.addView(chip);
//            }
//
//        }
//
//        etCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                if(categories.size()>0){
//                    for(int i=0; i<categories.size();i++){
//                        if(etCategory.getText().toString().equals(categories.get(i).getCategoryName())){
//                            Toast.makeText(activity, "Kategori sudah terpilih.", Toast.LENGTH_SHORT).show();
//                        } else {
//                            String str = new Gson().toJson(etCategory.getAdapter().getItem(position));
//                            try {
//                                JSONObject obj = new JSONObject(str);
//                                categories.add(new CategoryModel(obj.getString("categoryId"), obj.getString("categoryName")));
//                                category.add(obj.getString("categoryId"));
//
//                                final Chip chip = new Chip(activity);
//                                chip.setId(i);
//                                chip.setTag(i);
//
//                                chip.setText(categories.get(i).getCategoryName());
//                                chip.setCloseIconVisible(true);
//                                chip.setCheckable(false);
//
//                                chip.setCloseIconTintResource(R.color.colorWhite);
//                                chip.setChipBackgroundColorResource(R.color.colorThemeGreen);
//                                chip.setTextColor(getResources().getColor(R.color.colorWhite));
//                                chip.setOnCloseIconClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        int tag = (int) v.getTag();
//                                        chipCategory.removeView(v);
//                                        category.remove(tag);
//                                        categories.remove(tag);
//                                    }
//                                });
//                                chipCategory.addView(chip);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                } else {
//                    String str = new Gson().toJson(etCategory.getAdapter().getItem(position));
//                    try {
//                        JSONObject obj = new JSONObject(str);
//                        categories.add(new CategoryModel(obj.getString("categoryId"), obj.getString("categoryName")));
//                        category.add(obj.getString("categoryId"));
//
//                        for (int i=0; i<categories.size();i++){
//                            final Chip chip = new Chip(activity);
//                            chip.setId(i);
//                            chip.setTag(i);
//
//                            chip.setText(categories.get(i).getCategoryName());
//                            chip.setCloseIconVisible(true);
//                            chip.setCheckable(false);
//
//                            chip.setCloseIconTintResource(R.color.colorWhite);
//                            chip.setChipBackgroundColorResource(R.color.colorThemeGreen);
//                            chip.setTextColor(getResources().getColor(R.color.colorWhite));
//                            chip.setOnCloseIconClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    int tag = (int) v.getTag();
//                                    chipCategory.removeView(v);
//                                    category.remove(tag);
//                                    categories.remove(tag);
//                                }
//                            });
//                            chipCategory.addView(chip);
//                        }
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//
//                etCategory.setText("");
//            }
//        });
//    }
//
//    public static void loadData(Activity activity){
//        progressDialog.setMessage("Harap tunggu...");
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();
//        SupplierRequest.getSupplierCatalog(activity);
//        CategoryRequest.getCategoryCatalog(activity);
//    }
//
//    @Override
//    public void onBackPressed() {
//        finish();
//        CatalogProductActivity.requestFilter(CatalogFilterActivity.this);
//    }

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static ProgressDialog progressDialog;

    AppBarLayout appBarLayout;

    //Filter
    CardView cvSetFilter;
    static String categoryId = "";

    public static TextView tvCategory, tvStatus;
    public static Toolbar toolbarFilter;

    public static ArrayList<CategoryModel> categoryModels = new ArrayList<>();

    public static AutoCompleteTextView etCategory;

    ConstraintLayout mainLayout;

    //Category Filter
    static ArrayList<CategoryModel> categoryChips;
    static RecyclerView rcCategory;
    static CategoryChipsFilterAdapter categoryChipsFilterAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog_filter);

        appBarLayout = findViewById(R.id.appBar);
        SetHeader.isLolipop(CatalogFilterActivity.this, appBarLayout);

        categoryChips = new ArrayList<>();

        tvCategory = findViewById(R.id.tvCategoryLabel);
        tvStatus = findViewById(R.id.tvStatus);
        toolbarFilter = findViewById(R.id.toolbarFilter);

        rcCategory = findViewById(R.id.rc_category);

        etCategory = findViewById(R.id.etCategory);
        mainLayout = findViewById(R.id.mainLayout);

        cvSetFilter = findViewById(R.id.cv_btn_set_filter);
        cvSetFilter.setOnClickListener(this);

        setSupportActionBar(toolbarFilter);

        etCategory.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                CloseSoftKeyboard.hideSoftKeyboard(v, CatalogFilterActivity.this);
            }
        });

        toolbarFilter.setNavigationIcon(getResources().getDrawable(R.drawable.ic_close_black_24dp));
        toolbarFilter.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        categoryModels = new ArrayList<>();
        loadCategory(this);

        autoCompleteTextView(this, getBaseContext());



//        filterStatus(DashboardFilterActivity.this);

    }

    void autoCompleteTextView(final Activity activity, Context context) {
        final ArrayAdapter<CategoryModel> adapter =
                new ArrayAdapter<CategoryModel>(this, android.R.layout.simple_list_item_1, categoryModels);
        etCategory.setAdapter(adapter);

        etCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String getId = null, getName = null;
                try {
                    JSONObject obj = new JSONObject(new Gson().toJson(etCategory.getAdapter().getItem(position)));
                    getId = obj.getString("categoryId");
                    getName = obj.getString("categoryName");

                    categoryId = getId;
                    CatalogProductActivity.categoryId = categoryId;

                    categoryChips.add(new CategoryModel(getId,getName));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                etCategory.setText("");
                setChipsCategory(activity);

                etCategory.setEnabled(false);
            }
        });

    }

    static void setChipsCategory(Activity activity){
        categoryChipsFilterAdapter = new CategoryChipsFilterAdapter(activity, categoryChips, activity, new ChipsFilterListener() {
            @Override
            public void onDeleteItemClick(View v, int position) {
                categoryChips.remove(position);
                categoryChipsFilterAdapter.notifyItemRemoved(position);
                categoryChipsFilterAdapter.notifyItemRangeChanged(position, categoryChips.size());

                categoryId = "";
                CatalogProductActivity.categoryId = categoryId;

                etCategory.setEnabled(true);
            }
        });

        rcCategory.setLayoutManager(new LinearLayoutManager(activity, LinearLayout.HORIZONTAL, false));
        rcCategory.setAdapter(categoryChipsFilterAdapter);
    }

    public static void loadCategory(Activity activity){
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Harap tunggu...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        CategoryRequest.getCategoryCatalog(activity);
    }

    public static void validateCategoryId(Activity activity){
        if(!categoryId.equals("")){
            for(CategoryModel models : categoryModels){
                if(models.getCategoryId().equals(categoryId)){
                    categoryChips.add(new CategoryModel(models.getCategoryId(),models.getCategoryName()));
                }
            }
            setChipsCategory(activity);
        }

    }

    @Override
    public void onBackPressed() {
        categoryId = "";
        CatalogProductActivity.categoryId = categoryId;
        CatalogProductActivity.requestFilter(CatalogFilterActivity.this);
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cv_btn_set_filter:
                setFilter();
                break;
        }

    }

    private void setFilter() {
        CatalogProductActivity.requestFilter(CatalogFilterActivity.this);
        finish();
    }
}
