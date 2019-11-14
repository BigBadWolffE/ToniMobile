package co.crowde.toni.view.dialog.message.product;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import co.crowde.toni.R;

public class PasswordConfirmationDeleteDialog {
    public static EditText etPasswordConfirmation;
    public static ImageView close;
    public static CardView cvConfirmDeletion;

    public static AlertDialog dialogDelete;

    public static ProgressDialog progressDialog;

    public static void showDialog(final Activity activity,ProgressDialog progressDialog){
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
        etPasswordConfirmation = dialogView.findViewById(R.id.etPasswordDelete);
        cvConfirmDeletion = dialogView.findViewById(R.id.cvConfirmDelete);
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
