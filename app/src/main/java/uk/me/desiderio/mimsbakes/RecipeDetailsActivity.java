package uk.me.desiderio.mimsbakes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import uk.me.desiderio.mimsbakes.data.BakesContract.ShoppingEntry;
import uk.me.desiderio.mimsbakes.data.BakesDataIntentService;
import uk.me.desiderio.mimsbakes.data.BakesDataUtils;
import uk.me.desiderio.mimsbakes.data.model.Ingredient;
import uk.me.desiderio.mimsbakes.data.model.Recipe;
import uk.me.desiderio.mimsbakes.data.model.Step;

import static uk.me.desiderio.mimsbakes.MainActivity.EXTRA_RECIPE;
import static uk.me.desiderio.mimsbakes.StepVideoActivity.EXTRA_STEP;
import static uk.me.desiderio.mimsbakes.data.BakesDataIntentService.BROADCAST_ACTION_INGREDIENT_SHOPPING;
import static uk.me.desiderio.mimsbakes.data.BakesDataIntentService.EXTRA_INGREDIENT_LIST;
import static uk.me.desiderio.mimsbakes.data.BakesDataIntentService.EXTRA_INGREDIENT_NAME;
import static uk.me.desiderio.mimsbakes.data.BakesDataIntentService.EXTRA_INGREDIENT_SHOPPING_ACTION;
import static uk.me.desiderio.mimsbakes.data.BakesDataIntentService.EXTRA_RECIPE_ID;
import static uk.me.desiderio.mimsbakes.data.BakesDataIntentService.EXTRA_TASK_TYPE;
import static uk.me.desiderio.mimsbakes.data.BakesDataIntentService.SHOPPING_ACTION_INGREDIENT_DELETE;
import static uk.me.desiderio.mimsbakes.data.BakesDataIntentService.SHOPPING_ACTION_INGREDIENT_INSERT;
import static uk.me.desiderio.mimsbakes.data.BakesDataIntentService.TASK_BULK_DELETE_SHOP_INGREDIENT;
import static uk.me.desiderio.mimsbakes.data.BakesDataIntentService.TASK_BULK_INSERT_SHOP_INGREDIENT;
import static uk.me.desiderio.mimsbakes.data.BakesDataIntentService.TASK_DELETE_SHOP_INGREDIENT;
import static uk.me.desiderio.mimsbakes.data.BakesDataIntentService.TASK_INSERT_SHOP_INGREDIENT;

public class RecipeDetailsActivity extends AppCompatActivity implements
        RecipeDetailsFragment.OnRecipeDetailsFragmentItemClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = RecipeDetailsActivity.class.getSimpleName();
    private static final int STEP_REQUEST_CODE = 8;
    private static final int LOADER_ID = 667;

    // true in larger tablet screen where two pane layout will be shown
    private boolean isTwoPane;
    private Recipe recipe;

    private RecipeDetailsFragment detailsFragment;
    private StepVideoFragment stepVideoFragment;

    private final BroadcastReceiver ingredientActionsBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = getIngredientShopActionMessage(intent);
            showSnackBar(message);
            Log.d(TAG, "onReceive: " + message);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_recipe);

        recipe = getIntent().getParcelableExtra(EXTRA_RECIPE);

        initLayoutType();

        detailsFragment = (RecipeDetailsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.recipe_details_fragment);

        // video fragment only present in two pane layout file
        if(isTwoPane) {
            stepVideoFragment = (StepVideoFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.step_video_fragment);
        }

        detailsFragment.setData(recipe);

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(
                ingredientActionsBroadcastReceiver,
                new IntentFilter(BROADCAST_ACTION_INGREDIENT_SHOPPING));

        super.onResume();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).
                unregisterReceiver(ingredientActionsBroadcastReceiver);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shop_all:
                toggleAllIngredientInShoppingList();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }


    private boolean areAllIngredientInShoppingList() {
        List<Ingredient> ingredientList = recipe.getIngredients();

        for(Ingredient ingredient : ingredientList) {
            if(ingredient.getShoppingFlag() == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * adds or removes all ingredients to/from the shopping list
     * this is based on whether the ALL ingredients are on the list or not
     * when only some ingredients are on the list, it will add the remaining ingredient to it
     */
    private void toggleAllIngredientInShoppingList() {
        int recipeId = detailsFragment.getData().getRecipeId();
        List<Ingredient> ingredientList = detailsFragment.getData().getIngredients();
        String task = (areAllIngredientInShoppingList()) ?
                TASK_BULK_DELETE_SHOP_INGREDIENT :
                TASK_BULK_INSERT_SHOP_INGREDIENT;

        Intent dataIntent = new Intent(this, BakesDataIntentService.class);
        dataIntent.putExtra(EXTRA_TASK_TYPE, task);
        dataIntent.putExtra(EXTRA_RECIPE_ID, recipeId);
        dataIntent.putParcelableArrayListExtra(
                EXTRA_INGREDIENT_LIST,
                new ArrayList<>(ingredientList));
        startService(dataIntent);
    }

    /**
     * initialises layout flat so that the view shows as a one or two pane layout
     */
    private void initLayoutType() {
        isTwoPane = findViewById(R.id.step_video_fragment) != null;
    }


    /**
     * returns message to be displayed in the snackbar
     * feeds back the user on how many ingredients have been
     * added / removed from the shopping list
     */
    @NonNull
    private String getIngredientShopActionMessage(Intent intent) {
        StringBuilder stringBuilder;

        if(intent.hasExtra(EXTRA_INGREDIENT_NAME)) {
            String ingredientName = intent.getStringExtra(EXTRA_INGREDIENT_NAME);
            stringBuilder = new StringBuilder(ingredientName);
        } else {
            stringBuilder = new StringBuilder(getString(R.string.shopping_message_all_ingredients));
        }

        String action = intent.getStringExtra(EXTRA_INGREDIENT_SHOPPING_ACTION);
        switch (action) {
            case SHOPPING_ACTION_INGREDIENT_DELETE:
                stringBuilder.append(getString(R.string.shopping_message_removed));
                break;
            case SHOPPING_ACTION_INGREDIENT_INSERT:
                stringBuilder.append(getString(R.string.shopping_message_added));
                break;
        }
        stringBuilder.append(getString(R.string.shopping_message_end));

        return stringBuilder.toString();
    }

    private void showSnackBar(String message) {
        Snackbar.make(findViewById(R.id.recipe_details_fragment_container), message,
                Snackbar.LENGTH_SHORT).show();
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

    @Override
    public void onRecipeIngredientSelected(Ingredient ingredient, int recipeId) {
        // sets whether we need to insert or remove ingredient from shopping list
        // based on whether the ingredient is or not in the list
        String task = (ingredient.getShoppingFlag() == 0)?
                TASK_INSERT_SHOP_INGREDIENT :
                TASK_DELETE_SHOP_INGREDIENT;

        Intent intent = new Intent(this, BakesDataIntentService.class);
        intent.putExtra(EXTRA_TASK_TYPE, task);
        intent.putExtra(EXTRA_RECIPE_ID, recipeId);
        intent.putExtra(EXTRA_INGREDIENT_NAME, ingredient.getName());

        startService(intent);
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

    /**
     * implements {@link LoaderManager.LoaderCallbacks}
     * updates view when ingredients are added or removed from the shopping list
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = ShoppingEntry.COLUMN_RECIPE_FOREING_KEY + "=? ";
        String recipeIdString = String.valueOf(recipe.getRecipeId());
        String[] selectionArgs = new String[]{recipeIdString};

        return new CursorLoader(this,
                ShoppingEntry.CONTENT_URI,
                null,
                selection,
                selectionArgs,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        List<Ingredient> ingredientList = BakesDataUtils.getIngredientListFromCursor(cursor);
        detailsFragment.updateIngredients(ingredientList);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
