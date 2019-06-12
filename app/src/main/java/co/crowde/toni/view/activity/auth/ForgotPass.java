package co.crowde.toni.view.activity.auth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import co.crowde.toni.R;
import co.crowde.toni.helper.CallCenter;

public class ForgotPass extends AppCompatActivity {

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

        Login.btnLogin.setEnabled(true);
        cvBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(ForgotPass.this, Login.class);
                startActivity(login);
                finish();

            }
        });

        tvForgetPassDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallCenter.showDial(ForgotPass.this);
            }
        });
    }
}