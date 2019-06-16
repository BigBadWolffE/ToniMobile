package co.crowde.toni.view.activity.shop;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import co.crowde.toni.R;
import co.crowde.toni.controller.main.UserController;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.view.activity.home.MainActivity;
import co.crowde.toni.view.dialog.app.CloseAppsDialog;

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
                new AlertDialog.Builder(OpenShop.this)
                        .setTitle("Tutup Toko")
                        .setMessage("Apakah Anda ingin menutup Toko sekarang?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                progressDialog = new ProgressDialog(OpenShop.this);
                                progressDialog.setMessage("Harap tunggu...");
                                progressDialog.setCanceledOnTouchOutside(false);
                                progressDialog.show();
                                UserController.closedShop(OpenShop.this);
                            }
                        }).create().show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        CloseAppsDialog.showDialog(OpenShop.this);
    }
}
