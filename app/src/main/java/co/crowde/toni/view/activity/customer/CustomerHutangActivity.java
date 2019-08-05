package co.crowde.toni.view.activity.customer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStream;
import java.text.BreakIterator;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import co.crowde.toni.R;
import co.crowde.toni.constant.Const;
import co.crowde.toni.controller.print.PrintController;
import co.crowde.toni.helper.DecimalFormatRupiah;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.CustomerModel;
import co.crowde.toni.model.ShopModel;
import co.crowde.toni.network.CustomerRequest;
import co.crowde.toni.utils.SetHeader;
import co.crowde.toni.utils.analytics.AnalyticsToniUtils;
import co.crowde.toni.utils.print.PrinterNetwork;
import co.crowde.toni.view.activity.print.WaitingCreditPayActivity;
import co.crowde.toni.view.activity.transaction.DetailTransactionActivity;
import co.crowde.toni.view.dialog.message.customer.CreditPayDialog;
import co.crowde.toni.view.dialog.message.printer.PrinterConnectivityDialog;
import co.crowde.toni.view.fragment.modul.CustomerFragment;

import static co.crowde.toni.utils.print.PrinterNetwork.resetConnection;

public class CustomerHutangActivity extends AppCompatActivity {


    public static TextView tv_totalpelangganhutang;
    TextView tv_namapelangganhutang, tv_nomorponsel, tv_tanggalterdaftar;
    CardView btn_bayarhutang;
    RelativeLayout btn_cetakstruk;
    Toolbar toolbar;

    public static AlertDialog dialogCredit;
    TextView tvAmountCredit;
    EditText etAmount;
    CardView cvCreditPay;
    ImageView imgCreditClose;

    public static AlertDialog dialogSaldo;
    public static TextView tvCreditSaldo, labelCreditSaldo;
    public static CardView cvBtnPrintCreditPay;
    public static ImageView imgSaldoClose;


    TextView konfirmyacetak,konfirmtidakcetak;
    Dialog dialogconfirmcetakhutang;
    public static DecimalFormat formatNumber;
    public static CustomerModel customerModel;

    public static ProgressDialog progressDialog;

    AppBarLayout appBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_hutang);

        appBarLayout = findViewById(R.id.appBar);
        SetHeader.isLolipop(CustomerHutangActivity.this, appBarLayout);

        progressDialog = new ProgressDialog(CustomerHutangActivity.this);

        tv_namapelangganhutang = (TextView) findViewById(R.id.namapelanggandetailhutang);
        tv_nomorponsel = (TextView) findViewById(R.id.nomorponseldetailhutang);
        tv_tanggalterdaftar = (TextView) findViewById(R.id.tanggalterdaftardetailhutang);
        tv_totalpelangganhutang = (TextView) findViewById(R.id.totalhutangdetailpelanggan);
        btn_bayarhutang = (CardView) findViewById(R.id.btntransaksihutangpelanggan);
        btn_cetakstruk = (RelativeLayout) findViewById(R.id.btncetakstrukhutang);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));

        DecimalFormatRupiah.changeFormat(CustomerHutangActivity.this);

        customerModel = getIntent().getParcelableExtra(CustomerModel.class.getSimpleName());

        //date setup
        String tanggal = customerModel.getCreatedAt();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        Date date = null;
        try {
            date = dateFormat.parse(tanggal);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy"); //If you need time just put specific format for time like 'HH:mm:ss'
        String dateStr = formatter.format(date);

        tv_namapelangganhutang.setText(customerModel.getCustomerName());
        tv_nomorponsel.setText(customerModel.getPhone());
        tv_totalpelangganhutang.setText("Rp. "+
                DecimalFormatRupiah.formatNumber.format(customerModel.getSaldo())+",-");
        tv_tanggalterdaftar.setText(dateStr);

        getSaldo();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_bayarhutang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCredit(CustomerHutangActivity.this, customerModel);
            }
        });

        btn_cetakstruk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSaldo(CustomerHutangActivity.this, customerModel);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if(customerModel.getSaldo()!=Integer.parseInt(tv_totalpelangganhutang.getText().toString()
                .replaceAll("[Rp. ,-]",""))){
            finish();
            CustomerFragment.customerModelList.clear();
            CustomerRequest.page=1;
            CustomerRequest.getCustomerModulList(CustomerHutangActivity.this);
        } else {
            finish();
        }
    }

    public void getSaldo(){
        if (customerModel.getSaldo()!=0) {
            btn_bayarhutang.setVisibility(View.VISIBLE);
            btn_cetakstruk.setVisibility(View.VISIBLE);
        } else {
            btn_bayarhutang.setVisibility(View.GONE);
            btn_cetakstruk.setVisibility(View.GONE);
        }
    }

    @SuppressLint("SetTextI18n")
    public void showCredit(final Activity activity, CustomerModel model) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);


        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(activity)
                .inflate(R.layout.dialog_bayar_hutang,
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
        etAmount = dialogView.findViewById(R.id.etAmount);
        cvCreditPay = dialogView.findViewById(R.id.cvCreditPay);
        imgCreditClose = dialogView.findViewById(R.id.imgCreditClose);

        etAmount.addTextChangedListener(watcherAmount);

        tvAmountCredit.setText("Rp. "+formatNumber.format(model.getSaldo())+",-");

        imgCreditClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCredit.dismiss();
            }
        });

        dialogCredit.show();
        dialogCredit.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }

    public TextWatcher watcherAmount = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(etAmount.getText().length()>0){
                if (Integer.parseInt(etAmount.getText().toString()
                        .replaceAll("[,.-]",""))>999 &&
                        Integer.parseInt(etAmount.getText().toString()
                                .replaceAll("[,.-]",""))<= customerModel.getSaldo()) {
                    cvCreditPay.setCardBackgroundColor(getResources().getColor(R.color.colorThemeOrange));
                    cvCreditPay.setEnabled(true);

                    cvCreditPay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CreditPayDialog.showDialog(CustomerHutangActivity.this,
                                    Integer.parseInt(etAmount.getText().toString()
                                            .replaceAll("[,.-]","")), customerModel.getCustomerId());

                        }
                    });

                } else {
                    cvCreditPay.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGrey));
                    cvCreditPay.setEnabled(false);
                }
            } else{
                cvCreditPay.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGrey));
                cvCreditPay.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            etAmount.removeTextChangedListener(this);
            try {
                String originalString = s.toString();

                long longval;
                if (originalString.contains(",") || originalString.contains(".")) {
                    originalString = originalString.replaceAll("[,.]", "");
                }
                longval = Long.parseLong(originalString);

                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                formatter.applyPattern("###,###,###,###,###,###,###");
                String formattedString = formatter.format(longval);

                //setting text after format to EditText
                etAmount.setText(formattedString);
                etAmount.setSelection(etAmount.getText().length());

            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }

            etAmount.addTextChangedListener(this);
        }
    };

    @SuppressLint("SetTextI18n")
    public static void showSaldo(final Activity activity, CustomerModel model) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);


        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(activity)
                .inflate(R.layout.layout_customer_saldo,
                        viewGroup,
                        false);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        formatNumber = new DecimalFormat("###,###,###,###,###,###");

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        dialogSaldo = builder.create();

        //Get View Id
        tvCreditSaldo = dialogView.findViewById(R.id.tv_credit_saldo);
        labelCreditSaldo = dialogView.findViewById(R.id.tv_label_credit_saldo);
        cvBtnPrintCreditPay = dialogView.findViewById(R.id.cvPrintCreditPay);
        imgSaldoClose = dialogView.findViewById(R.id.imgSaldoClose);

        tvCreditSaldo.setText(activity.getResources()
                .getString(R.string.sisa_hutang_pelanggan_anda) + model.getCustomerName());
        tvCreditSaldo.setText("Rp. "+formatNumber.format(model.getSaldo())+",-");

        imgSaldoClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSaldo.dismiss();
            }
        });

        final Date currentDateTimeString = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        final String formattedDate = dateFormat.format(currentDateTimeString);

        cvBtnPrintCreditPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Harap tunggu...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        resetConnection();
                        if(SavePref.readDeviceAddress(activity)!=null){
                            PrinterNetwork.bluetoothAddress = SavePref.readDeviceAddress(activity);
                            PrinterNetwork.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                            PrinterNetwork.mBluetoothDevice= PrinterNetwork.mBluetoothAdapter.getRemoteDevice(PrinterNetwork.bluetoothAddress);
                            try {
                                PrinterNetwork.mBluetoothSocket = PrinterNetwork.createBluetoothSocket(PrinterNetwork.mBluetoothDevice);
                                PrinterNetwork.mBluetoothSocket.connect();
                                if (PrinterNetwork.mBluetoothSocket.isConnected()){
                                    PrintController.printCustomerCredit(activity, customerModel, formattedDate);

                                    AnalyticsToniUtils.getEvent(Const.CATEGORY_CUSTOMER,Const.MODUL_CUSTOMER,Const.LABEL_CUSTOMER_PRINT);

                                    Intent print = new Intent(activity, WaitingCreditPayActivity.class);
                                    activity.startActivity(print);
                                    progressDialog.dismiss();
                                }
                            } catch (IOException e) {
                                PrinterConnectivityDialog.showDialog(activity);
                                progressDialog.dismiss();
                                Log.e("Bluetooth","Can't Connect");
                            }

                        } else {
                            PrinterNetwork.pairingBluetooth(activity);
                            progressDialog.dismiss();
                        }
                    }
                }, 100);

            }
        });

        dialogSaldo.show();
        dialogSaldo.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }

    private void cetakHutang(Activity activity){

        final LayoutInflater inflaterUser = LayoutInflater.from(CustomerHutangActivity.this);
        final View dialogView = inflaterUser.inflate(R.layout.layout_konfirmasi_cetak_hutang, null);
        DisplayMetrics displayMetricsPW = this.getResources().getDisplayMetrics();
        dialogconfirmcetakhutang = new Dialog(CustomerHutangActivity.this);
        dialogconfirmcetakhutang.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //todays date
        final Date currentDateTimeString = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd MMM yyyy");
        final String formattedDate = dateFormat1.format(currentDateTimeString);
        dialogconfirmcetakhutang.setContentView(dialogView);
        dialogconfirmcetakhutang.setCanceledOnTouchOutside(true);
        int dialogWidth = (int) (displayMetricsPW.widthPixels * 0.9);
        int dialogHeight = (int) (displayMetricsPW.heightPixels * 0.5);
        dialogconfirmcetakhutang.getWindow().setLayout(dialogWidth, dialogHeight);
        final String detailToko = SavePref.readDetailToko(CustomerHutangActivity.this);
        final ShopModel shopModel = new Gson().fromJson(detailToko, ShopModel.class);
        konfirmyacetak = (TextView) dialogView.findViewById(R.id.confirmyacetakhutang);
        konfirmyacetak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                resetConnection();


            }
        });

        konfirmtidakcetak = (TextView)dialogView.findViewById(R.id.confirmtidakcetakhutang);
        konfirmtidakcetak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        dialogconfirmcetakhutang.show();
        dialogconfirmcetakhutang.getWindow().setBackgroundDrawableResource(android.R.color.white);


    }
}
