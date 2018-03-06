package uk.me.desiderio.mimsbakes;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.View;

import uk.me.desiderio.mimsbakes.data.BakesContract.RecipeEntry;
import uk.me.desiderio.mimsbakes.data.BakesDataIntentService;
import uk.me.desiderio.mimsbakes.data.model.Recipe;
import uk.me.desiderio.mimsbakes.espresso.RecipeIdlingResource;

import static uk.me.desiderio.mimsbakes.data.BakesDataIntentService.EXTRA_DATA_TASK_TYPE;
import static uk.me.desiderio.mimsbakes.data.BakesDataIntentService.TASK_REQUEST_SERVER_DATA;

public class MainActivity extends AppCompatActivity implements LoaderManager
        .LoaderCallbacks<Cursor>, RecipeListAdapter.RecipeClickListener {

    public static final String EXTRA_RECIPE = "extra_recipe";
    public static final String EXTRA_RECIPE_ID = "extra_recipe_id";

    private static final int LOADER_ID = 150;

    private View recipeEmptyViewContainter;

    private RecipeListAdapter adapter;

    private RecipeIdlingResource idlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recipeRecyclerView = findViewById(R.id.recipes_recycler_view);
        recipeEmptyViewContainter = findViewById(R.id.recipe_emty_state_container);

        int spanCount = getResources().getInteger(R.integer.main_list_column_count);
        LayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recipeRecyclerView.setLayoutManager(layoutManager);

        adapter = new RecipeListAdapter(this, this);
        recipeRecyclerView.setAdapter(adapter);

        recipeRecyclerView.setHasFixedSize(true);

        setIdleResourceState(false);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        Intent dataIntent = new Intent(this, BakesDataIntentService.class);
        dataIntent.putExtra(EXTRA_DATA_TASK_TYPE, TASK_REQUEST_SERVER_DATA);
        startService(dataIntent);
    }


    @Override
    public void onClick(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra(EXTRA_RECIPE, recipe);
        startActivity(intent);
    }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(this,
                    RecipeEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            adapter.swapCursor(data);

            if(data != null && data.getCount() > 0) {
                recipeEmptyViewContainter.setVisibility(View.GONE);
            } else {
                recipeEmptyViewContainter.setVisibility(View.VISIBLE);
            }

            setIdleResourceState(true);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            adapter.swapCursor(null);
        }


    /**
     * creates and returns a new {@link RecipeIdlingResource} - only used in tests
     */
    @VisibleForTesting
    @NonNull
    public RecipeIdlingResource getIdlingResource() {
        if (idlingResource == null) {
            idlingResource = new RecipeIdlingResource();
        }
        return idlingResource;
    }

    @VisibleForTesting
    private void setIdleResourceState(boolean isIdle) {
        if(idlingResource != null) {
            idlingResource.setIdleState(isIdle);
        }
    }
}
