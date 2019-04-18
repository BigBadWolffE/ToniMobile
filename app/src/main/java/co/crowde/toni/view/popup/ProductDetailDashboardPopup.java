package co.crowde.toni.view.popup;

import android.app.Activity;
import android.app.Dialog;
import android.support.design.chip.ChipGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import co.crowde.toni.R;
import co.crowde.toni.controller.network.API;
import co.crowde.toni.helper.DecimalFormatRupiah;
import co.crowde.toni.model.ProductModel;

public class ProductDetailDashboardPopup {

    public static TextView tvProductName, tvProductUnit, labelProductCategory, tvProductCategory,
            labelProductPrice, tvProductPrice, tvProductQty, tvProductDesc;
    public static ImageView imgProductDetail, imgBtnMinQty, imgBtnPlusQty, imgBtnAddCart;

    public static void showProductDetail(final Activity activity, ProductModel model) {
        final LayoutInflater inflater = LayoutInflater.from(activity);
        final View dialogView = inflater.inflate(
                R.layout.layout_popup_product_dashboard_detail, null);

        final Dialog dialog = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar);
        dialog.setContentView(dialogView);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimateSlide;

        //Get View Id
        tvProductName = dialogView.findViewById(R.id.tvProductName);
        tvProductUnit = dialogView.findViewById(R.id.tvProductUnit);
        labelProductCategory = dialogView.findViewById(R.id.labelProductCategory);
        tvProductCategory = dialogView.findViewById(R.id.tvProductCategory);
        labelProductPrice = dialogView.findViewById(R.id.labelProductPrice);
        tvProductPrice = dialogView.findViewById(R.id.tvProductPrice);
        tvProductQty = dialogView.findViewById(R.id.tvProductQty);
        tvProductDesc = dialogView.findViewById(R.id.tvProductDesc);
        imgProductDetail = dialogView.findViewById(R.id.imgProductDetail);
        imgBtnMinQty = dialogView.findViewById(R.id.imgBtnMinQty);
        imgBtnPlusQty = dialogView.findViewById(R.id.imgBtnPlusQty);
        imgBtnAddCart = dialogView.findViewById(R.id.imgBtnAddCart);

        //Fetch data
        tvProductName.setText(model.getProductName());
        tvProductUnit.setText("Kemasan "+model.getUnit());
        tvProductCategory.setText(model.getCategoryName());
        DecimalFormatRupiah.changeFormat(activity);
        tvProductPrice.setText("Rp. "+
                String.valueOf(DecimalFormatRupiah.formatNumber.format(model.getSellingPrice())));
        tvProductQty.setText("1");
        tvProductDesc.setText(model.getDescription());

        Picasso.with(activity).load(API.Host+model.getPicture())
                .into(imgProductDetail);

        dialog.show();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }
}
