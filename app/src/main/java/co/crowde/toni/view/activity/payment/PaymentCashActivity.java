package co.crowde.toni.view.activity.payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import co.crowde.toni.R;
import co.crowde.toni.base.BaseActivity;
import co.crowde.toni.helper.DecimalFormatRupiah;
import co.crowde.toni.utils.SetHeader;

public class PaymentCashActivity extends BaseActivity implements View.OnClickListener {

    AppBarLayout appBarLayout;
    Toolbar toolbar;

    EditText et_nomial;

    TextView tv_cash_all, tv_cash_1000, tv_cash_2000, tv_cash_5000,
            tv_cash_10000, tv_cash_20000, tv_cash_50000, tv_cash_100000,
            tv_sub_total, tv_total_amount, tv_total_payment, tv_change;

    Button btn_payment;

    int[] cash_payment = {1000, 2000, 5000, 10000, 20000, 50000, 100000};
    int sub_total, total_amount, nominal, change;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_cash);

        appBarLayout = findViewById(R.id.appBar);
        SetHeader.isLolipop(this, appBarLayout);
        toolbar = findViewById(R.id.toolbarCart);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        DecimalFormatRupiah.changeFormat(this);

        sub_total = Integer.parseInt(getIntent().getStringExtra("total_amount"));
        total_amount = Integer.parseInt(getIntent().getStringExtra("total_amount"));

        et_nomial = findViewById(R.id.et_nominal);
        tv_cash_all = findViewById(R.id.tv_cash_all);
        tv_cash_1000 = findViewById(R.id.tv_cash_1000);
        tv_cash_2000 = findViewById(R.id.tv_cash_2000);
        tv_cash_5000 = findViewById(R.id.tv_cash_5000);
        tv_cash_10000 = findViewById(R.id.tv_cash_10000);
        tv_cash_20000 = findViewById(R.id.tv_cash_20000);
        tv_cash_50000 = findViewById(R.id.tv_cash_50000);
        tv_cash_100000 = findViewById(R.id.tv_cash_100000);
        tv_sub_total = findViewById(R.id.tv_sub_total);
        tv_total_payment = findViewById(R.id.tv_total_amount_payment);
        tv_total_amount = findViewById(R.id.tv_total_amount);
        tv_change = findViewById(R.id.tv_amount_change);
        btn_payment = findViewById(R.id.btn_payment);

        tv_cash_1000.setText(String.valueOf(DecimalFormatRupiah.formatNumber.format(cash_payment[0])));
        tv_cash_2000.setText(String.valueOf(DecimalFormatRupiah.formatNumber.format(cash_payment[1])));
        tv_cash_5000.setText(String.valueOf(DecimalFormatRupiah.formatNumber.format(cash_payment[2])));
        tv_cash_10000.setText(String.valueOf(DecimalFormatRupiah.formatNumber.format(cash_payment[3])));
        tv_cash_20000.setText(String.valueOf(DecimalFormatRupiah.formatNumber.format(cash_payment[4])));
        tv_cash_50000.setText(String.valueOf(DecimalFormatRupiah.formatNumber.format(cash_payment[5])));
        tv_cash_100000.setText(String.valueOf(DecimalFormatRupiah.formatNumber.format(cash_payment[6])));

        tv_sub_total.setText("Rp. "+String.valueOf(DecimalFormatRupiah.formatNumber.format(sub_total)));
        tv_total_amount.setText("Rp. "+String.valueOf(DecimalFormatRupiah.formatNumber.format(total_amount)));

        nominal = 0;
        change = 0;
        setNominal();
        validateChange();

        tv_cash_all.setOnClickListener(this);
        tv_cash_1000.setOnClickListener(this);
        tv_cash_2000.setOnClickListener(this);
        tv_cash_5000.setOnClickListener(this);
        tv_cash_10000.setOnClickListener(this);
        tv_cash_20000.setOnClickListener(this);
        tv_cash_50000.setOnClickListener(this);
        tv_cash_100000.setOnClickListener(this);
        btn_payment.setOnClickListener(this);

        et_nomial.addTextChangedListener(nominalWatcher());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cash_all:
                nominal = total_amount;
                et_nomial.setText(""+nominal);
                setNominal();
                validateChange();
                break;

            case R.id.tv_cash_1000:
                nominal = nominal+cash_payment[0];
                et_nomial.setText(""+nominal);
                setNominal();
                validateChange();
                break;

            case R.id.tv_cash_2000:
                nominal = nominal+cash_payment[1];
                et_nomial.setText(""+nominal);
                setNominal();
                validateChange();
                break;

            case R.id.tv_cash_5000:
                nominal = nominal+cash_payment[2];
                et_nomial.setText(""+nominal);
                setNominal();
                validateChange();
                break;

            case R.id.tv_cash_10000:
                nominal = nominal+cash_payment[3];
                et_nomial.setText(""+nominal);
                setNominal();
                validateChange();
                break;

            case R.id.tv_cash_20000:
                nominal = nominal+cash_payment[4];
                et_nomial.setText(""+nominal);
                setNominal();
                validateChange();
                break;

            case R.id.tv_cash_50000:
                nominal = nominal+cash_payment[5];
                et_nomial.setText(""+nominal);
                setNominal();
                validateChange();
                break;

            case R.id.tv_cash_100000:
                nominal = nominal+cash_payment[6];
                et_nomial.setText(""+nominal);
                setNominal();
                validateChange();
                break;

            case R.id.btn_payment:
                break;
        }

    }

    private void setNominal() {
        if(nominal>=total_amount){
            tv_total_payment.setText("Rp. "+DecimalFormatRupiah.formatNumber.format(nominal));
        }
    }

    private void validateChange() {
        if(nominal>=total_amount){
            change=nominal-total_amount;
        }
        tv_change.setText("Rp. "+String.valueOf(DecimalFormatRupiah.formatNumber.format(change)));
    }

    public TextWatcher nominalWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setNominal();
                validateChange();
            }

            @Override
            public void afterTextChanged(Editable s) {
                et_nomial.removeTextChangedListener(this);
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
                    et_nomial.setText(formattedString);
                    et_nomial.setSelection(et_nomial.getText().length());

                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                et_nomial.addTextChangedListener(this);

            }
        };
    }
}
