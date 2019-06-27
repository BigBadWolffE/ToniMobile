package co.crowde.toni.view.activity.notification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import java.text.DecimalFormat;

import co.crowde.toni.R;
import co.crowde.toni.helper.DecimalFormatRupiah;
import co.crowde.toni.model.response.object.CreditPayModel;
import co.crowde.toni.network.CustomerRequest;
import co.crowde.toni.view.activity.customer.CustomerHutangActivity;
import co.crowde.toni.view.dialog.message.customer.CreditPayDialog;
import co.crowde.toni.view.fragment.modul.CustomerFragment;

public class SuccessCreditPayActivity extends AppCompatActivity {

    CardView cvBtnBack;
    DecimalFormat formatNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_credit_pay);

        formatNumber = new DecimalFormat("###,###,###,###,###,###");

        CreditPayModel model = new Gson().fromJson(getIntent().getStringExtra("saldo"), CreditPayModel.class);

        Log.e("Saldo", getIntent().getStringExtra("saldo"));

        cvBtnBack = findViewById(R.id.cvBtnBack);

        cvBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerHutangActivity.tv_totalpelangganhutang.setText("Rp. "+
                        formatNumber.format(model.getSaldo())+",-");
                CustomerHutangActivity.dialogCredit.dismiss();
                finish();
            }
        });
    }
}