package co.crowde.toni.view.activity.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;

import co.crowde.toni.R;
import co.crowde.toni.database.Cart;
import co.crowde.toni.helper.DecimalFormatRupiah;
import co.crowde.toni.model.CustomerModel;
import co.crowde.toni.utils.SetHeader;
import co.crowde.toni.view.activity.payment.PaymentCashActivity;
import co.crowde.toni.view.activity.payment.PaymentCreditActivity;
import co.crowde.toni.view.fragment.modul.DashboardFragment;

public class CartPaymentActivity extends AppCompatActivity implements View.OnClickListener {

    AppBarLayout appBarLayout;
    Toolbar toolbar;

    CustomerModel customerModel;

    ConstraintLayout layout_cash, layout_credit, layout_cash_credit;

    TextView tv_customer_name, tv_customer_phone, tv_customer_credit, tv_sub_total, tv_total_amount;

    int sub_total, total_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_payment);

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

        tv_customer_name = findViewById(R.id.tv_customer_name);
        tv_customer_phone = findViewById(R.id.tv_customer_phone);
        tv_customer_credit = findViewById(R.id.tv_customer_credit);
        tv_sub_total = findViewById(R.id.tv_sub_total);
        tv_total_amount = findViewById(R.id.tv_total_amount);
        layout_cash = findViewById(R.id.layout_payment_cash);
        layout_credit = findViewById(R.id.layout_payment_credit);
        layout_cash_credit = findViewById(R.id.layout_payment_cash_credit);

        DecimalFormatRupiah.changeFormat(this);

        customerModel = getIntent().getParcelableExtra(CustomerModel.class.getSimpleName());
        sub_total = Integer.parseInt(getIntent().getStringExtra("total_amount"));
        total_amount = Integer.parseInt(getIntent().getStringExtra("total_amount"));

        tv_customer_name.setText(customerModel.getCustomerName());
        tv_customer_phone.setText(customerModel.getPhone());
        tv_customer_credit.setText("Rp. "+String.valueOf(DecimalFormatRupiah.formatNumber.format(customerModel.getCredit())));
        tv_sub_total.setText("Rp. "+String.valueOf(DecimalFormatRupiah.formatNumber.format(sub_total)));
        tv_total_amount.setText("Rp. "+String.valueOf(DecimalFormatRupiah.formatNumber.format(total_amount)));

        layout_cash.setOnClickListener(this);
        layout_credit.setOnClickListener(this);
        layout_cash_credit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_payment_cash:
                Intent cash = new Intent(this, PaymentCashActivity.class);
                cash.putExtra(CustomerModel.class.getSimpleName(), customerModel);
                cash.putExtra("total_amount", ""+total_amount);
                startActivityForResult(cash, 123);
                break;

            case R.id.layout_payment_credit:
                Intent credit = new Intent(this, PaymentCreditActivity.class);
                credit.putExtra(CustomerModel.class.getSimpleName(), customerModel);
                credit.putExtra("total_amount", ""+total_amount);
                startActivityForResult(credit, 123);
                break;

            case R.id.layout_payment_cash_credit:
                Intent cash_credit = new Intent(this, PaymentCashActivity.class);
                cash_credit.putExtra(CustomerModel.class.getSimpleName(), customerModel);
                cash_credit.putExtra("total_amount", ""+total_amount);
                startActivityForResult(cash_credit, 123);
                break;
        }

    }
}
