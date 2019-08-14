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
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
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
import co.crowde.toni.utils.SetHeader;
import co.crowde.toni.view.activity.auth.RegisterActivity;
import co.crowde.toni.view.fragment.modul.DashboardFragment;

public class DashboardFilterActivity extends AppCompatActivity implements View.OnClickListener {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static ProgressDialog progressDialog;

    AppBarLayout appBarLayout;

    //Filter
    CardView cvSetFilter, cvAll, cvReady, cvAlmostEmpty, cvEmpty;
    TextView tvAll, tvReady, tvAlmostEmpty, tvEmpty;
    static String status = "";
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
        setContentView(R.layout.activity_dashboard_filter);

        appBarLayout = findViewById(R.id.appBar);
        SetHeader.isLolipop(DashboardFilterActivity.this, appBarLayout);

        categoryChips = new ArrayList<>();

        tvCategory = findViewById(R.id.tvCategoryLabel);
        tvStatus = findViewById(R.id.tvStatus);
        toolbarFilter = findViewById(R.id.toolbarFilter);

        rcCategory = findViewById(R.id.rc_category);

        etCategory = findViewById(R.id.etCategory);
        mainLayout = findViewById(R.id.mainLayout);

        //Status
        cvAll = findViewById(R.id.cv_btn_all);
        cvReady = findViewById(R.id.cv_btn_ready);
        cvAlmostEmpty = findViewById(R.id.cv_btn_almost_empty);
        cvEmpty = findViewById(R.id.cv_btn_empty);
        tvAll = findViewById(R.id.tv_all);
        tvReady = findViewById(R.id.tv_ready);
        tvAlmostEmpty = findViewById(R.id.tv_almost_empty);
        tvEmpty = findViewById(R.id.tv_empty);

        cvAll.setOnClickListener(this);
        cvReady.setOnClickListener(this);
        cvAlmostEmpty.setOnClickListener(this);
        cvEmpty.setOnClickListener(this);

        validateStatus();

        cvSetFilter = findViewById(R.id.cv_btn_set_filter);
        cvSetFilter.setOnClickListener(this);

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
                    DashboardFragment.categoryId = categoryId;

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
                DashboardFragment.categoryId = categoryId;

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
        CategoryRequest.getCategoryList(activity);
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
        status = "";
        categoryId = "";
        DashboardFragment.status = status;
        DashboardFragment.categoryId = categoryId;
        DashboardFragment.requestFilter(DashboardFilterActivity.this);
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cv_btn_all:
                statusAll();
                break;

            case R.id.cv_btn_ready:
                statusReady();
                break;

            case R.id.cv_btn_almost_empty:
                statusAlmostEmpty();
                break;

            case R.id.cv_btn_empty:
                statusEmpty();
                break;

            case R.id.cv_btn_set_filter:
                setFilter();
                break;
        }

    }

    private void statusAll() {
        status = "";
        DashboardFragment.status = status;
        validateStatus();
    }

    private void statusReady() {
        status = tvReady.getText().toString();
        DashboardFragment.status = status;
        validateStatus();
    }

    private void statusAlmostEmpty() {
        status = "Mulai%20habis";
        DashboardFragment.status = status;
        validateStatus();
    }

    private void statusEmpty() {
        status = tvEmpty.getText().toString();
        DashboardFragment.status = status;
        validateStatus();
    }

    private void setFilter() {
        DashboardFragment.requestFilter(DashboardFilterActivity.this);
        finish();
    }

    private void validateStatus() {
        switch (status) {
            case "Tersedia":
                cvAll.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGreyLight));
                cvReady.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGreenDark02));
                cvAlmostEmpty.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGreyLight));
                cvEmpty.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGreyLight));

                cvAll.setEnabled(true);
                cvReady.setEnabled(false);
                cvAlmostEmpty.setEnabled(true);
                cvEmpty.setEnabled(true);

                break;

            case "Mulai%20habis":
                cvAll.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGreyLight));
                cvReady.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGreyLight));
                cvAlmostEmpty.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGreenDark02));
                cvEmpty.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGreyLight));

                cvAll.setEnabled(true);
                cvReady.setEnabled(true);
                cvAlmostEmpty.setEnabled(false);
                cvEmpty.setEnabled(true);

                break;

            case "Habis":
                cvAll.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGreyLight));
                cvReady.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGreyLight));
                cvAlmostEmpty.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGreyLight));
                cvEmpty.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGreenDark02));

                cvAll.setEnabled(true);
                cvReady.setEnabled(true);
                cvAlmostEmpty.setEnabled(true);
                cvEmpty.setEnabled(false);

                break;

            default:
                cvAll.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGreenDark02));
                cvReady.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGreyLight));
                cvAlmostEmpty.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGreyLight));
                cvEmpty.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGreyLight));

                cvAll.setEnabled(false);
                cvReady.setEnabled(true);
                cvAlmostEmpty.setEnabled(true);
                cvEmpty.setEnabled(true);
                break;
        }
    }

}









