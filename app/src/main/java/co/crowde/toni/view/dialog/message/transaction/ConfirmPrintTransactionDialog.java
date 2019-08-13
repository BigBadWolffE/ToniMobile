package co.crowde.toni.view.dialog.message.transaction;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import co.crowde.toni.R;
import co.crowde.toni.controller.transaction.TransactionController;
import co.crowde.toni.network.TransactionRequest;

public class ConfirmPrintTransactionDialog {
    public static TextView tvHead, tvDesc, tvYes, tvNo;

    public static ImageView imgLogo;

    public static AlertDialog dialogConfirm;

    public static ProgressDialog progressDialog;

    public static void showDialog(final Activity activity, String data) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(activity)
                .inflate(R.layout.layout_custom_dialog_two_action,
                        viewGroup,
                        false);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        dialogConfirm = builder.create();

        //Get View Id
        imgLogo = dialogView.findViewById(R.id.img_logo);
        tvHead = dialogView.findViewById(R.id.tvHead);
        tvDesc = dialogView.findViewById(R.id.tvDesc);
        tvYes = dialogView.findViewById(R.id.tvYes);
        tvNo = dialogView.findViewById(R.id.tvNo);

        imgLogo.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_print_white_24dp));
        tvHead.setText(activity.getResources().getString(R.string.dialog_label_print_transaction));
        tvDesc.setText(activity.getResources().getString(R.string.dialog_desc_print_transaction));

        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvYes.setTextColor(activity.getResources().getColor(R.color.colorThemeOrange));
                progressDialog = new ProgressDialog(activity);
                progressDialog.setMessage("Harap tunggu...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                tvYes.setEnabled(false);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        activity.finish();
                        tvYes.setEnabled(true);
//                        TransactionRequest.postNewTransaction(activity);
//                        TransactionController.printBill(activity, data, progressDialog);
                    }
                }, 100);
            }
        });

        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvNo.setTextColor(activity.getResources().getColor(R.color.colorThemeOrange));
                TransactionController.removeHistory(activity);
//                progressDialog.dismiss();
                dialogConfirm.dismiss();
            }
        });


        dialogConfirm.show();
        dialogConfirm.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }
}
