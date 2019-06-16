package co.crowde.toni.view.activity.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import co.crowde.toni.R;
import co.crowde.toni.helper.CallCenter;

public class ForgotPassActivity extends AppCompatActivity {

    public static TextView tvForgetPass, tvForgetPassDesc,
            tvBtnBack;
    public static CardView cvBtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        //Get View Id
        tvForgetPass = findViewById(R.id.tvForgetPass);
        tvForgetPassDesc = findViewById(R.id.tvForgetPassDesc);
        cvBtnBack = findViewById(R.id.cvBtnBack);

        tvBtnBack = findViewById(R.id.tvBtnBack);

//        LoginActivity.btnLogin.setEnabled(true);
        cvBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        tvForgetPassDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallCenter.showDial(ForgotPassActivity.this);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
