package co.crowde.toni.view.fragment.reset_password;


import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import co.crowde.toni.R;
import co.crowde.toni.view.activity.notification.SuccessResetPasswordActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResetNewPassFragment extends Fragment {

    EditText etNewPass, etRetypePass;
    CardView cvConfirm;


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

        cvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent password = new Intent(getActivity(), SuccessResetPasswordActivity.class);
                startActivity(password);
                getActivity().finish();
            }
        });
        return view;
    }

}
