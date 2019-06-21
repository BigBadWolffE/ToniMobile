package co.crowde.toni.controller.user;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import co.crowde.toni.network.ShopRequest;
import co.crowde.toni.helper.DateTimeFormater;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.ShopModel;
import co.crowde.toni.view.activity.auth.LoginActivity;
import co.crowde.toni.view.activity.auth.LoginSuccessActivity;
import co.crowde.toni.view.activity.shop.OpenShopActivity;

public class UserController {

    public static void showLoginOwner(Activity activity){
        ShopRequest.getShopDetail(activity);

        if(SavePref.readUserDetail(activity)!=null){
            String user = SavePref.readUserDetail(activity);

            Log.e("USER", user);

            ShopModel shopModel = new Gson().fromJson(user, ShopModel.class);

            LoginSuccessActivity.tvOwnerName.setText(
                    shopModel.getOwnerName()+" - "+shopModel.getShopName());
        } else {
            Toast.makeText(activity, SavePref.readUserDetail(activity), Toast.LENGTH_SHORT).show();
        }

    }

    public static void getDataLogin(Activity activity){

        if(SavePref.readOpenShop(activity)==1){
            Intent open = new Intent(activity, OpenShopActivity.class);
            activity.startActivity(open);
            activity.finish();

        } else if(SavePref.readToken(activity)==null){
            Intent login = new Intent(activity, LoginActivity.class);
            activity.startActivity(login);
            activity.finish();

        }  else{
            Intent loginSuccess = new Intent(activity, LoginSuccessActivity.class);
            activity.startActivity(loginSuccess);
            activity.finish();

        }

    }

    public static void tokenExpired(Activity activity, String message){
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();

        SavePref.saveShopId(activity, null);
        SavePref.saveToken(activity, null);
        SavePref.saveUserDetail(activity, null);
        SavePref.saveOpenTime(activity, null);
        SavePref.saveOpenShop(activity, 0);

        DateTimeFormater.getCurrentDateClosed(activity);

        Toast.makeText(activity, "Toko Sudah ditutup.", Toast.LENGTH_SHORT).show();

        Intent logout = new Intent(activity, LoginActivity.class);
        activity.startActivity(logout);
        activity.finish();
    }

    public static void closedShop(Activity activity){
        ShopRequest.closedShop(activity);
        SavePref.saveShopId(activity, null);
        SavePref.saveToken(activity, null);
        SavePref.saveUserDetail(activity, null);
        SavePref.saveOpenTime(activity, null);
        SavePref.saveOpenShop(activity, 0);

        DateTimeFormater.getCurrentDateClosed(activity);

        Intent logout = new Intent(activity, LoginActivity.class);
        activity.startActivity(logout);
        activity.finish();

        Toast.makeText(activity, "Toko Sudah ditutup.", Toast.LENGTH_SHORT).show();
    }
}
