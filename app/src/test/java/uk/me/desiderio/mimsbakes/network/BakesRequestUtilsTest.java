package uk.me.desiderio.mimsbakes.network;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import retrofit2.Call;
import uk.me.desiderio.mimsbakes.data.model.Recipe;

import static junit.framework.Assert.assertNotNull;

/**
 * provides test for {@link BakesRequestUtils}
 */

@RunWith(JUnit4.class)
public class BakesRequestUtilsTest {

    private static final String BAKING_URL_STRING =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    @Test
    public void buildBakingApiTest() {

         Call<List<Recipe>> call = BakesRequestUtils.buildBakingApi();

         assertNotNull("Returns NULL Retrofit Call object", call);
         assertNotNull("Returns NULL Retrofit Request object",
                       call.request());

         String actualUrlString = call.request().url().toString();

         Assert.assertEquals("Return WRONG url: " + actualUrlString,
                             BAKING_URL_STRING,
                             actualUrlString);
     }
}