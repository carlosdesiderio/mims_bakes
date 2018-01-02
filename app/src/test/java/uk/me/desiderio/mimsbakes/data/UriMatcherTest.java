package uk.me.desiderio.mimsbakes.data;

import android.content.UriMatcher;
import android.net.Uri;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;


import uk.me.desiderio.mimsbakes.data.BakesContract.IngredientEntry;
import uk.me.desiderio.mimsbakes.data.BakesContract.RecipeEntry;
import uk.me.desiderio.mimsbakes.data.BakesContract.StepEntry;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Unit test for {@link UriMatcher} at the {@link BakesContentProvider}
 */

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class UriMatcherTest {

    private UriMatcher matcher;

    @Before
    public void setUp() {
        matcher = BakesContentProvider.buildUriMatcher();
    }

    @Test
    public void whenMatchingRecipeDirectoryUri_thenRecipeCodeIsReturned() {
        int match = matcher.match(RecipeEntry.CONTENT_URI);

        assertThat(match).isNotNegative();
        assertThat(match).isEqualTo(BakesContentProvider.RECIPES);
    }

    @Test
    public void whenMatchingSingleRecipeUri_thenRecipeCodeIsReturned() {
        String pathSegment = "/" + 3;
        Uri singleItemUri = Uri.withAppendedPath(RecipeEntry.CONTENT_URI,
                pathSegment);
        int match = matcher.match(singleItemUri);

        assertThat(match).isNotNegative();
        assertThat(match).isEqualTo(BakesContentProvider.RECIPES_WITH_ID);
    }

    @Test
    public void
    givenASingleRecipeUri_whenProvidingStringAsPathSegmnet_thenRecipeCodeIsNotReturned() {
        String pathSegment = "/" + "cake";
        Uri singleItemUri = Uri.withAppendedPath(RecipeEntry.CONTENT_URI,
                pathSegment);
        int match = matcher.match(singleItemUri);

        assertThat(match).isNegative();
    }

    @Test
    public void whenMatchingIngredientDirectoryUri_thenRecipeCodeIsReturned() {
        int match = matcher.match(IngredientEntry.CONTENT_URI);

        assertThat(match).isNotNegative();
        assertThat(match).isEqualTo(BakesContentProvider.INGREDIENTS);
    }

    @Test
    public void whenMatchingSingleIngredientUri_thenRecipeCodeIsReturned() {
        String pathSegment = "/" + 3;
        Uri singleItemUri = Uri.withAppendedPath(IngredientEntry.CONTENT_URI,
                pathSegment);
        int match = matcher.match(singleItemUri);

        assertThat(match).isNotNegative();
        assertThat(match).isEqualTo(BakesContentProvider.INGREDIENTS_WITH_ID);
    }

    @Test
    public void
    givenASingleIngredientUri_whenProvidingStringAsPathSegmnet_thenRecipeCodeIsNotReturned() {
        String pathSegment = "/" + "flour";
        Uri singleItemUri = Uri.withAppendedPath(IngredientEntry.CONTENT_URI,
                pathSegment);
        int match = matcher.match(singleItemUri);

        assertThat(match).isNegative();
    }


    @Test
    public void whenMatchingStepDirectoryUri_thenRecipeCodeIsReturned() {
        int match = matcher.match(StepEntry.CONTENT_URI);

        assertThat(match).isNotNegative();
        assertThat(match).isEqualTo(BakesContentProvider.STEPS);
    }

    @Test
    public void whenMatchingSingleStepUri_thenRecipeCodeIsReturned() {
        String pathSegment = "/" + 3;
        Uri singleItemUri = Uri.withAppendedPath(StepEntry.CONTENT_URI,
                pathSegment);
        int match = matcher.match(singleItemUri);

        assertThat(match).isNotNegative();
        assertThat(match).isEqualTo(BakesContentProvider.STEPS_WITH_ID);
    }

    @Test
    public void
    givenASingleStepUri_whenProvidingStringAsPathSegmnet_thenRecipeCodeIsNotReturned() {
        String pathSegment = "/" + "bake";
        Uri singleItemUri = Uri.withAppendedPath(StepEntry.CONTENT_URI,
                pathSegment);
        int match = matcher.match(singleItemUri);

        assertThat(match).isNegative();
    }
}
