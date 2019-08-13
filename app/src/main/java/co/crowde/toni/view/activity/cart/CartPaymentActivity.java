package co.crowde.toni.view.activity.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;

import org.apache.commons.lang3.StringUtils;

import co.crowde.toni.R;
import co.crowde.toni.database.Cart;
import co.crowde.toni.helper.DecimalFormatRupiah;
import co.crowde.toni.model.CartModel;
import co.crowde.toni.model.CustomerModel;
import co.crowde.toni.utils.SetHeader;
import co.crowde.toni.view.activity.payment.PaymentCashActivity;
import co.crowde.toni.view.activity.payment.PaymentCashCreditActivity;
import co.crowde.toni.view.activity.payment.PaymentCreditActivity;
import co.crowde.toni.view.fragment.modul.DashboardFragment;

import static co.crowde.toni.helper.DecimalFormatRupiah.formatNumber;
import static co.crowde.toni.utils.print.PrinterNetwork.printText;

public class CartPaymentActivity extends AppCompatActivity implements View.OnClickListener {

    AppBarLayout appBarLayout;
    Toolbar toolbar;

    CustomerModel customerModel;

    ConstraintLayout layout_cash, layout_credit, layout_cash_credit;

    TextView tv_customer_name, tv_customer_phone, tv_customer_credit, tv_sub_total, tv_amount_discount, tv_total_amount;

    Group group_discount_amount;

    int sub_total, total_amount, discount;

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

        sub_total = 0;
        discount = 0;
        total_amount = 0;

        tv_customer_name = findViewById(R.id.tv_customer_name);
        tv_customer_phone = findViewById(R.id.tv_customer_phone);
        tv_customer_credit = findViewById(R.id.tv_customer_credit);
        tv_sub_total = findViewById(R.id.tv_sub_total);
        tv_amount_discount = findViewById(R.id.tv_amount_discount);
        tv_total_amount = findViewById(R.id.tv_total_amount);
        layout_cash = findViewById(R.id.layout_payment_cash);
        layout_credit = findViewById(R.id.layout_payment_credit);
        layout_cash_credit = findViewById(R.id.layout_payment_cash_credit);
        group_discount_amount = findViewById(R.id.group_discount_amount);

        DecimalFormatRupiah.changeFormat(this);

        customerModel = getIntent().getParcelableExtra(CustomerModel.class.getSimpleName());
        total_amount = Integer.parseInt(getIntent().getStringExtra("total_amount"));

        layout_cash.setOnClickListener(this);
        layout_credit.setOnClickListener(this);
        layout_cash_credit.setOnClickListener(this);

        for(CartModel cartModel : DashboardFragment.cartModels){
            sub_total = sub_total+(cartModel.getQuantity()*cartModel.getSellingPrice());
            discount = discount+cartModel.getDiscount();
        }

        group_discount_amount.setVisibility(total_amount<sub_total ? View.VISIBLE : View.GONE);
        tv_amount_discount.setText("- Rp. "+ formatNumber.format(discount));
        tv_sub_total.setText("Rp. "+formatNumber.format(sub_total));
        tv_customer_name.setText(customerModel.getCustomerName());
        tv_customer_phone.setText(customerModel.getPhone());
        tv_customer_credit.setText("Rp. "+formatNumber.format(customerModel.getSaldo()));
        tv_total_amount.setText("Rp. "+formatNumber.format(total_amount));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_payment_cash:
                Intent cash = new Intent(this, PaymentCashActivity.class);
                cash.putExtra(CustomerModel.class.getSimpleName(), customerModel);
                cash.putExtra("total_amount", ""+total_amount);
                cash.putExtra("sub_total", ""+sub_total);
                cash.putExtra("discount", ""+discount);
                startActivityForResult(cash, 123);
                break;

            case R.id.layout_payment_credit:
                Intent credit = new Intent(this, PaymentCreditActivity.class);
                credit.putExtra(CustomerModel.class.getSimpleName(), customerModel);
                credit.putExtra("total_amount", ""+total_amount);
                credit.putExtra("sub_total", ""+sub_total);
                credit.putExtra("discount", ""+discount);
                startActivityForResult(credit, 123);
                break;

            case R.id.layout_payment_cash_credit:
                Intent cash_credit = new Intent(this, PaymentCashCreditActivity.class);
                cash_credit.putExtra(CustomerModel.class.getSimpleName(), customerModel);
                cash_credit.putExtra("total_amount", ""+total_amount);
                cash_credit.putExtra("sub_total", ""+sub_total);
                cash_credit.putExtra("discount", ""+discount);
                startActivityForResult(cash_credit, 123);
                break;
        }

    }
}
