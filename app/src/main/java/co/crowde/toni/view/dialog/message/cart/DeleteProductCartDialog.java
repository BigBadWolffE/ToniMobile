package co.crowde.toni.view.dialog.message.cart;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import co.crowde.toni.R;

public class DeleteProductCartDialog {
    public static TextView tvHead, tvDesc, tvYes, tvNo;

    public static AlertDialog dialogClose;

    public static ProgressDialog progressDialog;

    public static void showDialog(final Activity activity) {
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
        dialogClose = builder.create();

        //Get View Id
        tvHead = dialogView.findViewById(R.id.tv_dialog_label);
        tvDesc = dialogView.findViewById(R.id.tv_dialog_desc);
        tvYes = dialogView.findViewById(R.id.tv_yes);
        tvNo = dialogView.findViewById(R.id.tv_no);

        tvHead.setText(activity.getResources().getString(R.string.dialog_label_delete_cart));
        tvDesc.setText(activity.getResources().getString(R.string.dialog_desc_delete_cart));
        tvYes.setText(activity.getResources().getString(R.string.hapus_cap));
        tvNo.setText(activity.getResources().getString(R.string.batal_cap));

        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });

        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogClose.dismiss();
            }
        });


        dialogClose.show();
        dialogClose.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }


}
