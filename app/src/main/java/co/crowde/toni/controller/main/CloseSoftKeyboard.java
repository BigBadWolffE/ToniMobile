package co.crowde.toni.controller.main;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class CloseSoftKeyboard {

    public static void hideSoftKeyboard(View view, Activity activity) {
        InputMethodManager inputMethodManager =(InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
