package co.crowde.toni.view.dialog.catalog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import co.crowde.toni.R;
import co.crowde.toni.network.CatalogRequest;
import co.crowde.toni.network.ProductRequest;

public class MessageAddNewCatalog {

    public static TextView tvHead, tvDesc, tvYes, tvNo;

    public static AlertDialog dialogUpdate;

    public static ProgressDialog progressDialog;

    public static void showDialog(final Activity activity) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(activity)
                .inflate(R.layout.custom_dialog_add_new_catalog,
                        viewGroup,
                        false);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        dialogUpdate = builder.create();

        //Get View Id
        tvHead = dialogView.findViewById(R.id.tvHead);
        tvDesc = dialogView.findViewById(R.id.tvDesc);
        tvYes = dialogView.findViewById(R.id.tvYes);
        tvNo = dialogView.findViewById(R.id.tvNo);

        tvHead.setText(activity.getResources().getString(R.string.dialog_label_add_new_product));
        tvDesc.setText(activity.getResources().getString(R.string.dialog_desc_add_new_product));

        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvYes.setTextColor(activity.getResources().getColor(R.color.colorThemeOrange));
                progressDialog = new ProgressDialog(activity);
                progressDialog.setMessage("Harap tunggu...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                CatalogRequest.addNewProduct(activity);
            }
        });

        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvNo.setTextColor(activity.getResources().getColor(R.color.colorThemeOrange));
                dialogUpdate.dismiss();
            }
        });


        dialogUpdate.show();
        dialogUpdate.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }
}
