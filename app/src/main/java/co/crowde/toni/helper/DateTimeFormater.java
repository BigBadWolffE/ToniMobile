package co.crowde.toni.helper;

import android.app.Activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeFormater {
    public static String currentDateOpen, currentDateClosed;

    public static void getCurrentDateOpen() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm | EEE, dd MMMM yyyy", Locale.UK);
        Date currentTime = new Date();
        currentDateOpen = dateFormat.format(currentTime);
    }

    public static void getCurrentDateClosed(Activity activity) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm | EEE, dd MMMM yyyy", Locale.UK);
        Date currentTime = new Date();
        currentDateClosed = dateFormat.format(currentTime);
        SavePref.saveClosedTime(activity, currentDateClosed);
    }

}
