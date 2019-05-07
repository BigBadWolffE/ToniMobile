package co.crowde.toni.view.popup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import co.crowde.toni.R;
import co.crowde.toni.controller.network.API;
import co.crowde.toni.controller.network.CustomerRequest;
import co.crowde.toni.database.Cart;
import co.crowde.toni.helper.DecimalFormatRupiah;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.CartModel;
import co.crowde.toni.model.ProductModel;
import co.crowde.toni.view.fragment.Dashboard;

public class AddNewCustomerPopup {

    public static TextView tvHeader;
    public static ImageView tvClose;
    public static EditText etName, etPhone;
    public static CardView cvBtnAddNew;
    public static AlertDialog alertDialog;

    public static Activity activity;

    @SuppressLint("SetTextI18n")
    public static void showPopup(final Activity activity) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(activity)
                .inflate(R.layout.layout_add_new_popup,
                        viewGroup,
                        false);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        alertDialog = builder.create();

        //Get View Id
        tvHeader = dialogView.findViewById(R.id.tvHeader);
        tvClose = dialogView.findViewById(R.id.tvClose);
        etName = dialogView.findViewById(R.id.etName);
        etPhone = dialogView.findViewById(R.id.etPhone);
        cvBtnAddNew = dialogView.findViewById(R.id.cvBtnAddNew);

        etName.addTextChangedListener(watcherAdd);
        etPhone.addTextChangedListener(watcherAdd);
//        CustomerRequest.addNewCustomer(activity);

        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }

    public static TextWatcher watcherAdd = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (etName.getText().length() > 0 && etPhone.getText().length() > 0) {
                cvBtnAddNew.setCardBackgroundColor(activity.getResources().getColor(R.color.colorThemeOrange));
                cvBtnAddNew.setEnabled(true);
            } else {
                cvBtnAddNew.setCardBackgroundColor(activity.getResources().getColor(R.color.colorThemeGrey));
                cvBtnAddNew.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
