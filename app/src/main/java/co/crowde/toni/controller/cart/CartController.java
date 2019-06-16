package co.crowde.toni.controller.cart;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import co.crowde.toni.database.Cart;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.CartModel;
import co.crowde.toni.model.ProductModel;
import co.crowde.toni.view.dialog.product.ProductDetailDashboardPopup;
import co.crowde.toni.view.fragment.modul.Dashboard;

public class CartController {

    public static void addFromPopup(Activity activity, ProductModel model){
        String shopId = SavePref.readShopId(activity);
        String productId = String.valueOf(model.getProductId());
        String nama = String.valueOf(model.getProductName());
        String kemasan = String.valueOf(model.getUnit());
        int stok = model.getStock();
        String picture = String.valueOf(model.getPicture());
        int harga = model.getSellingPrice();
        int jumlah = Integer.valueOf(ProductDetailDashboardPopup.etQty.getText().toString());
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
                    ProductDetailDashboardPopup.alertDialog.dismiss();
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
                        ProductDetailDashboardPopup.alertDialog.dismiss();
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

    public static void addFromPlus(Activity activity, ProductModel model){
        String shopId = SavePref.readShopId(activity);
        String productId = String.valueOf(model.getProductId());
        String nama = String.valueOf(model.getProductName());
        String kemasan = String.valueOf(model.getUnit());
        int stok = model.getStock();
        String picture = String.valueOf(model.getPicture());
        int harga = model.getSellingPrice();
        int jumlah = 1;
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
                Dashboard.cartModel.setQuantity(Dashboard.cartModel.getQuantity()+jumlah);
                Dashboard.cartModel.setAmount(Dashboard.cartModel.getAmount()+total);
                if(Dashboard.cartModel.getQuantity()<=stok){
                    Dashboard.dbCart.updateItem(Dashboard.cartModel);
                    Dashboard.ifCartEmpty(activity);
                    Dashboard.productDashboardAdapter.notifyDataSetChanged();
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

    public static void minQtyCart(Activity activity, ProductModel model){
        String shopId = SavePref.readShopId(activity);
        String productId = String.valueOf(model.getProductId());
        String nama = String.valueOf(model.getProductName());
        String kemasan = String.valueOf(model.getUnit());
        int stok = model.getStock();
        String picture = String.valueOf(model.getPicture());
        int harga = model.getSellingPrice();
        int jumlah = -1;
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
                Dashboard.cartModel.setQuantity(Dashboard.cartModel.getQuantity()+jumlah);
                Dashboard.cartModel.setAmount(Dashboard.cartModel.getAmount()+total);
                if(Dashboard.cartModel.getQuantity()<=stok){
                    Dashboard.dbCart.updateItem(Dashboard.cartModel);
                    Dashboard.ifCartEmpty(activity);
                    Dashboard.productDashboardAdapter.notifyDataSetChanged();
                    if(Dashboard.dbCart.getItem(model.getProductId()).getQuantity()<1){
                        Dashboard.dbCart.deleteItem(Dashboard.dbCart.getItem(model.getProductId()));
                        Dashboard.ifCartEmpty(activity);
                    }
                }
            }
        }
        finally{
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
