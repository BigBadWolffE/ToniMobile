package co.crowde.toni.view.dialog.message.customer;

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
import co.crowde.toni.constant.Const;
import co.crowde.toni.network.CustomerRequest;
import co.crowde.toni.utils.analytics.AnalyticsToniUtils;

public class CreditPayDialog {
    public static TextView tvHead, tvDesc, tvYes, tvNo;

    public static ImageView imgLogo;

    public static AlertDialog dialogCredit;

    public static ProgressDialog progressDialog;

    public static void showDialog(final Activity activity, int credit, String customerId) {
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
        dialogCredit = builder.create();

        //Get View Id

        tvHead = dialogView.findViewById(R.id.tv_dialog_label);
        tvDesc = dialogView.findViewById(R.id.tv_dialog_desc);
        tvYes = dialogView.findViewById(R.id.tv_yes);
        tvNo = dialogView.findViewById(R.id.tv_no);

        tvHead.setText(activity.getResources().getString(R.string.dialog_label_credit_pay));
        tvDesc.setText(activity.getResources().getString(R.string.dialog_desc_credit_pay));
        tvYes.setText(activity.getResources().getString(R.string.bayar));
        tvNo.setText(activity.getResources().getString(R.string.batal_cap));

        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(activity);
                progressDialog.setMessage("Harap tunggu...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                tvYes.setEnabled(false);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tvYes.setEnabled(true);
                        CustomerRequest.payCustomerCredit(activity, credit, customerId);
//                        CustomerController.printCreditPay(activity, credit, customerId);

                        AnalyticsToniUtils.getEvent(Const.CATEGORY_CUSTOMER,Const.MODUL_CUSTOMER,Const.LABEL_CUSTOMER_CREDIT_PAY);
                    }
                }, 100);



            }
        });

        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCredit.dismiss();
            }
        });


        dialogCredit.show();
        dialogCredit.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }


}
