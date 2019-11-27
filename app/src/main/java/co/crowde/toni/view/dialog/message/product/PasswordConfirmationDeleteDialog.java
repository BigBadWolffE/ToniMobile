package co.crowde.toni.view.dialog.message.product;

import android.app.Activity;
import android.app.ProgressDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import co.crowde.toni.R;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.network.ProductRequest;

public class PasswordConfirmationDeleteDialog {
    public static EditText etPasswordConfirmation;
    public static ImageView close;
    public static CardView cvConfirmDeletion;

    public static AlertDialog dialogDelete;

    public static ProgressDialog progressDialog;
    public static TextWatcher passwordWatcher;

    public static void showDialog(final Activity activity,ProgressDialog progressDialogs){
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(activity)
                .inflate(R.layout.dialog_password_confirmation,
                        viewGroup,
                        false);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        dialogDelete= builder.create();

        //Get View Id
        passwordWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                cvConfirmDeletion.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etPasswordConfirmation.getText().toString().length() != 0){
                    cvConfirmDeletion.setEnabled(true);
                    cvConfirmDeletion.setCardBackgroundColor(dialogView.getResources().getColor(R.color.colorThemeOrange));

                }else {
                    cvConfirmDeletion.setCardBackgroundColor(dialogView.getResources().getColor(R.color.colorThemeGrey));
                    cvConfirmDeletion.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        etPasswordConfirmation = dialogView.findViewById(R.id.etPasswordDelete);
        etPasswordConfirmation.addTextChangedListener(passwordWatcher);
        cvConfirmDeletion = dialogView.findViewById(R.id.cvConfirmDelete);
        cvConfirmDeletion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, SavePref.readPasswordUser(activity),Toast.LENGTH_LONG);
                if(etPasswordConfirmation.getText().toString().equals(SavePref.readPasswordUser(activity))){
                    progressDialog = new ProgressDialog(activity);
                    progressDialog.setMessage("Harap tunggu...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    ProductRequest.deleteProduct(activity);
                }else {
                    etPasswordConfirmation.setError("Password Tidak Sama!");
                }

            }
        });
        close = dialogView.findViewById(R.id.imgCreditClose);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDelete.dismiss();
            }
        });

        dialogDelete.show();
        dialogDelete.getWindow().setBackgroundDrawableResource(android.R.color.transparent);






    }




}
