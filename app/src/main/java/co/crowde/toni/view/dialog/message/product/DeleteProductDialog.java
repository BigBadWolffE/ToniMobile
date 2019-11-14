package co.crowde.toni.view.dialog.message.product;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import co.crowde.toni.R;

public class DeleteProductDialog {

    public static TextView tvHead, tvDesc, tvYes, tvNo;

    public static ImageView imgLogo;

    public static AlertDialog dialogConfirm;

    public static ProgressDialog progressDialog;

    public static void showDialog(final Activity activity,
//                                  String productId, int qty, int purchase, int selling,
                                  ProgressDialog progressDialogs){
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(activity)
                .inflate(R.layout.custom_dialog_confirm_two_button,
                        viewGroup,
                        false);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        dialogConfirm = builder.create();

        //Get View Id
        tvHead = dialogView.findViewById(R.id.tv_dialog_label);
        tvDesc = dialogView.findViewById(R.id.tv_dialog_desc);
        tvYes = dialogView.findViewById(R.id.tv_yes);
        tvNo = dialogView.findViewById(R.id.tv_no);

        tvHead.setText("Apakah Anda Yakin Ingin Menghapus Produk Ini?");
        tvYes.setText("YA");
        tvNo.setText("TIDAK");

        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvYes.setTextColor(activity.getResources().getColor(R.color.colorThemeOrange));
                PasswordConfirmationDeleteDialog.showDialog(activity,progressDialog);
                dialogConfirm.dismiss();
                progressDialogs.dismiss();

            }
        });

        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvNo.setTextColor(activity.getResources().getColor(R.color.colorThemeOrange));
                dialogConfirm.dismiss();
                progressDialogs.dismiss();
            }
        });

        dialogConfirm.show();
        dialogConfirm.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


    }

}
