package co.crowde.toni.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.model.CartModel;
import co.crowde.toni.model.CategoryModel;
import co.crowde.toni.model.HistoryCategoryModel;

public class HistoryCategory extends SQLiteOpenHelper {
    private HistoryCategoryModel model;

    // static variable
    private static final int DATABASE_VERSION = 1;

    // Database name
    private static final String DATABASE_NAME = "dbToni";

    // table name
    private static final String TABLE_CATEGORY = "tbCategory";

    public HistoryCategory(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CartModel.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ HistoryCategoryModel.TABLE_NAME);
        onCreate(db);
    }

    //Create Data
    public long insertItem(HistoryCategoryModel model){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
//        values.put(KEY_ID, model.getId());
        values.put(HistoryCategoryModel.KEY_CATEGORY_ID, model.getCategoryId());
        values.put(HistoryCategoryModel.KEY_CATEGORY_NAME, model.getCategoryName());

        long id = db.insert(TABLE_CATEGORY, null, values);
        db.close();
        return id;
    }

    //Read Data
    public HistoryCategoryModel getItem(long id){
        model = new HistoryCategoryModel();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(CartModel.TABLE_NAME, new String[]{
                        HistoryCategoryModel.KEY_ID,
                        HistoryCategoryModel.KEY_CATEGORY_ID,
                        HistoryCategoryModel.KEY_CATEGORY_NAME},
                HistoryCategoryModel.KEY_ID+"=?",
                new String[]{ String.valueOf(id)}, null, null,null,null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                model = new HistoryCategoryModel(
                        cursor.getInt(cursor.getColumnIndex(HistoryCategoryModel.KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(HistoryCategoryModel.KEY_CATEGORY_ID)),
                        cursor.getString(cursor.getColumnIndex(HistoryCategoryModel.KEY_CATEGORY_NAME)));
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
    public HistoryCategoryModel getItem(String categoryId){

        HistoryCategoryModel models = new HistoryCategoryModel();

        //Select All Query
        String querySelect = "SELECT * FROM "
                + HistoryCategoryModel.TABLE_NAME
                + " WHERE categoryId='"
                + categoryId
                + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(querySelect, null);

        if(cursor.moveToFirst()){
            models.setId(cursor.getInt(cursor.getColumnIndex(HistoryCategoryModel.KEY_ID)));
            models.setCategoryId(cursor.getString(cursor.getColumnIndex(HistoryCategoryModel.KEY_CATEGORY_ID)));
            models.setCategoryName(cursor.getString(cursor.getColumnIndex(HistoryCategoryModel.KEY_CATEGORY_NAME)));
        }

        db.close();

        //return model
        return models;
    }

    //Get All Data
    public List<HistoryCategoryModel> getAllItem(){
        List<HistoryCategoryModel> modelList = new ArrayList<>();
        //Select All Query
        String querySelect = "SELECT * FROM "+ HistoryCategoryModel.TABLE_NAME+" ORDER BY "+HistoryCategoryModel.KEY_ID+" ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(querySelect, null);

        if(cursor.moveToFirst()){
            do{
                HistoryCategoryModel model = new HistoryCategoryModel();
                model.setId(cursor.getInt(cursor.getColumnIndex(HistoryCategoryModel.KEY_ID)));
                model.setCategoryId(cursor.getString(cursor.getColumnIndex(HistoryCategoryModel.KEY_CATEGORY_ID)));
                model.setCategoryName(cursor.getString(cursor.getColumnIndex(HistoryCategoryModel.KEY_CATEGORY_NAME)));

                modelList.add(model);
            } while (cursor.moveToNext());
        }

        db.close();

        //return modelList
        return modelList;
    }

    //Get Item Count
    public int getItemCount() {
        String queryCount = "SELECT * FROM " + HistoryCategoryModel.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryCount, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    //Update Data
    public int updateItem(HistoryCategoryModel model) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HistoryCategoryModel.KEY_CATEGORY_ID, model.getCategoryId());
        values.put(HistoryCategoryModel.KEY_CATEGORY_NAME, model.getCategoryName());

        // updating row
        return db.update(HistoryCategoryModel.TABLE_NAME, values, HistoryCategoryModel.KEY_ID + "=?",
                new String[] { String.valueOf(model.getId()) });
    }

    //Delete Item
    public void deleteItem(HistoryCategoryModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(HistoryCategoryModel.TABLE_NAME, HistoryCategoryModel.KEY_ID + "=?",
                new String[] { String.valueOf(model.getId()) });
        db.close();
    }


    public void deleteAllItem()
    {
        SQLiteDatabase db = this.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        db.delete(HistoryCategory.TABLE_CATEGORY, null, null);
    }
}
