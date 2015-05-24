package me.arthurveslo.honeymoney.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import me.arthurveslo.honeymoney.R;

/**
 * used to to create data base and update it
 */
public class FinanceDbHelper extends SQLiteOpenHelper {
    public final static int DATABASE_VERSION = 8;
    public final static String DATABASE_NAME = "finance.db";
    public static final String LOG_TAG = FinanceDbHelper.class.getSimpleName();
    private Context context;

    /**
     * constructor
     * @param context of activity
     */
    public FinanceDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
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
        /**INCOMES STANDARD**/
        ContentValues contentValues = new ContentValues();
        contentValues.put(FinanceContract.CategoryIncomeEntry._ID, 1);
        contentValues.put(FinanceContract.CategoryIncomeEntry.COLUMN_NAME, context.getResources().getString(R.string.deposits));
        db.insert(FinanceContract.CategoryIncomeEntry.TABLE_NAME, null, contentValues);
        contentValues.clear();
        contentValues.put(FinanceContract.CategoryIncomeEntry._ID, 2);
        contentValues.put(FinanceContract.CategoryIncomeEntry.COLUMN_NAME, context.getResources().getString(R.string.salary));
        db.insert(FinanceContract.CategoryIncomeEntry.TABLE_NAME, null, contentValues);
        contentValues.clear();
        contentValues.put(FinanceContract.CategoryIncomeEntry._ID, 3);
        contentValues.put(FinanceContract.CategoryIncomeEntry.COLUMN_NAME, context.getResources().getString(R.string.savings));
        db.insert(FinanceContract.CategoryIncomeEntry.TABLE_NAME, null, contentValues);
        contentValues.clear();
        /**COSTS STANDARD**/
        contentValues.put(FinanceContract.CategoryCostsEntry._ID, 1);
        contentValues.put(FinanceContract.CategoryCostsEntry.COLUMN_NAME, context.getResources().getString(R.string.hygiene));
        db.insert(FinanceContract.CategoryCostsEntry.TABLE_NAME, null, contentValues);
        contentValues.clear();
        contentValues.put(FinanceContract.CategoryCostsEntry._ID, 2);
        contentValues.put(FinanceContract.CategoryCostsEntry.COLUMN_NAME, context.getResources().getString(R.string.food));
        db.insert(FinanceContract.CategoryCostsEntry.TABLE_NAME, null, contentValues);
        contentValues.clear();
        contentValues.put(FinanceContract.CategoryCostsEntry._ID, 3);
        contentValues.put(FinanceContract.CategoryCostsEntry.COLUMN_NAME, context.getResources().getString(R.string.home));
        db.insert(FinanceContract.CategoryCostsEntry.TABLE_NAME, null, contentValues);
        contentValues.clear();
        contentValues.put(FinanceContract.CategoryCostsEntry._ID, 4);
        contentValues.put(FinanceContract.CategoryCostsEntry.COLUMN_NAME, context.getResources().getString(R.string.health));
        db.insert(FinanceContract.CategoryCostsEntry.TABLE_NAME, null, contentValues);
        contentValues.clear();
        contentValues.put(FinanceContract.CategoryCostsEntry._ID, 5);
        contentValues.put(FinanceContract.CategoryCostsEntry.COLUMN_NAME, context.getResources().getString(R.string.cafe));
        db.insert(FinanceContract.CategoryCostsEntry.TABLE_NAME, null, contentValues);
        contentValues.clear();
        contentValues.put(FinanceContract.CategoryCostsEntry._ID, 6);
        contentValues.put(FinanceContract.CategoryCostsEntry.COLUMN_NAME, context.getResources().getString(R.string.car));
        db.insert(FinanceContract.CategoryCostsEntry.TABLE_NAME, null, contentValues);
        contentValues.clear();
        contentValues.put(FinanceContract.CategoryCostsEntry._ID, 7);
        contentValues.put(FinanceContract.CategoryCostsEntry.COLUMN_NAME, context.getResources().getString(R.string.clothes));
        db.insert(FinanceContract.CategoryCostsEntry.TABLE_NAME, null, contentValues);
        contentValues.clear();
        contentValues.put(FinanceContract.CategoryCostsEntry._ID, 8);
        contentValues.put(FinanceContract.CategoryCostsEntry.COLUMN_NAME, context.getResources().getString(R.string.connection));
        db.insert(FinanceContract.CategoryCostsEntry.TABLE_NAME, null, contentValues);
        contentValues.clear();
        //Log.v(LOG_TAG, String.valueOf(R.string.deposits));
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
