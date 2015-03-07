package me.arthurveslo.honeymoney.data;

import android.provider.BaseColumns;

/**
 * Created by Arthur on 07.03.2015.
 */
public class FinanceContract {
    public static final String CONTENT_AUTHORITY = "me.arthurveslo.sunshine.data";

    public static final class IncomeEntry implements BaseColumns {
        public static final  String TABLE_NAME = "incomes";

        public static final String COLUMN_INCOME = "income";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_DATE = "date";

    }
    public static final class CostsEntry implements BaseColumns {
        public static final  String TABLE_NAME = "costs";

        public static final String COLUMN_COSTS = "costs";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_DATE = "date";
    }

    public static final class CategoryCostsEntry implements BaseColumns {
        public static final String TABLE_NAME = "category_costs";

        public static final String COLUMN_NAME = "name";

    }

    public static final class CategoryIncomeEntry implements BaseColumns {
        public static final String TABLE_NAME = "category_income";

        public static final String COLUMN_NAME = "name";
    }

}
