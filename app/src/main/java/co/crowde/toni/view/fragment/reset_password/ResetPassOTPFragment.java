package co.crowde.toni.view.fragment.reset_password;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import co.crowde.toni.R;
import co.crowde.toni.constant.Const;
import co.crowde.toni.utils.analytics.AnalyticsToniUtils;
import co.crowde.toni.view.activity.auth.ResetPasswordActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResetPassOTPFragment extends Fragment {

    TextView tvCount,
            tvOTP1, tvOTP2, tvOTP3, tvOTP4, tvOTP5, tvOTP6,
            tvSendMessage, tvResendMessage;
    ProgressDialog progressDialog;

    Bundle bundle;

    public ResetPassOTPFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_reset_pass_otp, container, false);
        progressDialog = new ProgressDialog(getActivity());

        tvOTP1 = view.findViewById(R.id.tv_otp_1);
        tvOTP2 = view.findViewById(R.id.tv_otp_2);
        tvOTP3 = view.findViewById(R.id.tv_otp_3);
        tvOTP4 = view.findViewById(R.id.tv_otp_4);
        tvOTP5 = view.findViewById(R.id.tv_otp_5);
        tvOTP6 = view.findViewById(R.id.tv_otp_6);

        tvCount = view.findViewById(R.id.tv_count_down_otp);
        tvSendMessage = view.findViewById(R.id.tv_notification_send_message);
        tvResendMessage = view.findViewById(R.id.tv_resend_message);

        bundle = this.getArguments();

        if(bundle != null){
            tvSendMessage.setText("Kami telah mengirim sms  OTP ke nomor ponsel anda  "
                    + getArguments().getString("phone"));
        }

        new CountDownTimer(61000, 1000) {

            public void onTick(long millisUntilFinished) {
                tvCount.setText(""+millisUntilFinished/1000);

//                if(millisUntilFinished/1000<21){
//                    tvOTPCountDown.setTextColor(getResources().getColor(R.color.color_theme_yellow));
//                }
//                if(millisUntilFinished/1000<11){
//                    tvOTPCountDown.setTextColor(getResources().getColor(R.color.color_theme_red));
//                }
            }

            public void onFinish() {
                tvOTP1.setText("0");
                tvOTP2.setText("2");
                tvOTP3.setText("0");
                tvOTP4.setText("2");
                tvOTP5.setText("9");
                tvOTP6.setText("6");

                tvCount.setText("0");

                if(bundle != null){
                    tvSendMessage.setText("Kode Verifikasi telah hangus. Silahkan kirim ulang kode verifikasi ke nomor "
                            + getArguments().getString("phone"));
                }
                tvResendMessage.setText("Kirim ulang kode.");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.setMessage("Harap tunggu...");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();

                                AnalyticsToniUtils.getEvent(Const.CATEGORY_AUTHENTIFICATION, Const.MODUL_PASS, Const.LABEL_RESET_PASS_SEND_OTP_SUCCESS);

                                ResetPasswordActivity.frames = ResetPasswordActivity.dots[2];
                                ResetPasswordActivity.changeLayout(getActivity(),"");
                            }
                        }, 2000);
                    }
                }, 1000);
            }

        }.start();

        return view;
    }

}
