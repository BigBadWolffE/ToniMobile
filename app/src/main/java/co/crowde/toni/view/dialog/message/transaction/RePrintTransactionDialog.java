package co.crowde.toni.view.dialog.message.transaction;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;

import co.crowde.toni.R;
import co.crowde.toni.controller.transaction.TransactionController;

import static co.crowde.toni.view.activity.transaction.DetailTransactionActivity.model;
import static co.crowde.toni.view.activity.transaction.DetailTransactionActivity.productModelList;

public class RePrintTransactionDialog {
    public static TextView tvHead, tvDesc, tvYes, tvNo;

    public static ImageView imgLogo;

    public static AlertDialog dialogConfirm;

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
        dialogConfirm = builder.create();

        //Get View Id
        tvHead = dialogView.findViewById(R.id.tv_dialog_label);
        tvDesc = dialogView.findViewById(R.id.tv_dialog_desc);
        tvYes = dialogView.findViewById(R.id.tv_yes);
        tvNo = dialogView.findViewById(R.id.tv_no);

        tvHead.setText(activity.getResources().getString(R.string.dialog_label_re_print_transaction));
        tvDesc.setText(activity.getResources().getString(R.string.dialog_desc_re_print_transaction));
        tvYes.setText(activity.getResources().getString(R.string.cetak_cap));
        tvNo.setText(activity.getResources().getString(R.string.batal_cap));

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
                        tvYes.setEnabled(true);
                        TransactionController.printDetailTransaction(activity, model, productModelList);
                    }
                }, 100);
            }
        });

        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvNo.setTextColor(activity.getResources().getColor(R.color.colorThemeOrange));
                dialogConfirm.dismiss();
            }
        });


        dialogConfirm.show();
        dialogConfirm.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }
}
