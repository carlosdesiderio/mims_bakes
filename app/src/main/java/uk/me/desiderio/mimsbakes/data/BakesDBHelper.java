package uk.me.desiderio.mimsbakes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import uk.me.desiderio.mimsbakes.data.BakesContract.IngredientEntry;
import uk.me.desiderio.mimsbakes.data.BakesContract.RecipeEntry;
import uk.me.desiderio.mimsbakes.data.BakesContract.ShoppingEntry;
import uk.me.desiderio.mimsbakes.data.BakesContract.StepEntry;

import static android.provider.BaseColumns._ID;

/**
 * Database helper for the baking recipes data
 */

public class BakesDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "recipes.db";


    public BakesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_RECIPES_TABLE = "CREATE TABLE " + RecipeEntry.TABLE_NAME +
                " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RecipeEntry.COLUMN_NAME_ID + " INTEGER NOT NULl, " +
                RecipeEntry.COLUMN_NAME_NAME + " TEXT NOT NULL, " +
                RecipeEntry.COLUMN_NAME_IMAGE + " TEXT NOT NULL, " +
                RecipeEntry.COLUMN_NAME_SERVINGS + " INTEGER NOT NULL, " +
                " UNIQUE (" + RecipeEntry.COLUMN_NAME_ID + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_INGREDIENTS_TABLE = "CREATE TABLE " + IngredientEntry.TABLE_NAME +
                " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                IngredientEntry.COLUMN_NAME_INGREDIENT_NAME + " TEXT NOT NULl, " +
                IngredientEntry.COLUMN_NAME_INGREDIENT_QUANTITY + " REAL NOT NULL, " +
                IngredientEntry.COLUMN_NAME_INGREDIENT_MEASURE + " TEXT NOT NULL, " +
                IngredientEntry.COLUMN_RECIPE_FOREING_KEY + " INTEGER NOT NULL, " +
                "FOREIGN KEY (" + IngredientEntry.COLUMN_RECIPE_FOREING_KEY + ") REFERENCES " +
                RecipeEntry.TABLE_NAME + " (" + RecipeEntry.COLUMN_NAME_ID + ")" +
                "ON UPDATE CASCADE ON DELETE CASCADE" +
                " UNIQUE (" + IngredientEntry.COLUMN_RECIPE_FOREING_KEY + "," + IngredientEntry
                .COLUMN_NAME_INGREDIENT_NAME + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_SHOPPING_TABLE = "CREATE TABLE " + ShoppingEntry.TABLE_NAME +
                " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ShoppingEntry.COLUMN_NAME_INGREDIENT_NAME + " TEXT NOT NULl, " +
                ShoppingEntry.COLUMN_RECIPE_FOREING_KEY + " INTEGER NOT NULL, " +
                "FOREIGN KEY (" + ShoppingEntry.COLUMN_RECIPE_FOREING_KEY + ") REFERENCES " +
                RecipeEntry.TABLE_NAME + " (" + RecipeEntry.COLUMN_NAME_ID + ")" +
                "ON UPDATE CASCADE ON DELETE CASCADE" +
                " UNIQUE (" +
                ShoppingEntry.COLUMN_NAME_INGREDIENT_NAME + "," +
                ShoppingEntry.COLUMN_RECIPE_FOREING_KEY + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_STEPS_TABLE = "CREATE TABLE " + StepEntry.TABLE_NAME +
                " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                StepEntry.COLUMN_NAME_STEP_ID + " INTEGER NOT NULl, " +
                StepEntry.COLUMN_NAME_STEP_SHORT_DESC + " TEXT NOT NULL, " +
                StepEntry.COLUMN_NAME_STEP_DESC + " TEXT NOT NULL, " +
                StepEntry.COLUMN_NAME_STEP_THUMBNAIL_URL + " TEXT NOT NULL, " +
                StepEntry.COLUMN_NAME_STEP_VIDEO_URL + " TEXT NOT NULL, " +
                StepEntry.COLUMN_RECIPE_FOREING_KEY + " INTEGER NOT NULL, " +
                "FOREIGN KEY (" + StepEntry.COLUMN_RECIPE_FOREING_KEY + ") REFERENCES " +
                RecipeEntry.TABLE_NAME + " (" + RecipeEntry.COLUMN_NAME_ID + ")" +
                "ON UPDATE CASCADE ON DELETE CASCADE" +
                " UNIQUE (" + StepEntry.COLUMN_RECIPE_FOREING_KEY + "," + StepEntry
                .COLUMN_NAME_STEP_ID + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_RECIPES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_INGREDIENTS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_STEPS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_SHOPPING_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // TODO: 23/12/2017 try to implement last changes with ALTER
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ShoppingEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + StepEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + IngredientEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
