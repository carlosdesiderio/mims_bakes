package uk.me.desiderio.mimsbakes;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.me.desiderio.mimsbakes.data.BakesContract;
import uk.me.desiderio.mimsbakes.data.BakesContract.RecipeEntry;
import uk.me.desiderio.mimsbakes.data.BakesDataIntentService;
import uk.me.desiderio.mimsbakes.data.BakesDataUtils;
import uk.me.desiderio.mimsbakes.data.model.Recipe;
import uk.me.desiderio.mimsbakes.network.BakesRequestUtils;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int LOADER_ID = 150;

    private BakesDataUtils dataUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataUtils = new BakesDataUtils(this);


        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        Intent dataIntent = new Intent(this, BakesDataIntentService.class);
        startService(dataIntent);
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


            List<Recipe> recipeList = dataUtils.getRecipeDataObjects(data);
            for(Recipe recipe: recipeList) {
                Log.d(TAG, "onLoadFinished : \n: " + recipe);

            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }


}
