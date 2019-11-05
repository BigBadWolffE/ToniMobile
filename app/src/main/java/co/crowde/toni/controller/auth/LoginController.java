package co.crowde.toni.controller.auth;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import co.crowde.toni.view.activity.auth.ForgotPassActivity;
import co.crowde.toni.view.activity.auth.ForgotUserActivity;

public class LoginController {

    public static void loginResponse(Activity activity, String message){
        switch (message) {
            case "Password anda salah!":
                Intent wrongPass = new Intent(activity, ForgotPassActivity.class);
                activity.startActivity(wrongPass);
                break;
            case "Username anda salah!":
                Intent wrongUser = new Intent(activity, ForgotUserActivity.class);
                activity.startActivity(wrongUser);
                break;
            default:
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public static void OtpResponse(Activity activity, String message){
        switch (message) {
            case "Nomor Telpon atau Data Anda Sudah Terdaftar!":
                Intent wrongPass = new Intent(activity, ForgotPassActivity.class);
                activity.startActivity(wrongPass);
                break;
            case "OTP Yang Anda Masukan Salah!":
                Intent wrongUser = new Intent(activity, ForgotUserActivity.class);
                activity.startActivity(wrongUser);
                break;
            default:
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
