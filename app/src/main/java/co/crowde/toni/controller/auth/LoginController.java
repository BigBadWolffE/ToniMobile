package co.crowde.toni.controller.auth;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import co.crowde.toni.view.activity.auth.ForgotPass;
import co.crowde.toni.view.activity.auth.ForgotUser;

public class LoginController {

    public static void loginResponse(Activity activity, String message){
        switch (message) {
            case "Password anda salah!":
                Intent wrongPass = new Intent(activity, ForgotPass.class);
                activity.startActivity(wrongPass);
                activity.finish();
                break;
            case "Username anda salah!":
                Intent wrongUser = new Intent(activity, ForgotUser.class);
                activity.startActivity(wrongUser);
                activity.finish();
                break;
            default:
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
