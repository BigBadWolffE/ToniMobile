package co.crowde.toni.view.dialog.message.customer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import co.crowde.toni.R;

public class CustomerAlreadyRegisterDialog {

    public static TextView tvHead, tvDesc, tvClose;

    public static ImageView imgLogo;

    public static AlertDialog dialogCustomer;

    public static ProgressDialog progressDialog;

    public static void showDialog(final Activity activity) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(activity)
                .inflate(R.layout.layout_custom_dialog_one_action,
                        viewGroup,
                        false);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        dialogCustomer = builder.create();

        //Get View Id
        imgLogo = dialogView.findViewById(R.id.img_logo);
        tvHead = dialogView.findViewById(R.id.tvHead);
        tvDesc = dialogView.findViewById(R.id.tvDesc);
        tvClose = dialogView.findViewById(R.id.tvClose);

        imgLogo.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_person_add_white_24dp));
        tvHead.setText(activity.getResources().getString(R.string.dialog_label_add_new_customer));
        tvDesc.setText(activity.getResources().getString(R.string.dialog_desc_add_new_customer));

        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCustomer.dismiss();
            }
        });

        dialogCustomer.show();
        dialogCustomer.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }
}
