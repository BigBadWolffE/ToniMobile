package co.crowde.toni.view.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import co.crowde.toni.R;
import co.crowde.toni.helper.CallCenter;

public class ForgotUserActivity extends AppCompatActivity {

    public static TextView tvForgetUser, tvForgetUserDesc,
            tvBtnBack, tvReset;
    public static CardView cvBtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_user);

        //Get View Id
        tvForgetUser = findViewById(R.id.tvForgetUser);
        tvForgetUserDesc = findViewById(R.id.tvForgetUserDesc);
        cvBtnBack = findViewById(R.id.cvBtnBack);
        tvReset = findViewById(R.id.tv_reset_password);

        tvBtnBack = findViewById(R.id.tvBtnBack);

        LoginActivity.btnLogin.setEnabled(true);
        cvBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

        tvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reset = new Intent(ForgotUserActivity.this, ResetPasswordActivity.class);
                startActivity(reset);
                finish();
            }
        });

        tvForgetUserDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallCenter.showDial(ForgotUserActivity.this);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
