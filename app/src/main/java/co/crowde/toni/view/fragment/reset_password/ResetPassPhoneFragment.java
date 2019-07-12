package co.crowde.toni.view.fragment.reset_password;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import co.crowde.toni.R;
import co.crowde.toni.view.activity.auth.ResetPasswordActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResetPassPhoneFragment extends Fragment {

    EditText etPhone;
    CardView cvSendOtp;
    boolean isEmpty;

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

        cvSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Harap tunggu...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                isEmpty = etPhone.getText().toString().length() <= 9;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();

                        if(!isEmpty){
                            ResetPasswordActivity.frames = ResetPasswordActivity.dots[1];
                            ResetPasswordActivity.changeLayout(getActivity(), etPhone.getText().toString());
                        }
                    }
                }, 1000);


            }
        });
        return  view;
    }

}
