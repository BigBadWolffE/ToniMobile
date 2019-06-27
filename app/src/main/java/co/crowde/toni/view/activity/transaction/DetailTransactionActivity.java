package co.crowde.toni.view.activity.transaction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.adapter.TransactionProductListAdapter;
import co.crowde.toni.adapter.TransaksiBagianPelangganAdapter;
import co.crowde.toni.helper.DecimalFormatRupiah;
import co.crowde.toni.model.CustomerModel;
import co.crowde.toni.model.TransactionProductModel;
import co.crowde.toni.model.response.list.TransactionModel;
import co.crowde.toni.network.CustomerRequest;
import co.crowde.toni.network.TransactionRequest;
import co.crowde.toni.view.dialog.message.transaction.RePrintTransactionDialog;
import co.crowde.toni.view.fragment.modul.InventoryFragment;

import static co.crowde.toni.helper.DateTimeFormater.lokal;

public class DetailTransactionActivity extends AppCompatActivity {

    public static TextView tvCustomerName, tvTransactionDate, tvTransactionId,
            tvTransactionAmount, tvPaymentType;

    public static RecyclerView rcTransactionProduct;

    public static List<TransactionProductModel> productModelList;
    public static TransactionProductListAdapter productListAdapter;

    public static CardView cvBtnPrintStruck;

    public static Toolbar toolbar;

    static DividerItemDecoration itemDecorator;

    public static ProgressDialog progressDialog;

    public static TransactionModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transaction);

        progressDialog = new ProgressDialog(DetailTransactionActivity.this);
        progressDialog.setMessage("Harap tunggu...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        tvCustomerName = findViewById(R.id.tv_transaction_customer_name);
        tvTransactionDate = findViewById(R.id.tv_transaction_date);
        tvTransactionId = findViewById(R.id.tv_transaction_id);
        tvTransactionAmount = findViewById(R.id.tv_amount_transaction);
        tvPaymentType = findViewById(R.id.tv_payment_type);
        rcTransactionProduct = findViewById(R.id.rc_transaction_product);
        cvBtnPrintStruck = findViewById(R.id.cv_re_print);
        toolbar = findViewById(R.id.toolbar_detail_transaction);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        itemDecorator = new DividerItemDecoration(getBaseContext(),
                DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getBaseContext(),
                R.drawable.divider_line_item));

        model = getIntent().getParcelableExtra(TransactionModel.class.getSimpleName());

        Log.e("Transaksi Model", new Gson().toJson(model));

        //date setup
        String tanggal = model.getCreatedAt();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        Date date = null;
        try {
            date = dateFormat.parse(tanggal);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat formatter = new SimpleDateFormat("dd EEEE yyyy", lokal);
        String dateStr = formatter.format(date);

        tvCustomerName.setText(model.getCustomerName());
        tvTransactionDate.setText(dateStr);
        tvTransactionId.setText(model.getTransactionId());
        if(model.getPaymentType().equals("Cash")){
            tvPaymentType.setText("Tunai");
        } else {
            tvPaymentType.setText("Hutang");
        }

        DecimalFormatRupiah.changeFormat(DetailTransactionActivity.this);
        tvTransactionAmount.setText("Rp. "+
                DecimalFormatRupiah.formatNumber.format(model.getAmount())+",-");

        initAdapterProduct(DetailTransactionActivity.this);
        TransactionRequest.getTransactionProductList(DetailTransactionActivity.this, model.getTransactionId());

        cvBtnPrintStruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RePrintTransactionDialog.showDialog(DetailTransactionActivity.this);
            }
        });
    }

    private void initAdapterProduct(Activity activity) {
        productModelList = new ArrayList<>();
        productListAdapter = new TransactionProductListAdapter(activity, productModelList, activity);

        rcTransactionProduct.setLayoutManager(new LinearLayoutManager(activity));
        rcTransactionProduct.addItemDecoration(itemDecorator);
        rcTransactionProduct.setAdapter(productListAdapter);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
