package co.crowde.toni.view.dialog.popup.product;

import android.app.Activity;
import android.app.ProgressDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import co.crowde.toni.R;
import co.crowde.toni.database.Cart;
import co.crowde.toni.helper.DecimalFormatRupiah;
import co.crowde.toni.model.CartModel;
import co.crowde.toni.network.ProductRequest;
import co.crowde.toni.view.activity.cart.CartListActivity;
import co.crowde.toni.view.fragment.modul.DashboardFragment;

public class ProductDiscountDialog {

    public static TextView tv_edit_discount, tv_remove_discount, tv_cancel;

    public static EditText et_discount;

    public static ImageView img_close;

    public static AlertDialog dialogClose;

    public static ProgressDialog progressDialog;

    public static void showDialog(final Activity activity, CartModel model, Cart dbCart) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(activity)
                .inflate(R.layout.custom_dialog_input_discount,
                        viewGroup,
                        false);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        dialogClose = builder.create();

        //Get View Id
        img_close = dialogView.findViewById(R.id.img_close);
        tv_edit_discount = dialogView.findViewById(R.id.tv_edit_discount);
        tv_remove_discount = dialogView.findViewById(R.id.tv_remove_discount);
        tv_cancel = dialogView.findViewById(R.id.tv_cancel);
        et_discount = dialogView.findViewById(R.id.et_discount);

        DecimalFormatRupiah.changeFormat(activity);

        if (model.getAmount() != model.getSellingPrice() * model.getQuantity()) {
            tv_remove_discount.setVisibility(View.VISIBLE);
            et_discount.setText(DecimalFormatRupiah.formatNumber.format(model.getDiscount()));
            tv_edit_discount.setText("UBAH");
        } else {
            tv_cancel.setVisibility(View.VISIBLE);
            et_discount.setText("");
            tv_edit_discount.setText("TAMBAH");
        }

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogClose.dismiss();
            }
        });

        tv_edit_discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_discount.getText().length() > 0) {
                    int discount = Integer.parseInt(et_discount.getText().toString().replaceAll("[,.]", ""));
                    if (discount <= (model.getQuantity() * model.getSellingPrice())) {
                        model.setDiscount(discount);
                        model.setAmount((model.getQuantity() * model.getSellingPrice()) - discount);
                        dbCart.updateItem(model);
                        CartListActivity.cartAdapter.notifyDataSetChanged();
                        ProductRequest.putProductDiscount(activity, model.getProductId(), discount);
                        DashboardFragment.requestFilter(activity);
                        DashboardFragment.ifCartEmpty(activity);
                        CartListActivity.setTotalAmount(activity);

                        dialogClose.dismiss();
                    }
                }
            }
        });

        tv_remove_discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setDiscount(0);
                model.setAmount(model.getQuantity() * model.getSellingPrice());
                dbCart.updateItem(model);
                CartListActivity.cartAdapter.notifyDataSetChanged();
                ProductRequest.putProductDiscount(activity, model.getProductId(), 0);
                DashboardFragment.requestFilter(activity);
                DashboardFragment.ifCartEmpty(activity);
                CartListActivity.setTotalAmount(activity);

                dialogClose.dismiss();
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogClose.dismiss();
            }
        });

        et_discount.addTextChangedListener(discountWatcher());

        dialogClose.show();
        dialogClose.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }

    public static TextWatcher discountWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                et_discount.removeTextChangedListener(this);
                try {
                    String originalString = s.toString();

                    long longval;
                    if (originalString.contains(",") || originalString.contains(".")) {
                        originalString = originalString.replaceAll("[.,]", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("###,###,###,###,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    et_discount.setText(formattedString);
                    et_discount.setSelection(et_discount.getText().length());

                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                et_discount.addTextChangedListener(this);

            }
        };
    }
}
