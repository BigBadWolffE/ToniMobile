package co.crowde.toni.view.login;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import co.crowde.toni.R;
import co.crowde.toni.controller.network.ShopRequest;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.ShopModel;

public class LoginSuccess extends AppCompatActivity {

    public static TextView tvLogSuccessHeader, tvClosedLabel, tvClosedTime,
            tvLoginAs, tvOwnerName,
            tvBtnOpenlabel;
    public static CardView cvBtnOpen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);

        //Get View Id
        tvLogSuccessHeader = findViewById(R.id.tvLogSuccessHeader);
        tvClosedLabel = findViewById(R.id.tvClosedLabel);
        tvClosedTime = findViewById(R.id.tvClosedTime);

        tvLoginAs = findViewById(R.id.tvLoginAs);
        tvOwnerName = findViewById(R.id.tvOwnerName);

        tvBtnOpenlabel = findViewById(R.id.tvBtnOpenLabel);

        cvBtnOpen = findViewById(R.id.cvBtnOpen);

        //Change Font Type
//        TypefaceTheme.fontMontserratReg(LoginSuccess.this);
//        tvLogSuccessHeader.setTypeface(TypefaceTheme.montserratBold);
//        tvOpenHeader.setTypeface(TypefaceTheme.montserratReg);
//        tvOpenTime.setTypeface(TypefaceTheme.montserratReg);
//
//        tvLoginAs.setTypeface(TypefaceTheme.montserratReg);
//        tvOwnerName.setTypeface(TypefaceTheme.montserratBold);
//
//        tvBtnOpenlabel.setTypeface(TypefaceTheme.montserratBold);

        setShopClosedTime(LoginSuccess.this);

        ShopRequest.getShopDetail(LoginSuccess.this);

        cvBtnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopRequest.openShop(LoginSuccess.this);
            }
        });

    }

    public static void setOwnerName(Activity activity, String user){

        //Parsing to model
        ShopModel shopModel = new Gson().fromJson(user, ShopModel.class);

        tvOwnerName.setText(
                shopModel.getOwnerName()+" - "+shopModel.getShopName());

    }

    public static void setShopClosedTime(Activity activity){
        if(SavePref.readClosedTime(activity)==null){
            tvClosedTime.setText(activity.getResources().getString(R.string.strips));
        } else {
            tvClosedTime.setText(SavePref.readClosedTime(activity));
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Tutup Aplikasi TONI")
                .setMessage("Apakah Anda ingin menutup aplikasi TONI?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        LoginSuccess.super.onBackPressed();
                    }
                }).create().show();
    }

}
