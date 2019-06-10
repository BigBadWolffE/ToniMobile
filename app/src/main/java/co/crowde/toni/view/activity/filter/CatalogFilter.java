package co.crowde.toni.view.activity.filter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.helper.CloseSoftKeyboard;
import co.crowde.toni.model.CategoryModel;
import co.crowde.toni.model.response.list.SupplierModel;
import co.crowde.toni.network.CategoryRequest;
import co.crowde.toni.network.SupplierRequest;

public class CatalogFilter extends AppCompatActivity {

    public static TextView tvSupplierLabel, tvCategoryLabel;
    public static ChipGroup chipSupplier, chipHistorySupplier, chipCategory, chipHistoryCategory;
    public static AutoCompleteTextView etSupplier, etCategory;
    public static Toolbar toolbarFilter;

    public static ProgressDialog progressDialog;

    public static List<SupplierModel> suppliers = new ArrayList<>();
    public static ArrayList<SupplierModel> supplierModels = new ArrayList<>();
    public static ArrayList<String> supplier = new ArrayList<>();

    public static List<CategoryModel> categories = new ArrayList<>();
    public static ArrayList<CategoryModel> categoryModels = new ArrayList<>();
    public static ArrayList<String> category = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog_filter);

        suppliers = new ArrayList<>();
        categories = new ArrayList<>();

        toolbarFilter = findViewById(R.id.toolbarFilter);
        tvSupplierLabel = findViewById(R.id.tvSupplierLabel);
        chipSupplier = findViewById(R.id.chipSupplier);
        etSupplier = findViewById(R.id.etSupplier);
        chipHistorySupplier = findViewById(R.id.chipHistorySupplier);
        tvCategoryLabel = findViewById(R.id.tvCategoryLabel);
        chipCategory = findViewById(R.id.chipCategory);
        chipHistoryCategory = findViewById(R.id.chipHistoryCategory);
        etCategory = findViewById(R.id.etCategory);

        progressDialog = new ProgressDialog(this);

        setSupportActionBar(toolbarFilter);

        toolbarFilter.setNavigationIcon(getResources().getDrawable(R.drawable.ic_close_black_24dp));
        toolbarFilter.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        etSupplier.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                CloseSoftKeyboard.hideSoftKeyboard(v, CatalogFilter.this);
            }
        });

        etCategory.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                CloseSoftKeyboard.hideSoftKeyboard(v, CatalogFilter.this);
            }
        });

        supplierModels = new ArrayList<>();
        categoryModels = new ArrayList<>();
        loadData(this);

        autoTextViewComplete(this);

        tvSupplierLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CatalogFilter.this, new Gson().toJson(suppliers), Toast.LENGTH_SHORT).show();
            }
        });
        tvCategoryLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CatalogFilter.this, new Gson().toJson(categories), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void autoTextViewComplete(final Activity activity) {
        final ArrayAdapter<CategoryModel> adapterSupplier =
                new ArrayAdapter<CategoryModel>(this, android.R.layout.simple_list_item_1, categoryModels);
        etSupplier.setAdapter(adapterSupplier);

        if(suppliers.size()>0){
            for (int i=0; i<suppliers.size();i++){
                final Chip chip = new Chip(activity);
                chip.setId(i);
                chip.setTag(i);

                chip.setText(suppliers.get(i).getSupplierName());
                chip.setCloseIconVisible(true);
                chip.setCheckable(false);

                chip.setCloseIconTintResource(R.color.colorWhite);
                chip.setChipBackgroundColorResource(R.color.colorThemeGreen);
                chip.setTextColor(getResources().getColor(R.color.colorWhite));
                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int tag = (int) v.getTag();
                        chipSupplier.removeView(v);
                        supplier.remove(tag);
                        suppliers.remove(tag);
                    }
                });
                chipSupplier.addView(chip);
            }

        }

        etCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if(categories.size()>0){
                    for(int i=0; i<categories.size();i++){
                        if(etCategory.getText().toString().equals(categories.get(i).getCategoryName())){
                            Toast.makeText(activity, "Kategori sudah terpilih.", Toast.LENGTH_SHORT).show();
                        } else {
                            String str = new Gson().toJson(etCategory.getAdapter().getItem(position));
                            try {
                                JSONObject obj = new JSONObject(str);
                                categories.add(new CategoryModel(obj.getString("categoryId"), obj.getString("categoryName")));
                                category.add(obj.getString("categoryId"));

                                final Chip chip = new Chip(activity);
                                chip.setId(i);
                                chip.setTag(i);

                                chip.setText(categories.get(i).getCategoryName());
                                chip.setCloseIconVisible(true);
                                chip.setCheckable(false);

                                chip.setCloseIconTintResource(R.color.colorWhite);
                                chip.setChipBackgroundColorResource(R.color.colorThemeGreen);
                                chip.setTextColor(getResources().getColor(R.color.colorWhite));
                                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        int tag = (int) v.getTag();
                                        chipCategory.removeView(v);
                                        category.remove(tag);
                                        categories.remove(tag);
                                    }
                                });
                                chipCategory.addView(chip);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                    String str = new Gson().toJson(etCategory.getAdapter().getItem(position));
                    try {
                        JSONObject obj = new JSONObject(str);
                        categories.add(new CategoryModel(obj.getString("categoryId"), obj.getString("categoryName")));
                        category.add(obj.getString("categoryId"));

                        for (int i=0; i<categories.size();i++){
                            final Chip chip = new Chip(activity);
                            chip.setId(i);
                            chip.setTag(i);

                            chip.setText(categories.get(i).getCategoryName());
                            chip.setCloseIconVisible(true);
                            chip.setCheckable(false);

                            chip.setCloseIconTintResource(R.color.colorWhite);
                            chip.setChipBackgroundColorResource(R.color.colorThemeGreen);
                            chip.setTextColor(getResources().getColor(R.color.colorWhite));
                            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int tag = (int) v.getTag();
                                    chipCategory.removeView(v);
                                    category.remove(tag);
                                    categories.remove(tag);
                                }
                            });
                            chipCategory.addView(chip);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                etCategory.setText("");
            }
        });

        final ArrayAdapter<CategoryModel> adapter =
                new ArrayAdapter<CategoryModel>(this, android.R.layout.simple_list_item_1, categoryModels);
        etCategory.setAdapter(adapter);

        if(categories.size()>0){
            for (int i=0; i<categories.size();i++){
                final Chip chip = new Chip(activity);
                chip.setId(i);
                chip.setTag(i);

                chip.setText(categories.get(i).getCategoryName());
                chip.setCloseIconVisible(true);
                chip.setCheckable(false);

                chip.setCloseIconTintResource(R.color.colorWhite);
                chip.setChipBackgroundColorResource(R.color.colorThemeGreen);
                chip.setTextColor(getResources().getColor(R.color.colorWhite));
                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int tag = (int) v.getTag();
                        chipCategory.removeView(v);
                        category.remove(tag);
                        categories.remove(tag);
                    }
                });
                chipCategory.addView(chip);
            }

        }

        etCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if(categories.size()>0){
                    for(int i=0; i<categories.size();i++){
                        if(etCategory.getText().toString().equals(categories.get(i).getCategoryName())){
                            Toast.makeText(activity, "Kategori sudah terpilih.", Toast.LENGTH_SHORT).show();
                        } else {
                            String str = new Gson().toJson(etCategory.getAdapter().getItem(position));
                            try {
                                JSONObject obj = new JSONObject(str);
                                categories.add(new CategoryModel(obj.getString("categoryId"), obj.getString("categoryName")));
                                category.add(obj.getString("categoryId"));

                                final Chip chip = new Chip(activity);
                                chip.setId(i);
                                chip.setTag(i);

                                chip.setText(categories.get(i).getCategoryName());
                                chip.setCloseIconVisible(true);
                                chip.setCheckable(false);

                                chip.setCloseIconTintResource(R.color.colorWhite);
                                chip.setChipBackgroundColorResource(R.color.colorThemeGreen);
                                chip.setTextColor(getResources().getColor(R.color.colorWhite));
                                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        int tag = (int) v.getTag();
                                        chipCategory.removeView(v);
                                        category.remove(tag);
                                        categories.remove(tag);
                                    }
                                });
                                chipCategory.addView(chip);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                    String str = new Gson().toJson(etCategory.getAdapter().getItem(position));
                    try {
                        JSONObject obj = new JSONObject(str);
                        categories.add(new CategoryModel(obj.getString("categoryId"), obj.getString("categoryName")));
                        category.add(obj.getString("categoryId"));

                        for (int i=0; i<categories.size();i++){
                            final Chip chip = new Chip(activity);
                            chip.setId(i);
                            chip.setTag(i);

                            chip.setText(categories.get(i).getCategoryName());
                            chip.setCloseIconVisible(true);
                            chip.setCheckable(false);

                            chip.setCloseIconTintResource(R.color.colorWhite);
                            chip.setChipBackgroundColorResource(R.color.colorThemeGreen);
                            chip.setTextColor(getResources().getColor(R.color.colorWhite));
                            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int tag = (int) v.getTag();
                                    chipCategory.removeView(v);
                                    category.remove(tag);
                                    categories.remove(tag);
                                }
                            });
                            chipCategory.addView(chip);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                etCategory.setText("");
            }
        });
    }

    public static void loadData(Activity activity){
        progressDialog.setMessage("Harap tunggu...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        SupplierRequest.getSupplierCatalog(activity);
        CategoryRequest.getCategoryCatalog(activity);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
