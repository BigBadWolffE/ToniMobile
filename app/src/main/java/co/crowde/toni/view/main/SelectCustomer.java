package co.crowde.toni.view.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.adapter.CustomerAdapter;
import co.crowde.toni.adapter.ProductDashboardAdapter;
import co.crowde.toni.adapter.ProductInventoryAdapter;
import co.crowde.toni.controller.network.CustomerRequest;
import co.crowde.toni.controller.network.ProductRequest;
import co.crowde.toni.model.CustomerModel;
import co.crowde.toni.model.ProductModel;
import co.crowde.toni.view.fragment.Customer;
import co.crowde.toni.view.popup.AddNewCustomerPopup;

public class SelectCustomer extends AppCompatActivity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static CardView cvAddNewCustomer;
    public static Toolbar toolbarCustomer;
    public static RecyclerView rcCustomer;
    public static EditText etSearchCustomer;
    public static ImageView imgBtnFilter;
    public static CustomerAdapter customerAdapter;
    public static List<CustomerModel> customerModels;
    public static List<CustomerModel> customerModelsFiltered;

    static DividerItemDecoration itemDecorator;

    static Drawable close;
    static Drawable search;

    public static TextView tvHeader;
    public static ImageView tvClose;
    public static EditText etName, etPhone;
    public static CardView cvBtnAddNew;
    public static AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_customer);

        cvAddNewCustomer = findViewById(R.id.cvAddNewCustomer);
        toolbarCustomer = findViewById(R.id.toolbarCustomer);
        rcCustomer = findViewById(R.id.rcCustomer);
        etSearchCustomer = findViewById(R.id.etSearchCustomer);
        imgBtnFilter = findViewById(R.id.imgBtnFilter);
        cvAddNewCustomer = findViewById(R.id.cvAddNewCustomer);
        setSupportActionBar(toolbarCustomer);

        toolbarCustomer.setNavigationIcon(getResources().getDrawable(R.drawable.ic_close_black_24dp));
        toolbarCustomer.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        itemDecorator = new DividerItemDecoration(getBaseContext(),
                DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getBaseContext(),
                R.drawable.divider_line_item));

        loadCustomerList(SelectCustomer.this);

        cvAddNewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(SelectCustomer.this);
            }
        });

        searchCustomer(SelectCustomer.this, getBaseContext());
    }

    public static void loadCustomerList(Activity activity) {
        CustomerRequest.getCustomerList(activity);
        customerModels = new ArrayList<>();
        customerAdapter = new CustomerAdapter(activity,
                customerModels, activity);
        rcCustomer.addItemDecoration(itemDecorator);
        rcCustomer.setLayoutManager(new LinearLayoutManager(activity));
        rcCustomer.setAdapter(customerAdapter);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void searchCustomer(Activity activity, Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            close = context.getDrawable(R.drawable.ic_close_black_24dp);
            search = context.getDrawable(R.drawable.ic_search_black_24dp);
        } else {
            close = activity.getResources().getDrawable(R.drawable.ic_close_black_24dp);
            search = activity.getResources().getDrawable(R.drawable.ic_search_black_24dp);
        }
        etSearchCustomer.setCompoundDrawablesWithIntrinsicBounds(null,null,search,null);
        etSearchCustomer.addTextChangedListener(searchWatcher);
        etSearchCustomer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (etSearchCustomer.getRight() -
                            etSearchCustomer.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        etSearchCustomer.setText("");
                        event.setAction(MotionEvent.ACTION_CANCEL);
                        return false;
                    }
                }
                return false;
            }
        });
    }

    public TextWatcher searchWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            customerAdapter.getFilter().filter(s);

            if (etSearchCustomer.getText().length() > 0) {
                etSearchCustomer.setCompoundDrawablesWithIntrinsicBounds(
                        null, null, close, null);

            } else {
                etSearchCustomer.setCompoundDrawablesWithIntrinsicBounds(
                        null,null,search,null);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @SuppressLint("SetTextI18n")
    public void showPopup(final Activity activity) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(activity)
                .inflate(R.layout.layout_add_new_popup,
                        viewGroup,
                        false);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        alertDialog = builder.create();

        //Get View Id
        tvHeader = dialogView.findViewById(R.id.tvHeader);
        tvClose = dialogView.findViewById(R.id.tvClose);
        etName = dialogView.findViewById(R.id.etName);
        etPhone = dialogView.findViewById(R.id.etPhone);
        cvBtnAddNew = dialogView.findViewById(R.id.cvBtnAddNew);

        etName.addTextChangedListener(watcherAdd);
        etPhone.addTextChangedListener(watcherAdd);
        cvBtnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerRequest.addNewCustomer(activity);
            }
        });

        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }

    public TextWatcher watcherAdd = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (etName.getText().length() > 0 && etPhone.getText().length() > 0) {
                cvBtnAddNew.setCardBackgroundColor(getResources().getColor(R.color.colorThemeOrange));
                cvBtnAddNew.setEnabled(true);
            } else {
                cvBtnAddNew.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGrey));
                cvBtnAddNew.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
