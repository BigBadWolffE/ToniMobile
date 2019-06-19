package co.crowde.toni.view.activity.shop;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import co.crowde.toni.R;
import co.crowde.toni.controller.main.UserController;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.view.activity.home.MainActivity;
import co.crowde.toni.view.dialog.message.app.CloseAppsDialog;
import co.crowde.toni.view.dialog.message.shop.CloseShopDialog;

public class OpenShop extends AppCompatActivity {
    public static TextView tvOpenShopHeader,
            tvOpenShopLabel, tvOpenShopTime,
            tvBtnOpenLabel,tvCloseShop;
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

        setContentView(R.layout.activity_open_shop);

        //Get Id View
        tvOpenShopHeader = findViewById(R.id.tvOpenShopHeader);
        tvOpenShopLabel = findViewById(R.id.tvOpenShopLabel);
        tvOpenShopTime = findViewById(R.id.tvOpenShopTime);
        tvBtnOpenLabel = findViewById(R.id.tvBtnOpenLabel);
        tvCloseShop = findViewById(R.id.tvCloseShop);
        cvBtnOpen = findViewById(R.id.cvBtnOpen);

//        setShopOpenTime(OpenShop.this);
        tvOpenShopTime.setText(SavePref.readOpenTime(OpenShop.this));

        cvBtnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent open = new Intent(OpenShop.this, MainActivity.class);
                startActivity(open);
                finish();
            }
        });

        tvCloseShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CloseShopDialog.showDialog(OpenShop.this);
            }
        });

    }

    @Override
    public void onBackPressed() {
        CloseAppsDialog.showDialog(OpenShop.this);
    }
}