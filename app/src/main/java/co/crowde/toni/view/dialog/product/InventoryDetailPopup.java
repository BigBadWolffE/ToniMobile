package co.crowde.toni.view.dialog.product;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import co.crowde.toni.R;
import co.crowde.toni.helper.CloseSoftKeyboard;
import co.crowde.toni.helper.DecimalFormatRupiah;
import co.crowde.toni.model.ProductModel;
import co.crowde.toni.network.API;

public class InventoryDetailPopup {

    public static TextView tvProductName, tvProductUnit,
            labelProductCategory, labelProductStock, tvProductCategory, tvProductStock,
            labelAddStock,
            labelChangePrice, labelPurchase, labelSelling, tvBtnUpdate;
    public static EditText etQty,
            etPurchase, etSelling;
    public static ImageView imgProductDetail, imgClose,
            imgBtnMinQty, imgBtnPlusQty;
    public static CardView cvLayoutMain, cvUpdate;
    public static ConstraintLayout constraintLayout;
    public  static  int qty, purchase, selling;

    public static AlertDialog alertDialog;

    public static void showPopup(final Activity activity, final ProductModel model) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);

//        boolean tabletSize = activity.getResources().getBoolean(R.bool.isTablet);
//        if (tabletSize) {
//            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        } else {
//            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }

        //then we will inflate the custom alert dialog xml that we created
        final View dialogView = LayoutInflater.from(activity)
                .inflate(R.layout.layout_popup_inventory_detail,
                        viewGroup,
                        false);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        alertDialog = builder.create();

        tvProductName = dialogView.findViewById(R.id.tvProductName);
        tvProductUnit = dialogView.findViewById(R.id.tvProductUnit);
        labelProductCategory = dialogView.findViewById(R.id.labelProductCategory);
        labelProductStock = dialogView.findViewById(R.id.labelProductStock);
        tvProductCategory = dialogView.findViewById(R.id.tvProductCategory);
        tvProductStock = dialogView.findViewById(R.id.tvProductStock);
        labelAddStock = dialogView.findViewById(R.id.labelAddStock);
        labelChangePrice = dialogView.findViewById(R.id.labelChangePrice);
        labelPurchase = dialogView.findViewById(R.id.labelPurchase);
        labelSelling = dialogView.findViewById(R.id.labelSelling);
        tvBtnUpdate = dialogView.findViewById(R.id.tvBtnUpdate);
        etQty = dialogView.findViewById(R.id.etQty);
        etPurchase = dialogView.findViewById(R.id.etPurchase);
        etSelling = dialogView.findViewById(R.id.etSelling);
        imgProductDetail = dialogView.findViewById(R.id.imgProductDetail);
        imgClose = dialogView.findViewById(R.id.imgClose);
        imgBtnMinQty = dialogView.findViewById(R.id.imgBtnMinQty);
        imgBtnPlusQty = dialogView.findViewById(R.id.imgBtnPlusQty);
        cvLayoutMain = dialogView.findViewById(R.id.cvLayoutMain);
        cvUpdate = dialogView.findViewById(R.id.cvUpdate);
        constraintLayout = dialogView.findViewById(R.id.constraintLayout);

        String product = model.getProductName();
        String nama;
        String varian;
        if(product.contains("_")){
            nama = StringUtils.substringBeforeLast(product, "_");
            varian = StringUtils.substringAfterLast(product, "_");
        } else {
            nama = product;
            varian = model.getUnit();
        }

        tvProductName.setText(nama);
        tvProductUnit.setText("Kemasan "+varian);
        tvProductCategory.setText(model.getCategoryName());
        tvProductStock.setText(String.valueOf(model.getStock()));
        DecimalFormatRupiah.changeFormat(activity);
        etQty.setText("0");
        etPurchase.setText(String.valueOf(
                DecimalFormatRupiah.formatNumber.format(model.getPurchasePrice())));
        etSelling.setText(String.valueOf(
                DecimalFormatRupiah.formatNumber.format(model.getSellingPrice())));

        Picasso.with(activity).load(API.Host+model.getPicture())
                .into(imgProductDetail);

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        cvLayoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CloseSoftKeyboard.hideSoftKeyboard(v, activity);
            }
        });
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        imgBtnPlusQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qty = Integer.parseInt(etQty.getText().toString());
                qty = qty+1;
                etQty.setText(String.valueOf(qty));
            }
        });
        imgBtnMinQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(qty!=0){
                    qty = Integer.parseInt(etQty.getText().toString());
                    qty = qty-1;
                    etQty.setText(String.valueOf(qty));
                }
            }
        });

        cvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageUpdateProduct.show(activity, model.getProductId());
            }
        });

        etPurchase.addTextChangedListener(purchasePrice());
        etSelling.addTextChangedListener(sellingPrice());

        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public static TextWatcher sellingPrice() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                etSelling.removeTextChangedListener(this);
                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("###,###,###,###,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    etSelling.setText(formattedString);
                    etSelling.setSelection(etSelling.getText().length());

                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                etSelling.addTextChangedListener(this);

            }
        };
    }

    public static TextWatcher purchasePrice() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                etPurchase.removeTextChangedListener(this);
                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("###,###,###,###,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    etPurchase.setText(formattedString);
                    etPurchase.setSelection(etPurchase.getText().length());

                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                etPurchase.addTextChangedListener(this);

            }
        };
    }

}
