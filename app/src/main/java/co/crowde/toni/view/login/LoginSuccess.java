package co.crowde.toni.view.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import co.crowde.toni.R;
import co.crowde.toni.controller.main.UserController;
import co.crowde.toni.controller.network.UserRequest;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.ShopModel;
import co.crowde.toni.model.UserModel;
import co.crowde.toni.view.main.TypefaceTheme;

public class LoginSuccess extends AppCompatActivity {

    public static TextView tvLogSuccessHeader, tvOpenHeader, tvOpenTime,
            tvLoginAs, tvOwnerName,
            tvBtnOpenlabel, tvCloseShop;
    public static CardView cvBtnOpen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);

        //Get View Id
        tvLogSuccessHeader = findViewById(R.id.tvLogSuccessHeader);
        tvOpenHeader = findViewById(R.id.tvOpenHeader);
        tvOpenTime = findViewById(R.id.tvOpenTime);

        tvLoginAs = findViewById(R.id.tvLoginAs);
        tvOwnerName = findViewById(R.id.tvOwnerName);

        tvBtnOpenlabel = findViewById(R.id.tvBtnOpenLabel);
        tvCloseShop = findViewById(R.id.tvCloseShop);

        cvBtnOpen = findViewById(R.id.cvBtnOpen);

        //Change Font Type
        TypefaceTheme.fontMontserratReg(LoginSuccess.this);
        tvLogSuccessHeader.setTypeface(TypefaceTheme.montserratBold);
        tvOpenHeader.setTypeface(TypefaceTheme.montserratReg);
        tvOpenTime.setTypeface(TypefaceTheme.montserratReg);

        tvLoginAs.setTypeface(TypefaceTheme.montserratReg);
        tvOwnerName.setTypeface(TypefaceTheme.montserratReg);

        tvBtnOpenlabel.setTypeface(TypefaceTheme.montserratBold);
        tvCloseShop.setTypeface(TypefaceTheme.montserratReg);

        //Set TextSize
        tvLogSuccessHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);

        UserRequest.getShopDetail(LoginSuccess.this);

        tvCloseShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserController.closedShop(LoginSuccess.this);

            }
        });

    }

    public static void setOwnerName(Activity activity, String user){
        UserController.getCurrentDateOpen();

        //Parsing to model
        ShopModel shopModel = new Gson().fromJson(user, ShopModel.class);

        //Set to View

        tvOpenTime.setText(UserController.currentDateOpen);
        tvOwnerName.setText(
                shopModel.getOwnerName()+" - "+shopModel.getShopName());

    }

}
