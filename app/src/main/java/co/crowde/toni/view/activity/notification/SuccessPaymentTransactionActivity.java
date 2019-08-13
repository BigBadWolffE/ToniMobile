package co.crowde.toni.view.activity.notification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import co.crowde.toni.R;
import co.crowde.toni.controller.transaction.TransactionController;
import co.crowde.toni.view.activity.home.MainActivity;
import co.crowde.toni.view.activity.print.WaitingPrintActivity;
import co.crowde.toni.view.dialog.message.transaction.ConfirmPrintTransactionDialog;
import co.crowde.toni.view.fragment.modul.DashboardFragment;

public class SuccessPaymentTransactionActivity extends AppCompatActivity {

    TextView tv_print_transaction, tv_back;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_payment_transaction);

        progressDialog = new ProgressDialog(this);

        tv_print_transaction = findViewById(R.id.tv_print_transaction);
        tv_back = findViewById(R.id.tv_back);

        String data = getIntent().getStringExtra("data");
        String payment_type = getIntent().getStringExtra("payment_type");
        String credit = getIntent().getStringExtra("credit");
        String saldo = getIntent().getStringExtra("saldo");

        tv_print_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Harap tunggu...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                tv_print_transaction.setEnabled(false);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        activity.finish();
                        tv_print_transaction.setEnabled(true);
                        TransactionController.printBill(SuccessPaymentTransactionActivity.this, data, progressDialog, payment_type, saldo ,credit);
                    }
                }, 100);
            }
        });

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DashboardFragment.dbCart.deleteAllItem();
        DashboardFragment.ifCartEmpty(this);
        Intent home = new Intent(SuccessPaymentTransactionActivity.this, MainActivity.class);
        home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(home);
        finish();
    }
}
