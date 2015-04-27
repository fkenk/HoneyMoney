package me.arthurveslo.honeymoney;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.TextView;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;

import me.arthurveslo.honeymoney.data.FinanceContract;
import me.arthurveslo.honeymoney.data.FinanceDbHelper;

public class MainActivity extends ActionBarActivity {
    FinanceDbHelper financeDbHelper = new FinanceDbHelper(this);
    static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        financeDbHelper = new FinanceDbHelper(this);
        db = financeDbHelper.getWritableDatabase();
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public static SQLiteDatabase getDb() {
        return db;
    }
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements View.OnClickListener {
        public static final String LOG_TAG = PlaceholderFragment.class.getSimpleName();
        Button btnAdd, btnTakeOff, btnChange;
        Double balance = 0.0;
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
            ((ActionBarActivity)getActivity()).setSupportActionBar(toolbar);
            ((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            new Drawer()
                    .withActivity(getActivity())
                    .withToolbar(toolbar)
                    .withActionBarDrawerToggle(true)
                    .withHeader(R.layout.drawer_header)
                    .addDrawerItems(
                            new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home).withBadge("99").withIdentifier(1),
                            new PrimaryDrawerItem().withName(R.string.drawer_item_free_play).withIcon(FontAwesome.Icon.faw_gamepad),
                            new PrimaryDrawerItem().withName(R.string.drawer_item_custom).withIcon(FontAwesome.Icon.faw_eye).withBadge("6").withIdentifier(2),
                            new SectionDrawerItem().withName(R.string.drawer_item_settings),
                            new SecondaryDrawerItem().withName(R.string.drawer_item_help).withIcon(FontAwesome.Icon.faw_cog),
                            new SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesome.Icon.faw_question).setEnabled(false),
                            new DividerDrawerItem(),
                            new SecondaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(FontAwesome.Icon.faw_github).withBadge("12+").withIdentifier(1)
                    )
                    .build();
            btnAdd = (Button) rootView.findViewById(R.id.add);
            btnAdd.setOnClickListener(this);
            btnTakeOff = (Button) rootView.findViewById(R.id.takeOff);
            btnTakeOff.setOnClickListener(this);
            btnChange = (Button) rootView.findViewById(R.id.change);
            btnChange.setOnClickListener(this);

            calculateBalance();
            TextView balanceText = (TextView) rootView.findViewById(R.id.balance);
            balanceText.setText(String.valueOf(balance));
            if(Double.valueOf((String) balanceText.getText()) >= 0) {
                balanceText.setTextColor(getResources().getColor(R.color.balance_green));
            } else {
                balanceText.setTextColor(getResources().getColor(R.color.balance_red));
            }
            String un  = getActivity().getIntent().getStringExtra("name");
            TextView personText = (TextView) rootView.findViewById(R.id.personText);
            personText.setText("Hi "+un);
            return rootView;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add :
                    Intent intent1 = new Intent(getActivity(),AddFinance.class);
                    intent1.putExtra("num", "1");
                    startActivity(intent1);
                    break;

                case R.id.takeOff :
                    Intent intent2 = new Intent(getActivity(),AddFinance.class);
                    intent2.putExtra("num", "2");
                    startActivity(intent2);
                    break;
                case R.id.change :
                    startActivity(new Intent(getActivity(), Login.class).putExtra("num", "1"));
                    getActivity().finish();
                    break;
            }

        }
        private void calculateBalance() {
            Cursor cursor = db.query(FinanceContract.IncomeEntry.TABLE_NAME, new String[]{FinanceContract.IncomeEntry._ID,
                            FinanceContract.IncomeEntry.COLUMN_INCOME,FinanceContract.IncomeEntry.COLUMN_CATEGORY, FinanceContract.IncomeEntry.COLUMN_DATE}, null,
                    null,
                    null,
                    null,
                    null
            );
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(FinanceContract.IncomeEntry._ID));
                double income = cursor.getDouble(cursor
                        .getColumnIndex(FinanceContract.IncomeEntry.COLUMN_INCOME));
                int category = cursor.getInt(cursor.getColumnIndex(FinanceContract.IncomeEntry.COLUMN_CATEGORY));
                String date = cursor.getString(cursor
                        .getColumnIndex(FinanceContract.IncomeEntry.COLUMN_DATE));
                balance += income;
            }
            cursor.close();
            cursor = db.query(FinanceContract.CostsEntry.TABLE_NAME, new String[]{FinanceContract.CostsEntry._ID,
                            FinanceContract.CostsEntry.COLUMN_COSTS,FinanceContract.CostsEntry.COLUMN_CATEGORY, FinanceContract.CostsEntry.COLUMN_DATE}, null,
                    null,
                    null,
                    null,
                    null
            );
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(FinanceContract.CostsEntry._ID));
                double costs = cursor.getDouble(cursor
                        .getColumnIndex(FinanceContract.CostsEntry.COLUMN_COSTS));
                int category = cursor.getInt(cursor.getColumnIndex(FinanceContract.CostsEntry.COLUMN_CATEGORY));
                String date = cursor.getString(cursor
                        .getColumnIndex(FinanceContract.CostsEntry.COLUMN_DATE));
                balance -= costs;
            }
            cursor.close();
        }
    }
}
