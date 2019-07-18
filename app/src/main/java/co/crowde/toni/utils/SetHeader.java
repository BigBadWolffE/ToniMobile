package co.crowde.toni.utils;

import android.app.Activity;
import android.os.Build;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.appbar.AppBarLayout;

public class SetHeader {

    public static int actionBarSize(Activity activity){
        float actionBarsize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, activity.getResources().getDisplayMetrics());
        return (int) actionBarsize;
    }

    public static int statusBarSize(Activity activity){
        float statusBarsize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, activity.getResources().getDisplayMetrics());
        return (int) statusBarsize;
    }

    public static boolean isLolipop(Activity activity, AppBarLayout layout){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            layout.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, actionBarSize(activity)));
            layout.setPadding(0,statusBarSize(activity),0,0);

            return true;
        }

        return false;
    }

    public static int actionBarSizeAccount(Activity activity){
        float actionBarsize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 205, activity.getResources().getDisplayMetrics());
        return (int) actionBarsize;
    }

//    public static boolean isLolipopAccount(Activity activity, ConstraintLayout layout, TextView tvAccount){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            layout.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, actionBarSizeAccount(activity)));
//            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
//                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
//                    ConstraintLayout.LayoutParams.WRAP_CONTENT
//            );
//            params.setMargins(0, actionBarSize(activity), 0, 0);
//            tvAccount.setLayoutParams(params);
//
//            return true;
//        }
//
//        return false;
//    }


}
