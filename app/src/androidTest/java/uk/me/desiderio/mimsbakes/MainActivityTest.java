package uk.me.desiderio.mimsbakes;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import uk.me.desiderio.mimsbakes.espresso.RecipeIdlingResource;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.BundleMatchers.hasEntry;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtras;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static uk.me.desiderio.mimsbakes.MatcherUtils.atPosition;
import static uk.me.desiderio.mimsbakes.MatcherUtils.hasDrawable;
import static uk.me.desiderio.mimsbakes.MatcherUtils.withRecipeName;

/**
 * Intrumented test for the {@link MainActivity}
 */

@RunWith(Parameterized.class)
public class MainActivityTest {

    private static final int RECIPE_COUNT = 4;

    @Parameterized.Parameters
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {0, "Nutella Pie", "8 Servings"},
                {1, "Brownies", "8 Servings"},
                {2, "Yellow Cake", "8 Servings"},
                {3, "Cheesecake", "8 Servings"}});
    }

    private String expectedRecipeName;
    private String expectedRecipeSubtitle;
    private int recipePosition;

    @Rule
    public IntentsTestRule<MainActivity> activityRule =
            new IntentsTestRule<>(MainActivity.class);
    private RecipeIdlingResource idlingResource;

    public MainActivityTest(int expectedPosition,
                            String expectedRecipeName,
                            String expectedRecipeSubtitle) {
        this.expectedRecipeName = expectedRecipeName;
        this.expectedRecipeSubtitle = expectedRecipeSubtitle;
        this.recipePosition = expectedPosition;
    }


    @Before
    public void setup() {
        idlingResource = activityRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
    }


    @Test
    public void whenMainActivityOpens_hasRightNumberOfItems() {
        onView(withId(R.id.recipes_recycler_view))
                .check(new RecyclerViewCountAssertion(RECIPE_COUNT));
    }

    @Test
    public void whenScrollToItem_thenItemIsPopulatedWithExpectedName() {
        onView(withId(R.id.recipes_recycler_view))
                .perform(scrollToPosition(recipePosition))
                .check(matches(atPosition(recipePosition,
                                          hasDescendant(withText(
                                                  expectedRecipeName)))));
    }

    @Test
    public void whenScrollToItem_thenItemIsPopulatedWithExpecteSubtitle() {
        onView(withId(R.id.recipes_recycler_view))
                .perform(scrollToPosition(recipePosition))
                .check(matches(atPosition(recipePosition,
                                          hasDescendant(withText(
                                                  expectedRecipeSubtitle)))));
    }

    @Test
    public void whenScrollToItem_thenItemHasAHeroImage() {
        onView(withId(R.id.recipes_recycler_view))
                .perform(scrollToPosition(recipePosition))
                .check(matches(atPosition(recipePosition,
                        hasDescendant(allOf(withId(R.id.recipe_item_image_view),
                                hasDrawable())))));
    }


    @Test
    public void whenItemIsSelected_thenRightIntentIsSent() {
        onView(withId(R.id.recipes_recycler_view))
                .perform(RecyclerViewActions.actionOnItem(
                        hasDescendant(withText(expectedRecipeName)), click()));

        String expectedComponentName = RecipeDetailsActivity.class.getName();

        intended(allOf(
                isInternal(),
                hasComponent(expectedComponentName),
                hasExtras(
                        hasEntry(MainActivity.EXTRA_RECIPE,
                                withRecipeName(expectedRecipeName)))));
    }

    @After
    public void teardown() {
        IdlingRegistry.getInstance().unregister(idlingResource);
    }


}
