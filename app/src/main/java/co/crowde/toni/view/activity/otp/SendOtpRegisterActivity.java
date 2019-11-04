package co.crowde.toni.view.activity.otp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import co.crowde.toni.R;
import co.crowde.toni.base.BaseActivity;
import co.crowde.toni.model.OtpModel;
import co.crowde.toni.network.LocationRequest;

public class SendOtpRegisterActivity extends BaseActivity implements View.OnClickListener{

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static Toolbar toolbar;
    public static TextView tvOTPCountDown, tvSendMessage, tvResendMessage,btnVerifikasiOtp,btnRetrySendOtp;
    public static EditText otp1,otp2,otp3,otp4,otp5,otp6;

    public static CardView labelVerifikasiotp,labelRetrySendOtp;

    public static String otpCode;
    public static boolean delete = false;

    public static ProgressDialog progressDialog;
    boolean isEmpty;

    public static CountDownTimer countDownTimer;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_otp_register);

        tvOTPCountDown = findViewById(R.id.tv_count_down_otp);
        tvSendMessage = findViewById(R.id.tv_notification_send_message);
        tvResendMessage = findViewById(R.id.tv_resend_message);
        btnVerifikasiOtp = findViewById(R.id.label_verifikasi_otp);
        btnRetrySendOtp = findViewById(R.id.label_resend_otp);
        labelRetrySendOtp = findViewById(R.id.btn_resend_otp);
        labelVerifikasiotp = findViewById(R.id.btn_verifikasi_otp);
        toolbar = findViewById(R.id.toolbar);
        otp1 = findViewById(R.id.tv_otp_1);



        progressDialog = new ProgressDialog(this);
//        otp2 = findViewById(R.id.tv_otp_2);
//        otp3 = findViewById(R.id.tv_otp_3);
//        otp4 = findViewById(R.id.tv_otp_4);
//        otp5 = findViewById(R.id.tv_otp_5);
//        otp6 = findViewById(R.id.tv_otp_6);

        otpCode = otp1.getText().toString();

        otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                otpCode = otp1.getText().toString();
                if (count > after)
                    delete = true;

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                StringBuilder sb = new StringBuilder(s.toString());
                int replacePosition = otp1.getSelectionEnd();

                if (s.length() != 6) { //where 6 is the character underline per text
                    if (!delete) {
                        if (replacePosition < s.length())
                            sb.deleteCharAt(replacePosition);
                    } else {
                        sb.insert(replacePosition, '_');
                    }

                    if (replacePosition < s.length() || delete) {
                        otp1.setText(sb.toString());
                        otp1.setSelection(replacePosition);
                    } else {
                        otp1.setText(otpCode);
                        otp1.setSelection(replacePosition - 1);
                    }
                }
                delete = false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        btnVerifikasiOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isEmpty = false;
                if (otp1.getText().toString().length()==0){
                    isEmpty = true;
                    Toast.makeText(SendOtpRegisterActivity.this,"Lengkapi Data",Toast.LENGTH_SHORT).show();
                }
                new Handler().postDelayed(() ->{
                    if (!isEmpty){
                        progressDialog.setMessage("Harap tunggu...");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();

                        OtpModel otpModel = new OtpModel();
                        LocationRequest.verifyOtp(SendOtpRegisterActivity.this,otpModel);
                    }

                } ,1000);

            }
        });





        tvSendMessage.setText(getResources()
                .getString(R.string.kami_telah_mengirim_sms_otp_n_ke_nomor)+" ");

        btnRetrySendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationRequest.resendOtp(SendOtpRegisterActivity.this);
                btnVerifikasiOtp.setEnabled(true);
                labelVerifikasiotp.setCardBackgroundColor(getResources().getColor(R.color.colorThemeOrange));

                countDownTimer = new CountDownTimer(31000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        tvOTPCountDown.setText(""+millisUntilFinished/1000);
                        tvSendMessage.setText(getResources()
                                .getString(R.string.kami_telah_mengirim_sms_otp_n_ke_nomor)+" ");
                        labelRetrySendOtp.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFinish() {
                        tvOTPCountDown.setText("0");
                        btnVerifikasiOtp.setEnabled(false);
                        labelVerifikasiotp.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGrey));
                        labelRetrySendOtp.setVisibility(View.VISIBLE);

                        tvSendMessage.setText("Kode Verifikasi telah hangus. Silahkan kirim ulang kode verifikasi ke nomor ");
                        tvResendMessage.setText("Kirim ulang kode.");
                    }
                }.start();
            }
        });

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        new CountDownTimer(31000, 1000) {

            public void onTick(long millisUntilFinished) {
                tvOTPCountDown.setText(""+millisUntilFinished/1000);
            }

            public void onFinish() {
                tvOTPCountDown.setText("0");
                btnVerifikasiOtp.setEnabled(false);
                labelVerifikasiotp.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGrey));
                labelRetrySendOtp.setVisibility(View.VISIBLE);

                tvSendMessage.setText("Kode Verifikasi telah hangus. Silahkan kirim ulang kode verifikasi ke nomor ");
                tvResendMessage.setText("Kirim ulang kode.");
            }


        }.start();
    }




    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View v) {

    }
}
