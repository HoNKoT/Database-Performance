package jp.honkot.checkdbperformance.pure;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "pure_android.db";

    public OpenHelper(Context con) {
        super(con, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createEventTable(db);
        createProductTable(db);
    }

    private void createEventTable(SQLiteDatabase db) {
        // make table
        db.execSQL("CREATE TABLE " + Event.Const.TABLE_NAME + " ("
                + Event.Const.COLUMN_ID + " integer primary key autoincrement,"
                + Event.Const.COLUMN_QUANTITY + " integer,"
                + Event.Const.COLUMN_ACTION + " text,"
                + Event.Const.COLUMN_STATUS + " integer,"
                + Event.Const.COLUMN_C1 + " integer,"
                + Event.Const.COLUMN_C2 + " integer,"
                + Event.Const.COLUMN_C3 + " integer,"
                + Event.Const.COLUMN_C4 + " integer,"
                + Event.Const.COLUMN_C5 + " integer,"
                + Event.Const.COLUMN_C6 + " integer,"
                + Event.Const.COLUMN_C7 + " integer,"
                + Event.Const.COLUMN_C8 + " integer,"
                + Event.Const.COLUMN_C9 + " integer,"
                + Event.Const.COLUMN_C10 + " integer,"
                + Event.Const.COLUMN_PRODUCT + " integer,"
                + " FOREIGN KEY (" + Event.Const.COLUMN_PRODUCT
                    + ") REFERENCES " + Product.Const.TABLE_NAME + "(" + Product.Const.COLUMN_ID + "))");
    }

    private void createProductTable(SQLiteDatabase db) {
        // make table
        db.execSQL("CREATE TABLE " + Product.Const.TABLE_NAME + " ("
                + Product.Const.COLUMN_ID + " integer primary key autoincrement,"
                + Product.Const.COLUMN_NAME + " text,"
                + Product.Const.COLUMN_PRICE + " integer)");
    }

    public void recreateEventTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE " + Event.Const.TABLE_NAME);
        createEventTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Write migration by your self.
    }
}
