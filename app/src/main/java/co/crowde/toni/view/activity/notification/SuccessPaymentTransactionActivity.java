package co.crowde.toni.view.activity.notification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;

import co.crowde.toni.R;
import co.crowde.toni.view.dialog.message.transaction.ConfirmPrintTransactionDialog;

public class SuccessPaymentTransactionActivity extends AppCompatActivity {

    CardView cvBtnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_payment_transaction);

        cvBtnHome = findViewById(R.id.cvBtnHome);

        String data = getIntent().getStringExtra("data");

        ConfirmPrintTransactionDialog.showDialog(SuccessPaymentTransactionActivity.this, data);

        cvBtnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
