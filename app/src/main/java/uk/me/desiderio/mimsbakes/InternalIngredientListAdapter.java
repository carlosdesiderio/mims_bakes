package uk.me.desiderio.mimsbakes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import uk.me.desiderio.mimsbakes.data.model.Ingredient;

/**
 * Adapter for the ingredient list
 */

public class InternalIngredientListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Ingredient> ingredientList;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .ingredient_list_item_layout, parent, false);
        return new IngredientViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        IngredientViewHolder ingredientViewHolder = (IngredientViewHolder) holder;
        Ingredient ingredient = ingredientList.get(position);
        String ingredientString = getFormatedIngredientString(
                ingredient.getName(),
                ingredient.getQuantity(),
                ingredient.getMeasure()
        );
        ingredientViewHolder.ingredienDetailsTextView.setText(ingredientString);
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public void swapIngredientList(List<Ingredient> ingredientList){
        this.ingredientList = ingredientList;
        notifyDataSetChanged();
    }

    private String getFormatedIngredientString(String name, float quantity, String meassure) {
        StringBuilder builder = new StringBuilder(getFormattedQuantity(quantity));
        builder.append(getFormatedMeassure(meassure))
                .append(" ")
                .append(name);

        return builder.toString();
    }

    // removes any extra zeros to the right
    private String getFormattedQuantity(float quantity) {
        String quantityString = String.valueOf(quantity);
        return (quantityString.indexOf(".") >= 0
                ?quantityString.replaceAll("\\.?0+$","")
                :quantityString);
    }

    // optimises meassure string
    private String getFormatedMeassure(String meassure) {
        String formattedMeasure = meassure.toLowerCase();

        switch (formattedMeasure) {
            case "unit":
                // removes this string
                return "";
            case "k":
                // changes it for readibility
                return "kg";
            case "g":
            case "oz":
                // removes space on the left
                return formattedMeasure;
            default:
                return " " + formattedMeasure;
        }
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder{
        public TextView ingredienDetailsTextView;

        public IngredientViewHolder(View itemView) {
            super(itemView);

            ingredienDetailsTextView = itemView.findViewById(R.id.ingredient_details_text_view);
        }
    }
}
