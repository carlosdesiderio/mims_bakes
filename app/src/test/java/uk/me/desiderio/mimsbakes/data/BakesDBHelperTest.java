package uk.me.desiderio.mimsbakes.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.List;

import uk.me.desiderio.mimsbakes.data.BakesContract.IngredientEntry;
import uk.me.desiderio.mimsbakes.data.BakesContract.RecipeEntry;
import uk.me.desiderio.mimsbakes.data.BakesContract.StepEntry;
import uk.me.desiderio.mimsbakes.data.DatabaseSchemaTestHelper.TableDescription;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import static uk.me.desiderio.mimsbakes.data.BakesMockDataUtils.RECIPE_MOCK_IMAGE_URL;
import static uk.me.desiderio.mimsbakes.data.BakesMockDataUtils.RECIPE_MOCK_NAME;
import static uk.me.desiderio.mimsbakes.data.BakesMockDataUtils.RECIPE_MOCK_SERVINGS;
import static uk.me.desiderio.mimsbakes.data.BakesMockDataUtils.getMockIngredientContentValues;
import static uk.me.desiderio.mimsbakes.data.BakesMockDataUtils.getMockIngredients;
import static uk.me.desiderio.mimsbakes.data.BakesMockDataUtils.getMockRecipe;
import static uk.me.desiderio.mimsbakes.data.BakesMockDataUtils.getMockStepContentValues;
import static uk.me.desiderio.mimsbakes.data.BakesMockDataUtils.getMockSteps;
import static uk.me.desiderio.mimsbakes.data.DatabaseSchemaTestHelper.DB_ROW_TYPE_INTEGER;
import static uk.me.desiderio.mimsbakes.data.DatabaseSchemaTestHelper.DB_ROW_TYPE_REAL;
import static uk.me.desiderio.mimsbakes.data.DatabaseSchemaTestHelper.DB_ROW_TYPE_TEXT;
import static uk.me.desiderio.mimsbakes.data.DatabaseSchemaTestHelper.getColumnDetailsOrNull;

/**
 * provides test for {@link BakesDBHelper}
 */

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class BakesDBHelperTest {

    private SQLiteDatabase database;

    @Before
    public void setUp() {
        BakesDBHelper dbHelper = new BakesDBHelper(RuntimeEnvironment.application);
        database = dbHelper.getWritableDatabase();
    }

    @Test
    public void whenDBIsAccessed_thenDBIsOpened(){
        // Verify is the DB is opening correctly
        assertTrue("DB didn't open", database.isOpen());
    }

    @Test
    public void whenCreated_databaseHasAllTables() {
        assertTrue(DatabaseSchemaTestHelper.doesTableExist(database, RecipeEntry.TABLE_NAME));
        assertTrue(DatabaseSchemaTestHelper.doesTableExist(database, IngredientEntry.TABLE_NAME));
        assertTrue(DatabaseSchemaTestHelper.doesTableExist(database, StepEntry.TABLE_NAME));
    }

    @Test
    public void whenDBCreated_recipeTableHasRightFormat() {
        List<String> columnNames = DatabaseSchemaTestHelper.listColumnNames(database, RecipeEntry
                .TABLE_NAME);
        assertThat(columnNames).containsExactly(
                RecipeEntry._ID,
                RecipeEntry.COLUMN_NAME_ID,
                RecipeEntry.COLUMN_NAME_NAME,
                RecipeEntry.COLUMN_NAME_IMAGE,
                RecipeEntry.COLUMN_NAME_SERVINGS);

        TableDescription rowIdDescription =
                getColumnDetailsOrNull(database, RecipeEntry.TABLE_NAME, RecipeEntry._ID);
        assertEquals(RecipeEntry._ID, rowIdDescription.name);
        assertEquals(DB_ROW_TYPE_INTEGER, rowIdDescription.type);
        assertFalse(rowIdDescription.isNotNull);
        assertTrue(rowIdDescription.isPk);

        TableDescription recipeIdDescription =
                getColumnDetailsOrNull(database, RecipeEntry.TABLE_NAME, RecipeEntry.COLUMN_NAME_ID);
        assertEquals(RecipeEntry.COLUMN_NAME_ID, recipeIdDescription.name);
        assertEquals(DB_ROW_TYPE_INTEGER, recipeIdDescription.type);
        assertTrue(recipeIdDescription.isNotNull);

        TableDescription nameDescription =
                getColumnDetailsOrNull(database, RecipeEntry.TABLE_NAME, RecipeEntry.COLUMN_NAME_NAME);
        assertEquals(RecipeEntry.COLUMN_NAME_NAME, nameDescription.name);
        assertEquals(DB_ROW_TYPE_TEXT, nameDescription.type);
        assertTrue(nameDescription.isNotNull);


        TableDescription imageDescription =
                getColumnDetailsOrNull(database, RecipeEntry.TABLE_NAME, RecipeEntry.COLUMN_NAME_IMAGE);
        assertEquals(RecipeEntry.COLUMN_NAME_IMAGE, imageDescription.name);
        assertEquals(DB_ROW_TYPE_TEXT, imageDescription.type);
        assertTrue(imageDescription.isNotNull);

        TableDescription stepsDescription =
                getColumnDetailsOrNull(database, RecipeEntry.TABLE_NAME, RecipeEntry.COLUMN_NAME_SERVINGS);
        assertEquals(RecipeEntry.COLUMN_NAME_SERVINGS, stepsDescription.name);
        assertEquals(DB_ROW_TYPE_INTEGER, stepsDescription.type);
        assertTrue(stepsDescription.isNotNull);
    }

    @Test
    public void whenDBCreated_ingredientsTableHasRightFormat() {
        List<String> columnNames = DatabaseSchemaTestHelper.listColumnNames(database, IngredientEntry
                .TABLE_NAME);
        assertThat(columnNames).containsExactly(
                IngredientEntry._ID,
                IngredientEntry.COLUMN_NAME_INGREDIENT_NAME,
                IngredientEntry.COLUMN_NAME_INGREDIENT_QUANTITY,
                IngredientEntry.COLUMN_NAME_INGREDIENT_MEASURE,
                IngredientEntry.COLUMN_RECIPE_FOREING_KEY);

        TableDescription rowIdDescription =
                getColumnDetailsOrNull(database, IngredientEntry.TABLE_NAME, IngredientEntry._ID);
        assertEquals(IngredientEntry._ID, rowIdDescription.name);
        assertEquals(DB_ROW_TYPE_INTEGER, rowIdDescription.type);
        assertFalse(rowIdDescription.isNotNull);
        assertTrue(rowIdDescription.isPk);

        TableDescription nameDescription =
                getColumnDetailsOrNull(database, IngredientEntry.TABLE_NAME, IngredientEntry.COLUMN_NAME_INGREDIENT_NAME);
        assertEquals(IngredientEntry.COLUMN_NAME_INGREDIENT_NAME, nameDescription.name);
        assertEquals(DB_ROW_TYPE_TEXT, nameDescription.type);
        assertTrue(nameDescription.isNotNull);

        TableDescription quantityDescription =
                getColumnDetailsOrNull(database, IngredientEntry.TABLE_NAME, IngredientEntry.COLUMN_NAME_INGREDIENT_QUANTITY);
        assertEquals(IngredientEntry.COLUMN_NAME_INGREDIENT_QUANTITY, quantityDescription.name);
        assertEquals(DB_ROW_TYPE_REAL, quantityDescription.type);
        assertTrue(quantityDescription.isNotNull);


        TableDescription measureDescription =
                getColumnDetailsOrNull(database, IngredientEntry.TABLE_NAME, IngredientEntry.COLUMN_NAME_INGREDIENT_MEASURE);
        assertEquals(IngredientEntry.COLUMN_NAME_INGREDIENT_MEASURE, measureDescription.name);
        assertEquals(DB_ROW_TYPE_TEXT, measureDescription.type);
        assertTrue(measureDescription.isNotNull);


        TableDescription foreignKeyDescription =
                getColumnDetailsOrNull(database, IngredientEntry.TABLE_NAME, IngredientEntry.COLUMN_RECIPE_FOREING_KEY);
        assertEquals(IngredientEntry.COLUMN_RECIPE_FOREING_KEY, foreignKeyDescription.name);
        assertEquals(DB_ROW_TYPE_INTEGER, foreignKeyDescription.type);
        assertTrue(foreignKeyDescription.isNotNull);
    }

    @Test
    public void whenDBCreated_stepsTableHasRightFormat() {
        List<String> columnNames = DatabaseSchemaTestHelper.listColumnNames(database, StepEntry.TABLE_NAME);
        assertThat(columnNames).containsExactly(
                StepEntry._ID,
                StepEntry.COLUMN_NAME_STEP_ID,
                StepEntry.COLUMN_NAME_STEP_SHORT_DESC,
                StepEntry.COLUMN_NAME_STEP_DESC,
                StepEntry.COLUMN_NAME_STEP_THUMBNAIL_URL,
                StepEntry.COLUMN_NAME_STEP_VIDEO_URL,
                StepEntry.COLUMN_RECIPE_FOREING_KEY);

        TableDescription rowIdDescription =
                getColumnDetailsOrNull(database, StepEntry.TABLE_NAME, StepEntry._ID);
        assertEquals(StepEntry._ID, rowIdDescription.name);
        assertEquals(DB_ROW_TYPE_INTEGER, rowIdDescription.type);
        assertFalse(rowIdDescription.isNotNull);
        assertTrue(rowIdDescription.isPk);

        TableDescription stepIdDescription =
                getColumnDetailsOrNull(database, StepEntry.TABLE_NAME, StepEntry.COLUMN_NAME_STEP_ID);
        assertEquals(StepEntry.COLUMN_NAME_STEP_ID, stepIdDescription.name);
        assertEquals(DB_ROW_TYPE_INTEGER, stepIdDescription.type);
        assertTrue(stepIdDescription.isNotNull);


        TableDescription shortDescDescription =
                getColumnDetailsOrNull(database, StepEntry.TABLE_NAME, StepEntry.COLUMN_NAME_STEP_SHORT_DESC);
        assertEquals(StepEntry.COLUMN_NAME_STEP_SHORT_DESC, shortDescDescription.name);
        assertEquals(DB_ROW_TYPE_TEXT, shortDescDescription.type);
        assertTrue(shortDescDescription.isNotNull);


        TableDescription longDescDescription =
                getColumnDetailsOrNull(database, StepEntry.TABLE_NAME, StepEntry.COLUMN_NAME_STEP_DESC);
        assertEquals(StepEntry.COLUMN_NAME_STEP_DESC, longDescDescription.name);
        assertEquals(DB_ROW_TYPE_TEXT, longDescDescription.type);
        assertTrue(longDescDescription.isNotNull);


        TableDescription thumbnailURLDescription =
                getColumnDetailsOrNull(database, StepEntry.TABLE_NAME, StepEntry.COLUMN_NAME_STEP_THUMBNAIL_URL);
        assertEquals(StepEntry.COLUMN_NAME_STEP_THUMBNAIL_URL, thumbnailURLDescription.name);
        assertEquals(DB_ROW_TYPE_TEXT, thumbnailURLDescription.type);
        assertTrue(thumbnailURLDescription.isNotNull);


        TableDescription videoURLDescription =
                getColumnDetailsOrNull(database, StepEntry.TABLE_NAME, StepEntry.COLUMN_NAME_STEP_SHORT_DESC);
        assertEquals(StepEntry.COLUMN_NAME_STEP_SHORT_DESC, videoURLDescription.name);
        assertEquals(DB_ROW_TYPE_TEXT, videoURLDescription.type);
        assertTrue(videoURLDescription.isNotNull);


        TableDescription nameDescription =
                getColumnDetailsOrNull(database, StepEntry.TABLE_NAME, StepEntry.COLUMN_NAME_STEP_VIDEO_URL);
        assertEquals(StepEntry.COLUMN_NAME_STEP_VIDEO_URL, nameDescription.name);
        assertEquals(DB_ROW_TYPE_TEXT, nameDescription.type);
        assertTrue(nameDescription.isNotNull);


        TableDescription foreignKeyDescription =
                getColumnDetailsOrNull(database, StepEntry.TABLE_NAME, StepEntry.COLUMN_RECIPE_FOREING_KEY);
        assertEquals(StepEntry.COLUMN_RECIPE_FOREING_KEY, foreignKeyDescription.name);
        assertEquals(DB_ROW_TYPE_INTEGER, foreignKeyDescription.type);
        assertTrue(foreignKeyDescription.isNotNull);
    }

    @Test
    public void whenInsertingRecipe_thenRecipeCanBeQueried() {
        int expectedRecipeId = 2;
        database.insert(RecipeEntry.TABLE_NAME, null, getMockRecipe(expectedRecipeId));

        Cursor cursor = database.query(RecipeEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        assertNotNull(cursor);
        assertEquals(1, cursor.getCount());
        cursor.moveToFirst();
        int rowId = cursor.getInt(cursor.getColumnIndex(RecipeEntry._ID));
        int recipeId = cursor.getInt(cursor.getColumnIndex(RecipeEntry.COLUMN_NAME_ID));
        String name = cursor.getString(cursor.getColumnIndex(RecipeEntry.COLUMN_NAME_NAME));
        String imageUrl = cursor.getString(cursor.getColumnIndex(RecipeEntry.COLUMN_NAME_IMAGE));
        int servings = cursor.getInt(cursor.getColumnIndex(RecipeEntry.COLUMN_NAME_SERVINGS));

        assertEquals(1, rowId);
        assertEquals(expectedRecipeId, recipeId);
        assertEquals(RECIPE_MOCK_NAME, name);
        assertEquals(RECIPE_MOCK_IMAGE_URL, imageUrl);
        assertEquals(RECIPE_MOCK_SERVINGS, servings);
    }

    @Test
    public void whenInsertingRecipeTwice_thenUniqueConstraintIsEnforced() {
        int expectedRecipeId = 2;
        database.insert(RecipeEntry.TABLE_NAME, null, getMockRecipe(expectedRecipeId));
        database.insert(RecipeEntry.TABLE_NAME, null, getMockRecipe(expectedRecipeId));

        Cursor cursor = database.query(RecipeEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        assertNotNull(cursor);
        assertEquals(1, cursor.getCount());
    }

    @Test
    public void givenARecipe_whenIngredientsAreInserted_thenIngredientCanBeRetrivedWithRecipeId
            () {
        int recipeId = 6;
        int otherRecipeId = 9;
        int ingredientCount = 9;

        database.insert(RecipeEntry.TABLE_NAME, null, getMockRecipe(recipeId));


        // insert ingredients of two different recipes
        List<ContentValues> ingredientList = getMockIngredients(ingredientCount, recipeId);
        List<ContentValues> otherRecipeIngredientList = getMockIngredients(ingredientCount,
                otherRecipeId);

        for (int i = 0; i < ingredientList.size(); i++) {
            float insertId = database.insert(IngredientEntry.TABLE_NAME, null, ingredientList.get(i));
            float otherInsertId = database.insert(IngredientEntry.TABLE_NAME, null, otherRecipeIngredientList
                    .get(i));
            assertThat(insertId).isNotNegative();
            assertThat(otherInsertId).isNotNegative();
        }

        // retrieve recipe data to get the recipe id
        Cursor recipeCursor = database.query(RecipeEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        assertNotNull(recipeCursor);
        assertEquals(1, recipeCursor.getCount());
        recipeCursor.moveToFirst();
        int actualRecipeId = recipeCursor.getInt(recipeCursor.getColumnIndex(RecipeEntry
                .COLUMN_NAME_ID));

        // get only ingredient of the actual recipe
        String selection = IngredientEntry.COLUMN_RECIPE_FOREING_KEY + " =? ";
        String[] selectionArgs = new String[]{String.valueOf(actualRecipeId)};

        Cursor ingredientCursor = database.query(IngredientEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null);

        assertNotNull(ingredientCursor);
        ingredientCursor.moveToFirst();
        assertEquals(ingredientCount, ingredientCursor.getCount());
    }


    @Test
    public void givenARecipe_whenStepsAreInserted_thenIngredientCanBeRetrivedWithRecipeId
            () {
        int recipeId = 6;
        int unrelatedRecipeId = 9;
        int ingredientCount = 9;

        database.insert(RecipeEntry.TABLE_NAME, null, getMockRecipe(recipeId));

        // insert steps of two different recipes
        List<ContentValues> stepsList = getMockSteps(ingredientCount, recipeId);
        List<ContentValues> otherStepList = getMockSteps(ingredientCount, unrelatedRecipeId);

        for (int i = 0; i < stepsList.size(); i++) {
            long insertId = database.insert(StepEntry.TABLE_NAME, null, stepsList.get(i));
            long otherInsertId = database.insert(StepEntry.TABLE_NAME, null, otherStepList.get(i));
            assertThat(insertId).isNotNegative();
            assertThat(otherInsertId).isNotNegative();
        }

        Cursor recipeCursor = database.query(RecipeEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        assertNotNull(recipeCursor);
        assertEquals(1, recipeCursor.getCount());
        recipeCursor.moveToFirst();
        int actualRecipeId = recipeCursor.getInt(recipeCursor.getColumnIndex(RecipeEntry
                .COLUMN_NAME_ID));

        // get only steps of the actual recipe
        String selection = StepEntry.COLUMN_RECIPE_FOREING_KEY + "=? ";
        String[] selectionArgs = new String[]{String.valueOf(actualRecipeId)};

        Cursor stepCursor = database.query(StepEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null);

        assertNotNull(stepCursor);
        stepCursor.moveToFirst();
        assertEquals(ingredientCount, stepCursor.getCount());
    }

    @Test
    public void
            givenARecipe_whenInsertingSameIngredient_thenUniqueConstraintsReplaceRow() {

        int randomForeingKey = 999;
        int newQuantity = 5;
        ContentValues oldValues = getMockIngredientContentValues(1, randomForeingKey);

        // same unique contraint values, different quantity
        ContentValues newValues = getMockIngredientContentValues(1, randomForeingKey);
        newValues.put(IngredientEntry.COLUMN_NAME_INGREDIENT_QUANTITY, newQuantity);

        database.insert(IngredientEntry.TABLE_NAME, null, oldValues);
        database.insert(IngredientEntry.TABLE_NAME, null, newValues);

        Cursor cursor = database.query(IngredientEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        assertNotNull(cursor);
        assertEquals(1, cursor.getCount());
        cursor.moveToFirst();
        int quantity = cursor.getInt(cursor.getColumnIndex(IngredientEntry
                .COLUMN_NAME_INGREDIENT_QUANTITY));
        assertEquals(newQuantity, quantity);
    }

    @Test
    public void
            givenARecipe_whenInsertingSameStep_thenUniqueConstraintsReplaceRow() {

        int randomForeingKey = 999;
        String newShortDesc = "shorter description";
        ContentValues oldValues = getMockStepContentValues(1, randomForeingKey);

        // same unique contraint values, different quantity
        ContentValues newValues = getMockStepContentValues(1, randomForeingKey);
        newValues.put(StepEntry.COLUMN_NAME_STEP_SHORT_DESC, newShortDesc);

        database.insert(StepEntry.TABLE_NAME, null, oldValues);
        database.insert(StepEntry.TABLE_NAME, null, newValues);

        Cursor cursor = database.query(StepEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        assertNotNull(cursor);
        assertEquals(1, cursor.getCount());
        cursor.moveToFirst();
        String shortDesc = cursor.getString(cursor.getColumnIndex(StepEntry
                .COLUMN_NAME_STEP_SHORT_DESC));
        assertEquals(newShortDesc, shortDesc);
    }

    @After
    public void tearDown() {
        database.close();
    }
}
