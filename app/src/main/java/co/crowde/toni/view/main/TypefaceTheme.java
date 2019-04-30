package co.crowde.toni.view.main;

import android.app.Activity;
import android.widget.TextView;

public class TypefaceTheme {

    public static android.graphics.Typeface montserratReg,
            montserratSemiBold, montserratBold,
            openSansReg, openSansSemiBold, openSansBold;

    public static void fontMontserratReg(Activity activity){
        montserratReg = android.graphics.Typeface.createFromAsset(
                activity.getAssets(), "fonts/Montserrat-Regular.ttf");
        montserratSemiBold = android.graphics.Typeface.createFromAsset(
                activity.getAssets(), "fonts/Montserrat-SemiBold.ttf");
        montserratBold = android.graphics.Typeface.createFromAsset(
                activity.getAssets(), "fonts/Montserrat-Bold.ttf");
        openSansReg = android.graphics.Typeface.createFromAsset(
                activity.getAssets(), "fonts/OpenSans-Regular.ttf");
        openSansSemiBold = android.graphics.Typeface.createFromAsset(
                activity.getAssets(), "fonts/OpenSans-SemiBold.ttf");
        openSansBold = android.graphics.Typeface.createFromAsset(
                activity.getAssets(), "fonts/OpenSans-Bold.ttf");
    }
}
