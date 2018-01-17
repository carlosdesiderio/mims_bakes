package uk.me.desiderio.mimsbakes;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import uk.me.desiderio.mimsbakes.InternalIngredientListAdapter.OnClickIngredientListener;
import uk.me.desiderio.mimsbakes.data.model.Ingredient;
import uk.me.desiderio.mimsbakes.data.model.Recipe;
import uk.me.desiderio.mimsbakes.data.model.Step;
import uk.me.desiderio.mimsbakes.view.ImageUtils;

import static android.content.ContentValues.TAG;

/**
 * Adapter for the recipe details
 *
 * The first list element is an {@link RecyclerView} ot show the list of ingredients
 * The rest of elements are each of the step of execution to carry out the current recipe
 */

public class RecipeDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_INGREDIENT_LIST = 2;
    private static final int VIEW_TYPE_STEP = 4;

    private Recipe recipe;
    private final OnClickListItemListener clickListener;


    public interface OnClickListItemListener {
        void onStepSelected(Step step);
        void onIngredientSelected(Ingredient ingredient, int recipeId);
    }

    public RecipeDetailsAdapter(OnClickListItemListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_INGREDIENT_LIST:
                view =  LayoutInflater.from(parent.getContext()).inflate(R.layout
                        .step_list_ingredient_list_layout, parent, false);
                return new IngredientViewHolder(view);
            case VIEW_TYPE_STEP:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                        .step_list_item_layout, parent, false);
                return new StepViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        Log.d(TAG, "onBindViewHolder : vieja: " + viewType + " + " + position);

        switch (viewType) {
            case VIEW_TYPE_INGREDIENT_LIST:
                List<Ingredient> ingredientList = recipe.getIngredients();
                IngredientViewHolder ingredientViewHolder = (IngredientViewHolder) holder;
                ingredientViewHolder.adapter.swapIngredientList(ingredientList);
                break;

            case VIEW_TYPE_STEP:
                StepViewHolder stepViewHolder = (StepViewHolder) holder;
                int stepIndex = getStepIndexFromPosition(position);
                final Step step = recipe.getSteps().get(stepIndex);
                String thumbnailUrl = step.getThumbnailURLString();

                loadStepThumbnail(stepViewHolder, thumbnailUrl);
                stepViewHolder.stepSmallDesc.setText(step.getShortDescription());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (clickListener != null) {
                            clickListener.onStepSelected(step);
                        }
                    }
                });
                break;
        }

    }

    private void loadStepThumbnail(StepViewHolder holder, String imageUrl) {
        Context context = holder.itemView.getContext();
        int defaultImageRes = ImageUtils.getRandomDefaultThumbnailRes(context,
                R.array.step_thumb_placeholder,
                R.drawable.step_thumb_default_1);

        ImageUtils.loadImage(context,
                holder.stepImageView,
                imageUrl,
                R.drawable.step_loading,
                defaultImageRes);
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if(recipe == null) {
            return count;
        }
        count = recipe.getSteps().size() + 1;
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) {
            return VIEW_TYPE_INGREDIENT_LIST;
        } else {
            return VIEW_TYPE_STEP;
        }
    }


    public void swapRecipe(Recipe recipe){
        this.recipe = recipe;
        notifyDataSetChanged();
    }

    /** refreshes ingredient data
     * used when adding and removing ingredients to shopping list
     */
    public void updateIngredientList(List<Ingredient> ingredientList) {
        this.recipe.setIngredients(ingredientList);
        notifyDataSetChanged();
    }

    public Recipe getData() {
        return recipe;
    }

    /**
     * returns the step list index. Remove one as the first list item
     * is reserved for the ingredient list
     */
    private int getStepIndexFromPosition(int position) {
        return position - 1;
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder{
        public final RecyclerView ingredienRecyclerView;
        public final InternalIngredientListAdapter adapter;

        public IngredientViewHolder(View itemView) {
            super(itemView);

            ingredienRecyclerView = itemView.findViewById(R.id.step_ingredients_recycler_view);

            LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext());
            ingredienRecyclerView.setLayoutManager(layoutManager);
            adapter = new InternalIngredientListAdapter();
            ingredienRecyclerView.setAdapter(adapter);
            adapter.setClickListener(new OnClickIngredientListener() {
                @Override
                public void onIngredientSelected(Ingredient ingredient) {
                    if(clickListener != null) {
                        clickListener.onIngredientSelected(ingredient, recipe.getRecipeId());
                    }
                }
            });
        }
    }

    public class StepViewHolder extends RecyclerView.ViewHolder{
        public final ImageView stepImageView;
        public final TextView stepSmallDesc;

        public StepViewHolder(View itemView) {
            super(itemView);

            stepImageView = itemView.findViewById(R.id.step_thumbnail_image_view);
            stepSmallDesc = itemView.findViewById(R.id.step_small_desc_text_view);
        }
    }
}
