package uk.me.desiderio.mimsbakes.data;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.lang.annotation.Retention;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.me.desiderio.mimsbakes.data.BakesContract.ShoppingEntry;
import uk.me.desiderio.mimsbakes.data.model.Ingredient;
import uk.me.desiderio.mimsbakes.data.model.Recipe;
import uk.me.desiderio.mimsbakes.network.BakesRequestUtils;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Intent Service to persist baking data request response
 */

public class BakesDataIntentService extends IntentService {

    private static final String TAG = BakesDataIntentService.class.getSimpleName();
    private static final String SERVICE_NAME =
            BakesDataIntentService.class.getSimpleName();

    public static final String BROADCAST_ACTION_INGREDIENT_SHOPPING =
            "data_service_broadcast_shopping_action";

    public static final String EXTRA_DATA_TASK_TYPE =
            "data_service_task_type";
    public static final String EXTRA_DATA_RECIPE_ID =
            "data_service_recipe_id";
    public static final String EXTRA_DATA_INGREDIENT_LIST =
            "data_service_ingredient_list";
    public static final String EXTRA_DATA_INGREDIENT_NAME =
            "data_service_ingredient_name";
    public static final String EXTRA_DATA_INGREDIENT_SHOPPING_ACTION =
            "data_service_ingredient_shopping_action";

    public static final String SHOPPING_ACTION_INGREDIENT_INSERT =
            "data_service_broadcast_insert";
    public static final String SHOPPING_ACTION_INGREDIENT_DELETE =
            "data_service_broadcast_delete";

    @Retention(SOURCE)
    @StringDef({
            TASK_REQUEST_SERVER_DATA,
            TASK_INSERT_SHOP_INGREDIENT,
            TASK_DELETE_SHOP_INGREDIENT,
            TASK_BULK_INSERT_SHOP_INGREDIENT,
            TASK_BULK_DELETE_SHOP_INGREDIENT
    })
    public @interface ServiceTaskType {}
    public static final String TASK_REQUEST_SERVER_DATA =
            "data_service_server_request";
    public static final String TASK_BULK_INSERT_SHOP_INGREDIENT =
            "data_service_bulk_ingredient";
    public static final String TASK_BULK_DELETE_SHOP_INGREDIENT =
            "data_service_bulk_delete_ingredient";
    public static final String TASK_INSERT_SHOP_INGREDIENT =
            "data_service_insert_ingredient";
    public static final String TASK_DELETE_SHOP_INGREDIENT =
            "data_service_delete_ingredient";

    private final BakesDataUtils dataUtils;

    public BakesDataIntentService() {
        super(SERVICE_NAME);
        dataUtils = new BakesDataUtils(this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String taskType = intent.getStringExtra(EXTRA_DATA_TASK_TYPE);
        switch (taskType) {
            case TASK_REQUEST_SERVER_DATA:
                requestServerData();
                break;
            case TASK_INSERT_SHOP_INGREDIENT:
                insertIngredientInShoppingList(intent);
                break;
            case TASK_DELETE_SHOP_INGREDIENT:
                deleteIngredientInShoppingList(intent);
                break;
            case TASK_BULK_INSERT_SHOP_INGREDIENT:
                bulkInsertIngredinetInShoppingList(intent);
                break;
            case TASK_BULK_DELETE_SHOP_INGREDIENT:
                bulkDeleteIngredientInShoppingList(intent);
                break;
        }

    }

    private void bulkDeleteIngredientInShoppingList(Intent intent) {
        Log.d(TAG, "bulkDeleteIngredientInShoppingList: ");
        int recipeId = intent.getIntExtra(EXTRA_DATA_RECIPE_ID, 0);

        String selection = ShoppingEntry.COLUMN_RECIPE_FOREING_KEY + "= ? ";
        String[] selectionArgs = new String[]{String.valueOf(recipeId)};
        int rowsDeleted = getContentResolver().delete(
                ShoppingEntry.CONTENT_URI,
                selection,
                selectionArgs);

        if(rowsDeleted > 0) {
            sendIngredientActionBroadcast(SHOPPING_ACTION_INGREDIENT_DELETE,
                                          null);
        }
    }

    private void deleteIngredientInShoppingList(Intent intent) {
        int recipeId = intent.getIntExtra(EXTRA_DATA_RECIPE_ID, 0);
        String ingredientName = intent.getStringExtra(EXTRA_DATA_INGREDIENT_NAME);
        Log.d(TAG, "deleteIngredientInShoppingList: " + ingredientName);

        String selection =
                ShoppingEntry.COLUMN_RECIPE_FOREING_KEY + "= ? AND " +
                ShoppingEntry.COLUMN_NAME_INGREDIENT_NAME + "= ? ";
        String[] selectionArgs = new String[]{String.valueOf(recipeId),
                                                ingredientName};
        int rowsDeleted = getContentResolver().delete(ShoppingEntry.CONTENT_URI,
                                                      selection,
                                                      selectionArgs);

        if(rowsDeleted > 0) {
            sendIngredientActionBroadcast(SHOPPING_ACTION_INGREDIENT_DELETE,
                                          ingredientName);
        }
    }

    private void bulkInsertIngredinetInShoppingList(@Nullable Intent intent) {
        int recipeId = intent.getIntExtra(EXTRA_DATA_RECIPE_ID, 0);
        List<Ingredient> ingredientList =
                intent.getParcelableArrayListExtra(EXTRA_DATA_INGREDIENT_LIST);

        ContentValues[] valuesArray = new ContentValues[ingredientList.size()];
        for (int i = 0; i < ingredientList.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(ShoppingEntry.COLUMN_NAME_INGREDIENT_NAME,
                       ingredientList.get(i).getName());
            values.put(ShoppingEntry.COLUMN_RECIPE_FOREING_KEY,
                       recipeId);
            valuesArray[i] = values;
        }
        int rowsInserted = getContentResolver()
                .bulkInsert(ShoppingEntry.CONTENT_URI, valuesArray);

        if(rowsInserted > 0) {
            sendIngredientActionBroadcast(SHOPPING_ACTION_INGREDIENT_INSERT,
                                          null);
        }
    }

    private void insertIngredientInShoppingList(Intent intent) {
        int recipeId = intent.getIntExtra(EXTRA_DATA_RECIPE_ID, 0);
        String ingredientName = intent.getStringExtra(EXTRA_DATA_INGREDIENT_NAME);
        Log.d(TAG, "insertIngredientInShoppingList: " + ingredientName);

        ContentValues contentValues = new ContentValues();
        contentValues.put(ShoppingEntry.COLUMN_NAME_INGREDIENT_NAME,
                          ingredientName);
        contentValues.put(ShoppingEntry.COLUMN_RECIPE_FOREING_KEY,
                          recipeId);
        Uri rowinsertedUri = getContentResolver()
                .insert(ShoppingEntry.CONTENT_URI, contentValues);

        if(rowinsertedUri != null) {
            sendIngredientActionBroadcast(SHOPPING_ACTION_INGREDIENT_INSERT,
                                          ingredientName);
        }
    }

    private void requestServerData() {
        Call<List<Recipe>> call = BakesRequestUtils.buildBakingApi();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call,
                                   @NonNull Response<List<Recipe>> response) {
                List<Recipe> recipes = response.body();
                dataUtils.persistRecipes(recipes);
                for(Recipe recipe : recipes){
                    Log.d(SERVICE_NAME, "onResponse: " + recipe.getName());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call,
                                  @NonNull Throwable t) {

            }
        });
    }

    private void sendIngredientActionBroadcast(String action,
                                               @Nullable String ingredientName) {
        Log.d(TAG, "sendIngredientActionBroadcast: ");

        Intent intent = new Intent(BROADCAST_ACTION_INGREDIENT_SHOPPING);
        intent.putExtra(EXTRA_DATA_INGREDIENT_SHOPPING_ACTION, action);
        if(ingredientName != null) {
            intent.putExtra(EXTRA_DATA_INGREDIENT_NAME, ingredientName);
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
