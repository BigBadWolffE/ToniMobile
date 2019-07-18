package co.crowde.toni.view.fragment.reset_password;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import co.crowde.toni.R;
import co.crowde.toni.constant.Const;
import co.crowde.toni.utils.analytics.AnalyticsToniUtils;
import co.crowde.toni.view.activity.auth.ResetPasswordActivity;
import co.crowde.toni.view.activity.notification.SuccessResetPasswordActivity;

import static co.crowde.toni.utils.ValidateEdittext.validateCreateNewPass;
import static co.crowde.toni.utils.ValidateEdittext.validateResetPass;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResetNewPassFragment extends Fragment implements View.OnClickListener {

    EditText etNewPass, etRetypePass;
    CardView cvConfirm;

    ProgressDialog progressDialog;

    public ResetNewPassFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_new_pass, container, false);

        etNewPass = view.findViewById(R.id.et_new_pass);
        etRetypePass = view.findViewById(R.id.et_retype_pass);
        cvConfirm = view.findViewById(R.id.cv_btn_confirm);

        progressDialog = new ProgressDialog(getContext());

        cvConfirm.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cv_btn_confirm:
                createNewPass();
                break;
        }

    }

    private void createNewPass() {
        progressDialog.setMessage("Harap tunggu...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        validateCreateNewPass(etNewPass, etRetypePass);

        if(validateCreateNewPass(etNewPass, etRetypePass)){
            progressDialog.dismiss();

            AnalyticsToniUtils.getEvent(Const.CATEGORY_AUTHENTIFICATION, Const.MODUL_PASS, Const.LABEL_SET_NEW_PASS_SUCCESS);

            Intent password = new Intent(getActivity(), SuccessResetPasswordActivity.class);
            startActivity(password);
            getActivity().finish();
        } else {
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Silahkan isi formulir dengan benar.", Toast.LENGTH_SHORT).show();
        }
    }
}
