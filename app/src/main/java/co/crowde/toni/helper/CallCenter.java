package co.crowde.toni.helper;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class CallCenter {

    public static void showDial(Activity activity){
        Intent callIntent = new Intent(Intent.ACTION_DIAL,
                Uri.fromParts("tel", "082213161296", null));
        activity.startActivity(callIntent);
    }
}
