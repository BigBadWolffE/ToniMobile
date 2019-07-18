package co.crowde.toni.view.activity.filter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.nex3z.flowlayout.FlowLayout;
import com.pchmn.materialchips.ChipsInput;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.adapter.CustomerAdapter;
import co.crowde.toni.adapter.chips.CategoryChipsFilterAdapter;
import co.crowde.toni.helper.CloseSoftKeyboard;
import co.crowde.toni.listener.ChipsFilterListener;
import co.crowde.toni.model.CategoryModel;
import co.crowde.toni.network.CategoryRequest;
import co.crowde.toni.view.fragment.modul.DashboardFragment;

public class DashboardFilterActivity extends AppCompatActivity {

    public static ProgressDialog progressDialog;

    public static ArrayList<Boolean> booleanArrayList = new ArrayList<>();
    public static ArrayList<String> statusList = new ArrayList<>();
    public static String[] status = {"Tersedia", "Mulai habis", "Habis"};

    ChipsFilterListener listener;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static TextView tvCategory, tvStatus;
    public static Toolbar toolbarFilter;
    public static ChipsInput chipsInput;

    public static List<CategoryModel> categories = new ArrayList<>();
    public static ArrayList<CategoryModel> categoryModels = new ArrayList<>();
    public static ArrayList<String> category = new ArrayList<>();
    public static ChipGroup chipHistoryCategory, chipStatus;

    public static AutoCompleteTextView etCategory;

    ConstraintLayout mainLayout;

    ArrayList<CategoryModel> historyCategory;

    //Category Filter
    ArrayList<CategoryModel> categoryChips = new ArrayList<>();
    RecyclerView rcCategory;
    CategoryChipsFilterAdapter categoryChipsFilterAdapter;
    FlowLayout flowLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_filter);

        tvCategory = findViewById(R.id.tvCategoryLabel);
        tvStatus = findViewById(R.id.tvStatus);
        toolbarFilter = findViewById(R.id.toolbarFilter);
        chipHistoryCategory = findViewById(R.id.chipHistoryCategory);
        chipStatus = findViewById(R.id.chipStatus);

        rcCategory = findViewById(R.id.rc_category);
        flowLayout = findViewById(R.id.layoutSearch);

        etCategory = findViewById(R.id.etCategory);
        mainLayout = findViewById(R.id.mainLayout);

        setSupportActionBar(toolbarFilter);

        etCategory.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                CloseSoftKeyboard.hideSoftKeyboard(v, DashboardFilterActivity.this);
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

        filterStatus(DashboardFilterActivity.this);

    }

    void setChipsCategory(Activity activity){
        categoryChipsFilterAdapter = new CategoryChipsFilterAdapter(activity, categoryChips, activity, new ChipsFilterListener() {
            @Override
            public void onDeleteItemClick(View v, int position) {
                categoryChips.remove(position);
            }
        });

        rcCategory.setLayoutManager(new LinearLayoutManager(activity, LinearLayout.HORIZONTAL, false));
        rcCategory.setAdapter(categoryChipsFilterAdapter);
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

                    categoryChips.add(new CategoryModel(getId,getName));
//                    categoryChipsFilterAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                etCategory.setText("");
                setChipsCategory(activity);
            }
        });

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

//        etCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                if(category.size()>0){
//                    for(int i=0; i<category.size();i++){
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
//                etCategory.setText("");
//
//            }
//        });
    }

    public static void loadCategory(Activity activity){
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Harap tunggu...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        CategoryRequest.getCategoryList(activity);
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
                            statusList.clear();
                            statusList.add("Mulai%20habis");
                        } else {
                            statusList.clear();
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

                    Log.e("StatusStock",new Gson().toJson(statusList));
                }
            });
            chipStatus.addView(chip);
        }
    }
    @Override
    public void onBackPressed() {
        DashboardFragment.requestFilter(DashboardFilterActivity.this);
        super.onBackPressed();
    }
}









