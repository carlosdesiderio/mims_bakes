package uk.me.desiderio.mimsbakes.data;

import android.content.ContentValues;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import uk.me.desiderio.mimsbakes.data.model.Ingredient;
import uk.me.desiderio.mimsbakes.data.model.Recipe;
import uk.me.desiderio.mimsbakes.data.model.Step;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.when;


/**
 * provides test for data class {@link Recipe}
 */

@RunWith(RobolectricTestRunner.class)
@Config(manifest= Config.NONE)
public class RecipeTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private static final int MOCK_RECIPE_ID = 66;
    private static final String MOCK_RECIPE_NAME = "Nutella Pie";
    private static final int MOCK_RECIPE_SERVINGS = 8;
    private static final String MOCK_RECIPE_IMAGE_URL = "http://someadrres.com/path";

    private static List<Ingredient> mockRecipeIngredients;
    private static List<Step> mockRecipeSteps;

    @Mock Recipe recipe;
    @Mock Ingredient ingredient;
    @Mock Step step;

    @Before
    public void setUp() {
        mockRecipeIngredients = Arrays.asList(ingredient, ingredient);
        mockRecipeSteps = Arrays.asList(step, step, step);

        when(recipe.getRecipeId()).thenReturn(MOCK_RECIPE_ID);
        when(recipe.getName()).thenReturn(MOCK_RECIPE_NAME);
        when(recipe.getServings()).thenReturn(MOCK_RECIPE_SERVINGS);
        when(recipe.getImageURLString()).thenReturn(MOCK_RECIPE_IMAGE_URL);
        when(recipe.getIngredients()).thenReturn(mockRecipeIngredients);
        when(recipe.getSteps()).thenReturn(mockRecipeSteps);

        when(recipe.toContentValues()).thenCallRealMethod();
    }


    @Test
    public void givenARecipe_whenAccessingProperties_returnAllAsExpected() {

        assertEquals("Recipe returned wrong id", MOCK_RECIPE_ID, recipe.getRecipeId());
        assertEquals("Recipe returned wrong name", MOCK_RECIPE_NAME, recipe.getName());
        assertEquals("Recipe returned wrong serving", MOCK_RECIPE_SERVINGS, recipe.getServings());
        assertEquals("Recipe returned wrong url", MOCK_RECIPE_IMAGE_URL, recipe.getImageURLString());
        assertEquals("Recipe returned wrong ingredients", mockRecipeIngredients, recipe.getIngredients());
        assertEquals("Recipe returned wrong steps", mockRecipeSteps, recipe.getSteps());
    }

    @Test
    public void givenARecipe_whenGettingContentValue_returnsAsExpected() {
        ContentValues values = recipe.toContentValues();

        assertNotNull("Recipe return null ContentValues", values);

        int id = values.getAsInteger(Recipe.NODE_NAME_ID);
        String name = values.getAsString(Recipe.NODE_NAME_NAME);
        int serving = values.getAsInteger(Recipe.NODE_NAME_SERVINGS);
        String url = values.getAsString(Recipe.NODE_NAME_IMAGE);

        assertEquals("Recipe ContentValues returned with wrong id", MOCK_RECIPE_ID, id);
        assertEquals("Recipe ContentValues returned with wrong name", MOCK_RECIPE_NAME, name);
        assertEquals("Recipe ContentValues returned with wrong serving", MOCK_RECIPE_SERVINGS, serving);
        assertEquals("Recipe ContentValues returned with wrong url", MOCK_RECIPE_IMAGE_URL, url);

    }
}
