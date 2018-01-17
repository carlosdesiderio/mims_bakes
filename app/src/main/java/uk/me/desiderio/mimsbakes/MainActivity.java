package uk.me.desiderio.mimsbakes;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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
import uk.me.desiderio.mimsbakes.data.BakesDataUtils;
import uk.me.desiderio.mimsbakes.data.model.Recipe;

import static uk.me.desiderio.mimsbakes.data.BakesDataIntentService.EXTRA_TASK_TYPE;
import static uk.me.desiderio.mimsbakes.data.BakesDataIntentService.TASK_REQUEST_SERVER_DATA;

public class MainActivity extends AppCompatActivity implements LoaderManager
        .LoaderCallbacks<Cursor>, RecipeListAdapter.RecipeClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String EXTRA_RECIPE = "extra_recipe";

    private static final int LOADER_ID = 150;

    private BakesDataUtils dataUtils;
    private RecyclerView recipeRecyclerView;
    private View recipeEmptyViewContainter;

    private RecipeListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataUtils = new BakesDataUtils(this);

        recipeRecyclerView = findViewById(R.id.recipes_recycler_view);
        recipeEmptyViewContainter = findViewById(R.id.recipe_emty_state_container);

        int spanCount = getResources().getInteger(R.integer.main_list_column_count);
        LayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recipeRecyclerView.setLayoutManager(layoutManager);

        adapter = new RecipeListAdapter(this, this);
        recipeRecyclerView.setAdapter(adapter);

        recipeRecyclerView.setHasFixedSize(true);

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        Intent dataIntent = new Intent(this, BakesDataIntentService.class);
        dataIntent.putExtra(EXTRA_TASK_TYPE, TASK_REQUEST_SERVER_DATA);
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
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            adapter.swapCursor(null);
        }
}
