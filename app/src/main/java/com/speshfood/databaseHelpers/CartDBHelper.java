package com.speshfood.databaseHelpers;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.speshfood.models.cartModel;
import com.speshfood.models.itemModel;

public class CartDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "cart.db";
    public static final String DATABASE_TABLE_NAME = "cart";
    public static final String COLUMN_ITEM_CODE = "code";
    public static final String COLUMN_ITEM_NAME = "name";
    public static final String COLUMN_ITEM_QTY = "qty";
    public static final String COLUMN_ITEM_PRICE = "price";
    public CartDBHelper(Context context) {
        super (context, DATABASE_NAME,null, 1);
    }

    @Override
    public
    void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table "+DATABASE_TABLE_NAME+
                        "(id integer primary key AUTOINCREMENT,"+COLUMN_ITEM_CODE+" text,"+COLUMN_ITEM_NAME+" text,"+COLUMN_ITEM_QTY+" text,"+COLUMN_ITEM_PRICE+" text)"
        );
    }

    @Override
    public
    void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE_NAME);
        onCreate(db);
    }
    public boolean addItemToCart (String code, String name, String qty, String price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ITEM_CODE, code);
        contentValues.put(COLUMN_ITEM_NAME, name);
        contentValues.put(COLUMN_ITEM_QTY, qty);
        contentValues.put(COLUMN_ITEM_PRICE, price);
        db.insert(DATABASE_TABLE_NAME, null, contentValues);
        return true;
    }
    public
    cartModel getData(String itemCode) {
        String selectQuery = "SELECT  * FROM "+DATABASE_TABLE_NAME+" where id="+ itemCode;
        SQLiteDatabase db = this.getReadableDatabase ();
        Cursor cursor = db.rawQuery (selectQuery, null);
        // String[] data      = null;
        cartModel itemModeldb = new cartModel ();
        if (cursor.moveToFirst ()) {
            do {
                itemModeldb.setItemName (cursor.getString (cursor.getColumnIndex (COLUMN_ITEM_NAME)));
                itemModeldb.setItemAmount (cursor.getString (cursor.getColumnIndex (COLUMN_ITEM_PRICE)));
                itemModeldb.setItemQty (cursor.getString (cursor.getColumnIndex (COLUMN_ITEM_QTY)));
            } while (cursor.moveToNext ());

            cursor.close ();

        }
        return itemModeldb;
    }
    public int getProfilesCount() {
        String countQuery = "SELECT  * FROM " + DATABASE_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

}

