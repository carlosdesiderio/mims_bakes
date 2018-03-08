package uk.me.desiderio.mimsbakes.network;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import uk.me.desiderio.mimsbakes.data.model.Ingredient;
import uk.me.desiderio.mimsbakes.data.model.Recipe;
import uk.me.desiderio.mimsbakes.data.model.Step;

import static org.junit.Assert.*;
import static uk.me.desiderio.mimsbakes.network.BakesJSONParserUtils.parseBakeRecipesJsonString;
import static uk.me.desiderio.mimsbakes.network.BakesJSONParserUtils.readJsonFile;

/**
 * test {@link BakesJSONParserUtils}
 */
@RunWith(JUnit4.class)
public class BakesJSONParserUtilsTest {

    private static final int MOCK_RECIPE_EXPECTED_COUNT = 4;

    private static final int MOCK_RECIPE_EXPECTED_ID = 1;
    private static final String MOCK_RECIPE_EXPECTED_NAME = "Nutella Pie";
    private static final int MOCK_RECIPE_EXPECTED_SERVINGS = 8;
    private static final String MOCK_RECIPE_EXPECTED_IMAGE_URL = "";

    private static final int MOCK_RECIPE_EXPECTED_INGREDIENT_COUNT = 9;
    private static final String MOCK_RECIPE_EXPECTED_INGREDIENT_NAME
            = "Graham Cracker crumbs";
    private static final float MOCK_RECIPE_EXPECTED_INGREDIENT_QUANTITY = 2;
    private static final String MOCK_RECIPE_EXPECTED_INGREDIENT_MEASURE = "CUP";

    private static final int MOCK_RECIPE_EXPECTED_STEP_COUNT = 7;
    private static final int MOCK_RECIPE_EXPECTED_STEP_ID = 3;
    private static final String MOCK_RECIPE_EXPECTED_STEP_SHORT_DESC =
            "Press the crust into baking form.";
    private static final String MOCK_RECIPE_EXPECTED_STEP_DESC =
            "3. Press the cookie crumb mixture into the prepared pie pan and bake for 12 minutes. Let crust cool to room temperature.";
    private static final String MOCK_RECIPE_EXPECTED_STEP_VIDEO_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd9cb_4-press-crumbs-in-pie-plate-creampie/4-press-crumbs-in-pie-plate-creampie.mp4";
    private static final String MOCK_RECIPE_EXPECTED_STEP_THUMBNAIL_URL = "";

    private Gson gson;
    @Before
    public void setUp() throws Exception {
        gson = new Gson();
    }

    @Test
    public void whenreadJsonFile_thenReturnsNonEmptyString() throws Exception {

        String jsonResponseString = readJsonFile("json/baking.json");

        assertNotNull("readJsonFile: No JSON was return",
                      jsonResponseString);
    }

    @Test
    public void whenreadJsonFile_thenReturnsAValidJSONString()
            throws Exception {

        String jsonResponseString = readJsonFile("json/baking.json");

        try {
            gson.fromJson(jsonResponseString, Object.class);
        } catch(com.google.gson.JsonSyntaxException ex) {
            fail("String has not a valid Json format " + ex);
        }

    }

    @Test
    public void whenParseBakeRecipesJsonString_thenReturnsPopulatedDataObjectArray()
            throws Exception {
        String jsonResponseString = readJsonFile("json/baking.json");
        List<Recipe> recipes = parseBakeRecipesJsonString(jsonResponseString);

        assertNotNull(recipes);
        assertEquals("Wrong number of element parsed",
                     MOCK_RECIPE_EXPECTED_COUNT,
                     recipes.size());
    }

    @Test
    public void whenParseBakeRecipesJsonString_thenReturnsInstatiatedRecipe()
            throws Exception {
        String jsonResponseString = readJsonFile("json/baking.json");
        List<Recipe> recipes = parseBakeRecipesJsonString(jsonResponseString);
        Recipe recipe = recipes.get(0);

        assertNotNull(recipe);
        assertTrue("Wrong type parsed: Expected Recipe",
                   recipe instanceof Recipe);

        assertEquals("Wrong parsed recipe id",
                     MOCK_RECIPE_EXPECTED_ID,
                     recipe.getRecipeId());
        assertEquals("Wrong parsed recipe name",
                     MOCK_RECIPE_EXPECTED_NAME,
                     recipe.getName());
        assertEquals("Wrong parsed recipe servings",
                     MOCK_RECIPE_EXPECTED_SERVINGS,
                     recipe.getServings());
        assertEquals("Wrong parsed recipe url string",
                     MOCK_RECIPE_EXPECTED_IMAGE_URL,
                     recipe.getImageURLString());
    }

    @Test
    public void whenParseBakeRecipesJsonString_thenReturnsRecipeWithIngredients()
            throws Exception {
        String jsonResponseString = readJsonFile("json/baking.json");
        List<Recipe> recipes = parseBakeRecipesJsonString(jsonResponseString);
        Recipe recipe = recipes.get(0);
        List<Ingredient> ingredients = recipe.getIngredients();
        Ingredient ingredient = ingredients.get(0);

        assertNotNull("No ingredients were parsed", ingredients);
        assertEquals("",
                     MOCK_RECIPE_EXPECTED_INGREDIENT_COUNT,
                     ingredients.size());

        assertEquals("Wrong parsed ingredient name",
                     MOCK_RECIPE_EXPECTED_INGREDIENT_NAME,
                     ingredient.getName());
        assertEquals("Wrong parsed ingredient quantity",
                     MOCK_RECIPE_EXPECTED_INGREDIENT_QUANTITY,
                     ingredient.getQuantity(), 0);
        assertEquals("Wrong parsed ingredient measure unit",
                     MOCK_RECIPE_EXPECTED_INGREDIENT_MEASURE,
                     ingredient.getMeasure());
    }

    @Test
    public void whenParseBakeRecipesJsonString_thenReturnsRecipeWithSteps()
            throws Exception {
        String jsonResponseString = readJsonFile("json/baking.json");
        List<Recipe> recipes = parseBakeRecipesJsonString(jsonResponseString);
        Recipe recipe = recipes.get(0);
        List<Step> steps = recipe.getSteps();
        Step step = steps.get(3);

        assertNotNull("No steps were parsed", steps);
        assertEquals("", MOCK_RECIPE_EXPECTED_STEP_COUNT, steps.size());

        assertEquals("Wrong parsed step id",
                     MOCK_RECIPE_EXPECTED_STEP_ID,
                     step.getStepId());
        assertEquals("Wrong parsed step short description",
                     MOCK_RECIPE_EXPECTED_STEP_SHORT_DESC,
                     step.getShortDescription());
        assertEquals("Wrong parsed step description",
                     MOCK_RECIPE_EXPECTED_STEP_DESC,
                     step.getDescription());
        assertEquals("Wrong parsed step video url",
                     MOCK_RECIPE_EXPECTED_STEP_VIDEO_URL,
                     step.getVideoURLString());
        assertEquals("Wrong parsed step thumbnail url",
                     MOCK_RECIPE_EXPECTED_STEP_THUMBNAIL_URL,
                     step.getThumbnailURLString());
    }

}