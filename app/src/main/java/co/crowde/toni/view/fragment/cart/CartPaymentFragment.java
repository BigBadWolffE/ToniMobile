package co.crowde.toni.view.fragment.cart;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import co.crowde.toni.R;
import co.crowde.toni.helper.CloseSoftKeyboard;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.CustomerModel;
import co.crowde.toni.view.activity.cart.CartListActivity;
import co.crowde.toni.view.dialog.message.transaction.ConfirmTransactionDialog;
import co.crowde.toni.view.fragment.modul.DashboardFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartPaymentFragment extends Fragment
implements View.OnClickListener{

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static TextView tvCreditLabel , tvCredit,
            tvPaymentLabel, tvPayment,
            tvAmountLabel, tvAmount,
            tvChangeLabel, tvChange,
            tvCreditTotalLabel, tvCreditTotal;
    public static CardView cvCash, cvCredit, cvCashCredit,
            cvConfirm;
    public static ImageView imgCashBtn, imgCreditBtn, imgCashCreditBtn;

    public static boolean
            cash,
            credit,
            cashCredit;

    private static DecimalFormat formatNumber;

    public static AlertDialog dialogCash, dialogCredit, dialogCashCredit, dialogConfirm;

    //Cash
    public static TextView tvTotalAmount, btnOK,
                        tvCash01, tvCash02, tvCash03, tvCash04;
    public static ImageView imgCashClose;
    public static EditText etCash;
    public static int cash01, cash02, cash03, cash04, cash05;

    //Credit
    public static TextView tvAmountCredit, tvCredit01;
    public static ImageView imgCreditClose;

    //CashCredit
    public static TextView tvAmountCashCredit, tvCreditAmount, btnCreditOK,
            labelCredit, tvCreditAdd, btnNominalOK,
            tvCashCredit01, tvCashCredit02, tvCashCredit03, tvCashCredit04;
    public static ImageView imgClose;
    public static EditText etCashCredit, etCashCreditNominal;
    public static CustomerModel model;
    public static LinearLayout layoutCredit;
    public static int cashCredit01, cashCredit02, cashCredit03, cashCredit04, cashCredit05;
    public static Group groupCredit, groupNominal, groupNominalAmount;
    public static ConstraintLayout layoutNominalCashCredit, layoutNominalGroup;

    public static int nominal, change, creditPay, totalBill, totalCredit;
    public static boolean payment;
    public static String paymentType;

    //Confirm
    public static TextView tvYes, tvNo;

    ConstraintLayout layoutCartList;

    public CartPaymentFragment() {
        // Required empty public constructor
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

//        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
//        if (tabletSize) {
//            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        } else {
//            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }

        View  view =  inflater.inflate(R.layout.fragment_cart_payment, container, false);

        formatNumber = new DecimalFormat("###,###,###,###,###,###");
        payment  = false;

        imgCashBtn = view.findViewById(R.id.imgCashBtn);
        imgCreditBtn = view.findViewById(R.id.imgCreditBtn);
        imgCashCreditBtn = view.findViewById(R.id.imgCashCreditBtn);

        tvCreditLabel = view.findViewById(R.id.tvCreditLabel);
        tvCredit = view.findViewById(R.id.tvCredit);
        imgCashBtn = view.findViewById(R.id.imgCashBtn);
        tvPaymentLabel = view.findViewById(R.id.tvPaymentLabel);
        tvPayment = view.findViewById(R.id.tvPayment);
        tvAmountLabel = view.findViewById(R.id.tvAmountLabel);
        tvAmount = view.findViewById(R.id.tvAmount);
        tvChangeLabel = view.findViewById(R.id.tvChangeLabel);
        tvChange = view.findViewById(R.id.tvChange);
        tvCreditTotalLabel = view.findViewById(R.id.tvCreditTotalLabel);
        tvCreditTotal = view.findViewById(R.id.tvCreditTotal);
        cvCash = view.findViewById(R.id.cvCash);
        cvCredit = view.findViewById(R.id.cvCredit);
        cvCashCredit = view.findViewById(R.id.cvCashCredit);
        cvConfirm = view.findViewById(R.id.cvConfirm);
        layoutCartList = view.findViewById(R.id.layout_cart_list);

        cash = false;
        credit = false;
        cashCredit = false;
        showLabel(getActivity());

        tvAmount.setText("Rp. "
                +formatNumber.format(DashboardFragment.totalAmount)+",-");

        model = new Gson().fromJson(SavePref.readCustomer(getActivity()), CustomerModel.class);

        enablePayBill();

        cvCash.setOnClickListener(this);
        cvCredit.setOnClickListener(this);
        cvCashCredit.setOnClickListener(this);
        layoutCartList.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.cvCash:
                showCash(getActivity());

                // do your code
                break;

            case R.id.cvCredit:
                showCredit(getActivity());
                // do your code
                break;

            case R.id.cvCashCredit:
                showCashCredit(getActivity());
                // do your code
                break;

            case R.id.layout_cart_list:
                break;

            default:
                break;
        }

    }

    @SuppressLint("SetTextI18n")
    public void showCash(final Activity activity) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);


        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(activity)
                .inflate(R.layout.layout_payment_cash_popup,
                        viewGroup,
                        false);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        formatNumber = new DecimalFormat("###,###,###,###,###,###");

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        dialogCash = builder.create();

        //Get View Id
        imgCashClose = dialogView.findViewById(R.id.imgCashClose);
        tvTotalAmount = dialogView.findViewById(R.id.tvTotalAmount);
        etCash = dialogView.findViewById(R.id.etCash);
        btnOK = dialogView.findViewById(R.id.btnOK);
        tvCash01 = dialogView.findViewById(R.id.tvCash01);
        tvCash02 = dialogView.findViewById(R.id.tvCash02);
        tvCash03 = dialogView.findViewById(R.id.tvCash03);
        tvCash04 = dialogView.findViewById(R.id.tvCash04);

        cash01 = (int) Math.ceil((double) DashboardFragment.totalAmount/5000)*5000;
        cash02 = (int) Math.ceil((double) DashboardFragment.totalAmount/10000)*10000;
        cash03 = (int) Math.ceil((double) DashboardFragment.totalAmount/20000)*20000;
        cash04 = (int) Math.ceil((double) DashboardFragment.totalAmount/50000)*50000;
        cash05 = (int) Math.ceil((double) DashboardFragment.totalAmount/100000)*100000;

        if(DashboardFragment.totalAmount<49999){
            if(cash01==cash02 || cash01==cash03 || cash02==cash03){
                tvCash02.setText(formatNumber.format(cash01)+",-");
                tvCash03.setText(formatNumber.format(cash04)+",-");
                tvCash04.setText(formatNumber.format(cash05)+",-");
            } else {
                tvCash02.setText(formatNumber.format(cash01)+",-");
                tvCash03.setText(formatNumber.format(cash02)+",-");
                tvCash04.setText(formatNumber.format(cash03)+",-");

            }
        } else if(DashboardFragment.totalAmount>49999 && DashboardFragment.totalAmount<100000){
            if(cash01==cash02 || cash01==cash03 || cash02==cash03){
                tvCash02.setText(formatNumber.format(cash02)+",-");
                tvCash03.setText(formatNumber.format(cash03)+",-");
                tvCash04.setText(formatNumber.format(cash04)+",-");
            } else {
                tvCash02.setText(formatNumber.format(cash01)+",-");
                tvCash03.setText(formatNumber.format(cash02)+",-");
                tvCash04.setVisibility(View.VISIBLE);
                tvCash04.setText(formatNumber.format(cash03)+",-");

            }

        } else if(DashboardFragment.totalAmount>99999){
            if(cash01==cash02 || cash01==cash03 || cash02==cash03){
                tvCash02.setText(formatNumber.format(cash03)+",-");
                tvCash03.setText(formatNumber.format(cash04)+",-");
                tvCash04.setText(formatNumber.format(cash05)+",-");
                if(cash04==cash05){
                    tvCash04.setVisibility(View.GONE);
                } else {
                    tvCash04.setVisibility(View.VISIBLE);
                }
            } else {
                tvCash02.setText(formatNumber.format(cash01)+",-");
                tvCash03.setText(formatNumber.format(cash02)+",-");
                tvCash04.setVisibility(View.VISIBLE);
                tvCash04.setText(formatNumber.format(cash03)+",-");

            }

        }

        tvTotalAmount.setText("Rp. "+formatNumber.format(DashboardFragment.totalAmount)+",-");
        etCash.addTextChangedListener(watcherCash);
//        etCash.addTextChangedListener(Cash());

        imgCashClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCash.dismiss();
            }
        });

        tvCash01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment = true;
                cash = true;
                credit = false;
                cashCredit = false;
                paymentType = "Cash";
                enablePayBill();
                nominal = DashboardFragment.totalAmount;
                tvPayment.setText("Rp. "+formatNumber.format(nominal)+",-");
                showLabel(activity);
                dialogCash.dismiss();
            }
        });

        tvCash02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment = true;
                cash = true;
                credit = false;
                cashCredit = false;
                paymentType = "Cash";
                enablePayBill();
                nominal = Integer.parseInt(tvCash02.getText().toString()
                        .replaceAll("[.,-]",""));
                tvPayment.setText("Rp. "+formatNumber.format(nominal)+",-");
                showLabel(activity);
                dialogCash.dismiss();
            }
        });

        tvCash03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment = true;
                cash = true;
                credit = false;
                cashCredit = false;
                paymentType = "Cash";
                enablePayBill();
                nominal = Integer.parseInt(tvCash03.getText().toString()
                        .replaceAll("[.,-]",""));
                tvPayment.setText("Rp. "+formatNumber.format(nominal)+",-");
                showLabel(activity);
                dialogCash.dismiss();
            }
        });

        tvCash04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment = true;
                cash = true;
                credit = false;
                cashCredit = false;
                paymentType = "Cash";
                enablePayBill();
                nominal = Integer.parseInt(tvCash04.getText().toString()
                        .replaceAll("[.,-]",""));
                tvPayment.setText("Rp. "+formatNumber.format(nominal)+",-");
                showLabel(activity);
                dialogCash.dismiss();
            }
        });

        etCash.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                CloseSoftKeyboard.hideSoftKeyboard(v, activity);
            }
        });

        dialogCash.show();
        dialogCash.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }

    public TextWatcher watcherCash = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(etCash.getText().length()>0){
                if (Integer.parseInt(etCash.getText().toString()
                        .replaceAll("[.,-]",""))<= DashboardFragment.totalAmount) {
                    btnOK.setBackground(getResources().getDrawable(R.drawable.bg_rec_stroke_radius_5dp_grey));
                    btnOK.setTextColor(getResources().getColor(R.color.colorThemeGrey));
                    btnOK.setEnabled(false);
                } else {
                    btnOK.setBackground(getResources().getDrawable(R.drawable.bg_rec_stroke_radius_5dp_orange));
                    btnOK.setTextColor(getResources().getColor(R.color.colorThemeOrange));
                    btnOK.setEnabled(true);

                    btnOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            payment = true;
                            cash = true;
                            credit = false;
                            cashCredit = false;
                            paymentType = "Cash";
                            enablePayBill();
                            nominal = Integer.parseInt(etCash.getText().toString()
                                    .replaceAll("[.,-]",""));
                            tvPayment.setText("Rp. "+formatNumber.format(nominal)+",-");
                            showLabel(getActivity());
                            dialogCash.dismiss();
                        }
                    });
                }
            } else{
                btnOK.setBackground(getResources().getDrawable(R.drawable.bg_rec_stroke_radius_5dp_grey));
                btnOK.setTextColor(getResources().getColor(R.color.colorThemeGrey));
                btnOK.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            etCash.removeTextChangedListener(this);
            try {
                String originalString = s.toString();

                long longval;
                if (originalString.contains(",")|| originalString.contains(".")) {
                    originalString = originalString.replaceAll("[.,]", "");
                }
                longval = Long.parseLong(originalString);

                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                formatter.applyPattern("###,###,###,###,###,###,###");
                String formattedString = formatter.format(longval);

                //setting text after format to EditText
                etCash.setText(formattedString);
                etCash.setSelection(etCash.getText().length());

            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }

            etCash.addTextChangedListener(this);
        }
    };

    @SuppressLint("SetTextI18n")
    public void showCredit(final Activity activity) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);


        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(activity)
                .inflate(R.layout.layout_payment_credit_popup,
                        viewGroup,
                        false);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        formatNumber = new DecimalFormat("###,###,###,###,###,###");

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        dialogCredit = builder.create();

        //Get View Id
        tvAmountCredit = dialogView.findViewById(R.id.tvTotalAmount);
        tvCredit01 = dialogView.findViewById(R.id.tvCredit01);
        imgCreditClose = dialogView.findViewById(R.id.imgCreditClose);

        tvAmountCredit.setText("Rp. "+formatNumber.format(model.getSaldo())+",-");

        imgCreditClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCredit.dismiss();
            }
        });

        tvCredit01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nominal = 0;
                change = 0;
                payment = true;
                cash = false;
                credit = true;
                cashCredit = false;
                paymentType = "Credit";
                enablePayBill();
                totalCredit = model.getSaldo() + DashboardFragment.totalAmount;
                tvCredit.setText("Rp. "+formatNumber.format(model.getSaldo())+",-");
                tvCreditTotal.setText("Rp. "+formatNumber.format(totalCredit)+",-");
                showLabel(activity);
                dialogCredit.dismiss();
            }
        });


        dialogCredit.show();
        dialogCredit.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }

    @SuppressLint("SetTextI18n")
    public void showCashCredit(final Activity activity) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);


        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(activity)
                .inflate(R.layout.layout_payment_cash_credit_popup,
                        viewGroup,
                        false);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        formatNumber = new DecimalFormat("###,###,###,###,###,###");
        creditPay = 0;
        totalBill = 0;

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        dialogCashCredit = builder.create();

        //Get View Id
        imgClose = dialogView.findViewById(R.id.imgClose);
        tvAmountCashCredit = dialogView.findViewById(R.id.tvTotalAmount);
        labelCredit = dialogView.findViewById(R.id.labelCredit);
        tvCreditAdd = dialogView.findViewById(R.id.tvCredit);
        etCashCredit = dialogView.findViewById(R.id.etCashCredit);
        etCashCreditNominal = dialogView.findViewById(R.id.etCashCreditNominal);
        btnCreditOK = dialogView.findViewById(R.id.btnCreditOK);
        btnNominalOK = dialogView.findViewById(R.id.btnNominalOK);
        tvCashCredit01 = dialogView.findViewById(R.id.tvCashCredit01);
        tvCashCredit02 = dialogView.findViewById(R.id.tvCashCredit02);
        tvCashCredit03 = dialogView.findViewById(R.id.tvCashCredit03);
        tvCashCredit04 = dialogView.findViewById(R.id.tvCashCredit04);
        layoutCredit = dialogView.findViewById(R.id.layoutCredit);
        groupCredit = dialogView.findViewById(R.id.groupCredit);
        layoutNominalCashCredit = dialogView.findViewById(R.id.layout_input_nominal);
        layoutNominalGroup = dialogView.findViewById(R.id.layout_nominal_group);
//        groupNominal = dialogView.findViewById(R.id.groupNominal);
//        groupNominalAmount = dialogView.findViewById(R.id.groupNOminalAmount);

        showCreditGroup(activity);

        tvAmountCashCredit.setText("Rp. "+formatNumber.format(DashboardFragment.totalAmount)+",-");
//        etCashCredit.addTextChangedListener(watcherCashCredit);
        etCashCreditNominal.addTextChangedListener(watcherCashCreditNominal);

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCashCredit.dismiss();
            }
        });

        tvCashCredit01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment = true;
                cash = false;
                credit = false;
                cashCredit = true;
                paymentType = "Cash";
                enablePayBill();
                nominal = totalBill;

                showLabel(activity);
                dialogCashCredit.dismiss();
            }
        });

        tvCashCredit02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment = true;
                cash = false;
                credit = false;
                cashCredit = true;
                paymentType = "Cash";
                enablePayBill();
                nominal = Integer.parseInt(tvCashCredit02.getText().toString()
                        .replaceAll("[.,-]",""));

                showLabel(activity);
                dialogCashCredit.dismiss();
            }
        });

        tvCashCredit03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment = true;
                cash = false;
                credit = false;
                cashCredit = true;
                paymentType = "Cash";
                enablePayBill();
                nominal = Integer.parseInt(tvCashCredit03.getText().toString()
                        .replaceAll("[,.-]",""));

                showLabel(activity);
                dialogCashCredit.dismiss();
            }
        });

        tvCashCredit04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment = true;
                cash = false;
                credit = false;
                cashCredit = true;
                paymentType = "Cash";
                enablePayBill();
                nominal = Integer.parseInt(tvCashCredit04.getText().toString()
                        .replaceAll("[,.-]",""));

                showLabel(activity);
                dialogCashCredit.dismiss();
            }
        });

        dialogCashCredit.show();
        dialogCashCredit.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }



//    public TextWatcher watcherCashCredit = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            if(etCashCredit.getText().length()>0){
//                if (Integer.parseInt(etCashCredit.getText().toString()
//                        .replaceAll("[,-]",""))<=999 ||
//                        Integer.parseInt(etCashCredit.getText().toString()
//                                .replaceAll("[,-]",""))>model.getSaldo()) {
//                    btnCreditOK.setBackground(getResources().getDrawable(R.drawable.bg_rec_stroke_radius_5dp_grey));
//                    btnCreditOK.setTextColor(getResources().getColor(R.color.colorThemeGrey));
//                    btnCreditOK.setEnabled(false);
//                } else {
//                    btnCreditOK.setBackground(getResources().getDrawable(R.drawable.bg_rec_stroke_radius_5dp_orange));
//                    btnCreditOK.setTextColor(getResources().getColor(R.color.colorThemeOrange));
//                    btnCreditOK.setEnabled(true);
//
//                    btnCreditOK.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            creditPay = Integer.parseInt(etCashCredit.getText().toString()
//                                    .replaceAll("[,-]",""));
//                            showCreditGroup(getActivity());
//                        }
//                    });
//                }
//            } else{
//                btnCreditOK.setBackground(getResources().getDrawable(R.drawable.bg_rec_stroke_radius_5dp_grey));
//                btnCreditOK.setTextColor(getResources().getColor(R.color.colorThemeGrey));
//                btnCreditOK.setEnabled(false);
//            }
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//            etCashCredit.removeTextChangedListener(this);
//            try {
//                String originalString = s.toString();
//
//                Long longval;
//                if (originalString.contains(",")) {
//                    originalString = originalString.replaceAll(",", "");
//                }
//                longval = Long.parseLong(originalString);
//
//                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//                formatter.applyPattern("###,###,###,###,###,###,###");
//                String formattedString = formatter.format(longval);
//
//                //setting text after format to EditText
//                etCashCredit.setText(formattedString);
//                etCashCredit.setSelection(etCashCredit.getText().length());
//
//            } catch (NumberFormatException nfe) {
//                nfe.printStackTrace();
//            }
//
//            etCashCredit.addTextChangedListener(this);
//        }
//    };

    public TextWatcher watcherCashCreditNominal = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(etCashCreditNominal.getText().length()>0){
                if (Integer.parseInt(etCashCreditNominal.getText().toString()
                        .replaceAll("[.,-]",""))<=900) {
                    btnNominalOK.setBackground(getResources().getDrawable(R.drawable.bg_rec_stroke_radius_5dp_grey));
                    btnNominalOK.setTextColor(getResources().getColor(R.color.colorThemeGrey));
                    btnNominalOK.setEnabled(false);
                } else {
                    btnNominalOK.setBackground(getResources().getDrawable(R.drawable.bg_rec_stroke_radius_5dp_orange));
                    btnNominalOK.setTextColor(getResources().getColor(R.color.colorThemeOrange));
                    btnNominalOK.setEnabled(true);

                    btnNominalOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            payment = true;
                            cash = false;
                            credit = false;
                            cashCredit = true;
                            paymentType = "Cash";
                            enablePayBill();
                            nominal = Integer.parseInt(etCashCreditNominal.getText().toString()
                                    .replaceAll("[.,-]",""));
                            tvPayment.setText("Rp. "+formatNumber.format(nominal)+",-");
                            showLabel(getActivity());
                            dialogCashCredit.dismiss();
                        }
                    });
                }
            } else{
                btnNominalOK.setBackground(getResources().getDrawable(R.drawable.bg_rec_stroke_radius_5dp_grey));
                btnNominalOK.setTextColor(getResources().getColor(R.color.colorThemeGrey));
                btnNominalOK.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            etCashCreditNominal.removeTextChangedListener(this);
            try {
                String originalString = s.toString();

                long longval;
                if (originalString.contains(",")|| originalString.contains(".")) {
                    originalString = originalString.replaceAll("[,.]", "");
                }
                longval = Long.parseLong(originalString);

                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                formatter.applyPattern("###,###,###,###,###,###,###");
                String formattedString = formatter.format(longval);

                //setting text after format to EditText
                etCashCreditNominal.setText(formattedString);
                etCashCreditNominal.setSelection(etCashCreditNominal.getText().length());

            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }

            etCashCreditNominal.addTextChangedListener(this);
        }
    };

    public void enablePayBill(){
        if(payment){
            cvConfirm.setCardBackgroundColor(getResources().getColor(R.color.colorThemeOrange));
            cvConfirm.setEnabled(true);
        } else {
            cvConfirm.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGrey));
            cvConfirm.setEnabled(true);
        }
    }

    public void showLabel(final Activity activity){
        if(cash){

            if(payment){
                change = nominal - DashboardFragment.totalAmount;
                tvChange.setText("Rp. "+formatNumber.format(change)+",-");
                imgCashBtn.setImageDrawable(activity
                        .getResources().getDrawable(R.drawable.ic_check_yellow_24dp));
                imgCreditBtn.setImageDrawable(activity
                        .getResources().getDrawable(R.drawable.ic_add_black_24dp));
                imgCashCreditBtn.setImageDrawable(activity
                        .getResources().getDrawable(R.drawable.ic_add_black_24dp));

                cvCash.setCardBackgroundColor(activity.getResources().getColor(R.color.CardView));
                cvCash.setCardElevation(2);
                cvCredit.setCardBackgroundColor(activity.getResources().getColor(R.color.EdittextLogin));
                cvCredit.setCardElevation(0);
                cvCashCredit.setCardBackgroundColor(activity.getResources().getColor(R.color.EdittextLogin));
                cvCashCredit.setCardElevation(0);
            }

            tvPaymentLabel.setVisibility(View.VISIBLE);
            tvPayment.setVisibility(View.VISIBLE);

            tvCreditLabel.setVisibility(View.GONE);
            tvCredit.setVisibility(View.GONE);

            tvChangeLabel.setVisibility(View.VISIBLE);
            tvChange.setVisibility(View.VISIBLE);

            tvCreditTotalLabel.setVisibility(View.GONE);
            tvCreditTotal.setVisibility(View.GONE);

            cvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConfirmTransactionDialog.showDialog(activity);
                }
            });

        } else if(credit){

            if(payment){
                imgCashBtn.setImageDrawable(activity
                        .getResources().getDrawable(R.drawable.ic_add_black_24dp));
                imgCreditBtn.setImageDrawable(activity
                        .getResources().getDrawable(R.drawable.ic_check_yellow_24dp));
                imgCashCreditBtn.setImageDrawable(activity
                        .getResources().getDrawable(R.drawable.ic_add_black_24dp));

                cvCash.setCardBackgroundColor(activity.getResources().getColor(R.color.EdittextLogin));
                cvCash.setCardElevation(0);
                cvCredit.setCardBackgroundColor(activity.getResources().getColor(R.color.CardView));
                cvCredit.setCardElevation(2);
                cvCashCredit.setCardBackgroundColor(activity.getResources().getColor(R.color.EdittextLogin));
                cvCashCredit.setCardElevation(0);
            }

            tvPaymentLabel.setVisibility(View.GONE);
            tvPayment.setVisibility(View.GONE);

            tvCreditLabel.setVisibility(View.VISIBLE);
            tvCredit.setVisibility(View.VISIBLE);

            tvChangeLabel.setVisibility(View.GONE);
            tvChange.setVisibility(View.GONE);

            tvCreditTotalLabel.setVisibility(View.VISIBLE);
            tvCreditTotal.setVisibility(View.VISIBLE);

            cvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConfirmTransactionDialog.showDialog(activity);
                }
            });

        } else if(cashCredit){

            if(payment){
                change = nominal - totalBill;
                totalCredit = model.getSaldo() - creditPay;

                tvPayment.setText("Rp. "+formatNumber.format(nominal)+",-");
                tvCredit.setText("Rp. "+formatNumber.format(creditPay)+",-");
                tvAmount.setText("Rp. " +formatNumber.format(totalBill)+",-");
                tvChange.setText("Rp. "+formatNumber.format(change)+",-");
                tvCreditTotal.setText("Rp. "+formatNumber.format(totalCredit)+",-");

                imgCashBtn.setImageDrawable(activity
                        .getResources().getDrawable(R.drawable.ic_add_black_24dp));
                imgCreditBtn.setImageDrawable(activity
                        .getResources().getDrawable(R.drawable.ic_add_black_24dp));
                imgCashCreditBtn.setImageDrawable(activity
                        .getResources().getDrawable(R.drawable.ic_check_yellow_24dp));

                cvCash.setCardBackgroundColor(activity.getResources().getColor(R.color.EdittextLogin));
                cvCash.setCardElevation(0);
                cvCredit.setCardBackgroundColor(activity.getResources().getColor(R.color.EdittextLogin));
                cvCredit.setCardElevation(0);
                cvCashCredit.setCardBackgroundColor(activity.getResources().getColor(R.color.CardView));
                cvCashCredit.setCardElevation(2);
            }

            tvPaymentLabel.setVisibility(View.VISIBLE);
            tvPayment.setVisibility(View.VISIBLE);

            tvCreditLabel.setVisibility(View.VISIBLE);
            tvCredit.setVisibility(View.VISIBLE);

            tvChangeLabel.setVisibility(View.VISIBLE);
            tvChange.setVisibility(View.VISIBLE);

            tvCreditTotalLabel.setVisibility(View.VISIBLE);
            tvCreditTotal.setVisibility(View.VISIBLE);

            cvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConfirmTransactionDialog.showDialog(activity);
                }
            });

        } else {

            if(!payment){
                imgCashBtn.setImageDrawable(activity
                        .getResources().getDrawable(R.drawable.ic_add_black_24dp));
                imgCreditBtn.setImageDrawable(activity
                        .getResources().getDrawable(R.drawable.ic_add_black_24dp));
                imgCashCreditBtn.setImageDrawable(activity
                        .getResources().getDrawable(R.drawable.ic_add_black_24dp));

                cvCash.setCardBackgroundColor(activity.getResources().getColor(R.color.CardView));
                cvCash.setCardElevation(2);
                cvCredit.setCardBackgroundColor(activity.getResources().getColor(R.color.CardView));
                cvCredit.setCardElevation(2);
                cvCashCredit.setCardBackgroundColor(activity.getResources().getColor(R.color.CardView));
                cvCashCredit.setCardElevation(2);
            }

            tvPaymentLabel.setVisibility(View.GONE);
            tvPayment.setVisibility(View.GONE);

            tvCreditLabel.setVisibility(View.GONE);
            tvCredit.setVisibility(View.GONE);

            tvChangeLabel.setVisibility(View.GONE);
            tvChange.setVisibility(View.GONE);

            tvCreditTotalLabel.setVisibility(View.GONE);
            tvCreditTotal.setVisibility(View.GONE);
        }
    }

    public void showCreditGroup(final Activity activity){
        if(creditPay!=0){

//            totalBill = DashboardFragment.totalAmount+Integer.parseInt(etCashCredit.getText().toString()
////                    .replaceAll("[,-]",""));
            totalBill = DashboardFragment.totalAmount+model.getSaldo();
            squareupTotalAmount();


//            groupCredit.setVisibility(View.GONE);
//            groupNominal.setVisibility(View.VISIBLE);
            layoutNominalGroup.setVisibility(View.VISIBLE);
            layoutNominalCashCredit.setVisibility(View.VISIBLE);

            labelCredit.setText("Berhasil menambahkan");
            labelCredit.setTextColor(activity.getResources().getColor(R.color.colorThemeOrange));
            tvCreditAdd.setText("Hutang Rp."+formatNumber.format(creditPay)+",-");
            tvCreditAdd.setTextColor(activity.getResources().getColor(R.color.colorThemeOrange));
            layoutCredit.setBackground(activity.getResources().getDrawable(R.drawable.bg_rec_stroke_radius_5dp_orange));
            layoutCredit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    totalBill = 0;
                    creditPay = 0;
                    etCashCredit.setText("");
                    showCreditGroup(activity);
                    
                }
            });

            tvAmountCashCredit.setTextColor(activity.getResources().getColor(R.color.colorThemeOrange));
            tvAmountCashCredit.setText("Rp. "+(formatNumber.format(totalBill))+",-");


        } else {
//            groupCredit.setVisibility(View.VISIBLE);
//            groupNominal.setVisibility(View.GONE);
            layoutNominalGroup.setVisibility(View.GONE);
            layoutNominalCashCredit.setVisibility(View.GONE);

            labelCredit.setText("Hutang");
            labelCredit.setTextColor(activity.getResources().getColor(R.color.colorWhite));
            tvCreditAdd.setText("Rp. "+formatNumber.format(model.getSaldo())+",-");
            tvCreditAdd.setTextColor(activity.getResources().getColor(R.color.colorWhite));
            layoutCredit.setBackground(activity.getResources().getDrawable(R.drawable.bg_rec_orange_radius_5dp));
            layoutCredit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    totalBill = 0;
                    creditPay = model.getSaldo();
//                    etCashCredit.setText("");
                    showCreditGroup(activity);

                }
            });

            tvAmountCashCredit.setTextColor(activity.getResources().getColor(R.color.colorWhite));
            tvAmountCashCredit.setText("Rp. "+formatNumber.format(DashboardFragment.totalAmount)+",-");
        }
    }

    private void squareupTotalAmount() {
        cashCredit01 = (int) Math.ceil((double)totalBill/5000)*5000;
        cashCredit02 = (int) Math.ceil((double)totalBill/10000)*10000;
        cashCredit03 = (int) Math.ceil((double)totalBill/20000)*20000;
        cashCredit04 = (int) Math.ceil((double)totalBill/50000)*50000;
        cashCredit05 = (int) Math.ceil((double)totalBill/100000)*100000;

        if(totalBill<49999){
            if(cashCredit01==cashCredit02 || cashCredit01==cashCredit03 || cashCredit02==cashCredit03){
                tvCashCredit02.setText(formatNumber.format(cashCredit01)+",-");
                tvCashCredit03.setText(formatNumber.format(cashCredit04)+",-");
                tvCashCredit04.setText(formatNumber.format(cashCredit05)+",-");
            } else {
                tvCashCredit02.setText(formatNumber.format(cashCredit01)+",-");
                tvCashCredit03.setText(formatNumber.format(cashCredit02)+",-");
                tvCashCredit04.setText(formatNumber.format(cashCredit03)+",-");

            }
        } else if(totalBill>49999 && totalBill<100000){
            if(cashCredit01==cashCredit02 || cashCredit01==cashCredit03 || cashCredit02==cashCredit03){
                tvCashCredit02.setText(formatNumber.format(cashCredit02)+",-");
                tvCashCredit03.setText(formatNumber.format(cashCredit03)+",-");
                tvCashCredit04.setText(formatNumber.format(cashCredit04)+",-");
            } else {
                tvCashCredit02.setText(formatNumber.format(cashCredit01)+",-");
                tvCashCredit03.setText(formatNumber.format(cashCredit02)+",-");
                tvCashCredit04.setVisibility(View.VISIBLE);
                tvCashCredit04.setText(formatNumber.format(cashCredit03)+",-");

            }

        } else if(totalBill>99999){
            if(cashCredit01==cashCredit02 || cashCredit01==cashCredit03 || cashCredit02==cashCredit03){
                tvCashCredit02.setText(formatNumber.format(cashCredit03)+",-");
                tvCashCredit03.setText(formatNumber.format(cashCredit04)+",-");
                tvCashCredit04.setText(formatNumber.format(cashCredit05)+",-");
                if(cashCredit04==cashCredit05){
                    tvCashCredit04.setVisibility(View.GONE);
                } else {
                    tvCashCredit04.setVisibility(View.VISIBLE);
                }
            } else {
                tvCashCredit02.setText(formatNumber.format(cashCredit01)+",-");
                tvCashCredit03.setText(formatNumber.format(cashCredit02)+",-");
                tvCashCredit04.setVisibility(View.VISIBLE);
                tvCashCredit04.setText(formatNumber.format(cashCredit03)+",-");

            }

        }
    }

}
