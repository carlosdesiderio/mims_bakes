package uk.me.desiderio.mimsbakes;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import uk.me.desiderio.mimsbakes.data.model.Ingredient;
import uk.me.desiderio.mimsbakes.view.StringUtils;

/**
 * Adapter for the ingredient list
 */

public class InternalIngredientListAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<Ingredient> ingredientList;
    private OnClickIngredientListener listener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .ingredient_list_item_layout, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        IngredientViewHolder ingredientViewHolder = (IngredientViewHolder) holder;
        final Ingredient ingredient = ingredientList.get(position);
        setIngredientHighlight(ingredientViewHolder, ingredient.getShoppingFlag());
        String ingredientString = StringUtils.getFormatedIngredientString(
                ingredient.getName(),
                ingredient.getQuantity(),
                ingredient.getMeasure()
        );
        ingredientViewHolder.ingredienDetailsTextView.setText(ingredientString);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onIngredientSelected(ingredient);
                }
            }
        });
    }

    /**
     * set background colour.
     * highlight ingredients that are in the shopping list
     */
    private void setIngredientHighlight(IngredientViewHolder holder, int isInShoppingList) {
        int colorRes;
        if (isInShoppingList == 1) {
            colorRes = R.color.colorPrimaryBright;
            holder.ingredientDetailBasquetImage.setVisibility(View.VISIBLE);
        } else {
            colorRes = android.R.color.transparent;
            holder.ingredientDetailBasquetImage.setVisibility(View.INVISIBLE);
        }

        holder.ingredientDetailsContainer.setBackgroundColor(
                holder.itemView.getResources().getColor(colorRes));
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public void setClickListener(OnClickIngredientListener listener) {
        this.listener = listener;
    }

    public void swapIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
        notifyDataSetChanged();
    }

    public interface OnClickIngredientListener {
        void onIngredientSelected(Ingredient ingredient);
    }

    public class IngredientViewHolder extends ViewHolder {
        public final TextView ingredienDetailsTextView;
        public final View ingredientDetailsContainer;
        public final View ingredientDetailBasquetImage;

        public IngredientViewHolder(View itemView) {
            super(itemView);

            ingredienDetailsTextView = itemView.findViewById(R.id.ingredient_details_text_view);
            ingredientDetailsContainer = itemView.findViewById(R.id.ingredient_details_container);
            ingredientDetailBasquetImage = itemView.findViewById(R.id.ingredient_details_basquet_image_view);
        }
    }
}
