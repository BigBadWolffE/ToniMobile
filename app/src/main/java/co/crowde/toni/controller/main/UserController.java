package co.crowde.toni.controller.main;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import co.crowde.toni.controller.network.UserRequest;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.ShopModel;
import co.crowde.toni.view.login.Login;
import co.crowde.toni.view.login.LoginSuccess;

public class UserController {
    public static String currentDateOpen, currentDateClosed;


    public static void showLoginOwner(Activity activity){
        UserRequest.getShopDetail(activity);

        if(SavePref.readUserDetail(activity)!=null){
            String user = SavePref.readUserDetail(activity);

            Log.e("USER", user);

            ShopModel shopModel = new Gson().fromJson(user, ShopModel.class);

            LoginSuccess.tvOwnerName.setText(
                    shopModel.getOwnerName()+" - "+shopModel.getShopName());
        } else {
            Toast.makeText(activity, SavePref.readUserDetail(activity), Toast.LENGTH_SHORT).show();
        }

    }

    public static void getDataLogin(Activity activity){
        if(SavePref.readToken(activity)==null){
            Intent login = new Intent(activity, Login.class);
            activity.startActivity(login);
            activity.finish();
        } else {
            Intent loginSuccess = new Intent(activity, LoginSuccess.class);
            activity.startActivity(loginSuccess);
            activity.finish();
        }

    }

    public static void getCurrentDateOpen() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm | EEE, dd MMMM yyyy", Locale.UK);
        Date currentTime = new Date();
        currentDateOpen = dateFormat.format(currentTime);
    }

    public static void getCurrentDateClosed(Activity activity) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm | EEE, dd MMMM yyyy", Locale.UK);
        Date currentTime = new Date();
        currentDateClosed = dateFormat.format(currentTime);
        SavePref.saveClosedTime(activity, currentDateClosed);
    }

    public static void closedShop(Activity activity){
        SavePref.saveShopId(activity, null);
        SavePref.saveToken(activity, null);
        SavePref.saveUserDetail(activity, null);

        getCurrentDateClosed(activity);

        Toast.makeText(activity, "Toko Sudah ditutup.", Toast.LENGTH_SHORT).show();

        Intent logout = new Intent(activity, Login.class);
        activity.startActivity(logout);
        activity.finish();
    }
}
