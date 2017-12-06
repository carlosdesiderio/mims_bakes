package uk.me.desiderio.mimsbakes.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.me.desiderio.mimsbakes.data.model.Recipe;

/**
 * Util class for the HTTP request using Retrofit
 */

public class BakesRequestUtils {

    // TODO: 05/12/2017 add retrofit proguard settings

    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net";

    /** provides a {@link Call} object that will make a request to the baking service
     * and return a list of {@link Recipe}
     */
    public static Call<List<Recipe>> buildBakingApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BakingAPI bakingAPI = retrofit.create(BakingAPI.class);

        return bakingAPI.getRecipes();
    }
}
