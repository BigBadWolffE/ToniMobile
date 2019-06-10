package co.crowde.toni.view.activity.auth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import co.crowde.toni.R;
import co.crowde.toni.helper.CallCenter;
import co.crowde.toni.view.main.TypefaceTheme;

public class ForgotUser extends AppCompatActivity {

    public static TextView tvForgetUser, tvForgetUserDesc,
            tvBtnBack;
    public static CardView cvBtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_user);

        //Get View Id
        tvForgetUser = findViewById(R.id.tvForgetUser);
        tvForgetUserDesc = findViewById(R.id.tvForgetUserDesc);
        cvBtnBack = findViewById(R.id.cvBtnBack);

        tvBtnBack = findViewById(R.id.tvBtnBack);

        Login.btnLogin.setEnabled(true);
        cvBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(ForgotUser.this, Login.class);
                startActivity(login);
                finish();

            }
        });

        tvForgetUserDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallCenter.showDial(ForgotUser.this);
            }
        });
    }
}
