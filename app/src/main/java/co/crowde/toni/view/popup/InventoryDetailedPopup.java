package co.crowde.toni.view.popup;

import android.app.Activity;
import android.app.Dialog;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import co.crowde.toni.R;
import co.crowde.toni.controller.network.API;
import co.crowde.toni.helper.DecimalFormatRupiah;
import co.crowde.toni.model.ProductModel;

public class InventoryDetailedPopup {

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

    public static void showPopup(final Activity activity, ProductModel model) {
        final LayoutInflater inflater = LayoutInflater.from(activity);
        final View dialogView = inflater.inflate(
                R.layout.layout_popup_inventory_detail, null);

        final Dialog dialog = new Dialog(activity, android.R.style.Theme_NoTitleBar);
        dialog.setContentView(dialogView);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimateFade;
        dialog.setCanceledOnTouchOutside(true);

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

        tvProductName.setText(model.getProductName());
        tvProductUnit.setText("Kemasan "+model.getUnit());
        tvProductCategory.setText(model.getCategoryName());
        tvProductStock.setText(String.valueOf(model.getStock()));
        DecimalFormatRupiah.changeFormat(activity);
        etPurchase.setText(String.valueOf(
                DecimalFormatRupiah.formatNumber.format(model.getPurchasePrice())));
        etSelling.setText(String.valueOf(
                DecimalFormatRupiah.formatNumber.format(model.getSellingPrice())));

        Picasso.with(activity).load(API.Host+model.getPicture())
                .into(imgProductDetail);

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.getWindow().setBackgroundDrawableResource(R.color.overlayBackground);

    }
}
