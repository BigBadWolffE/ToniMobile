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

public class StockInsufficientDialog {

    public static TextView tvHead, tvDesc, tvClose;

    public static ImageView imgLogo;

    public static AlertDialog dialogEmpty;

    public static ProgressDialog progressDialog;

    public static void showDialog(final Activity activity) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(activity)
                .inflate(R.layout.custom_dialog_confirm_one_button,
                        viewGroup,
                        false);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        dialogEmpty = builder.create();

        //Get View Id
        tvHead = dialogView.findViewById(R.id.tv_dialog_label);
        tvDesc = dialogView.findViewById(R.id.tv_dialog_desc);
        tvClose = dialogView.findViewById(R.id.tv_close);

        tvHead.setText(activity.getResources().getString(R.string.dialog_label_insufficient_stock));
        tvDesc.setText(activity.getResources().getString(R.string.dialog_desc_insufficient_stock));
        tvClose.setText(activity.getResources().getString(R.string.tutup));

        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEmpty.dismiss();
            }
        });

        dialogEmpty.show();
        dialogEmpty.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }

}
