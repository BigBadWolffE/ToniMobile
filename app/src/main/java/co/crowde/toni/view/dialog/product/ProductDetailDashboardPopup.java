package co.crowde.toni.view.dialog.product;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
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
import co.crowde.toni.network.API;
import co.crowde.toni.database.Cart;
import co.crowde.toni.helper.DecimalFormatRupiah;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.CartModel;
import co.crowde.toni.model.ProductModel;
import co.crowde.toni.view.fragment.modul.Dashboard;

public class ProductDetailDashboardPopup {

    public static TextView tvProductName, tvProductUnit, labelProductCategory, tvProductCategory,
            labelProductPrice, tvProductPrice, tvProductDesc;
    public static ImageView imgProductDetail, imgBtnMinQty, imgBtnPlusQty, imgBtnAddCart;
    public static ConstraintLayout constraintLayout;

    public static EditText etQty;
    public static int tambahStok;

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
        final AlertDialog alertDialog = builder.create();

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
                String shopId = SavePref.readShopId(activity);
                String productId = String.valueOf(model.getProductId());
                String nama = String.valueOf(model.getProductName());
                String kemasan = String.valueOf(model.getUnit());
                int stok = model.getStock();
                String picture = String.valueOf(model.getPicture());
                int harga = model.getSellingPrice();
                int jumlah = Integer.valueOf(etQty.getText().toString());
                int total = harga * jumlah;

                Dashboard.cartModel_ = new CartModel();
                Dashboard.cartModel_.setShopId(shopId);
                Dashboard.cartModel_.setProductId(productId);
                Dashboard.cartModel_.setProductName(nama);
                Dashboard.cartModel_.setUnit(kemasan);
                Dashboard.cartModel_.setStok(stok);
                Dashboard.cartModel_.setPicture(picture);
                Dashboard.cartModel_.setQuantity(jumlah);
                Dashboard.cartModel_.setSellingPrice(harga);
                Dashboard.cartModel_.setAmount(total);

                //Select All Query
                String querySelect = "SELECT * FROM " + CartModel.TABLE_NAME + " WHERE productId='" + productId + "'";
                Dashboard.dbCart = new Cart(activity);
                SQLiteDatabase db = Dashboard.dbCart.getWritableDatabase();
                Cursor cursor = db.rawQuery(querySelect, null);

                try {
                    if (cursor != null && cursor.moveToFirst()) {
                        Dashboard.cartModel = new CartModel(
                                cursor.getInt(cursor.getColumnIndex(CartModel.KEY_ID)),
                                cursor.getString(cursor.getColumnIndex(CartModel.KEY_SHOP_ID)),
                                cursor.getString(cursor.getColumnIndex(CartModel.KEY_PRODUCT_ID)),
                                cursor.getString(cursor.getColumnIndex(CartModel.KEY_PRODUCT_NAME)),
                                cursor.getString(cursor.getColumnIndex(CartModel.KEY_UNIT)),
                                cursor.getInt(cursor.getColumnIndex(CartModel.KEY_STOK)),
                                cursor.getString(cursor.getColumnIndex(CartModel.KEY_PICTURE)),
                                cursor.getInt(cursor.getColumnIndex(CartModel.KEY_QUANTITY)),
                                cursor.getInt(cursor.getColumnIndex(CartModel.KEY_SELLING_PRICE)),
                                cursor.getInt(cursor.getColumnIndex(CartModel.KEY_AMOUNT)));
//                        Toast.makeText(activity, "" + new Gson().toJson(keranjangBelanjaModel), Toast.LENGTH_SHORT).show();
                        Dashboard.cartModel.setQuantity(Dashboard.cartModel.getQuantity()+jumlah);
                        Dashboard.cartModel.setAmount(Dashboard.cartModel.getAmount()+total);
                        if(Dashboard.cartModel.getQuantity()<=stok){
                            Dashboard.dbCart.updateItem(Dashboard.cartModel);
                            Dashboard.ifCartEmpty(activity);
                            Dashboard.productDashboardAdapter.notifyDataSetChanged();
                            alertDialog.dismiss();
                        } else {
                            Toast.makeText(activity, "Stok tidak mencukupi permintaan pelanggan.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (stok > 0 && jumlah <= stok) {
                            long id = Dashboard.dbCart.insertItem(Dashboard.cartModel_);
                            CartModel models = Dashboard.dbCart.getItem(id);
                            if (models != null) {
                                Dashboard.ifCartEmpty(activity);
                                Dashboard.productDashboardAdapter.notifyDataSetChanged();
                                alertDialog.dismiss();
                            } else {
                                Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(activity, "Maaf stok tidak mencukupi permintaan pelanggan", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                finally{
                    if (cursor != null) {
                        cursor.close();
                    }
                }

            }
        });
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }
}
