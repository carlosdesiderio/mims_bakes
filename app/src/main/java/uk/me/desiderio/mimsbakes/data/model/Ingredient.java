package uk.me.desiderio.mimsbakes.data.model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * holds data for every ingredient of a recipe
 */

public class Ingredient implements Parcelable {

    public static final String NODE_NAME_INGREDIENT_QUANTITY = "quantity";
    public static final String NODE_NAME_INGREDIENT_MEASURE = "measure";
    public static final String NODE_NAME_INGREDIENT_NAME = "ingredient";
    public static final String NODE_NAME_INGREDIENT_SHOPPING = "shopping";

    private float quantity;
    private String measure;
    private String ingredient;
    private int shoppingFlag;

    public Ingredient() {}

    public Ingredient(String name, float quantity, String measure, int shoppingFlag) {
        this.ingredient = name;
        this.quantity = quantity;
        this.measure = measure;
        this.shoppingFlag = shoppingFlag;
    }

    public Ingredient(Parcel in) {
        this.ingredient = in.readString();
        this.quantity = in.readFloat();
        this.measure = in.readString();
        this.shoppingFlag = in.readInt();
    }

    /** returns ingredient name */
    public String getName() {
        return ingredient;
    }

    public void setName(String name) {
        this.ingredient = name;
    }

    /** returns ingredient amount as a float */
    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /** returns the measure unit that the quantity is measure in */
    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public int getShoppingFlag() {
        return shoppingFlag;
    }

    /** returns a {@link android.content.ContentValues} object with the ingredient's property value
     * to be stored in the local database.
     */
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(NODE_NAME_INGREDIENT_NAME, getName());
        values.put(NODE_NAME_INGREDIENT_QUANTITY, getQuantity());
        values.put(NODE_NAME_INGREDIENT_MEASURE, getMeasure());
        return values;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ingredient);
        parcel.writeFloat(quantity);
        parcel.writeString(measure);
        parcel.writeInt(shoppingFlag);
    }
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Ingredient> CREATOR
            = new Parcelable.Creator<Ingredient>() {
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}
