package co.crowde.toni.view.activity.filter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.pchmn.materialchips.ChipsInput;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.controller.main.ProductController;
import co.crowde.toni.helper.CloseSoftKeyboard;
import co.crowde.toni.model.CategoryModel;
import co.crowde.toni.network.CategoryRequest;
import co.crowde.toni.view.fragment.modul.Inventory;

public class InventoryFilter extends AppCompatActivity {

    public static ProgressDialog progressDialog;

    public static ArrayList<Boolean> booleanArrayList = new ArrayList<>();
    public static ArrayList<String> statusList = new ArrayList<>();
    public static String[] status = {"Tersedia", "Mulai habis", "Habis"};

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static TextView tvCategory, tvStatus;
    public static Toolbar toolbarFilter;
    public static ChipsInput chipsInput;

    public static List<CategoryModel> categories = new ArrayList<>();
    public static ArrayList<CategoryModel> categoryModels = new ArrayList<>();
    public static ArrayList<String> category = new ArrayList<>();
    public static ChipGroup chipCategory, chipHistoryCategory, chipStatus;

    public static AutoCompleteTextView etCategory;

    ConstraintLayout mainLayout;

    ArrayList<CategoryModel> historyCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_filter);

//        categories = new ArrayList<>();

        tvCategory = findViewById(R.id.tvCategoryLabel);
        tvStatus = findViewById(R.id.tvStatus);
        toolbarFilter = findViewById(R.id.toolbarFilter);
        chipCategory = findViewById(R.id.chipCategory);
        chipHistoryCategory = findViewById(R.id.chipHistoryCategory);
        chipStatus = findViewById(R.id.chipStatus);

        etCategory = findViewById(R.id.etCategory);
        mainLayout = findViewById(R.id.mainLayout);

        setSupportActionBar(toolbarFilter);


        etCategory.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                CloseSoftKeyboard.hideSoftKeyboard(v, InventoryFilter.this);
            }
        });

        toolbarFilter.setNavigationIcon(getResources().getDrawable(R.drawable.ic_close_black_24dp));
        toolbarFilter.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Inventory.requestFilter(InventoryFilter.this);
            }
        });

        categoryModels = new ArrayList<>();
        loadCategory(this);

        autoTextViewComplete(this);

//        etCategory.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if(etCategory.length()==0){
//                    if(keyCode == KeyEvent.KEYCODE_DEL
//                            && etCategory.length()==0
//                            && chipCategory.getChildCount()>0) {
//                        chipCategory.removeViewAt(chipCategory.getChildCount()-1);
//                        //this is for backspace
//                    }
//                } else {
//
//                }
//
//                return false;
//            }
//        });

        tvStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(InventoryFilter.this, new Gson().toJson(categories), Toast.LENGTH_SHORT).show();
            }
        });

        filterStatus(InventoryFilter.this);

    }

    private void autoTextViewComplete(final Activity activity) {
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

    public static void loadCategory(Activity activity){
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Harap tunggu...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        CategoryRequest.getCategoryInventory(activity);
    }

    private static void filterStatus(final Activity activity) {
        for (int i=0;i<status.length;i++){
            final Chip chip = new Chip(activity);
            chip.setId(i);
            chip.setTag(i);

            chip.setText(status[i]);


            chip.setCheckable(true);
            booleanArrayList.add(false);

            for(int j=0; j<statusList.size();j++){
                if(chip.getText().equals(statusList.get(j))){
                    chip.setChecked(true);
                    chip.setChipBackgroundColor(ColorStateList
                            .valueOf(activity.getResources()
                                    .getColor(R.color.colorThemeGreen)));
                    chip.setTextColor(ColorStateList
                            .valueOf(activity.getResources()
                                    .getColor(R.color.colorWhite)));
                } else if(statusList.get(j).equals("Mulai%20habis")){
                    if(chip.getText().equals("Mulai habis")){
                        chip.setChecked(true);
                        chip.setChipBackgroundColor(ColorStateList
                                .valueOf(activity.getResources()
                                        .getColor(R.color.colorThemeGreen)));
                        chip.setTextColor(ColorStateList
                                .valueOf(activity.getResources()
                                        .getColor(R.color.colorWhite)));
                    }

                }

            }

            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    int tag = (int) compoundButton.getTag();
                    booleanArrayList.set(tag, b);

                    if(b){
                        if(chip.getText().equals("Mulai habis")){
                            statusList.add("Mulai%20habis");
                        } else {
                            statusList.add(chip.getText().toString());
                        }

//                        ProductController.statusInventory.add(chip.getText().toString());
                        chip.setChipBackgroundColor(ColorStateList
                                .valueOf(activity.getResources()
                                        .getColor(R.color.colorThemeGreen)));
                        chip.setTextColor(ColorStateList
                                .valueOf(activity.getResources()
                                        .getColor(R.color.colorWhite)));
                    } else {
                        if(chip.getText().equals("Mulai habis")){
                            statusList.remove("Mulai%20habis");
                        } else {
                            statusList.remove(chip.getText().toString());
                        }
//                        statusList.remove(chip.getText().toString());
//                        ProductController.statusInventory.remove(chip.getText().toString());
                        chip.setChipBackgroundColor(ColorStateList
                                .valueOf(activity.getResources()
                                        .getColor(R.color.colorThemeGreyLight)));
                        chip.setTextColor(ColorStateList
                                .valueOf(activity.getResources()
                                        .getColor(R.color.colorBlack)));
                    }

                    Log.e("StatusStock",new Gson().toJson(ProductController.statusInventory));
                }
            });
            chipStatus.addView(chip);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        Inventory.requestFilter(InventoryFilter.this);
    }
}
