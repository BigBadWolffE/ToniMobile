package co.crowde.toni.view.dialog.message.transaction;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import co.crowde.toni.R;
import co.crowde.toni.database.Cart;
import co.crowde.toni.helper.DecimalFormatRupiah;
import co.crowde.toni.model.CartModel;
import co.crowde.toni.model.body.post.AddTransactionModel;
import co.crowde.toni.model.response.list.TransactionModel;
import co.crowde.toni.model.response.object.AddNewTransactionModel;
import co.crowde.toni.network.ProductRequest;
import co.crowde.toni.network.TransactionRequest;
import co.crowde.toni.view.activity.cart.CartListActivity;
import co.crowde.toni.view.fragment.modul.DashboardFragment;

public class ConfirmPaymentDialog {
    public static TextView tv_dialog_label, tv_dialog_desc, tv_yes, tv_no;

    public static AlertDialog dialogConfirm;

    public static ProgressDialog progressDialog;

    public static void showDialog(final Activity activity, AddTransactionModel add, int saldo, int credit) {
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
        tv_dialog_label = dialogView.findViewById(R.id.tv_dialog_label);
        tv_dialog_desc = dialogView.findViewById(R.id.tv_dialog_desc);
        tv_yes = dialogView.findViewById(R.id.tv_yes);
        tv_no = dialogView.findViewById(R.id.tv_no);

        tv_dialog_label.setText(activity.getResources().getString(R.string.konfirmasi_transaksi));
        tv_dialog_desc.setText(activity.getResources().getString(R.string.confirm_payment_desc));
        tv_yes.setText("Bayar");
        tv_no.setText("Kembali");

        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(activity);
                progressDialog.setMessage("Harap tunggu...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                tv_yes.setEnabled(false);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        activity.finish();
                        tv_yes.setEnabled(true);
                        TransactionRequest.postNewTransaction(activity, add, saldo, credit);
//                        TransactionController.printBill(activity);
                    }
                }, 100);
            }
        });

        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogConfirm.dismiss();
            }
        });


        dialogConfirm.show();
        dialogConfirm.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }
}
