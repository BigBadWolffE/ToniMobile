package co.crowde.toni.view.fragment.reset_password;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import co.crowde.toni.R;
import co.crowde.toni.constant.Const;
import co.crowde.toni.utils.analytics.AnalyticsToniUtils;
import co.crowde.toni.view.activity.auth.LoginActivity;
import co.crowde.toni.view.activity.auth.ResetPasswordActivity;

import static co.crowde.toni.utils.ValidateEdittext.validateResetPass;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResetPassPhoneFragment extends Fragment implements View.OnClickListener {

    EditText etPhone;
    CardView cvSendOtp;

    ProgressDialog progressDialog;

    public ResetPassPhoneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_pass_phone, container, false);

        progressDialog = new ProgressDialog(getActivity());

        etPhone = view.findViewById(R.id.et_reset_phone);
        cvSendOtp = view.findViewById(R.id.cv_btn_send);

        cvSendOtp.setOnClickListener(this);

        return  view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cv_btn_send:
                sendOtp();
                break;
        }

    }

    void sendOtp(){
        progressDialog.setMessage("Harap tunggu...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        validateResetPass(etPhone);

        if(validateResetPass(etPhone)){
            progressDialog.dismiss();

            AnalyticsToniUtils.getEvent(Const.CATEGORY_AUTHENTIFICATION, Const.MODUL_PASS, Const.LABEL_RESET_PASS_SEND_OTP);

            ResetPasswordActivity.frames = ResetPasswordActivity.dots[1];
            ResetPasswordActivity.changeLayout(getActivity(), etPhone.getText().toString());
        } else {
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Silahkan isi formulir dengan benar.", Toast.LENGTH_SHORT).show();
        }
    }
}
