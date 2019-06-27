package co.crowde.toni.controller.cart;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import co.crowde.toni.database.Cart;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.CartModel;
import co.crowde.toni.model.ProductModel;
import co.crowde.toni.view.dialog.popup.product.ProductDetailDashboardPopup;
import co.crowde.toni.view.fragment.modul.DashboardFragment;

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

        DashboardFragment.cartModel_ = new CartModel();
        DashboardFragment.cartModel_.setShopId(shopId);
        DashboardFragment.cartModel_.setProductId(productId);
        DashboardFragment.cartModel_.setProductName(nama);
        DashboardFragment.cartModel_.setUnit(kemasan);
        DashboardFragment.cartModel_.setStok(stok);
        DashboardFragment.cartModel_.setPicture(picture);
        DashboardFragment.cartModel_.setQuantity(jumlah);
        DashboardFragment.cartModel_.setSellingPrice(harga);
        DashboardFragment.cartModel_.setAmount(total);

        //Select All Query
        String querySelect = "SELECT * FROM " + CartModel.TABLE_NAME + " WHERE productId='" + productId + "'";
        DashboardFragment.dbCart = new Cart(activity);
        SQLiteDatabase db = DashboardFragment.dbCart.getWritableDatabase();
        Cursor cursor = db.rawQuery(querySelect, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                DashboardFragment.cartModel = new CartModel(
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
                DashboardFragment.cartModel.setQuantity(DashboardFragment.cartModel.getQuantity()+jumlah);
                DashboardFragment.cartModel.setAmount(DashboardFragment.cartModel.getAmount()+total);
                if(DashboardFragment.cartModel.getQuantity()<=stok){
                    DashboardFragment.dbCart.updateItem(DashboardFragment.cartModel);
                    DashboardFragment.ifCartEmpty(activity);
                    DashboardFragment.productDashboardAdapter.notifyDataSetChanged();
                    ProductDetailDashboardPopup.alertDialog.dismiss();
                } else {

                    Toast.makeText(activity, "Stok tidak mencukupi permintaan pelanggan.", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (stok > 0 && jumlah <= stok) {
                    long id = DashboardFragment.dbCart.insertItem(DashboardFragment.cartModel_);
                    CartModel models = DashboardFragment.dbCart.getItem(id);
                    if (models != null) {
                        DashboardFragment.ifCartEmpty(activity);
                        DashboardFragment.productDashboardAdapter.notifyDataSetChanged();
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

        DashboardFragment.cartModel_ = new CartModel();
        DashboardFragment.cartModel_.setShopId(shopId);
        DashboardFragment.cartModel_.setProductId(productId);
        DashboardFragment.cartModel_.setProductName(nama);
        DashboardFragment.cartModel_.setUnit(kemasan);
        DashboardFragment.cartModel_.setStok(stok);
        DashboardFragment.cartModel_.setPicture(picture);
        DashboardFragment.cartModel_.setQuantity(jumlah);
        DashboardFragment.cartModel_.setSellingPrice(harga);
        DashboardFragment.cartModel_.setAmount(total);

        //Select All Query
        String querySelect = "SELECT * FROM " + CartModel.TABLE_NAME + " WHERE productId='" + productId + "'";
        DashboardFragment.dbCart = new Cart(activity);
        SQLiteDatabase db = DashboardFragment.dbCart.getWritableDatabase();
        Cursor cursor = db.rawQuery(querySelect, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                DashboardFragment.cartModel = new CartModel(
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
                DashboardFragment.cartModel.setQuantity(DashboardFragment.cartModel.getQuantity()+jumlah);
                DashboardFragment.cartModel.setAmount(DashboardFragment.cartModel.getAmount()+total);
                if(DashboardFragment.cartModel.getQuantity()<=stok){
                    DashboardFragment.dbCart.updateItem(DashboardFragment.cartModel);
                    DashboardFragment.ifCartEmpty(activity);
                    DashboardFragment.productDashboardAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(activity, "Stok tidak mencukupi permintaan pelanggan.", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (stok > 0 && jumlah <= stok) {
                    long id = DashboardFragment.dbCart.insertItem(DashboardFragment.cartModel_);
                    CartModel models = DashboardFragment.dbCart.getItem(id);
                    if (models != null) {
                        DashboardFragment.ifCartEmpty(activity);
                        DashboardFragment.productDashboardAdapter.notifyDataSetChanged();
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

        DashboardFragment.cartModel_ = new CartModel();
        DashboardFragment.cartModel_.setShopId(shopId);
        DashboardFragment.cartModel_.setProductId(productId);
        DashboardFragment.cartModel_.setProductName(nama);
        DashboardFragment.cartModel_.setUnit(kemasan);
        DashboardFragment.cartModel_.setStok(stok);
        DashboardFragment.cartModel_.setPicture(picture);
        DashboardFragment.cartModel_.setQuantity(jumlah);
        DashboardFragment.cartModel_.setSellingPrice(harga);
        DashboardFragment.cartModel_.setAmount(total);

        //Select All Query
        String querySelect = "SELECT * FROM " + CartModel.TABLE_NAME + " WHERE productId='" + productId + "'";
        DashboardFragment.dbCart = new Cart(activity);
        SQLiteDatabase db = DashboardFragment.dbCart.getWritableDatabase();
        Cursor cursor = db.rawQuery(querySelect, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                DashboardFragment.cartModel = new CartModel(
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
                DashboardFragment.cartModel.setQuantity(DashboardFragment.cartModel.getQuantity()+jumlah);
                DashboardFragment.cartModel.setAmount(DashboardFragment.cartModel.getAmount()+total);
                if(DashboardFragment.cartModel.getQuantity()<=stok){
                    DashboardFragment.dbCart.updateItem(DashboardFragment.cartModel);
                    DashboardFragment.ifCartEmpty(activity);
                    DashboardFragment.productDashboardAdapter.notifyDataSetChanged();
                    if(DashboardFragment.dbCart.getItem(model.getProductId()).getQuantity()<1){
                        DashboardFragment.dbCart.deleteItem(DashboardFragment.dbCart.getItem(model.getProductId()));
                        DashboardFragment.ifCartEmpty(activity);
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
