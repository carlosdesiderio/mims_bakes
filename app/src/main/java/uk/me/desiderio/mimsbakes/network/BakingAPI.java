package uk.me.desiderio.mimsbakes.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import uk.me.desiderio.mimsbakes.data.model.Recipe;

/**
 * Interface that provides the baking service endpoint
 */

public interface BakingAPI {

    String BAKING_PATH = "/topher/2017/May/59121517_baking/baking.json";

    @GET(BAKING_PATH)
    Call<List<Recipe>> getRecipes();
}
