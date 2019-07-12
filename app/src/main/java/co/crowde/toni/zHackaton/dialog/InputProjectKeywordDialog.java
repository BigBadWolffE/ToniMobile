package co.crowde.toni.zHackaton.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import java.text.DecimalFormat;

import co.crowde.toni.R;
import co.crowde.toni.view.fragment.modul.DashboardFragment;
import co.crowde.toni.zHackaton.activity.ProjectInformationActivity;

public class InputProjectKeywordDialog {

    public static AlertDialog dialogProject;
    public static CardView btnProject;
    public static TextView tvSeeRAB;
    public static ImageView imgCreditClose;

    @SuppressLint("SetTextI18n")
    public static void showCredit(final Activity activity) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);


        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(activity)
                .inflate(R.layout.layout_input_code_dialog,
                        viewGroup,
                        false);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        dialogProject = builder.create();

        //Get View Id
        tvSeeRAB = dialogView.findViewById(R.id.tv_see_rab);
        imgCreditClose = dialogView.findViewById(R.id.imgCreditClose);

        imgCreditClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogProject.dismiss();
            }
        });

        tvSeeRAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent show = new Intent(activity, ProjectInformationActivity.class);
                activity.startActivity(show);
                dialogProject.dismiss();
            }
        });

        dialogProject.show();
        dialogProject.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }
}
