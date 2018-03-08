package uk.me.desiderio.mimsbakes.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import uk.me.desiderio.mimsbakes.data.BakesContract.IngredientEntry;
import uk.me.desiderio.mimsbakes.data.BakesContract.StepEntry;
import uk.me.desiderio.mimsbakes.data.model.Ingredient;
import uk.me.desiderio.mimsbakes.data.model.Recipe;
import uk.me.desiderio.mimsbakes.data.model.Step;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * unit tests for the {@link BakesDataUtils}
 */
// TODO: 25/12/2017 how to inject content resolver
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class BakesDataUtilsTest {

    private static final int RECIPE_MOCK_COUNT = 4;

    private static final int RECIPE_ID_EXPECTED_VALUE = 5;
    public static final String RECIPE_NAME_EXPECTED_VALUE = "Recipe Name";
    public static final String RECIPE_IMAGE_URL_EXPECTED_VALUE = "http://expected.url";
    private static final int RECIPE_SERVINGS_EXPECTED_VALUE = 12;

    public static final String INGREDIENT_NAME_EXPECTED_VALUE = "Ingredient name";
    private static final float INGREDIENT_QUANTITY_EXPECTED_VALUE = 100;
    public static final String INGREDIENT_MEASURE_EXPECTED_VALUE = "grams";

    private static final int STEP_ID_EXPECTED_VALUE = 2;
    public static final String STEP_SHORT_DESC_EXPECTED_VALUE = "Short description";
    public static final String STEP_DESC_EXPECTED_VALUE = "Description";
    public static final String STEP_VIDEO_URL_EXPECTED_VALUE = "http://video.url";
    public static final String STEP_THUMBANIL_URL_EXPECTED_VALUE = "http://thumbnail.url";


    @Mock
    Recipe recipe;
    @Mock
    Ingredient ingredient;
    @Mock
    Step step;
    @Mock
    List<Recipe> recipeList;

    List<Ingredient> ingredientList;
    @Mock
    List<Step> stepList;
    @Mock
    Context context;
    @Mock
    ContentResolver contentResolver;

    @Mock
            ContentValues contentValues;

    BakesDataUtils dataUtils;

    @Before
    public void setUp() {
        initMocks(this);
        dataUtils = spy(new BakesDataUtils(context));
        setUpPersistData();

    }

    private void setUpPersistData() {

        when (step.getStepId()).thenReturn(STEP_ID_EXPECTED_VALUE);
        when (step.getShortDescription()).thenReturn(STEP_SHORT_DESC_EXPECTED_VALUE);
        when (step.getDescription()).thenReturn(STEP_DESC_EXPECTED_VALUE);
        when (step.getVideoURLString()).thenReturn(STEP_VIDEO_URL_EXPECTED_VALUE);
        when (step.getThumbnailURLString()).thenReturn(STEP_THUMBANIL_URL_EXPECTED_VALUE);
        when(step.toContentValues()).thenReturn(contentValues);
        stepList = spy(new ArrayList<Step>());
        stepList.add(step);
        stepList.add(step);
        stepList.add(step);
        stepList.add(step);

        when (ingredient.getName()).thenReturn(INGREDIENT_NAME_EXPECTED_VALUE);
        when (ingredient.getQuantity()).thenReturn(INGREDIENT_QUANTITY_EXPECTED_VALUE);
        when (ingredient.getMeasure()).thenReturn(INGREDIENT_MEASURE_EXPECTED_VALUE);
        when(ingredient.toContentValues()).thenReturn(contentValues);
        ingredientList = spy(new ArrayList<Ingredient>());
        ingredientList.add(ingredient);
        ingredientList.add(ingredient);
        ingredientList.add(ingredient);
        ingredientList.add(ingredient);

        when (recipe.getRecipeId()).thenReturn(RECIPE_ID_EXPECTED_VALUE);
        when (recipe.getName()).thenReturn(RECIPE_NAME_EXPECTED_VALUE);
        when (recipe.getImageURLString()).thenReturn(RECIPE_IMAGE_URL_EXPECTED_VALUE);
        when (recipe.getServings()).thenReturn(RECIPE_SERVINGS_EXPECTED_VALUE);
        when (recipe.getIngredients()).thenReturn(ingredientList);
        when (recipe.getSteps()).thenReturn(stepList);
        recipeList = new ArrayList<>();
        recipeList.add(recipe);
        recipeList.add(recipe);
        recipeList.add(recipe);
        recipeList.add(recipe);

        when(context.getContentResolver()).thenReturn(contentResolver);

    }

    @Test
    public void whenGettingRecipeDataObjects_thenExpectedDataIsReturn() {
        Cursor recipeCursor = mock(Cursor.class);

        int recipeCount = 2;
        int recipeId = 8;
        String recipeName = "Potato";
        int recipeServings = 12;
        String recipeImageUrl = "http:url";

        when(recipeCursor.moveToFirst())
                .thenReturn(true);
        when(recipeCursor.moveToNext())
                .thenReturn(true, false);
        when(recipeCursor.getInt(anyInt()))
                .thenReturn(recipeId).thenReturn(recipeServings);
        when(recipeCursor.getString(anyInt()))
                .thenReturn(recipeName).thenReturn(recipeImageUrl);

        doNothing().when(dataUtils).addIngredients(Matchers.<Recipe>any());
        doNothing().when(dataUtils).addSteps(Matchers.<Recipe>any());

        List<Recipe> actualRecipeList =
                dataUtils.getRecipeDataObjects(recipeCursor);

        verify(dataUtils, times(recipeCount))
                .addIngredients(Matchers.<Recipe>any());
        verify(dataUtils, times(recipeCount))
                .addSteps(Matchers.<Recipe>any());

        Recipe actualRecipe = actualRecipeList.get(0);

        assertThat(actualRecipe.getRecipeId(), is(recipeId));
        assertThat(actualRecipe.getName(), is(recipeName));
        assertThat(actualRecipe.getServings(), is(recipeServings));
        assertThat(actualRecipe.getImageURLString(), is(recipeImageUrl));
    }

    @Test
    public void whenRetrievingIngredientDataFromDatabase_thenIngredientsArePopulated() {
        Recipe recipe = new Recipe(0,
                null,
                0,
                null,
                new ArrayList<Ingredient>(),
                null);
        Cursor ingredientCursor = mock(Cursor.class);

        String name = "Potato";
        float quantity = 100f;
        String meassure = "grams";

        when(ingredientCursor.getString(anyInt()))
                .thenReturn(name).thenReturn(meassure);
        when(ingredientCursor.getFloat(anyInt()))
                .thenReturn(quantity);
        when(ingredientCursor.moveToNext())
                .thenReturn(true, false);

        when(contentResolver.query(eq(IngredientEntry.CONTENT_URI),
                Matchers.<String[]>any(),
                anyString(),
                Matchers.<String[]>any(),
                anyString())).thenReturn(ingredientCursor);

        dataUtils.addIngredients(recipe);

        assertThat(recipe.getIngredients().size(), is(1));
        Ingredient ingredient = recipe.getIngredients().get(0);
        assertThat(ingredient.getName(), is(name));
        assertThat(ingredient.getQuantity(), is(quantity));
        assertThat(ingredient.getMeasure(), is(meassure));
    }

    @Test
    public void whenRetrievingStepDataFromDatabase_thenIngredientsArePopulated() {
        Recipe recipe = new Recipe(0,
                null,
                0,
                null,
                null,
                new ArrayList<Step>());
        Cursor ingredientCursor = mock(Cursor.class);

        int id = 2;
        String shortDescription = "short description";
        String description = "long description";
        String videoURL = "http://video.url";
        String thumbnailURL = "http://image.url";

        when(ingredientCursor.getInt(anyInt())).thenReturn(id);
        when(ingredientCursor.getString(anyInt()))
                .thenReturn(shortDescription)
                .thenReturn(description)
                .thenReturn(videoURL)
                .thenReturn(thumbnailURL);
        when(ingredientCursor.moveToNext()).thenReturn(true,
                                                       false);

        when(contentResolver.query(eq(StepEntry.CONTENT_URI),
                Matchers.<String[]>any(),
                anyString(),
                Matchers.<String[]>any(),
                anyString())).thenReturn(ingredientCursor);

        dataUtils.addSteps(recipe);

        assertThat(recipe.getSteps().size(), is(1));
        Step step = recipe.getSteps().get(0);
        assertThat(step.getStepId(), is(id));
        assertThat(step.getShortDescription(), is(shortDescription));
        assertThat(step.getDescription(), is(description));
        assertThat(step.getVideoURLString(), is(videoURL));
        assertThat(step.getThumbnailURLString(), is(thumbnailURL));
    }

    @Test
    public void whenPersistingRecipeDataObject_thenDataIsInsertedInDatabase() {
        dataUtils.persistRecipes(recipeList);
        int recipeId = recipe.getRecipeId();

        // get recipe ContentValues and insert them in the database for all
        // recipes
        verify(recipe, times(RECIPE_MOCK_COUNT)).toContentValues();
        verify(contentResolver, times(RECIPE_MOCK_COUNT))
                .insert(Matchers.<Uri>any(), Matchers.<ContentValues>any());

        // delegate ingredient insertion to repective methods per all recipes
        verify(recipe, times(RECIPE_MOCK_COUNT)).getIngredients();
        verify(dataUtils, times(RECIPE_MOCK_COUNT))
                .persistIngredients(recipe.getIngredients(), recipeId);

        // delegate step insertion to repective methods per all recipes
        verify(recipe, times(RECIPE_MOCK_COUNT)).getSteps();
        verify(dataUtils,
               times(RECIPE_MOCK_COUNT)).persistSteps(recipe.getSteps(),
                                                      recipeId);


    }

    @Test
    public void whenPersistingIngredientsDataObject_thenDataIsInsertedInDatabase() {
        int recipeId = 7;
        dataUtils.persistIngredients(ingredientList, recipeId);

        // get ingredient ContentValues and add foreign key
        verify(ingredient, times(RECIPE_MOCK_COUNT)).toContentValues();
        verify(contentValues, times(RECIPE_MOCK_COUNT))
                .put(IngredientEntry.COLUMN_RECIPE_FOREING_KEY, recipeId);

        // bulk insert ingredients
        verify(contentResolver, times(1))
                .bulkInsert(eq(IngredientEntry.CONTENT_URI), Matchers.<ContentValues[]>any());
    }

    @Test
    public void whenPersistingStepsDataObject_thenDataIsInsertedInDatabase() {
        int recipeId = 3;
        dataUtils.persistSteps(stepList, recipeId);

        // get step ContentValues and add foreign key
        verify(step, times(RECIPE_MOCK_COUNT)).toContentValues();
        verify(contentValues, times(RECIPE_MOCK_COUNT))
                .put(StepEntry.COLUMN_RECIPE_FOREING_KEY, recipeId);

        // bulk insert ingredients
        verify(contentResolver, times(1))
                .bulkInsert(eq(StepEntry.CONTENT_URI), Matchers.<ContentValues[]>any());
    }
}