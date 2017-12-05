package uk.me.desiderio.mimsbakes.network;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

import uk.me.desiderio.mimsbakes.data.model.Recipe;

/**
 * provides utility methods to parse the Recipe Json String
 */

public class BakesJSONParserUtils {

    public static final String  ASSET_BASE_PATH = "app/src/main/assets/";

    /**
     * reads local file with a mock version of the baking JSON response
     */
    public static String readJsonFile (String filename) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ASSET_BASE_PATH + filename)));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        while (line != null) {
            sb.append(line);
            line = br.readLine();
        }

        return sb.toString();
    }

    /**
     * parses baking JSON response string into an array of {@link Recipe} objects
     */
    public static List<Recipe> parseBakeRecipesJsonString(String jsonString) throws JSONException {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Recipe>>(){}.getType();
        return gson.fromJson(jsonString,listType );
    }
}
