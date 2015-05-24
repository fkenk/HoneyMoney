package me.arthurveslo.honeymoney;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.arthurveslo.honeymoney.data.FinanceContract;

/**
 * activity used to add finace activity
 */
public class AddFinance extends ActionBarActivity {
    public static final String LOG_TAG = AddFinance.class.getSimpleName();
    static SQLiteDatabase db;
    ArrayList<String> names = new ArrayList<String>();
    String selectedFromList;
    String num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = MainActivity.getDb();
        num = getIntent().getStringExtra("num");
        Log.v(LOG_TAG, num);
        setContentView(R.layout.activity_add_finance);
        if(num.equals("1")){
            getIncomes();
            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText("Add Incomes");
        } else if(num.equals("2")) {
            getCosts();
            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText("Add Costs");
        }
        final ListView list = (ListView) findViewById(R.id.listView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setPressed(true);
                selectedFromList =(String) (list.getItemAtPosition(position));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_finance, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * get incomes to the database and set adaptep to the listview
     */
    private void getIncomes() {
        final ListView listView = (ListView) findViewById(R.id.listView);

        Cursor cursor = db.query(FinanceContract.CategoryIncomeEntry.TABLE_NAME, new String[]{FinanceContract.CategoryIncomeEntry._ID,
                        FinanceContract.CategoryIncomeEntry.COLUMN_NAME}, null,
                null,
                null,
                null,
                null
        );
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(FinanceContract.CategoryIncomeEntry._ID));
            String name = cursor.getString(cursor
                    .getColumnIndex(FinanceContract.CategoryIncomeEntry.COLUMN_NAME));
            names.add(name);
        }
        cursor.close();
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.my_list_item, names);
        listView.setAdapter(adapter);
    }

    /**
     * get costs to the database and set adaptep to the listview
     */
    public void getCosts() {
        ListView listView = (ListView) findViewById(R.id.listView);

        Cursor cursor = db.query(FinanceContract.CategoryCostsEntry.TABLE_NAME, new String[]{FinanceContract.CategoryCostsEntry._ID,
                        FinanceContract.CategoryCostsEntry.COLUMN_NAME}, null,
                null,
                null,
                null,
                null
        );
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(FinanceContract.CategoryCostsEntry._ID));
            String name = cursor.getString(cursor
                    .getColumnIndex(FinanceContract.CategoryCostsEntry.COLUMN_NAME));
            names.add(name);
        }
        cursor.close();
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.my_list_item, names);
        listView.setAdapter(adapter);

    }

    /**
     * click on numeric button
     * @param v view
     */
    public void buttonOnClick(View v) {
        Button button = (Button) v;
        EditText editText = (EditText) findViewById(R.id.editText);
        String value = String.valueOf(button.getText().charAt(0));
        Pattern p = Pattern.compile("[0-9]*\\.[0-9]*");
        Matcher m = p.matcher(editText.getText().toString());
        boolean b = m.matches();
        if(b && value.equals(".")) {
        } else {
            editText.setText(editText.getText() + value);
            editText.setSelection(editText.getText().length());
        }
    }

    /**
     * click on GO button
     * @param v view
     */
    public void buttonGOClick(View v) {
        if(num.equals("1")) {
            int id = 0;
            EditText editText = (EditText) findViewById(R.id.editText);
            Cursor cursor = db.query(FinanceContract.CategoryIncomeEntry.TABLE_NAME, new String[]{FinanceContract.CategoryIncomeEntry._ID,
                            FinanceContract.CategoryIncomeEntry.COLUMN_NAME}, null,
                    null,
                    null,
                    null,
                    null
            );
            while (cursor.moveToNext()) {
                id = cursor.getInt(cursor.getColumnIndex(FinanceContract.CategoryIncomeEntry._ID));
                String name = cursor.getString(cursor
                        .getColumnIndex(FinanceContract.CategoryIncomeEntry.COLUMN_NAME));
                if (name.equals(selectedFromList)) break;
            }
            cursor.close();
            Log.v(LOG_TAG, "Edit Text " + editText.getText().toString());
            Log.v(LOG_TAG, "id " + id);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String currentDateandTime = sdf.format(new Date());
            Log.v(LOG_TAG, "data " + currentDateandTime);
            ContentValues values = new ContentValues();
            values.put(FinanceContract.IncomeEntry.COLUMN_INCOME, Double.valueOf(editText.getText().toString()));
            values.put(FinanceContract.IncomeEntry.COLUMN_CATEGORY, id);
            values.put(FinanceContract.IncomeEntry.COLUMN_DATE, currentDateandTime);

            db.insert(FinanceContract.IncomeEntry.TABLE_NAME, null, values);

        }
        if(num.equals("2")) {
            int id = 0;
            EditText editText = (EditText) findViewById(R.id.editText);
            Cursor cursor = db.query(FinanceContract.CategoryCostsEntry.TABLE_NAME, new String[]{FinanceContract.CategoryCostsEntry._ID,
                            FinanceContract.CategoryCostsEntry.COLUMN_NAME}, null,
                    null,
                    null,
                    null,
                    null
            );
            while (cursor.moveToNext()) {
                id = cursor.getInt(cursor.getColumnIndex(FinanceContract.CategoryCostsEntry._ID));
                String name = cursor.getString(cursor
                        .getColumnIndex(FinanceContract.CategoryCostsEntry.COLUMN_NAME));
                if (name.equals(selectedFromList)) break;
            }
            cursor.close();
            Log.v(LOG_TAG, "Edit Text " + editText.getText().toString());
            Log.v(LOG_TAG, "id " + id);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String currentDateandTime = sdf.format(new Date());
            Log.v(LOG_TAG, "data " + currentDateandTime);
            ContentValues values = new ContentValues();
            values.put(FinanceContract.CostsEntry.COLUMN_COSTS, Double.valueOf(editText.getText().toString()));
            values.put(FinanceContract.CostsEntry.COLUMN_CATEGORY, id);
            values.put(FinanceContract.CostsEntry.COLUMN_DATE, currentDateandTime);

            db.insert(FinanceContract.CostsEntry.TABLE_NAME, null, values);
        }
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
