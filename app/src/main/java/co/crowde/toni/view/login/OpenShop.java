package co.crowde.toni.view.login;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import co.crowde.toni.R;
import co.crowde.toni.controller.main.UserController;
import co.crowde.toni.controller.network.ShopRequest;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.view.main.MainActivity;

public class OpenShop extends AppCompatActivity {
    public static TextView tvOpenShopHeader,
            tvOpenShopLabel, tvOpenShopTime,
            tvBtnOpenLabel,tvCloseShop;
    public static CardView cvBtnOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_shop);

        //Get Id View
        tvOpenShopHeader = findViewById(R.id.tvOpenShopHeader);
        tvOpenShopLabel = findViewById(R.id.tvOpenShopLabel);
        tvOpenShopTime = findViewById(R.id.tvOpenShopTime);
        tvBtnOpenLabel = findViewById(R.id.tvBtnOpenLabel);
        tvCloseShop = findViewById(R.id.tvCloseShop);
        cvBtnOpen = findViewById(R.id.cvBtnOpen);

        setShopOpenTime(OpenShop.this);

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
                                UserController.closedShop(OpenShop.this);
                            }
                        }).create().show();
            }
        });

    }

    public static void setShopOpenTime(Activity activity){
        if(SavePref.readClosedTime(activity)==null){
            tvOpenShopLabel.setText(activity.getResources().getString(R.string.strips));
        } else {
            tvOpenShopTime.setText(SavePref.readOpenTime(activity));
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
                        OpenShop.super.onBackPressed();
                    }
                }).create().show();
    }
}
