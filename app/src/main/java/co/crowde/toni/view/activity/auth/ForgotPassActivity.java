package co.crowde.toni.view.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import co.crowde.toni.R;
import co.crowde.toni.helper.CallCenter;

public class ForgotPassActivity extends AppCompatActivity {

    public static TextView tvForgetPass, tvForgetPassDesc,
            tvBtnBack, tvReset;
    public static CardView cvBtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        //Get View Id
        tvForgetPass = findViewById(R.id.tvForgetPass);
        tvForgetPassDesc = findViewById(R.id.tvForgetPassDesc);
        cvBtnBack = findViewById(R.id.cvBtnBack);
        tvReset = findViewById(R.id.tv_reset_password);

        tvBtnBack = findViewById(R.id.tvBtnBack);

        cvBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

        tvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reset = new Intent(ForgotPassActivity.this, ResetPasswordActivity.class);
                startActivity(reset);
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
