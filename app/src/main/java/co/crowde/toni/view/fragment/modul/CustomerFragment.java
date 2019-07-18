package co.crowde.toni.view.fragment.modul;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.adapter.TransaksiBagianPelangganAdapter;
import co.crowde.toni.constant.Const;
import co.crowde.toni.helper.CloseSoftKeyboard;
import co.crowde.toni.model.CustomerModel;
import co.crowde.toni.network.CustomerRequest;
import co.crowde.toni.network.ProductRequest;
import co.crowde.toni.utils.analytics.AnalyticsToniUtils;
import co.crowde.toni.view.activity.filter.InventoryFilterActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerFragment extends Fragment {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static RecyclerView recyclerView;
    public static TransaksiBagianPelangganAdapter transaksiBagianPelangganAdapter;

    FloatingActionButton btntambahpelanggan;
    public static EditText etSearchPelanggan;
    public static List<CustomerModel> customerModelList = new ArrayList<>();

    static DividerItemDecoration itemDecorator;

    static Drawable close;
    static Drawable search;

    public static ProgressDialog progressDialog;

    public static TextView tvHeader, tvEmptyField;
    public static ImageView tvClose;
    public static EditText etName, etPhone;
    public static CardView cvBtnAddNew;
    public static AlertDialog alertDialog;

    public CustomerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_customer, container, false);

        progressDialog = new ProgressDialog(getActivity());

        tvEmptyField = rootView.findViewById(R.id.tv_empty_field);
        etSearchPelanggan = rootView.findViewById(R.id.et_search_pelanggan);
        recyclerView = rootView.findViewById(R.id.rc_list_pelanggan);
        btntambahpelanggan = rootView.findViewById(R.id.btn_tambah_pelanggan);

        itemDecorator = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(),
                R.drawable.divider_line_item));

        initAdapterCustomer(getActivity());
        getCustomerList(getActivity());
        initScrollListenerCustomer(getActivity());

        //Setup Button tambah pelanggan
        btntambahpelanggan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(getActivity());
            }
        });

//        getData(pageCustomer);

        searchCustomer(getActivity(), getContext());

        return rootView;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void searchCustomer(final Activity activity, final Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            close = context.getDrawable(R.drawable.ic_close_black_24dp);
            search = context.getDrawable(R.drawable.ic_search_black_24dp);
        } else {
            close = activity.getResources().getDrawable(R.drawable.ic_close_black_24dp);
            search = activity.getResources().getDrawable(R.drawable.ic_search_black_24dp);
        }
        etSearchPelanggan.setCompoundDrawablesWithIntrinsicBounds(null,null,search,null);
        etSearchPelanggan.addTextChangedListener(searchWatcher);
        etSearchPelanggan.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Harap tunggu...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    CustomerRequest.namaPelanggan = etSearchPelanggan.getText().toString();
                    CustomerRequest.page = 1;
                    customerModelList.clear();
                    CustomerRequest.getCustomerModulList(activity);
                }

            }
        });
        etSearchPelanggan.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Harap tunggu...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                CustomerRequest.namaPelanggan = etSearchPelanggan.getText().toString();
                CustomerRequest.page = 1;
                customerModelList.clear();
                CustomerRequest.getCustomerModulList(activity);
                return false;
            }
        });

        etSearchPelanggan.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (etSearchPelanggan.getRight() -
                            etSearchPelanggan.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if(etSearchPelanggan.length()>0){
                            progressDialog = new ProgressDialog(getActivity());
                            progressDialog.setMessage("Harap tunggu...");
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show();
                            etSearchPelanggan.setText("");
                            event.setAction(MotionEvent.ACTION_CANCEL);
                            CustomerRequest.namaPelanggan ="";
                            CustomerRequest.page = 1;
                            CustomerRequest.getCustomerModulList(activity);
                        }

                        return false;
                    }
                }
                return false;
            }
        });

        etSearchPelanggan.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
//            customerAdapter.getFilter().filter(s);

            if (etSearchPelanggan.getText().length() > 0) {
                etSearchPelanggan.setCompoundDrawablesWithIntrinsicBounds(
                        null, null, close, null);

            } else {
                etSearchPelanggan.setCompoundDrawablesWithIntrinsicBounds(
                        null,null,search,null);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public static void showListField(Activity activity){
        if(CustomerFragment.customerModelList.size()!=0){
            tvEmptyField.setVisibility(View.GONE);
        } else {
            tvEmptyField.setVisibility(View.VISIBLE);
        }
    }

    public static void initAdapterCustomer(Activity activity) {
        transaksiBagianPelangganAdapter = new TransaksiBagianPelangganAdapter(activity, customerModelList, activity);

        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.addItemDecoration(itemDecorator);
        recyclerView.setAdapter(transaksiBagianPelangganAdapter);

    }

    public static void updateDataCustomer(List<CustomerModel> customer, int page) {
        if (customerModelList.size() != 0) {
            customerModelList.remove(customerModelList.size() - 1);
            int scrollPosition = customerModelList.size();
            transaksiBagianPelangganAdapter.notifyItemRemoved(scrollPosition);
        }

        if (page == 1)
            customerModelList.clear();
        customerModelList.addAll(customer);
        transaksiBagianPelangganAdapter.replaceItemFiltered(customerModelList);
        isLoadingCustomer = false;

        if (page == 1)
            if (customerModelList.size() > 0)
                recyclerView.scrollToPosition(0);
    }

    static boolean isLoadingCustomer = false;

    public static void initScrollListenerCustomer(final Activity activity) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoadingCustomer) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == customerModelList.size() - 1) {
                        //bottom of list!
                        loadMoreCustomer(activity);
                        isLoadingCustomer = true;
                    }
                }
            }
        });
    }

    public static void loadMoreCustomer(Activity activity) {
        customerModelList.add(null);
        transaksiBagianPelangganAdapter.notifyItemInserted(customerModelList.size() - 1);

        CustomerRequest.page = CustomerRequest.page + 1;
        CustomerRequest.getCustomerModulList(activity);
    }

    public static void getCustomerList(Activity activity){
        progressDialog.setMessage("Harap tunggu...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        CustomerRequest.page = 1;
        CustomerRequest.namaPelanggan = etSearchPelanggan.getText().toString();
        customerModelList.clear();

        if(etSearchPelanggan.getText().toString().length()>1){
            AnalyticsToniUtils.getEvent(Const.CATEGORY_CUSTOMER,Const.MODUL_CUSTOMER,Const.LABEL_CUSTOMER_SEARCH);
        } else {
            AnalyticsToniUtils.getEvent(Const.CATEGORY_CUSTOMER,Const.MODUL_CUSTOMER,Const.LABEL_CUSTOMER_LIST);
        }

        CustomerRequest.getCustomerModulList(activity);
    }

    @SuppressLint("SetTextI18n")
    public static void showPopup(final Activity activity) {
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

        etName.addTextChangedListener(addWatcher(activity));
        etPhone.addTextChangedListener(addWatcher(activity));

        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                AnalyticsToniUtils.getEvent(Const.CATEGORY_CUSTOMER,Const.MODUL_CUSTOMER,Const.LABEL_CUSTOMER_CANCEL_ADD);
            }
        });

        cvBtnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Harap tunggu...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                CustomerRequest.addNewCustomerModul(activity);
            }
        });

        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }

    private static TextWatcher addWatcher(final Activity activity) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etName.getText().length() > 2 && etPhone.getText().length() > 9) {
                    cvBtnAddNew.setCardBackgroundColor(activity.getResources().getColor(R.color.colorThemeOrange));
                    cvBtnAddNew.setEnabled(true);
                } else {
                    cvBtnAddNew.setCardBackgroundColor(activity.getResources().getColor(R.color.colorThemeGrey));
                    cvBtnAddNew.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        };
    }


}
