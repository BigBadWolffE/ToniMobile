package co.crowde.toni.utils.print;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import co.crowde.toni.R;
import co.crowde.toni.helper.SavePref;

public class PopUpPaperSize {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static CardView btnKecil,btnBesar,btnSedang;
    public static TextView headerPaper;
    public static AlertDialog dialog;

    public static void showDialog(final Activity activity){

        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(activity).
                inflate(R.layout.popup_paper_size,viewGroup,
                        false);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        dialog = builder.create();

        btnKecil = dialogView.findViewById(R.id.inputCustom2);
        btnBesar = dialogView.findViewById(R.id.inputCustom4);

        btnKecil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String paperSize = "small";
                SavePref.savePaperSize(activity,paperSize);
                Toast.makeText(activity, SavePref.readPaperSize(activity),Toast.LENGTH_LONG).show();
                dialog.dismiss();

            }
        });
        btnBesar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String paperSize = "large";
                SavePref.savePaperSize(activity,paperSize);
                Toast.makeText(activity, SavePref.readPaperSize(activity),Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


    }

}
