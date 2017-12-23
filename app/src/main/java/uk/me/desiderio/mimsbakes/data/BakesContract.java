package uk.me.desiderio.mimsbakes.data;

import android.provider.BaseColumns;

/**
 * Provides Contract for the Bake's recipe database
 */

// TODO add urimatcher test lesson 10 video 15 and all the test in that lesson examples
// TODO: 06/12/2017 NEXT   create data base + test || create provider || then android service and task to load data
class BakesContract {


    static final class RecipeEntry implements BaseColumns {
        static final String TABLE_NAME = "recipes";

        static final String COLUMN_NAME_ID = "recipeId";
        static final String COLUMN_NAME_NAME = "name";
        static final String COLUMN_NAME_SERVINGS = "servings";
        static final String COLUMN_NAME_IMAGE = "image";

    }

    static final class IngredientEntry implements BaseColumns {
        static final String TABLE_NAME = "ingredients";

        static final String COLUMN_NAME_INGREDIENT_QUANTITY = "quantity";
        static final String COLUMN_NAME_INGREDIENT_MEASURE = "measure";
        static final String COLUMN_NAME_INGREDIENT_NAME = "ingredient";
        static final String COLUMN_RECIPE_FOREING_KEY = "recipeForeign";
    }

    static final class StepEntry implements BaseColumns {
        static final String TABLE_NAME = "steps";

        static final String COLUMN_NAME_STEP_ID = "stepId";
        static final String COLUMN_NAME_STEP_SHORT_DESC = "shortDescription";
        static final String COLUMN_NAME_STEP_DESC = "description";
        static final String COLUMN_NAME_STEP_VIDEO_URL = "videoURL";
        static final String COLUMN_NAME_STEP_THUMBNAIL_URL = "thumbnailURL";
        static final String COLUMN_RECIPE_FOREING_KEY = "recipeForeign";


    }
}
