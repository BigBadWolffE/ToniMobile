package co.crowde.toni.view.activity.auth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import com.google.gson.Gson;

import co.crowde.toni.R;
import co.crowde.toni.network.ShopRequest;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.ShopModel;
import co.crowde.toni.view.dialog.message.app.CloseAppsDialog;

public class LoginSuccessActivity extends AppCompatActivity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static TextView tvLogSuccessHeader, tvClosedLabel, tvClosedTime,
            tvLoginAs, tvOwnerName,
            tvBtnOpenlabel;
    public static CardView cvBtnOpen;

    public static ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        setContentView(R.layout.activity_login_success);

        progressDialog = new ProgressDialog(LoginSuccessActivity.this);
        progressDialog.setMessage("Harap tunggu...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        //Get View Id
        tvLogSuccessHeader = findViewById(R.id.tvLogSuccessHeader);
        tvClosedLabel = findViewById(R.id.tvClosedLabel);
        tvClosedTime = findViewById(R.id.tvClosedTime);

        tvLoginAs = findViewById(R.id.tvLoginAs);
        tvOwnerName = findViewById(R.id.tvOwnerName);

        tvBtnOpenlabel = findViewById(R.id.tvBtnOpenLabel);

        cvBtnOpen = findViewById(R.id.cvBtnOpen);

        //Change Font Type
//        TypefaceTheme.fontMontserratReg(LoginSuccessActivity.this);
//        tvLogSuccessHeader.setTypeface(TypefaceTheme.montserratBold);
//        tvOpenHeader.setTypeface(TypefaceTheme.montserratReg);
//        tvOpenTime.setTypeface(TypefaceTheme.montserratReg);
//
//        tvLoginAs.setTypeface(TypefaceTheme.montserratReg);
//        tvOwnerName.setTypeface(TypefaceTheme.montserratBold);
//
//        tvBtnOpenlabel.setTypeface(TypefaceTheme.montserratBold);

        setShopClosedTime(LoginSuccessActivity.this);


        ShopRequest.getShopDetail(LoginSuccessActivity.this);

        cvBtnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(LoginSuccessActivity.this);
                progressDialog.setMessage("Harap tunggu...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                ShopRequest.openShop(LoginSuccessActivity.this);
            }
        });

    }

    public static void setOwnerName(Activity activity, String user){

        //Parsing to model
        ShopModel shopModel = new Gson().fromJson(user, ShopModel.class);

        tvOwnerName.setText(
                shopModel.getOwnerName()+" - "+shopModel.getShopName());

        progressDialog.dismiss();

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
        CloseAppsDialog.showDialog(LoginSuccessActivity.this);
    }

}
