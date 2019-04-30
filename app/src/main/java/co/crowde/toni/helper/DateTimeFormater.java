package co.crowde.toni.helper;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeFormater {
    public static String currentDateOpen, currentDateClosed;
    public static Locale lokal = new Locale("id");

    public static void getCurrentDateOpen(Activity activity) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm | EEEE, dd MMMM yyyy", lokal);
        Date currentTime = new Date();
        currentDateOpen = dateFormat.format(currentTime);
        SavePref.saveOpenTime(activity, currentDateOpen);
    }

    public static void getCurrentDateClosed(Activity activity) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm | EEEE, dd MMMM yyyy", lokal);
        Date currentTime = new Date();
        currentDateClosed = dateFormat.format(currentTime);
        SavePref.saveClosedTime(activity, currentDateClosed);
    }


}
