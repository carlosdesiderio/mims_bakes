package uk.me.desiderio.mimsbakes.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Provides Contract for the Bake's recipe database
 */

// TODO add urimatcher test lesson 10 video 15 and all the test in that lesson examples
// TODO: 06/12/2017 NEXT   create data base + test || create provider || then android service and task to load data
public class BakesContract {

    static final String CONTENT_AUTHORITY = "uk.me.desiderio.mimsbakes";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    static final String PATH_RECIPES = "recipes";
    static final String PATH_INGREDIENTS = "ingredients";
    static final String PATH_STEPS = "steps";
    static final String PATH_SHOPPING = "shopping";


    public static final class RecipeEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.
                buildUpon().
                appendPath(PATH_RECIPES).
                build();

        static final String TABLE_NAME = "recipes";

        public static final String COLUMN_NAME_ID = "recipeId";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_SERVINGS = "servings";
        public static final String COLUMN_NAME_IMAGE = "image";

    }

    public static final class IngredientEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.
                buildUpon().
                appendPath(PATH_INGREDIENTS).
                build();

        public static final String TABLE_NAME = "ingredients";

        public static final String COLUMN_NAME_INGREDIENT_QUANTITY = "quantity";
        public static final String COLUMN_NAME_INGREDIENT_MEASURE = "measure";
        public static final String COLUMN_NAME_INGREDIENT_NAME = "ingredient";
        public static final String COLUMN_RECIPE_FOREING_KEY = "recipeForeign";
        public static final String FLAG_NAME_SHOPPING = "shopping";

    }

    public static final class ShoppingEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.
                buildUpon().
                appendPath(PATH_SHOPPING).
                build();

        public static final String TABLE_NAME = "shopping";

        public static final String COLUMN_NAME_INGREDIENT_NAME = "shop_ingredient";
        public static final String COLUMN_RECIPE_FOREING_KEY = "shop_recipeForeign";
    }

    public static final class StepEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.
                buildUpon().
                appendPath(PATH_STEPS).
                build();

        static final String TABLE_NAME = "steps";

        static final String COLUMN_NAME_STEP_ID = "stepId";
        static final String COLUMN_NAME_STEP_SHORT_DESC = "shortDescription";
        static final String COLUMN_NAME_STEP_DESC = "description";
        static final String COLUMN_NAME_STEP_VIDEO_URL = "videoURL";
        static final String COLUMN_NAME_STEP_THUMBNAIL_URL = "thumbnailURL";
        static final String COLUMN_RECIPE_FOREING_KEY = "recipeForeign";
    }
}
