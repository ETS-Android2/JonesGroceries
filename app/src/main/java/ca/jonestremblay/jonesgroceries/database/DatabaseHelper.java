package ca.jonestremblay.jonesgroceries.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import ca.jonestremblay.jonesgroceries.entities.Product;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

public class DatabaseHelper extends SQLiteOpenHelper {
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "groceriesCatalog.db";
    static final String DROP_STATEMENT = "DROP TABLE IF EXISTS ";

    static ArrayList<String> SQL_CREATE_TABLE_ENTRIES = new ArrayList<>();
    static ArrayList<String> SQL_DELETE_TABLE_ENTRIES = new ArrayList<>();

    /** Tables constants */
    final String TABLE_CATEGORIES = "categories";
    final String COL_CATEGORIES_CATEGORY = "category_name";
    final String COL_CATEGORIES_ICON_ID = "icon_id";

    final String TABLE_PRODUCTS = "products";
    final String PRODUCTS_ID = "_id";
    final String COL_PRODUCT_NAME = "product_name";
    final String COL_PRODUCT_CATEGORY = "product_category";

    final String TABLE_PRODUCTS_LISTS = "products_lists";
    final String COL_LIST_ID = "list_id";
    final String COL_PRODUCT_ID = "product_id";
    final String COL_QUANTITY = "quantity";
    final String COL_MEASURE_UNIT = "measure_unit";
    final String COL_NOTES = "notes";
    final String COL_COMPLETED = "completed";

    final String TABLE_GROCERIES = "groceries";
    final String COL_GROCERY_ID = "list_id";
    final String COL_GROCERY_NAME = "grocery_name";
    final String COL_ICON_ID = "icon_id";
    final String COL_COMPLETED_GROCERY = "completed";

    final String TABLE_RECIPES = "recipes";
    final String COL_RECIPE_ID = "list_id";
    final String COL_RECIPE_NAME = "recipe_name";
    // final String COL_ICON_ID = "icon_id";
    final String COL_COMPLETED_RECIPE = "completed";
    /** end of table constants */

    /** SQL entries for creating tables */
    final String SQL_CREATE_CATEGORIES =
            "CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORIES + "("
                    + COL_CATEGORIES_CATEGORY + " TEXT,"
                    + COL_CATEGORIES_ICON_ID + " INTEGER"
                    + ");";

    final String SQL_CREATE_ARTICLES =
            "CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCTS + "("
                    + PRODUCTS_ID + "INTEGER PRIMARY KEY,"
                    + COL_PRODUCT_NAME + " TEXT,"
                    + COL_PRODUCT_CATEGORY + " TEXT,"
                    + "CONSTRAINT fk_" + COL_PRODUCT_CATEGORY
                    + " FOREIGN KEY (" + COL_PRODUCT_CATEGORY + ")"
                    + " REFERENCES " + TABLE_CATEGORIES + "("+ COL_PRODUCT_CATEGORY + ")"
                    + ");";

    final String SQL_CREATE_PRODUCTS_LIST =
            "CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCTS_LISTS + "("
                    + COL_LIST_ID + "INTEGER PRIMARY KEY,"
                    + COL_PRODUCT_ID + " INTEGER NOT NULL,"
                    + COL_QUANTITY + " INTEGER,"
                    + COL_MEASURE_UNIT + " TEXT,"
                    + COL_NOTES  + " TEXT,"
                    + COL_COMPLETED + " BOOLEAN,"
                    + "CONSTRAINT fk_" + COL_PRODUCT_ID
                    + " FOREIGN KEY (" + COL_PRODUCT_ID + ")"
                    + " REFERENCES " + TABLE_PRODUCTS + "("+ COL_PRODUCT_ID + ")"
                    + ");";

    final String SQL_CREATE_GROCERIES =
            "CREATE TABLE IF NOT EXISTS " + TABLE_GROCERIES + "("
                    + COL_GROCERY_ID  + " INTEGER PRIMARY KEY,"
                    + COL_GROCERY_NAME + " TEXT UNIQUE NOT NULL,"
                    + COL_ICON_ID + " INT,"
                    + COL_COMPLETED_GROCERY + " BOOLEAN,"
                    + "CONSTRAINT fk_" + COL_GROCERY_ID
                    + " FOREIGN KEY (" + COL_GROCERY_ID + ")"
                    + " REFERENCES " + TABLE_PRODUCTS_LISTS + "("+ COL_LIST_ID + ")"
                    + ");";

    final String SQL_CREATE_RECIPES =
            "CREATE TABLE IF NOT EXISTS " + TABLE_RECIPES + "("
                    + COL_RECIPE_ID  + " INTEGER PRIMARY KEY,"
                    + COL_RECIPE_NAME + " TEXT UNIQUE NOT NULL,"
                    + COL_ICON_ID + " INT,"
                    + COL_COMPLETED_RECIPE + " BOOLEAN,"
                    + "CONSTRAINT fk_" + COL_RECIPE_ID
                    + " FOREIGN KEY (" + COL_RECIPE_ID + ")"
                    + " REFERENCES " + TABLE_PRODUCTS_LISTS + "("+ COL_LIST_ID + ")"
                    + ");";
    /** end of SQL entries */


    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /***
     * Called when it's the first time the database is accessed.
     * There's code to create a new database.
     * @param db database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        /**
         * Defines every table in the database.
         * */
        defineEveryTable();
        for (String createTableEntry : SQL_CREATE_TABLE_ENTRIES) {
            db.execSQL(createTableEntry);
        }
    }

    /***
     * For every new table defined in this class as a SQL_CREATE_{ TABLE-NAME },
     * need to add the SQL create entry in the proper ArrayList.
     * Important ! -- > Don't forget to also add the delete entry for that table !
     */
    private void defineEveryTable() {
        /** categories Table */
        SQL_CREATE_TABLE_ENTRIES.add(SQL_CREATE_CATEGORIES);
        SQL_DELETE_TABLE_ENTRIES.add(DROP_STATEMENT + TABLE_CATEGORIES);

        /** articles Table */
        SQL_CREATE_TABLE_ENTRIES.add(SQL_CREATE_ARTICLES);
        SQL_DELETE_TABLE_ENTRIES.add(DROP_STATEMENT + TABLE_PRODUCTS);

        /** products_lists Table */
        SQL_CREATE_TABLE_ENTRIES.add(SQL_CREATE_PRODUCTS_LIST);
        SQL_DELETE_TABLE_ENTRIES.add(DROP_STATEMENT + TABLE_PRODUCTS_LISTS);

        /** groceries Table */
        SQL_CREATE_TABLE_ENTRIES.add(SQL_CREATE_GROCERIES);
        SQL_DELETE_TABLE_ENTRIES.add(DROP_STATEMENT + TABLE_GROCERIES);

        /** recipes Table */
        SQL_CREATE_TABLE_ENTRIES.add(SQL_CREATE_RECIPES);
        SQL_DELETE_TABLE_ENTRIES.add(DROP_STATEMENT + TABLE_RECIPES);
    }

    /***
     * Called when the version's number of the database is changed.
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (String deleteTableEntry : SQL_DELETE_TABLE_ENTRIES) {
            db.execSQL(deleteTableEntry);
        }
    }


    public boolean addProduct(Product product){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PRODUCT_NAME, product.name);
        values.put(COL_PRODUCT_CATEGORY, product.category.category_name);
        long insert = db.insertOrThrow("articles", null, values);

        if (insert == -1){
            return false;
        } else {
            return true;
        }
    }

}
