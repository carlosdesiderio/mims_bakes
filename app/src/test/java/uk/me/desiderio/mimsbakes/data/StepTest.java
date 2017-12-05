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

import uk.me.desiderio.mimsbakes.data.model.Step;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.when;


/**
 * provides test for data class {@link Step}
 */

@RunWith(RobolectricTestRunner.class)
@Config(manifest= Config.NONE)
public class StepTest {

    private static final int MOCK_RECIPE_STEP_ID = 3;
    private static final String MOCK_RECIPE_STEP_SHORT_DESC = "Press the crust into baking form.";
    private static final String MOCK_RECIPE_STEP_DESC = "3. Press the cookie crumb mixture into the prepared pie pan and bake for 12 minutes. Let crust cool to room temperature.";
    private static final String MOCK_RECIPE_STEP_VIDEO_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd9cb_4-press-crumbs-in-pie-plate-creampie/4-press-crumbs-in-pie-plate-creampie.mp4";
    private static final String MOCK_RECIPE_STEP_THUMBNAIL_URL = "http://someaddress/path";

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    Step step;

    @Before
    public void setUp() {
        when(step.getStepId()).thenReturn(MOCK_RECIPE_STEP_ID);
        when(step.getShortDescription()).thenReturn(MOCK_RECIPE_STEP_SHORT_DESC);
        when(step.getDescription()).thenReturn(MOCK_RECIPE_STEP_DESC);
        when(step.getVideoURLString()).thenReturn(MOCK_RECIPE_STEP_VIDEO_URL);
        when(step.getThumbnailURLString()).thenReturn(MOCK_RECIPE_STEP_THUMBNAIL_URL);

        when(step.toContentValues()).thenCallRealMethod();
    }

    @Test
    public void givenAnIngredient_whenAccessingProperties_returnAllAsExpected() {
        assertEquals("Step returned wrong id", MOCK_RECIPE_STEP_ID, step.getStepId());
        assertEquals("Step returned wrong short description", MOCK_RECIPE_STEP_SHORT_DESC, step.getShortDescription());
        assertEquals("Step returned wrong long description", MOCK_RECIPE_STEP_DESC, step.getDescription());
        assertEquals("Step returned wrong long description", MOCK_RECIPE_STEP_VIDEO_URL, step.getVideoURLString());
        assertEquals("Step returned wrong long description", MOCK_RECIPE_STEP_THUMBNAIL_URL, step.getThumbnailURLString());
    }

    @Test
    public void givenARecipe_whenGettingContentValue_returnsAsExpected() {
        ContentValues values = step.toContentValues();

        assertNotNull("Step return null ContentValues", values);

        int id = values.getAsInteger(Step.NODE_NAME_STEP_ID);
        String shortDesc = values.getAsString(Step.NODE_NAME_STEP_SHORT_DESC);
        String desc = values.getAsString(Step.NODE_NAME_STEP_DESC);
        String videoUrl = values.getAsString(Step.NODE_NAME_STEP_VIDEO_URL);
        String thumbnailUrl = values.getAsString(Step.NODE_NAME_STEP_THUMBNAIL_URL);

        assertEquals("Step ContentValues returned with wrong id", MOCK_RECIPE_STEP_ID, id);
        assertEquals("Step ContentValues returned with wrong short description", MOCK_RECIPE_STEP_SHORT_DESC, shortDesc);
        assertEquals("Step ContentValues returned with wrong long description", MOCK_RECIPE_STEP_DESC, desc);
        assertEquals("Step ContentValues returned with wrong video url", MOCK_RECIPE_STEP_VIDEO_URL, videoUrl);
        assertEquals("Step ContentValues returned with wrong thumbnail url", MOCK_RECIPE_STEP_THUMBNAIL_URL, thumbnailUrl);

    }
}
