package co.crowde.toni.helper;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class CallCenter {

    public static void showDial(Activity activity){
        Intent callIntent = new Intent(Intent.ACTION_DIAL,
                Uri.fromParts("tel", "021 8765432", null));
        activity.startActivity(callIntent);
    }

    public static void showDialCustomer(Activity activity, String phoneNumber){
        Intent callIntent = new Intent(Intent.ACTION_DIAL,
                Uri.fromParts("tel", phoneNumber, null));
        activity.startActivity(callIntent);
    }
}
