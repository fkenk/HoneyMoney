package me.arthurveslo.honeymoney.data;

import android.provider.BaseColumns;

/**
 * used for building schema of database
 */
public class FinanceContract {

    public static final String CONTENT_AUTHORITY = "me.arthurveslo.sunshine.data";

    /**
     * schema for income entry
     */
    public static final class IncomeEntry implements BaseColumns {
        public static final  String TABLE_NAME = "incomes";

        public static final String COLUMN_INCOME = "income";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_DATE = "date";

    }

    /**
     * schema for costs entry
     */
    public static final class CostsEntry implements BaseColumns {
        public static final  String TABLE_NAME = "costs";

        public static final String COLUMN_COSTS = "costs";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_DATE = "date";
    }

    /**
     * schema for category costs entry
     */
    public static final class CategoryCostsEntry implements BaseColumns {
        public static final String TABLE_NAME = "category_costs";

        public static final String COLUMN_NAME = "name";

    }

    /**
     * schema for category income entry
     */
    public static final class CategoryIncomeEntry implements BaseColumns {
        public static final String TABLE_NAME = "category_income";

        public static final String COLUMN_NAME = "name";
    }

}
