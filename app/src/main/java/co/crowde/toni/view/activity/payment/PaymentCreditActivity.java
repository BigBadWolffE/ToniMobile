package co.crowde.toni.view.activity.payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.Group;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;

import co.crowde.toni.R;
import co.crowde.toni.helper.DecimalFormatRupiah;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.CustomerModel;
import co.crowde.toni.model.body.post.AddTransactionModel;
import co.crowde.toni.utils.SetHeader;
import co.crowde.toni.view.dialog.message.transaction.ConfirmPaymentDialog;
import co.crowde.toni.view.fragment.modul.DashboardFragment;

import static co.crowde.toni.helper.DecimalFormatRupiah.formatNumber;

public class PaymentCreditActivity extends AppCompatActivity {

    AppBarLayout appBarLayout;
    Toolbar toolbar;

    TextView tv_customer_name, tv_customer_phone, tv_customer_credit,
            tv_sub_total, tv_discount, tv_total_amount, tv_total_credit, tv_btn_payment;

    Group group_discount_amount;

    int sub_total, total_amount, discount, total_credit;

    CustomerModel customerModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_credit);

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

        total_credit = customerModel.getSaldo() + total_amount;

        tv_customer_name = findViewById(R.id.tv_customer_name);
        tv_customer_phone = findViewById(R.id.tv_customer_phone);
        tv_customer_credit = findViewById(R.id.tv_customer_credit);
        tv_sub_total = findViewById(R.id.tv_sub_total);
        tv_discount = findViewById(R.id.tv_amount_discount);
        tv_total_amount = findViewById(R.id.tv_total_amount);
        tv_total_credit = findViewById(R.id.tv_total_credit);
        tv_btn_payment = findViewById(R.id.tv_btn_payment);
        group_discount_amount = findViewById(R.id.group_discount_amount);

        tv_customer_name.setText(customerModel.getCustomerName());
        tv_customer_phone.setText(customerModel.getPhone());
        tv_customer_credit.setText("Rp. " + formatNumber.format(customerModel.getSaldo()));

        group_discount_amount.setVisibility(total_amount < sub_total ? View.VISIBLE : View.GONE);
        tv_discount.setText("- Rp. " + formatNumber.format(discount));
        tv_sub_total.setText("Rp. " + formatNumber.format(sub_total));
        tv_total_amount.setText("Rp. " + formatNumber.format(total_amount));
        tv_total_credit.setText("Rp." + formatNumber.format(total_credit));

        tv_btn_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTransactionModel add = new AddTransactionModel();
                add.setShopId(SavePref.readShopId(PaymentCreditActivity.this));
                add.setCustomerId(customerModel.getCustomerId());
                add.setPaymentType("Credit");
                add.setAmount(String.valueOf(total_amount));
                add.setPaid(String.valueOf(0));
                add.set_change(String.valueOf(0));
                add.setDetails(DashboardFragment.cartModels);

                ConfirmPaymentDialog.showDialog(PaymentCreditActivity.this, add, customerModel.getSaldo(), customerModel.getSaldo());
            }
        });

    }
}
