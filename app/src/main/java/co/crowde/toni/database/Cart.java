package co.crowde.toni.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.model.CartModel;

/**
 * Created by Firiyah on 2/12/2019.
 */

public class Cart extends SQLiteOpenHelper {

    private CartModel model;

    // static variable
    private static final int DATABASE_VERSION = 1;

    // Database name
    private static final String DATABASE_NAME = "dbToni";

    // table name
    private static final String TABLE_KERANJANG = "table_cart";

    public Cart(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CartModel.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ CartModel.TABLE_NAME);
        onCreate(db);
    }

    //Create Data
    public long insertItem(CartModel model){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
//        values.put(KEY_ID, model.getId());
        values.put(CartModel.KEY_SHOP_ID, model.getShopId());
        values.put(CartModel.KEY_PRODUCT_ID, model.getProductId());
        values.put(CartModel.KEY_PRODUCT_NAME, model.getProductName());
        values.put(CartModel.KEY_UNIT, model.getUnit());
        values.put(CartModel.KEY_STOK, model.getStok());
        values.put(CartModel.KEY_PICTURE, model.getPicture());
        values.put(CartModel.KEY_QUANTITY, model.getQuantity());
        values.put(CartModel.KEY_SELLING_PRICE, model.getSellingPrice());
        values.put(CartModel.KEY_AMOUNT, model.getAmount());
        values.put(CartModel.KEY_DISCOUNT, model.getDiscount());

        long id = db.insert(TABLE_KERANJANG, null, values);
        db.close();
        return id;
    }

    //Read Data
    public CartModel getItem(long id){
        model = new CartModel();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(CartModel.TABLE_NAME, new String[]{
                        CartModel.KEY_ID,
                        CartModel.KEY_SHOP_ID,
                        CartModel.KEY_PRODUCT_ID,
                        CartModel.KEY_PRODUCT_NAME,
                        CartModel.KEY_PICTURE,
                        CartModel.KEY_UNIT,
                        CartModel.KEY_STOK,
                        CartModel.KEY_QUANTITY,
                        CartModel.KEY_SELLING_PRICE,
                        CartModel.KEY_AMOUNT,
                        CartModel.KEY_DISCOUNT},
                CartModel.KEY_ID+"=?",
                new String[]{ String.valueOf(id)}, null, null,null,null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                model = new CartModel(
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
            }
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return model;
    }

    //Get Data
    public CartModel getItem(String productId){

        CartModel models = new CartModel();

        //Select All Query
        String querySelect = "SELECT * FROM "
                + CartModel.TABLE_NAME
                + " WHERE productId='"
                + productId
                + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(querySelect, null);

        if(cursor.moveToFirst()){
            models.setId(cursor.getInt(cursor.getColumnIndex(CartModel.KEY_ID)));
            models.setShopId(cursor.getString(cursor.getColumnIndex(CartModel.KEY_SHOP_ID)));
            models.setProductId(cursor.getString(cursor.getColumnIndex(CartModel.KEY_PRODUCT_ID)));
            models.setProductName(cursor.getString(cursor.getColumnIndex(CartModel.KEY_PRODUCT_NAME)));
            models.setUnit(cursor.getString(cursor.getColumnIndex(CartModel.KEY_UNIT)));
            models.setStok(cursor.getInt(cursor.getColumnIndex(CartModel.KEY_STOK)));
            models.setPicture(cursor.getString(cursor.getColumnIndex(CartModel.KEY_PICTURE)));
            models.setQuantity(cursor.getInt(cursor.getColumnIndex(CartModel.KEY_QUANTITY)));
            models.setSellingPrice(cursor.getInt(cursor.getColumnIndex(CartModel.KEY_SELLING_PRICE)));
            models.setAmount(cursor.getInt(cursor.getColumnIndex(CartModel.KEY_AMOUNT)));
            models.setDiscount(cursor.getInt(cursor.getColumnIndex(CartModel.KEY_DISCOUNT)));
        }

        db.close();

        //return model
        return models;
    }

    //Get All Data
    public List<CartModel> getAllItem(){
        List<CartModel> modelList = new ArrayList<>();
        //Select All Query
        String querySelect = "SELECT * FROM "+ CartModel.TABLE_NAME+" ORDER BY "+CartModel.KEY_ID+" ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(querySelect, null);

        if(cursor.moveToFirst()){
            do{
                CartModel model = new CartModel();
                model.setId(cursor.getInt(cursor.getColumnIndex(CartModel.KEY_ID)));
                model.setShopId(cursor.getString(cursor.getColumnIndex(CartModel.KEY_SHOP_ID)));
                model.setProductId(cursor.getString(cursor.getColumnIndex(CartModel.KEY_PRODUCT_ID)));
                model.setProductName(cursor.getString(cursor.getColumnIndex(CartModel.KEY_PRODUCT_NAME)));
                model.setUnit(cursor.getString(cursor.getColumnIndex(CartModel.KEY_UNIT)));
                model.setStok(cursor.getInt(cursor.getColumnIndex(CartModel.KEY_STOK)));
                model.setPicture(cursor.getString(cursor.getColumnIndex(CartModel.KEY_PICTURE)));
                model.setQuantity(cursor.getInt(cursor.getColumnIndex(CartModel.KEY_QUANTITY)));
                model.setSellingPrice(cursor.getInt(cursor.getColumnIndex(CartModel.KEY_SELLING_PRICE)));
                model.setAmount(cursor.getInt(cursor.getColumnIndex(CartModel.KEY_AMOUNT)));
                model.setDiscount(cursor.getInt(cursor.getColumnIndex(CartModel.KEY_DISCOUNT)));

                modelList.add(model);
            } while (cursor.moveToNext());
        }

        db.close();

        //return modelList
        return modelList;
    }

    //Get Data
    public int getQty(String productId){

        CartModel models = new CartModel();

        //Select All Query
        String querySelect = "SELECT * FROM "
                + CartModel.TABLE_NAME
                + " WHERE productId='"
                + productId
                + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(querySelect, null);

        if(cursor.moveToFirst()){
            models.setId(cursor.getInt(cursor.getColumnIndex(CartModel.KEY_ID)));
            models.setShopId(cursor.getString(cursor.getColumnIndex(CartModel.KEY_SHOP_ID)));
            models.setProductId(cursor.getString(cursor.getColumnIndex(CartModel.KEY_PRODUCT_ID)));
            models.setProductName(cursor.getString(cursor.getColumnIndex(CartModel.KEY_PRODUCT_NAME)));
            models.setUnit(cursor.getString(cursor.getColumnIndex(CartModel.KEY_UNIT)));
            models.setStok(cursor.getInt(cursor.getColumnIndex(CartModel.KEY_STOK)));
            models.setPicture(cursor.getString(cursor.getColumnIndex(CartModel.KEY_PICTURE)));
            models.setQuantity(cursor.getInt(cursor.getColumnIndex(CartModel.KEY_QUANTITY)));
            models.setSellingPrice(cursor.getInt(cursor.getColumnIndex(CartModel.KEY_SELLING_PRICE)));
            models.setAmount(cursor.getInt(cursor.getColumnIndex(CartModel.KEY_AMOUNT)));
            models.setDiscount(cursor.getInt(cursor.getColumnIndex(CartModel.KEY_DISCOUNT)));
        }

        db.close();

        //return model
        return models.getQuantity();
    }

    //Get Item Count
    public int getItemCount() {
        String queryCount = "SELECT * FROM " + CartModel.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryCount, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    //Get Item Count
    public int getQtyount() {
        String queryCount = "SELECT * FROM " + CartModel.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryCount, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    //Update Data
    public int updateItem(CartModel model) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CartModel.KEY_SHOP_ID, model.getShopId());
        values.put(CartModel.KEY_PRODUCT_ID, model.getProductId());
        values.put(CartModel.KEY_PRODUCT_NAME, model.getProductName());
        values.put(CartModel.KEY_UNIT, model.getUnit());
        values.put(CartModel.KEY_STOK, model.getStok());
        values.put(CartModel.KEY_PICTURE, model.getPicture());
        values.put(CartModel.KEY_QUANTITY, model.getQuantity());
        values.put(CartModel.KEY_SELLING_PRICE, model.getSellingPrice());
        values.put(CartModel.KEY_AMOUNT, model.getAmount());
        values.put(CartModel.KEY_DISCOUNT, model.getDiscount());

        // updating row
        return db.update(CartModel.TABLE_NAME, values, CartModel.KEY_ID + "=?",
                new String[] { String.valueOf(model.getId()) });
    }

    //Delete Item
    public void deleteItem(CartModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CartModel.TABLE_NAME, CartModel.KEY_ID + "=?",
                new String[] { String.valueOf(model.getId()) });
        db.close();
    }


    public void deleteAllItem()
    {
        SQLiteDatabase db = this.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        db.delete(Cart.TABLE_KERANJANG, null, null);
    }
}
