package co.crowde.toni.base;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import co.crowde.toni.view.activity.auth.LoginActivity;

public class BaseActivity extends AppCompatActivity {

    protected ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(this);
    }

    protected void showDialog(){
        progressDialog.setMessage("Harap tunggu...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }


    protected void dismissDialog(){
        progressDialog.dismiss();
    }
}
