package uk.me.desiderio.mimsbakes.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import uk.me.desiderio.mimsbakes.data.BakesContract.IngredientEntry;
import uk.me.desiderio.mimsbakes.data.BakesContract.RecipeEntry;
import uk.me.desiderio.mimsbakes.data.BakesContract.StepEntry;
import uk.me.desiderio.mimsbakes.data.model.Ingredient;
import uk.me.desiderio.mimsbakes.data.model.Recipe;
import uk.me.desiderio.mimsbakes.data.model.Step;

/**
 * Utility class to persist data and deserialise database data into {@link Recipe} objects
 */

public class BakesDataUtils {
    private static final String TAG = BakesDataUtils.class.getSimpleName();

    private Context context;

    public BakesDataUtils(Context context) {
        this.context = context;
    }

    public ContentResolver getContentResolver(){
        return context.getContentResolver();
    }

    public List<Recipe> getRecipeDataObjects(Cursor cursor){
        List<Recipe> recipeList = new ArrayList<>();
        while(cursor.moveToNext()) {
            int recipeId = cursor.getInt(cursor.getColumnIndex(RecipeEntry.COLUMN_NAME_ID));
            String name = cursor.getString(cursor.getColumnIndex(RecipeEntry.COLUMN_NAME_NAME));
            int servings = cursor.getInt(cursor.getColumnIndex(RecipeEntry.COLUMN_NAME_SERVINGS));
            String imageURLString = cursor.getString(cursor.getColumnIndex(RecipeEntry
                    .COLUMN_NAME_IMAGE));

            Recipe recipe = new Recipe(recipeId,
                    name,
                    servings,
                    imageURLString,
                    new ArrayList<Ingredient>(),
                    new ArrayList<Step>());
            recipeList.add(recipe);

            addIngredients(recipe);
            addSteps(recipe);
        }

        return recipeList;
    }

    void addIngredients(Recipe recipe) {
        String  recipeId = String.valueOf(recipe.getRecipeId());
        String selection = IngredientEntry.COLUMN_RECIPE_FOREING_KEY + "=? ";
        String[] selectionArgs = {recipeId};

        Cursor cursor = context.getContentResolver().query(IngredientEntry.CONTENT_URI,
                null,
                selection,
                selectionArgs,
                null);

        recipe.setIngredients(getIngredientListFromCursor(cursor));
    }

    public static List<Ingredient> getIngredientListFromCursor(Cursor cursor) {
        List<Ingredient> ingredientList = new ArrayList<>();

        while(cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(IngredientEntry
                    .COLUMN_NAME_INGREDIENT_NAME));
            float quantity = cursor.getFloat(cursor.getColumnIndex(IngredientEntry
                    .COLUMN_NAME_INGREDIENT_QUANTITY));
            String measure = cursor.getString(cursor.getColumnIndex(IngredientEntry
                    .COLUMN_NAME_INGREDIENT_MEASURE));
            int shopping = cursor.getInt(cursor.getColumnIndex(IngredientEntry.FLAG_NAME_SHOPPING));

            Ingredient ingredient = new Ingredient(name, quantity, measure, shopping);
            ingredientList.add(ingredient);
        }

        return ingredientList;
    }

    void addSteps(Recipe recipe) {
        String  recipeId = String.valueOf(recipe.getRecipeId());
        String selection = StepEntry.COLUMN_RECIPE_FOREING_KEY + "=? ";
        String[] selectionArgs = {recipeId};

        Cursor cursor = context.getContentResolver().query(StepEntry.CONTENT_URI,
                null,
                selection,
                selectionArgs,
                null);

        while(cursor.moveToNext()) {
            int stepId = cursor.getInt(cursor.getColumnIndex(StepEntry
                    .COLUMN_NAME_STEP_ID));
            String shortDesc = cursor.getString(cursor.getColumnIndex(StepEntry
                    .COLUMN_NAME_STEP_SHORT_DESC));
            String desc = cursor.getString(cursor.getColumnIndex(StepEntry
                    .COLUMN_NAME_STEP_DESC));
            String videoUrl = cursor.getString(cursor.getColumnIndex(StepEntry
                    .COLUMN_NAME_STEP_VIDEO_URL));
            String thumbnailUrl = cursor.getString(cursor.getColumnIndex(StepEntry
                    .COLUMN_NAME_STEP_THUMBNAIL_URL));

            Step step = new Step(stepId, shortDesc, desc, videoUrl, thumbnailUrl);

            recipe.addStep(step);
        }
    }

    public void persistRecipes(List<Recipe> recipes) {
        for (Recipe recipe: recipes) {
            Log.d(TAG, "SALMOM persistRecipes: recipe : " + recipe.getName());
            ContentResolver resolver = context.getContentResolver();
                    resolver.insert(RecipeEntry.CONTENT_URI, recipe
                    .toContentValues());

            int recipeId = recipe.getRecipeId();

            persistIngredients(recipe.getIngredients(), recipeId);
            persistSteps(recipe.getSteps(), recipeId);
        }
    }

    void persistIngredients(List<Ingredient> ingredients, int recipeId) {
        List<ContentValues> valuesList = new ArrayList<>();
        for(Ingredient ingredient : ingredients) {
            ContentValues values = ingredient.toContentValues();
            values.put(IngredientEntry.COLUMN_RECIPE_FOREING_KEY, recipeId);
            valuesList.add(values);
        }

        ContentValues[] valuesArray = new ContentValues[valuesList.size()];
        valuesArray = valuesList.toArray(valuesArray);

        context.getContentResolver().bulkInsert(IngredientEntry.CONTENT_URI, valuesArray);
    }

    void persistSteps(List<Step> steps, int recipeId) {
        List<ContentValues> valuesList = new ArrayList<>();
        for(Step step : steps) {
            ContentValues values = step.toContentValues();
            values.put(StepEntry.COLUMN_RECIPE_FOREING_KEY, recipeId);
            valuesList.add(values);
        }

        ContentValues[] valuesArray = new ContentValues[valuesList.size()];
        valuesArray = valuesList.toArray(valuesArray);

        context.getContentResolver().bulkInsert(StepEntry.CONTENT_URI, valuesArray);
    }
}
