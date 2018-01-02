package uk.me.desiderio.mimsbakes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import uk.me.desiderio.mimsbakes.data.model.Recipe;
import uk.me.desiderio.mimsbakes.data.model.Step;

import static uk.me.desiderio.mimsbakes.MainActivity.EXTRA_RECIPE;
import static uk.me.desiderio.mimsbakes.StepVideoActivity.EXTRA_STEP;

public class RecipeDetailsActivity extends AppCompatActivity implements RecipeDetailsFragment.OnRecipeDetailsFragmentItemClickListener {

    public static final int STEP_REQUEST_CODE = 8;

    // true in larger tablet screen where two pane layout will be shown
    private boolean isTwoPane;

    private RecipeDetailsFragment detailsFragment;
    private StepVideoFragment stepVideoFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_recipe);

        Recipe recipe = getIntent().getParcelableExtra(EXTRA_RECIPE);

        initLayoutType();

        detailsFragment = (RecipeDetailsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.recipe_details_fragment);

        // video fragment only present in two pane layout file
        if(isTwoPane) {
            stepVideoFragment = (StepVideoFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.step_video_fragment);
        }

        detailsFragment.setData(recipe);
    }

    /**
     * initialises layout flat so that the view shows as a one or two pane layout
     */
    private void initLayoutType() {
        isTwoPane = findViewById(R.id.step_video_fragment) != null;
    }

    @Override
    public void onRecipeStepSelected(Step step) {
        if(isTwoPane) {
            stepVideoFragment.swapData(step);
        } else {
            Intent intent = new Intent(this, StepVideoActivity.class);
            intent.putExtra(EXTRA_STEP, step);
            startActivityForResult(intent, STEP_REQUEST_CODE);
        }
    }

    /**
     * When rotation to landscape in tablets, {@link StepVideoActivity} is closed to show the two
     * pain {@link RecipeDetailsActivity}
     *
     * Ensure that the {@link RecipeDetailsFragment} (right pane) is updated with the same step
     * that the shown in the {@link StepVideoActivity}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(isTwoPane) {
            Step step = data.getParcelableExtra(EXTRA_STEP);
            stepVideoFragment.swapData(step);
        }
    }
}
