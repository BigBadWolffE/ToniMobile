package co.crowde.toni.helper;

import android.app.Activity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class DecimalFormatRupiah {

    public static DecimalFormat formatNumber;

    public static void changeFormat(Activity activity){

        formatNumber = new DecimalFormat("###,###,###,###,###,###,###");
    }
}
