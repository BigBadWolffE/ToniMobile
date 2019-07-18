package co.crowde.toni.view.activity.otp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import co.crowde.toni.R;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.UserDetailModel;

public class SendOtpRegisterActivity extends AppCompatActivity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    Toolbar toolbar;
    TextView tvOTPCountDown, tvSendMessage, tvResendMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_otp_register);

        tvOTPCountDown = findViewById(R.id.tv_count_down_otp);
        tvSendMessage = findViewById(R.id.tv_notification_send_message);
        tvResendMessage = findViewById(R.id.tv_resend_message);
        toolbar = findViewById(R.id.toolbar);

        tvSendMessage.setText(getResources()
                .getString(R.string.kami_telah_mengirim_sms_otp_n_ke_nomor)+" ");

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        new CountDownTimer(61000, 1000) {

            public void onTick(long millisUntilFinished) {
                tvOTPCountDown.setText(""+millisUntilFinished/1000);
            }

            public void onFinish() {
                tvOTPCountDown.setText("0");

                tvSendMessage.setText("Kode Verifikasi telah hangus. Silahkan kirim ulang kode verifikasi ke nomor ");
                tvResendMessage.setText("Kirim ulang kode.");
            }

        }.start();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
