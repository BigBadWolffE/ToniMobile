package co.crowde.toni.controller.cart;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import co.crowde.toni.adapter.ProductDashboardAdapter;
import co.crowde.toni.constant.Const;
import co.crowde.toni.database.Cart;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.CartModel;
import co.crowde.toni.model.ProductModel;
import co.crowde.toni.utils.analytics.AnalyticsToniUtils;
import co.crowde.toni.view.activity.cart.CartListActivity;
import co.crowde.toni.view.activity.product.ProductDashboardDetailActivity;
import co.crowde.toni.view.dialog.message.product.StockInsufficientDialog;
import co.crowde.toni.view.fragment.modul.DashboardFragment;

public class CartController {

    public static void addFromDetail(Activity activity, ProductModel model, int qty, int discount){
        String shopId = SavePref.readShopId(activity);
        String productId = String.valueOf(model.getProductId());
        String nama = String.valueOf(model.getProductName());
        String kemasan = String.valueOf(model.getUnit());
        int stok = model.getStock();
        String picture = String.valueOf(model.getPicture());
        int harga = model.getSellingPrice();
        int total = (harga * qty)-discount;

        DashboardFragment.cartModel_ = new CartModel();
        DashboardFragment.cartModel_.setShopId(shopId);
        DashboardFragment.cartModel_.setProductId(productId);
        DashboardFragment.cartModel_.setProductName(nama);
        DashboardFragment.cartModel_.setUnit(kemasan);
        DashboardFragment.cartModel_.setStok(stok);
        DashboardFragment.cartModel_.setPicture(picture);
        DashboardFragment.cartModel_.setQuantity(qty);
        DashboardFragment.cartModel_.setSellingPrice(harga);
        DashboardFragment.cartModel_.setAmount(total);
        DashboardFragment.cartModel_.setDiscount(discount);

        //Select All Query
        String querySelect = "SELECT * FROM " + CartModel.TABLE_NAME + " WHERE productId='" + productId + "'";
        DashboardFragment.dbCart = new Cart(activity);
        SQLiteDatabase db = DashboardFragment.dbCart.getWritableDatabase();

        try (Cursor cursor = db.rawQuery(querySelect, null)) {
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
                        cursor.getInt(cursor.getColumnIndex(CartModel.KEY_AMOUNT)),
                        cursor.getInt(cursor.getColumnIndex(CartModel.KEY_DISCOUNT)));
//                        Toast.makeText(activity, "" + new Gson().toJson(keranjangBelanjaModel), Toast.LENGTH_SHORT).show();
                DashboardFragment.cartModel.setQuantity(qty);
                DashboardFragment.cartModel.setAmount(total);
                DashboardFragment.cartModel.setDiscount(discount);
                if (DashboardFragment.cartModel.getQuantity() <= stok) {
                    DashboardFragment.dbCart.updateItem(DashboardFragment.cartModel);
                    DashboardFragment.ifCartEmpty(activity);
                    DashboardFragment.productDashboardAdapter.notifyDataSetChanged();
                    activity.finish();
                } else {
                    StockInsufficientDialog.showDialog(activity);
                }
            } else {
                if (stok > 0 && qty <= stok) {
                    long id = DashboardFragment.dbCart.insertItem(DashboardFragment.cartModel_);
                    CartModel models = DashboardFragment.dbCart.getItem(id);
                    if (models != null) {
                        DashboardFragment.ifCartEmpty(activity);
                        DashboardFragment.productDashboardAdapter.notifyDataSetChanged();
                        activity.finish();
                    } else {
                        Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
                    }

                    AnalyticsToniUtils.getEvent(Const.CATEGORY_TRANSACTION, Const.MODUL_CART, Const.LABEL_CART_ADD_PRODUCT_POPUP);
                } else {
                    StockInsufficientDialog.showDialog(activity);
                }
            }

        }
        DashboardFragment.requestFilter(activity);
    }

    public static void addFromCartDetail(Activity activity, ProductModel model, int qty, int discount){
        String shopId = SavePref.readShopId(activity);
        String productId = String.valueOf(model.getProductId());
        String nama = String.valueOf(model.getProductName());
        String kemasan = String.valueOf(model.getUnit());
        int stok = model.getStock();
        String picture = String.valueOf(model.getPicture());
        int harga = model.getSellingPrice();
        int total = (harga * qty)-discount;

        String querySelect = "SELECT * FROM " + CartModel.TABLE_NAME + " WHERE productId='" + productId + "'";
        DashboardFragment.dbCart = new Cart(activity);
        SQLiteDatabase db = DashboardFragment.dbCart.getWritableDatabase();

        try (Cursor cursor = db.rawQuery(querySelect, null)) {
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
                        cursor.getInt(cursor.getColumnIndex(CartModel.KEY_AMOUNT)),
                        cursor.getInt(cursor.getColumnIndex(CartModel.KEY_DISCOUNT)));
//                        Toast.makeText(activity, "" + new Gson().toJson(keranjangBelanjaModel), Toast.LENGTH_SHORT).show();
                DashboardFragment.cartModel.setQuantity(qty);
                DashboardFragment.cartModel.setAmount(total);
                DashboardFragment.cartModel.setDiscount(discount);
                if (DashboardFragment.cartModel.getQuantity() <= stok) {
                    DashboardFragment.dbCart.updateItem(DashboardFragment.cartModel);
                    DashboardFragment.ifCartEmpty(activity);
                    DashboardFragment.productDashboardAdapter.notifyDataSetChanged();
                    CartListActivity.itemCartListener(activity);
                    CartListActivity.setTotalAmount(activity);
                    activity.finish();
                } else {
                    StockInsufficientDialog.showDialog(activity);
                }
            }

        }
    }

    public static void addFromPlus(Activity activity, ProductModel model, ProductDashboardAdapter adapter){
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
        DashboardFragment.cartModel_.setDiscount(model.getDiscount());

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
                        cursor.getInt(cursor.getColumnIndex(CartModel.KEY_AMOUNT)),
                        cursor.getInt(cursor.getColumnIndex(CartModel.KEY_DISCOUNT)));
                DashboardFragment.cartModel.setQuantity(DashboardFragment.cartModel.getQuantity()+jumlah);
                DashboardFragment.cartModel.setAmount(DashboardFragment.cartModel.getAmount()+total);
                if(DashboardFragment.cartModel.getQuantity()<=stok){
                    DashboardFragment.dbCart.updateItem(DashboardFragment.cartModel);
                    DashboardFragment.ifCartEmpty(activity);
                    DashboardFragment.productDashboardAdapter.notifyDataSetChanged();
                } else {
                    StockInsufficientDialog.showDialog(activity);
                }
            } else {
                if (stok > 0 && jumlah <= stok) {
                    long id = DashboardFragment.dbCart.insertItem(DashboardFragment.cartModel_);
                    CartModel models = DashboardFragment.dbCart.getItem(id);
                    if (models != null) {
                        DashboardFragment.ifCartEmpty(activity);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
                    }

                    AnalyticsToniUtils.getEvent(Const.CATEGORY_TRANSACTION,Const.MODUL_CART,Const.LABEL_CART_ADD_PRODUCT_DASHBOARD);
                } else {
                    StockInsufficientDialog.showDialog(activity);
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
        DashboardFragment.cartModel_.setDiscount(model.getDiscount());

        //Select All Query
        String querySelect = "SELECT * FROM " + CartModel.TABLE_NAME + " WHERE productId='" + productId + "'";
        DashboardFragment.dbCart = new Cart(activity);
        SQLiteDatabase db = DashboardFragment.dbCart.getWritableDatabase();

        try (Cursor cursor = db.rawQuery(querySelect, null)) {
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
                        cursor.getInt(cursor.getColumnIndex(CartModel.KEY_AMOUNT)),
                        cursor.getInt(cursor.getColumnIndex(CartModel.KEY_DISCOUNT)));
                DashboardFragment.cartModel.setQuantity(DashboardFragment.cartModel.getQuantity() + jumlah);
                DashboardFragment.cartModel.setAmount(DashboardFragment.cartModel.getAmount() + total);
                if (DashboardFragment.cartModel.getQuantity() <= stok) {
                    DashboardFragment.dbCart.updateItem(DashboardFragment.cartModel);
                    DashboardFragment.ifCartEmpty(activity);
                    DashboardFragment.productDashboardAdapter.notifyDataSetChanged();
                    if (DashboardFragment.dbCart.getItem(model.getProductId()).getQuantity() < 1) {
                        DashboardFragment.dbCart.deleteItem(DashboardFragment.dbCart.getItem(model.getProductId()));
                        DashboardFragment.ifCartEmpty(activity);
                    }
                }
            }
        }
    }

}
