package co.crowde.toni.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import co.crowde.toni.controller.network.API;

public class SavePref {

    //UserDetail
    public static void saveToken(Activity activity, String token) {
        SharedPreferences sharedPref = activity.getSharedPreferences(API.TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("token", token);
        editor.commit();
    }
    public static String readToken(Activity activity) {
        SharedPreferences sharedPref = activity.getSharedPreferences(API.TAG, Context.MODE_PRIVATE);
        return sharedPref.getString("token", null);
    }

    //UserDetail
    public static void saveShopId(Activity activity, String shopId) {
        SharedPreferences sharedPref = activity.getSharedPreferences(API.TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("shopId", shopId);
        editor.commit();
    }
    public static String readShopId(Activity activity) {
        SharedPreferences sharedPref = activity.getSharedPreferences(API.TAG, Context.MODE_PRIVATE);
        return sharedPref.getString("shopId", null);
    }

    //UserDetail
    public static void saveUserDetail(Activity activity, String userDetail) {
        SharedPreferences sharedPref = activity.getSharedPreferences(API.TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("user_detail", userDetail);
        editor.commit();
    }
    public static String readUserDetail(Activity activity) {
        SharedPreferences sharedPref = activity.getSharedPreferences(API.TAG, Context.MODE_PRIVATE);
        return sharedPref.getString("user_detail", null);
    }

    //ClosedTime
    public static void saveClosedTime(Activity activity, String closedTime) {
        SharedPreferences sharedPref = activity.getSharedPreferences(API.TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("closedTime", closedTime);
        editor.commit();
    }
    public static String readClosedTime(Activity activity) {
        SharedPreferences sharedPref = activity.getSharedPreferences(API.TAG, Context.MODE_PRIVATE);
        return sharedPref.getString("closedTime", null);
    }
}
