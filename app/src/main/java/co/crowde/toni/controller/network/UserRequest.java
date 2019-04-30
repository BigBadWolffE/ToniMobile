package co.crowde.toni.controller.network;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import co.crowde.toni.controller.main.UserController;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.UserModel;
import co.crowde.toni.view.login.ForgetPassword;
import co.crowde.toni.view.login.ForgetUsername;
import co.crowde.toni.view.login.Login;
import co.crowde.toni.view.login.LoginSuccess;

public class UserRequest {

}
