package uk.me.desiderio.mimsbakes.data;

import android.content.ContentValues;

import java.util.ArrayList;
import java.util.List;

import uk.me.desiderio.mimsbakes.data.BakesContract.IngredientEntry;
import uk.me.desiderio.mimsbakes.data.BakesContract.RecipeEntry;
import uk.me.desiderio.mimsbakes.data.BakesContract.StepEntry;

/**
 * Provides mock {@link ContentValues} to test the Bakes Database tables
 */

public class BakesMockDataUtils {

    public static final String RECIPE_MOCK_NAME = "some recipe name";
    public static final String RECIPE_MOCK_IMAGE_URL = "http://someaddress.com";
    public static final int RECIPE_MOCK_SERVINGS = 771;

    public static final String INGREDIENT_MOCK_NAME = "Ingredient Name ";
    public static final int INGREDIENT_MOCK_QUANTITY = 100;
    public static final String INGREDIENT_MOCK_MEASURE = "grams ";

    public static final String STEP_MOCK_SHORT_DESCRIPTION
            = "Very short description ";
    public static final String STEP_MOCK_DESCRIPTION = "Very long description ";
    public static final String STEP_MOCK_THUMBNAIL_URL = "url.to.thumbnail ";
    public static final String STEP_MOCK_VIDEO_URL = "url.to.video ";


    public static ContentValues getMockRecipe(int recipeId) {
        ContentValues values = new ContentValues();
        values.put(RecipeEntry.COLUMN_NAME_ID, recipeId);
        values.put(RecipeEntry.COLUMN_NAME_NAME, RECIPE_MOCK_NAME);
        values.put(RecipeEntry.COLUMN_NAME_IMAGE, RECIPE_MOCK_IMAGE_URL);
        values.put(RecipeEntry.COLUMN_NAME_SERVINGS, RECIPE_MOCK_SERVINGS);
        return values;
    }


    public static List<ContentValues> getMockIngredients(int ingredientsCount,
                                                         int foreignKey) {
        List<ContentValues> valuesList = new ArrayList<>();
        for (int i = 0; i < ingredientsCount; i++) {
            ContentValues values = getMockIngredientContentValues(i, foreignKey);
            valuesList.add(values);
        }
        return valuesList;
    }

    public static ContentValues getMockIngredientContentValues(int i,
                                                               int foreignKey) {
        ContentValues values = new ContentValues();
        values.put(IngredientEntry.COLUMN_NAME_INGREDIENT_NAME,
                   INGREDIENT_MOCK_NAME  + i);
        values.put(IngredientEntry.COLUMN_NAME_INGREDIENT_QUANTITY,
                   INGREDIENT_MOCK_QUANTITY  + i);
        values.put(IngredientEntry.COLUMN_NAME_INGREDIENT_MEASURE,
                   INGREDIENT_MOCK_MEASURE  + i);
        values.put(IngredientEntry.COLUMN_RECIPE_FOREING_KEY, foreignKey);
        return values;
    }

    public static List<ContentValues> getMockSteps(int stepsCount,
                                                   int foreignKey) {
        List<ContentValues> valuesList = new ArrayList<>();
        for (int i = 0; i < stepsCount; i++) {
            ContentValues values = getMockStepContentValues(i, foreignKey);
            valuesList.add(values);
        }
        return valuesList;
    }

    public static ContentValues getMockStepContentValues(int i,
                                                         int foreignKey) {
        ContentValues values = new ContentValues();
        values.put(StepEntry.COLUMN_NAME_STEP_ID, i);
        values.put(StepEntry.COLUMN_NAME_STEP_SHORT_DESC,
                   STEP_MOCK_SHORT_DESCRIPTION + i);
        values.put(StepEntry.COLUMN_NAME_STEP_DESC,
                   STEP_MOCK_DESCRIPTION + i);
        values.put(StepEntry.COLUMN_NAME_STEP_THUMBNAIL_URL,
                   STEP_MOCK_THUMBNAIL_URL + i);
        values.put(StepEntry.COLUMN_NAME_STEP_VIDEO_URL,
                   STEP_MOCK_VIDEO_URL + i);
        values.put(StepEntry.COLUMN_RECIPE_FOREING_KEY,
                   foreignKey);
        return values;
    }
}
