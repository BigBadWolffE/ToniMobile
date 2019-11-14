package co.crowde.toni.view.activity.payment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.Group;

import com.google.android.material.appbar.AppBarLayout;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import co.crowde.toni.R;
import co.crowde.toni.helper.DecimalFormatRupiah;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.CustomerModel;
import co.crowde.toni.model.body.post.AddTransactionModel;
import co.crowde.toni.utils.SetHeader;
import co.crowde.toni.view.dialog.message.transaction.ConfirmPaymentDialog;
import co.crowde.toni.view.fragment.modul.DashboardFragment;

import static co.crowde.toni.helper.DecimalFormatRupiah.formatNumber;

public class PaymentCashActivity extends AppCompatActivity implements View.OnClickListener {

    AppBarLayout appBarLayout;
    Toolbar toolbar;

    EditText et_nominal;
    ImageView img_reset;

    CustomerModel customerModel;

    TextView tv_cash_all, tv_cash_1000, tv_cash_2000, tv_cash_5000,
            tv_cash_10000, tv_cash_20000, tv_cash_50000, tv_cash_100000,
            tv_sub_total, tv_total_amount, tv_total_payment, tv_change, tv_btn_payment, tv_amount_discount;

    int[] cash_payment = {1000, 2000, 5000, 10000, 20000, 50000, 100000};
    int sub_total, total_amount, nominal, change, discount;

    Group group_discount_amount;

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

        customerModel = getIntent().getParcelableExtra(CustomerModel.class.getSimpleName());

        sub_total = Integer.parseInt(getIntent().getStringExtra("sub_total"));
        total_amount = Integer.parseInt(getIntent().getStringExtra("total_amount"));
        discount = Integer.parseInt(getIntent().getStringExtra("discount"));

        et_nominal = findViewById(R.id.et_nominal);
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
        tv_btn_payment = findViewById(R.id.tv_btn_payment);
        img_reset = findViewById(R.id.img_reset);
        tv_amount_discount = findViewById(R.id.tv_amount_discount);
        group_discount_amount = findViewById(R.id.group_discount_amount);

        tv_cash_1000.setText(String.valueOf(DecimalFormatRupiah.formatNumber.format(cash_payment[0])));
        tv_cash_2000.setText(String.valueOf(DecimalFormatRupiah.formatNumber.format(cash_payment[1])));
        tv_cash_5000.setText(String.valueOf(DecimalFormatRupiah.formatNumber.format(cash_payment[2])));
        tv_cash_10000.setText(String.valueOf(DecimalFormatRupiah.formatNumber.format(cash_payment[3])));
        tv_cash_20000.setText(String.valueOf(DecimalFormatRupiah.formatNumber.format(cash_payment[4])));
        tv_cash_50000.setText(String.valueOf(DecimalFormatRupiah.formatNumber.format(cash_payment[5])));
        tv_cash_100000.setText(String.valueOf(DecimalFormatRupiah.formatNumber.format(cash_payment[6])));

        group_discount_amount.setVisibility(total_amount < sub_total ? View.VISIBLE : View.GONE);
        tv_amount_discount.setText("- Rp. " + formatNumber.format(discount));
        tv_sub_total.setText("Rp. " + formatNumber.format(sub_total));
        tv_total_amount.setText("Rp. " + formatNumber.format(total_amount));

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
        tv_btn_payment.setOnClickListener(this);
        img_reset.setOnClickListener(this);

//        et_nominal.addTextChangedListener(nominalWatcher());
        et_nominal.addTextChangedListener(paymentWatcher());
        setButtonEnabled();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cash_all:
                nominal = total_amount;
                et_nominal.setText("" + nominal);
                setNominal();
                validateChange();
                break;

            case R.id.tv_cash_1000:
                nominal = nominal + cash_payment[0];
                et_nominal.setText("" + nominal);
                setNominal();
                validateChange();
                break;

            case R.id.tv_cash_2000:
                nominal = nominal + cash_payment[1];
                et_nominal.setText("" + nominal);
                setNominal();
                validateChange();
                break;

            case R.id.tv_cash_5000:
                nominal = nominal + cash_payment[2];
                et_nominal.setText("" + nominal);
                setNominal();
                validateChange();
                break;

            case R.id.tv_cash_10000:
                nominal = nominal + cash_payment[3];
                et_nominal.setText("" + nominal);
                setNominal();
                validateChange();
                break;

            case R.id.tv_cash_20000:
                nominal = nominal + cash_payment[4];
                et_nominal.setText("" + nominal);
                setNominal();
                validateChange();
                break;

            case R.id.tv_cash_50000:
                nominal = nominal + cash_payment[5];
                et_nominal.setText("" + nominal);
                setNominal();
                validateChange();
                break;

            case R.id.tv_cash_100000:
                nominal = nominal + cash_payment[6];
                et_nominal.setText("" + nominal);
                setNominal();
                validateChange();
                break;

            case R.id.tv_btn_payment:
                AddTransactionModel add = new AddTransactionModel();
                add.setShopId(SavePref.readShopId(this));
                add.setCustomerId(customerModel.getCustomerId());
                add.setPaymentType("Cash");
                add.setAmount(String.valueOf(total_amount));
                add.setPaid(String.valueOf(nominal));
                add.set_change(String.valueOf(change));
                add.setDetails(DashboardFragment.cartModels);
                ConfirmPaymentDialog.showDialog(this, add, customerModel.getSaldo(), 0);
                break;

            case R.id.img_reset:
                nominal = 0;
                tv_total_payment.setText("Rp. " + DecimalFormatRupiah.formatNumber.format(nominal));
                et_nominal.setText("");
                break;
        }

    }

    private void setNominal() {
        if (nominal >= total_amount) {
            tv_total_payment.setText("Rp. " + DecimalFormatRupiah.formatNumber.format(nominal));
        }
    }

    private void validateChange() {
        if (nominal >= total_amount) {
            change = nominal - total_amount;
        }
        tv_change.setText("Rp. " + String.valueOf(DecimalFormatRupiah.formatNumber.format(change)));
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
                img_reset.setVisibility(et_nominal.getText().length() > 0 ? View.VISIBLE : View.GONE);
                setButtonEnabled();
            }

            @Override
            public void afterTextChanged(Editable s) {
                et_nominal.removeTextChangedListener(this);
                try {
                    String originalString = s.toString();

                    long longval;
                    if (originalString.contains(",") || originalString.contains(".")) {
                        originalString = originalString.replaceAll("[.,]", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("###,###,###,###,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    et_nominal.setText(formattedString);
                    et_nominal.setSelection(et_nominal.getText().length());

                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                et_nominal.addTextChangedListener(this);

            }
        };
    }

    public TextWatcher paymentWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(et_nominal.length()>0){
                    nominal = Integer.parseInt(et_nominal.getText().toString().replaceAll("[,.]",""));
                }
                setNominal();
                validateChange();
                img_reset.setVisibility(et_nominal.getText().length() > 0 ? View.VISIBLE : View.GONE);
                setButtonEnabled();
            }

            @Override
            public void afterTextChanged(Editable s) {
                et_nominal.removeTextChangedListener(this);
                try {
                    String originalString = s.toString();

                    long longval;
                    if (originalString.contains(",") || originalString.contains(".")) {
                        originalString = originalString.replaceAll("[.,]", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("###,###,###,###,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    et_nominal.setText(formattedString);
                    et_nominal.setSelection(et_nominal.getText().length());

                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                et_nominal.addTextChangedListener(this);

            }
        };
    }




    private void setButtonEnabled() {
        if (nominal >= total_amount) {
            tv_btn_payment.setEnabled(true);
            tv_btn_payment.setBackground(getResources().getDrawable(R.drawable.bg_rec_orange_radius_2dp));
            tv_btn_payment.setTextColor(getResources().getColor(R.color.colorWhite));
        } else {
            tv_btn_payment.setEnabled(false);
            tv_btn_payment.setBackground(getResources().getDrawable(R.drawable.bg_grey_cccccc_2dp));
            tv_btn_payment.setTextColor(getResources().getColor(R.color.color61000000));
        }
    }
}
