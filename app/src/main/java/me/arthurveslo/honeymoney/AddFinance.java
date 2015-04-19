package me.arthurveslo.honeymoney;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import me.arthurveslo.honeymoney.data.FinanceContract;


public class AddFinance extends ActionBarActivity {
    public static final String LOG_TAG = AddFinance.class.getSimpleName();
    static SQLiteDatabase db;
    ArrayList<String> names = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = MainActivity.getDb();
        String num = getIntent().getStringExtra("num");
        Log.v(LOG_TAG, num);
        setContentView(R.layout.activity_add_finance);
        if(num.equals("1")){
            getIncomes();
        } else if(num.equals("2")) {
            getCosts();
        }
        ListView list = (ListView) findViewById(R.id.listView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setPressed(true);
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

    private void getIncomes() {
        ListView listView = (ListView) findViewById(R.id.listView);

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

    public void buttonOnClick(View v) {
        Button button = (Button) v;
        EditText editText = (EditText) findViewById(R.id.editText);

        String value = String.valueOf(button.getText().charAt(0));
        editText.setText(editText.getText() + value);

    }
}
