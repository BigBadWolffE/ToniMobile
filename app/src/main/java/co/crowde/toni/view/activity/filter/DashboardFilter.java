package co.crowde.toni.view.activity.filter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.support.constraint.ConstraintLayout;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pchmn.materialchips.ChipsInput;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.crowde.toni.R;
import co.crowde.toni.helper.CloseSoftKeyboard;
import co.crowde.toni.model.CategoryModel;
import co.crowde.toni.model.custom.CategoryChipsModel;
import co.crowde.toni.model.custom.StatusModel;
import co.crowde.toni.network.CategoryRequest;
import co.crowde.toni.view.fragment.modul.Dashboard;

public class DashboardFilter extends AppCompatActivity {

    public static ProgressDialog progressDialog;

    public static ArrayList<CategoryModel> categoryModels = new ArrayList<>();
    public static ArrayList<CategoryChipsModel> category = new ArrayList<>();
    public static ArrayList<StatusModel> statusList = new ArrayList<>();

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static TextView tvCategory, tvStatus;
    public static Toolbar toolbarFilter;
    public static ChipsInput chipsInput;

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
                CloseSoftKeyboard.hideSoftKeyboard(v, DashboardFilter.this);
            }
        });

        toolbarFilter.setNavigationIcon(getResources().getDrawable(R.drawable.ic_close_black_24dp));
        toolbarFilter.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Dashboard.requestFilter(DashboardFilter.this);
            }
        });

        categoryModels = new ArrayList<>();
        loadCategory(this);

        autoCompleteTextView(this);

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

        tvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(DashboardFilter.this,
//                        "Categories: "+new Gson().toJson(categories)+"\n"
//                                +"Category: "+new Gson().toJson(category)+"\n"
//                                +"boolean: "+new Gson().toJson(booleanCategory), Toast.LENGTH_SHORT).show();
            }
        });

        tvStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashboardFilter.this, new Gson().toJson(statusList), Toast.LENGTH_SHORT).show();
            }
        });

        statusList();
        filterStatus(DashboardFilter.this);

    }

    private void autoCompleteTextView(final Activity activity) {
        final ArrayAdapter<CategoryModel> adapter =
                new ArrayAdapter<CategoryModel>(this, android.R.layout.simple_list_item_1, categoryModels);
        etCategory.setAdapter(adapter);

//        if(category.size()>0){
//            for (int i=0; i<category.size();i++){
//                final Chip chip = new Chip(activity);
//                chip.setId(i);
//                chip.setTag(i);
//
//                chip.setText(category.get(i).getCategoryName());
//                chip.setCloseIconVisible(true);
//                chip.setCheckable(false);
//
//                chip.setCloseIconTintResource(R.color.colorWhite);
//                chip.setChipBackgroundColorResource(R.color.colorThemeGreen);
//                chip.setTextColor(getResources().getColor(R.color.colorWhite));
//                chip.setOnCloseIconClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        category.remove(v.getId());
//                    }
//                });
//                chipCategory.addView(chip);
//            }
//
//        }

        etCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if(category.size()>0){
                    for(int i=0; i<category.size();i++){
                        if(etCategory.getText().toString().equals(category.get(i).getCategoryName())){
                            Toast.makeText(activity, "Kategori sudah terpilih.", Toast.LENGTH_SHORT).show();
                        } else {
                            String str = new Gson().toJson(etCategory.getAdapter().getItem(position));
                            try {
                                JSONObject obj = new JSONObject(str);
                                category.add(new CategoryChipsModel(true, obj.getString("categoryId"), obj.getString("categoryName")));

                                final Chip chip = new Chip(activity);
                                chip.setId(i);
                                chip.setTag(i);

                                chip.setText(category.get(i).getCategoryName());
                                chip.setCloseIconVisible(true);
                                chip.setCheckable(false);

                                chip.setCloseIconTintResource(R.color.colorWhite);
                                chip.setChipBackgroundColorResource(R.color.colorThemeGreen);
                                chip.setTextColor(getResources().getColor(R.color.colorWhite));
                                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        category.remove(new CategoryChipsModel(chip.getText().toString()));
                                    }
                                });
                                chip.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
//                                        Toast.makeText(activity, category, Toast.LENGTH_SHORT).show();
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
                        category.add(new CategoryChipsModel(true, obj.getString("categoryId"), obj.getString("categoryName")));

                        for (int i=0; i<category.size();i++){
                            final Chip chip = new Chip(activity);
                            chip.setId(i);
                            chip.setTag(i);

                            chip.setText(category.get(i).getCategoryName());
                            chip.setCloseIconVisible(true);
                            chip.setCheckable(false);

                            chip.setCloseIconTintResource(R.color.colorWhite);
                            chip.setChipBackgroundColorResource(R.color.colorThemeGreen);
                            chip.setTextColor(getResources().getColor(R.color.colorWhite));
                            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    category.remove(new CategoryChipsModel(chip.getText().toString()));
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
        CategoryRequest.getCategoryList(activity);
    }

    private static void filterStatus(final Activity activity) {
        for (int i=0;i<statusList.size();i++){
            final Chip chip = new Chip(activity);
            chip.setId(i);
            chip.setTag(i);

            chip.setText(statusList.get(i).getStatus());
            chip.setCheckable(true);

            if(statusList.get(i).isActive()){
                chip.setChecked(true);
                chip.setChipBackgroundColor(ColorStateList
                        .valueOf(activity.getResources()
                                .getColor(R.color.colorThemeGreen)));
                chip.setTextColor(ColorStateList
                        .valueOf(activity.getResources()
                                .getColor(R.color.colorWhite)));
            } else {
                chip.setChecked(false);
                chip.setChipBackgroundColor(ColorStateList
                        .valueOf(activity.getResources()
                                .getColor(R.color.colorThemeGreyLight)));
                chip.setTextColor(ColorStateList
                        .valueOf(activity.getResources()
                                .getColor(R.color.colorBlack)));
            }

            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    if(b){
                        statusList.set(chip.getId(),
                                new StatusModel(true, chip.getText().toString()));
                        chip.setChipBackgroundColor(ColorStateList
                                .valueOf(activity.getResources()
                                        .getColor(R.color.colorThemeGreen)));
                        chip.setTextColor(ColorStateList
                                .valueOf(activity.getResources()
                                        .getColor(R.color.colorWhite)));
                    } else {
                        statusList.set(chip.getId(),
                                new StatusModel(false, chip.getText().toString()));
                        chip.setChipBackgroundColor(ColorStateList
                                .valueOf(activity.getResources()
                                        .getColor(R.color.colorThemeGreyLight)));
                        chip.setTextColor(ColorStateList
                                .valueOf(activity.getResources()
                                        .getColor(R.color.colorBlack)));
                    }
                }
            });
            chipStatus.addView(chip);
        }
    }

    public static void statusList(){
        if(statusList.size()==0){
            statusList.add(new StatusModel(
                    false,
                    "Tersedia"));
            statusList.add(new StatusModel(
                    false,
                    "Mulai habis"));
            statusList.add(new StatusModel(
                    false,
                    "Habis"));
        }

    }

    @Override
    public void onBackPressed() {
        finish();
        Dashboard.requestFilter(DashboardFilter.this);
    }
}
