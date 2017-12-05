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

import uk.me.desiderio.mimsbakes.data.model.Ingredient;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.when;


/**
 * provides test for data class {@link Ingredient}
 */

@RunWith(RobolectricTestRunner.class)
@Config(manifest= Config.NONE)
public class IngredientTest {

    private static final String MOCK_RECIPE_INGREDIENT_NAME = "Graham Cracker crumbs";
    private static final float MOCK_RECIPE_INGREDIENT_QUANTITY = 2;
    private static final String MOCK_RECIPE_INGREDIENT_MEASURE = "CUP";

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    Ingredient ingredient;

    @Before
    public void setUp() {
        when(ingredient.getName()).thenReturn(MOCK_RECIPE_INGREDIENT_NAME);
        when(ingredient.getQuantity()).thenReturn(MOCK_RECIPE_INGREDIENT_QUANTITY);
        when(ingredient.getMeasure()).thenReturn(MOCK_RECIPE_INGREDIENT_MEASURE);

        when(ingredient.toContentValues()).thenCallRealMethod();

    }

    @Test
    public void givenAnIngredient_whenAccessingProperties_returnAllAsExpected() {
        assertEquals("Ingredient returned wrong name", MOCK_RECIPE_INGREDIENT_NAME, ingredient.getName());
        assertEquals("Ingredient returned wrong quantity value", MOCK_RECIPE_INGREDIENT_QUANTITY, ingredient.getQuantity());
        assertEquals("Ingredient returned wrong measure units", MOCK_RECIPE_INGREDIENT_MEASURE, ingredient.getMeasure());
    }

    @Test
    public void givenARecipe_whenGettingContentValue_returnsAsExpected() {
        ContentValues values = ingredient.toContentValues();

        assertNotNull("Ingredient return null ContentValues", values);


        String name = values.getAsString(Ingredient.NODE_NAME_INGREDIENT_NAME);
        float quantity = values.getAsFloat(Ingredient.NODE_NAME_INGREDIENT_QUANTITY);
        String measure = values.getAsString(Ingredient.NODE_NAME_INGREDIENT_MEASURE);

        assertEquals("Recipe ContentValues returned with wrong name", MOCK_RECIPE_INGREDIENT_NAME, name);
        assertEquals("Recipe ContentValues returned with wrong quantity", MOCK_RECIPE_INGREDIENT_QUANTITY, quantity, 0);
        assertEquals("Recipe ContentValues returned with wrong measure", MOCK_RECIPE_INGREDIENT_MEASURE, measure);

    }
}
