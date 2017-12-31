package uk.me.desiderio.mimsbakes.data;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.me.desiderio.mimsbakes.data.model.Recipe;
import uk.me.desiderio.mimsbakes.network.BakesRequestUtils;

/**
 * Intent Service to persist baking data request response
 */

public class BakesDataIntentService extends IntentService {
    private static final String SERVICE_NAME = BakesDataIntentService.class.getSimpleName();

    private BakesDataUtils dataUtils;



    public BakesDataIntentService() {
        super(SERVICE_NAME);
        dataUtils = new BakesDataUtils(this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Call<List<Recipe>> call = BakesRequestUtils.buildBakingApi();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                List<Recipe> recipes = response.body();
                dataUtils.persistRecipes(recipes);
                for(Recipe recipe : recipes){
                    Log.d(SERVICE_NAME, "onResponse: " + recipe.getName());
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {

            }
        });
    }
}
