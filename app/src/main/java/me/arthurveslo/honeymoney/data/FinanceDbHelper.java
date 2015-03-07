package me.arthurveslo.honeymoney.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Arthur on 07.03.2015.
 */
public class FinanceDbHelper extends SQLiteOpenHelper {
    public final static int DATABASE_VERSION = 1;
    public final static String DATABASE_NAME = "finance.db";
    public static final String LOG_TAG = FinanceDbHelper.class.getSimpleName();
    public FinanceDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_INCOME_TABLE = "CREATE TABLE "+ FinanceContract.IncomeEntry.TABLE_NAME +" (\n" +
                "\t"+FinanceContract.IncomeEntry._ID+"\tINTEGER,\n" +
                "\t"+FinanceContract.IncomeEntry.COLUMN_INCOME+"\tREAL NOT NULL,\n" +
                "\t"+FinanceContract.IncomeEntry.COLUMN_CATEGORY+"\tINTEGER NOT NULL,\n" +
                "\t"+FinanceContract.IncomeEntry.COLUMN_DATE+"\tTEXT NOT NULL,\n" +
                "\tPRIMARY KEY("+FinanceContract.IncomeEntry._ID+"),\n" +
                "\tFOREIGN KEY("+FinanceContract.IncomeEntry.COLUMN_CATEGORY+") " +
                "REFERENCES \""+FinanceContract.CategoryIncomeEntry.TABLE_NAME+"\" ( \""+FinanceContract.CategoryIncomeEntry._ID+"\" )\n" +
                ");";
        final String SQL_CREATE_COSTS_TABLE = "CREATE TABLE "+ FinanceContract.CostsEntry.TABLE_NAME +" (\n" +
                "\t"+FinanceContract.CostsEntry._ID+"\tINTEGER,\n" +
                "\t"+FinanceContract.CostsEntry.COLUMN_COSTS+"\tREAL NOT NULL,\n" +
                "\t"+FinanceContract.CostsEntry.COLUMN_CATEGORY+"\tINTEGER NOT NULL,\n" +
                "\t"+FinanceContract.CostsEntry.COLUMN_DATE+"\tTEXT NOT NULL,\n" +
                "\tPRIMARY KEY("+FinanceContract.CostsEntry._ID+"),\n" +
                "\tFOREIGN KEY("+FinanceContract.CostsEntry.COLUMN_CATEGORY+") " +
                "REFERENCES \""+FinanceContract.CategoryCostsEntry.TABLE_NAME+"\" ( \""+FinanceContract.CategoryCostsEntry._ID+"\" )\n" +
                ");";

        final String SQL_CREATE_CATEGORY_COSTS_TABLE = "CREATE TABLE "+FinanceContract.CategoryCostsEntry.TABLE_NAME+" (\n" +
                    "\t"+FinanceContract.CategoryCostsEntry._ID+"\tINTEGER,\n" +
                "\t"+FinanceContract.CategoryCostsEntry.COLUMN_NAME+"\tTEXT,\n" +
                "\tPRIMARY KEY("+FinanceContract.CategoryCostsEntry._ID+")\n" +
                ");";
        final String SQL_CREATE_CATEGORY_INCOME_TABLE = "CREATE TABLE "+FinanceContract.CategoryIncomeEntry.TABLE_NAME+" (\n" +
                "\t"+FinanceContract.CategoryIncomeEntry._ID+"\tINTEGER,\n" +
                "\t"+FinanceContract.CategoryIncomeEntry.COLUMN_NAME+"\tTEXT,\n" +
                "\tPRIMARY KEY("+FinanceContract.CategoryIncomeEntry._ID+")\n" +
                ");";
        db.execSQL(SQL_CREATE_CATEGORY_COSTS_TABLE);
        db.execSQL(SQL_CREATE_CATEGORY_INCOME_TABLE);
        db.execSQL(SQL_CREATE_COSTS_TABLE);
        db.execSQL(SQL_CREATE_INCOME_TABLE);
        //Log.v(LOG_TAG,"On Create method");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FinanceContract.IncomeEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FinanceContract.CostsEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FinanceContract.CategoryIncomeEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FinanceContract.CategoryCostsEntry.TABLE_NAME);
        onCreate(db);
    }
}
