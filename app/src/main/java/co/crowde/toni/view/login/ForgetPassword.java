package co.crowde.toni.view.login;

import android.app.Activity;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import co.crowde.toni.R;
import co.crowde.toni.helper.CallCenter;
import co.crowde.toni.view.main.TypefaceTheme;

public class ForgetPassword{

    public static TextView tvForgetPass, tvForgetPassDesc,
            tvBtnBack;
    public static CardView cvBtnBack;

    public static void showForgetPass(final Activity activity) {
        boolean tabletSize = activity.getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        final LayoutInflater inflater = LayoutInflater.from(activity);
        final View dialogView = inflater.inflate(
                R.layout.activity_forget_password, null);

        final Dialog dialog = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar);
        dialog.setContentView(dialogView);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimateFade;
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        //Get View Id
        tvForgetPass = dialogView.findViewById(R.id.tvForgetPass);
        tvForgetPassDesc = dialogView.findViewById(R.id.tvForgetPassDesc);
        cvBtnBack = dialogView.findViewById(R.id.cvBtnBack);

        tvBtnBack = dialogView.findViewById(R.id.tvBtnBack);

        //Change Font Type
        TypefaceTheme.fontMontserratReg(activity);
        tvForgetPass.setTypeface(TypefaceTheme.montserratBold);
        tvForgetPassDesc.setTypeface(TypefaceTheme.montserratReg);

        tvBtnBack.setTypeface(TypefaceTheme.montserratBold);

        Login.btnLogin.setEnabled(true);
        cvBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        tvForgetPassDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallCenter.showDial(activity);
            }
        });

    }

    public static void forgetPassword(final Activity activity) {

        boolean tabletSize = activity.getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        final LayoutInflater inflater = LayoutInflater.from(activity);
        final View dialogView = inflater.inflate(
                R.layout.activity_forget_password, null);

        final Dialog dialog = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar);
        dialog.setContentView(dialogView);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimateFade;
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        //Get View Id
        tvForgetPass = dialogView.findViewById(R.id.tvForgetPass);
        tvForgetPassDesc = dialogView.findViewById(R.id.tvForgetPassDesc);
        cvBtnBack = dialogView.findViewById(R.id.cvBtnBack);

        tvBtnBack = dialogView.findViewById(R.id.tvBtnBack);

        //Change Font Type
        TypefaceTheme.fontMontserratReg(activity);
        tvForgetPass.setTypeface(TypefaceTheme.montserratBold);
        tvForgetPassDesc.setTypeface(TypefaceTheme.montserratReg);

        tvBtnBack.setTypeface(TypefaceTheme.montserratBold);

        cvBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        tvForgetPassDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallCenter.showDial(activity);
            }
        });

    }
}
