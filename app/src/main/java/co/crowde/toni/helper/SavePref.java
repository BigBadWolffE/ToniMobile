package co.crowde.toni.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import co.crowde.toni.controller.network.API;

public class SavePref {

    //Token
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

    //ShopId
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

    //Picture
    public static void savePicture(Activity activity, String picture) {
        SharedPreferences sharedPref = activity.getSharedPreferences(API.TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("picture", picture);
        editor.commit();
    }
    public static String readPicture(Activity activity) {
        SharedPreferences sharedPref = activity.getSharedPreferences(API.TAG, Context.MODE_PRIVATE);
        return sharedPref.getString("picture", null);
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

    //OpenTime
    public static void saveOpenTime(Activity activity, String openTime) {
        SharedPreferences sharedPref = activity.getSharedPreferences(API.TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("openTime", openTime);
        editor.commit();
    }
    public static String readOpenTime(Activity activity) {
        SharedPreferences sharedPref = activity.getSharedPreferences(API.TAG, Context.MODE_PRIVATE);
        return sharedPref.getString("openTime", null);
    }

    //isOpenShop
    public static void saveOpenShop(Activity activity, int isOpen) {
        SharedPreferences sharedPref = activity.getSharedPreferences(API.TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("isOpen", isOpen);
        editor.commit();
    }
    public static int readOpenShop(Activity activity) {
        SharedPreferences sharedPref = activity.getSharedPreferences(API.TAG, Context.MODE_PRIVATE);
        return sharedPref.getInt("isOpen", 0);
    }

    //Save ID Pelanggan
    public static void saveCustomerId(Activity activity, String customerId) {
        SharedPreferences sharedPref = activity.getSharedPreferences(API.TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("customerId", customerId);
        editor.commit();
    }
    public static String readCustomerId(Activity activity) {
        SharedPreferences sharedPref = activity.getSharedPreferences(API.TAG, Context.MODE_PRIVATE);
        return sharedPref.getString("customerId", null);
    }

    //Save Pelanggan
    public static void saveCustomer(Activity activity, String customer) {
        SharedPreferences sharedPref = activity.getSharedPreferences(API.TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("customer", customer);
        editor.commit();
    }
    public static String readCustomer(Activity activity) {
        SharedPreferences sharedPref = activity.getSharedPreferences(API.TAG, Context.MODE_PRIVATE);
        return sharedPref.getString("customer", null);
    }
}
