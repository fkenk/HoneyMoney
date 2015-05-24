package me.arthurveslo.honeymoney;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import me.arthurveslo.honeymoney.data.FinanceContract;
import me.arthurveslo.honeymoney.data.FinanceDbHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * contain main window of application
 */
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

    /**
     * used to get database object
     * @return object of SQLiteDatabase
     */
    public static SQLiteDatabase getDb() {
        return db;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements View.OnClickListener {
        public static final String LOG_TAG = PlaceholderFragment.class.getSimpleName();
        private Button btnAdd, btnTakeOff, btnChange;
        private Double balance = 0.0;
        static String un;
        private static HorizontalBarChart chart;


        /**
         * constructo for create fragment
         */
        public PlaceholderFragment() {
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            chart = (HorizontalBarChart) rootView.findViewById(R.id.chart1);

            Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
            ((ActionBarActivity) getActivity()).setSupportActionBar(toolbar);
            ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            new Drawer()
                    .withActivity(getActivity())
                    .withToolbar(toolbar)
                    .withActionBarDrawerToggle(true)
                    .withHeader(R.layout.drawer_header)
                    .addDrawerItems(
                            new SectionDrawerItem().withName("SORT"),
                            new PrimaryDrawerItem().withName("Months"),
                            new PrimaryDrawerItem().withName("Days")
                    )
                    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                            if (position == 2)
                                setMonthBarEntryIncomes();
                            if (position == 3)
                                setDayBarEntryIncomes();
                        }
                    })
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
            if (Double.valueOf((String) balanceText.getText()) >= 0) {
                balanceText.setTextColor(getResources().getColor(R.color.balance_green));
            } else {
                balanceText.setTextColor(getResources().getColor(R.color.balance_red));
            }
            un = getActivity().getIntent().getStringExtra("name");
            TextView personText = (TextView) rootView.findViewById(R.id.personText);
            /*Need global var*/
            personText.setText("Hi " + "Arthur Veslo");


            setMonthBarEntryIncomes();


            return rootView;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add:
                    Intent intent1 = new Intent(getActivity(), AddFinance.class);
                    intent1.putExtra("num", "1");
                    startActivity(intent1);
                    break;

                case R.id.takeOff:
                    Intent intent2 = new Intent(getActivity(), AddFinance.class);
                    intent2.putExtra("num", "2");
                    startActivity(intent2);
                    break;
                case R.id.change:
                    startActivity(new Intent(getActivity(), Login.class).putExtra("num", "1"));
                    getActivity().finish();
                    break;
            }

        }

        /**
         * take values of icomes and costs from database
         */
        private void calculateBalance() {
            Cursor cursor = db.query(FinanceContract.IncomeEntry.TABLE_NAME, new String[]{FinanceContract.IncomeEntry._ID,
                            FinanceContract.IncomeEntry.COLUMN_INCOME, FinanceContract.IncomeEntry.COLUMN_CATEGORY, FinanceContract.IncomeEntry.COLUMN_DATE}, null,
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
                            FinanceContract.CostsEntry.COLUMN_COSTS, FinanceContract.CostsEntry.COLUMN_CATEGORY, FinanceContract.CostsEntry.COLUMN_DATE}, null,
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

        /**
         * set
         * @return
         */
        private BarData setMonthBarEntryIncomes() {
        /*Icnomes Select*/
            ArrayList<BarEntry> entriesIncomes = new ArrayList<>();
            HashMap<Integer, Float> sumForMonthIncomes = new HashMap();
            Cursor cursor = db.query(FinanceContract.IncomeEntry.TABLE_NAME, new String[]{FinanceContract.IncomeEntry._ID,
                            FinanceContract.IncomeEntry.COLUMN_INCOME, FinanceContract.IncomeEntry.COLUMN_CATEGORY, FinanceContract.IncomeEntry.COLUMN_DATE}, null,
                    null,
                    null,
                    null,
                    null
            );
            while (cursor.moveToNext()) {
                float income = cursor.getFloat(cursor
                        .getColumnIndex(FinanceContract.IncomeEntry.COLUMN_INCOME));
                String date = cursor.getString(cursor
                        .getColumnIndex(FinanceContract.IncomeEntry.COLUMN_DATE));
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    java.util.Date date2 = format.parse(date);
                    SimpleDateFormat df = new SimpleDateFormat("MM");
                    Log.d(LOG_TAG, df.format(date2));
                    float value = (float) 0.0;
                    if (sumForMonthIncomes.get(Integer.valueOf(df.format(date2))) != null) {
                        value = sumForMonthIncomes.get(Integer.valueOf(df.format(date2)));
                    }
                    sumForMonthIncomes.put(Integer.valueOf(df.format(date2)), value + income);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            cursor.close();
            for (int i = 0; i < 12; i++) {
                entriesIncomes.add(new BarEntry(0f, i));
            }
            for (Map.Entry<Integer, Float> integerFloatEntry : sumForMonthIncomes.entrySet()) {
                entriesIncomes.set(integerFloatEntry.getKey() - 1, new BarEntry(integerFloatEntry.getValue(), integerFloatEntry.getKey() - 1));
            }
        /*Costs Select*/
            ArrayList<BarEntry> entriesCosts = new ArrayList<>();
            HashMap<Integer, Float> sumForMonthCosts = new HashMap();
            cursor = db.query(FinanceContract.CostsEntry.TABLE_NAME, new String[]{FinanceContract.CostsEntry._ID,
                            FinanceContract.CostsEntry.COLUMN_COSTS, FinanceContract.CostsEntry.COLUMN_CATEGORY, FinanceContract.CostsEntry.COLUMN_DATE}, null,
                    null,
                    null,
                    null,
                    null
            );
            while (cursor.moveToNext()) {
                float costs = cursor.getFloat(cursor
                        .getColumnIndex(FinanceContract.CostsEntry.COLUMN_COSTS));
                String date = cursor.getString(cursor
                        .getColumnIndex(FinanceContract.CostsEntry.COLUMN_DATE));
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    java.util.Date date2 = format.parse(date);
                    SimpleDateFormat df = new SimpleDateFormat("MM");
                    Log.d(LOG_TAG, df.format(date2));
                    float value = (float) 0.0;
                    if (sumForMonthCosts.get(Integer.valueOf(df.format(date2))) != null) {
                        value = sumForMonthCosts.get(Integer.valueOf(df.format(date2)));
                    }
                    sumForMonthCosts.put(Integer.valueOf(df.format(date2)), value + costs);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            cursor.close();

            for (int i = 0; i < 12; i++) {
                entriesCosts.add(new BarEntry(0f, i));
            }
            for (Map.Entry<Integer, Float> integerFloatEntry : sumForMonthCosts.entrySet()) {
                entriesCosts.set(integerFloatEntry.getKey() - 1, new BarEntry(integerFloatEntry.getValue(), integerFloatEntry.getKey() - 1));

            }

            Log.e(LOG_TAG, "Sum for monh costs " + sumForMonthCosts);
            Log.e(LOG_TAG, "Sum for monh incomes " + sumForMonthIncomes);
            Log.e(LOG_TAG, "Entry incomes " + entriesIncomes);
            Log.e(LOG_TAG, "Entry costs " + entriesCosts);
            BarDataSet datasetIncomes = new BarDataSet(entriesIncomes, "Incomes");
            datasetIncomes.setColor(Color.rgb(104, 241, 175));
            BarDataSet datasetCosts = new BarDataSet(entriesCosts, "Costs");
            datasetCosts.setColor(Color.rgb(196, 19, 36));
            ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
            dataSets.add(datasetCosts);
            dataSets.add(datasetIncomes);


            ArrayList<String> labels = new ArrayList<String>();
            labels.add("January");
            labels.add("February");
            labels.add("March");
            labels.add("April");
            labels.add("May");
            labels.add("June");
            labels.add("July");
            labels.add("August");
            labels.add("September");
            labels.add("October");
            labels.add("November");
            labels.add("December");
            BarData barData = new BarData(labels, dataSets);
            chart.setDescription("For current year");
            chart.animateY(2000);
            chart.setData(barData);
            chart.setWillNotDraw(false);
            chart.invalidate();
            return barData;
        }

        private BarData setDayBarEntryIncomes() {
            /*Income Select*/
            ArrayList<BarEntry> entriesIncome = new ArrayList<>();
            HashMap<Integer, Float> sumForDaysIncomes = new HashMap();
            java.util.Date currentDate = new java.util.Date();

            SimpleDateFormat mf = new SimpleDateFormat("MM");
            SimpleDateFormat yf = new SimpleDateFormat("yyyy");
            Calendar mycal = new GregorianCalendar(Integer.parseInt(yf.format(currentDate)), Integer.parseInt(mf.format(currentDate)) - 1, 1);
            int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
            Cursor cursor = db.query(FinanceContract.IncomeEntry.TABLE_NAME, new String[]{FinanceContract.IncomeEntry._ID,
                            FinanceContract.IncomeEntry.COLUMN_INCOME, FinanceContract.IncomeEntry.COLUMN_CATEGORY, FinanceContract.IncomeEntry.COLUMN_DATE}, null,
                    null,
                    null,
                    null,
                    null
            );
            while (cursor.moveToNext()) {
                float income = cursor.getFloat(cursor
                        .getColumnIndex(FinanceContract.IncomeEntry.COLUMN_INCOME));
                String date = cursor.getString(cursor
                        .getColumnIndex(FinanceContract.IncomeEntry.COLUMN_DATE));
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    java.util.Date date2 = format.parse(date);
                    if (mf.format(date2).equals(mf.format(currentDate))) {
                        SimpleDateFormat df = new SimpleDateFormat("dd");
                        Log.d(LOG_TAG, df.format(date2));
                        float value = (float) 0.0;
                        if (sumForDaysIncomes.get(Integer.valueOf(df.format(date2))) != null) {
                            value = sumForDaysIncomes.get(Integer.valueOf(df.format(date2)));
                        }
                        sumForDaysIncomes.put(Integer.valueOf(df.format(date2)), value + income);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            cursor.close();
            for (int i = 0; i < daysInMonth; i++) {
                entriesIncome.add(new BarEntry(0f, i));
            }
            for (Map.Entry<Integer, Float> integerFloatEntry : sumForDaysIncomes.entrySet()) {
                entriesIncome.set(integerFloatEntry.getKey() - 1, new BarEntry(integerFloatEntry.getValue(), integerFloatEntry.getKey() - 1));

            }
             /*Costs Select*/
            ArrayList<BarEntry> entriesCosts = new ArrayList<>();
            HashMap<Integer, Float> sumForDaysCosts = new HashMap();
            cursor = db.query(FinanceContract.CostsEntry.TABLE_NAME, new String[]{FinanceContract.CostsEntry._ID,
                            FinanceContract.CostsEntry.COLUMN_COSTS, FinanceContract.CostsEntry.COLUMN_CATEGORY, FinanceContract.CostsEntry.COLUMN_DATE}, null,
                    null,
                    null,
                    null,
                    null
            );
            while (cursor.moveToNext()) {
                float costs = cursor.getFloat(cursor
                        .getColumnIndex(FinanceContract.CostsEntry.COLUMN_COSTS));
                String date = cursor.getString(cursor
                        .getColumnIndex(FinanceContract.CostsEntry.COLUMN_DATE));
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    java.util.Date date2 = format.parse(date);
                    if (mf.format(date2).equals(mf.format(currentDate))) {
                        SimpleDateFormat df = new SimpleDateFormat("dd");
                        Log.d(LOG_TAG, df.format(date2));
                        float value = (float) 0.0;
                        if (sumForDaysCosts.get(Integer.valueOf(df.format(date2))) != null) {
                            value = sumForDaysCosts.get(Integer.valueOf(df.format(date2)));
                        }
                        sumForDaysCosts.put(Integer.valueOf(df.format(date2)), value + costs);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            cursor.close();
            for (int i = 0; i < daysInMonth; i++) {
                entriesCosts.add(new BarEntry(0f, i));
            }
            for (Map.Entry<Integer, Float> integerFloatEntry : sumForDaysCosts.entrySet()) {
                entriesCosts.set(integerFloatEntry.getKey() - 1, new BarEntry(integerFloatEntry.getValue(), integerFloatEntry.getKey() - 1));

            }

            BarDataSet datasetIncomes = new BarDataSet(entriesIncome, "Incomes");
            datasetIncomes.setColor(Color.rgb(104, 241, 175));
            BarDataSet datasetCosts = new BarDataSet(entriesCosts, "Costs");
            datasetCosts.setColor(Color.rgb(196, 19, 36));
            ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
            dataSets.add(datasetIncomes);
            dataSets.add(datasetCosts);

            ArrayList<String> labels = new ArrayList<String>();


            Log.v(LOG_TAG, "" + mycal.getTime());
            Log.v(LOG_TAG, "" + daysInMonth);
            for (int i = 1; i <= daysInMonth; i++) {
                labels.add("" + i);
            }

            BarData barData = new BarData(labels, dataSets);
            chart.setDescription("For current month");
            chart.animateY(2000);
            chart.setScaleMinima(1f, 1.5f);
            chart.setData(barData);
            chart.setWillNotDraw(false);
            chart.invalidate();
            return barData;
        }
    }
}
