package co.crowde.toni.view.login;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import co.crowde.toni.R;
import co.crowde.toni.helper.CallCenter;
import co.crowde.toni.view.main.TypefaceTheme;

public class ForgetUsername{

    public static TextView tvForgetUser, tvForgetUserDesc,
            tvBtnBack;
    public static CardView cvBtnBack;

    public static void showForgetUser(final Activity activity) {
        final LayoutInflater inflater = LayoutInflater.from(activity);
        final View dialogView = inflater.inflate(
                R.layout.activity_forget_username, null);

        final Dialog dialog = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar);
        dialog.setContentView(dialogView);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimateSlide;
        dialog.show();

        //Get View Id
        tvForgetUser = dialogView.findViewById(R.id.tvForgetUser);
        tvForgetUserDesc = dialogView.findViewById(R.id.tvForgetUserDesc);
        cvBtnBack = dialogView.findViewById(R.id.cvBtnBack);

        tvBtnBack = dialogView.findViewById(R.id.tvBtnBack);

        //Change Font Type
        TypefaceTheme.fontMontserratReg(activity);
        tvForgetUser.setTypeface(TypefaceTheme.montserratBold);
        tvForgetUserDesc.setTypeface(TypefaceTheme.montserratReg);

        tvBtnBack.setTypeface(TypefaceTheme.montserratBold);

        Login.btnLogin.setEnabled(true);
        cvBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        tvForgetUserDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallCenter.showDial(activity);
            }
        });

    }
}
