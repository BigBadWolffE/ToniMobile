package co.crowde.toni.view.dialog.popup.product;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import co.crowde.toni.R;
import co.crowde.toni.controller.cart.CartController;
import co.crowde.toni.network.API;
import co.crowde.toni.helper.DecimalFormatRupiah;
import co.crowde.toni.model.ProductModel;

public class ProductDetailDashboardPopup {

    public static TextView tvProductName, tvProductUnit, labelProductCategory, tvProductCategory,
            labelProductPrice, tvProductPrice, tvProductDesc;
    public static ImageView imgProductDetail, imgBtnMinQty, imgBtnPlusQty, imgBtnAddCart;
    public static ConstraintLayout constraintLayout;

    public static EditText etQty;
    public static int tambahStok;

    public static AlertDialog alertDialog;

    @SuppressLint("SetTextI18n")
    public static void showPopup(final Activity activity, final ProductModel model) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);

        boolean tabletSize = activity.getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(activity)
                .inflate(R.layout.layout_popup_product_dashboard_detail,
                        viewGroup,
                        false);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        alertDialog = builder.create();

        //Get View Id
        tvProductName = dialogView.findViewById(R.id.tvProductName);
        tvProductUnit = dialogView.findViewById(R.id.tvProductUnit);
        labelProductCategory = dialogView.findViewById(R.id.labelProductCategory);
        tvProductCategory = dialogView.findViewById(R.id.tvProductCategory);
        labelProductPrice = dialogView.findViewById(R.id.labelProductPrice);
        tvProductPrice = dialogView.findViewById(R.id.tvProductPrice);
        etQty = dialogView.findViewById(R.id.etQty);
        tvProductDesc = dialogView.findViewById(R.id.tvProductDesc);
        imgProductDetail = dialogView.findViewById(R.id.imgProductDetail);
        imgBtnMinQty = dialogView.findViewById(R.id.imgBtnMinQty);
        imgBtnPlusQty = dialogView.findViewById(R.id.imgBtnPlusQty);
        imgBtnAddCart = dialogView.findViewById(R.id.imgBtnAddCart);
        constraintLayout = dialogView.findViewById(R.id.constraintLayout);

        String product = model.getProductName();
        String nama;
        String varian;
        if(product.contains("_")){
            nama = StringUtils.substringBeforeLast(product, "_");
            varian = StringUtils.substringAfterLast(product, "_");
        } else {
            nama = product;
            varian = "-";
        }

        //Fetch data
        tvProductName.setText(nama);
        tvProductUnit.setText("Kemasan "+varian);
        tvProductCategory.setText(model.getCategoryName());
        DecimalFormatRupiah.changeFormat(activity);
        tvProductPrice.setText("Rp. "+
                String.valueOf(DecimalFormatRupiah.formatNumber.format(model.getSellingPrice())));
        etQty.setText("1");
        tvProductDesc.setText(model.getDescription());

        String stok = etQty.getText().toString();
        tambahStok = Integer.parseInt(stok);

        imgBtnPlusQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tambahStok = tambahStok+1;
                if(tambahStok>0){
                    etQty.setText(""+tambahStok);
                } else {
                    tambahStok=1;
                    etQty.setText("1");
                }
            }
        });
        imgBtnMinQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tambahStok = tambahStok-1;
                if(tambahStok>0){
                    etQty.setText(""+tambahStok);
                } else {
                    tambahStok=1;
                    etQty.setText("1");
                }
            }
        });

        Picasso.with(activity).load(API.Host+model.getPicture())
                .into(imgProductDetail);

        imgBtnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartController.addFromPopup(activity, model);
            }
        });
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }
}
