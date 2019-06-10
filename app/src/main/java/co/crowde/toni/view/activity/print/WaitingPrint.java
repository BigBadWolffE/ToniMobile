package co.crowde.toni.view.activity.print;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.FrameLayout;

import co.crowde.toni.R;
import co.crowde.toni.controller.transaction.TransactionController;
import co.crowde.toni.view.activity.home.MainActivity;
import co.crowde.toni.view.fragment.print.PrintDone;
import co.crowde.toni.view.fragment.print.PrintWait;

public class WaitingPrint extends AppCompatActivity {

    FrameLayout layoutPrint;
    CardView cvBtnHome;
    boolean print;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_print);

        print = false;

        layoutPrint = findViewById(R.id.layoutPrint);
        cvBtnHome = findViewById(R.id.cvBtnHome);

        TransactionController.removeHistory(WaitingPrint.this);
        enabledHome();
        changeFragment();

        cvBtnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                print = true;
                changeFragment();
                enabledHome();
            }
        }, 5000);

    }

    public void enabledHome(){
        if(print){
            cvBtnHome.setCardBackgroundColor(getResources().getColor(R.color.colorThemeOrange));
            cvBtnHome.setEnabled(true);
        } else {
            cvBtnHome.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGrey));
            cvBtnHome.setEnabled(true);
        }
    }

    public void changeFragment(){
        if(print){
            PrintDone done = new PrintDone();
            FragmentTransaction doneTransaction = getSupportFragmentManager()
                    .beginTransaction();
//            waitTransaction.setCustomAnimations(
//                    android.R.anim.fade_in, android.R.anim.fade_out);
            doneTransaction.replace(R.id.layoutPrint, done);
            doneTransaction.commit();

        } else{
            PrintWait wait = new PrintWait();
            FragmentTransaction waitTransaction = getSupportFragmentManager()
                    .beginTransaction();
//            waitTransaction.setCustomAnimations(
//                    android.R.anim.fade_in, android.R.anim.fade_out);
            waitTransaction.replace(R.id.layoutPrint, wait);
            waitTransaction.commit();

        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
