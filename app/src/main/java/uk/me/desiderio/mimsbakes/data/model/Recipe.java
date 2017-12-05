package uk.me.desiderio.mimsbakes.data.model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * holds data about a recipe. The class is used to parse recipe data from JSON with GSON
 */

public class Recipe implements Parcelable {

    private static final byte PARCEL_FLAG_WITH_LIST = (byte) 0x00;
    private static final byte PARCEL_FLAG_WITHOUT_LIST = (byte) 0x01;

    public static final String NODE_NAME_ID = "id";
    public static final String NODE_NAME_NAME = "name";
    public static final String NODE_NAME_INGREDIENTS = "ingredients";
    public static final String NODE_NAME_STEPS = "steps";
    public static final String NODE_NAME_SERVINGS = "servings";
    public static final String NODE_NAME_IMAGE = "image";

    private int id;
    private String name;
    private int servings;
    private String image;
    private List<Ingredient> ingredients;
    private List<Step> steps;

    public Recipe() {}

    public Recipe(int recipeId, String name, int servings, String imageURLString, List<Ingredient> ingredients, List<Step> steps) {
        this.id = recipeId;
        this.name = name;
        this.servings = servings;
        this.image = imageURLString;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public Recipe(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.servings = in.readInt();
        this.image = in.readString();
        if(in.readByte() == PARCEL_FLAG_WITH_LIST) {
            in.readTypedList(this.ingredients, Ingredient.CREATOR);
        }
        if(in.readByte() == PARCEL_FLAG_WITH_LIST) {
            in.readTypedList(this.steps, Step.CREATOR);
        }
    }

    //* returns the recipe id as returned in the json response */
    public int getRecipeId() {
        return id;
    }

    public void setRecipeId(int recipeId) {
        this.id = recipeId;
    }

    //* returns recipe name */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /** returns list of ingredients for the recipe */
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    /** returns list of steps involved to produce the recipe */
    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    /** returns number of serving that the recipe will serve */
    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    /** returns the recipe image url as a {@link String} */
    public String getImageURLString() {
        return image;
    }

    public void setImageURLString(String imageURLString) {
        this.image = imageURLString;
    }

    /** returns a {@link android.content.ContentValues} object with the recipe's property value
     * to be stored in the local database. These are all the primitive values not including
     * the lists that will be stored in a different table
     */
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(NODE_NAME_ID, getRecipeId());
        values.put(NODE_NAME_NAME, getName());
        values.put(NODE_NAME_SERVINGS, getServings());
        values.put(NODE_NAME_IMAGE, getImageURLString());
        return values;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(servings);
        parcel.writeString(image);
        if(ingredients != null) {
            parcel.writeByte(PARCEL_FLAG_WITH_LIST);
            parcel.writeList(ingredients);
        } else {
            parcel.writeByte(PARCEL_FLAG_WITHOUT_LIST);
        }
        if(steps != null) {
            parcel.writeByte(PARCEL_FLAG_WITH_LIST);
            parcel.writeList(steps);
        } else {
            parcel.writeByte(PARCEL_FLAG_WITHOUT_LIST);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Recipe> CREATOR
            = new Parcelable.Creator<Recipe>() {
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

}
