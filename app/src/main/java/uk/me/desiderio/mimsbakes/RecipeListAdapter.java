package uk.me.desiderio.mimsbakes;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import uk.me.desiderio.mimsbakes.data.BakesDataUtils;
import uk.me.desiderio.mimsbakes.data.model.Recipe;
import uk.me.desiderio.mimsbakes.view.ImageUtils;

/**
 * Adapter for the list of recipes
 */

public class RecipeListAdapter extends
        RecyclerView.Adapter<RecipeListAdapter.RecipeHolder> {

    private static final String TAG = RecipeListAdapter.class.getSimpleName();

    private List<Recipe> recipeList;
    private BakesDataUtils dataUtils;
    private RecipeClickListener listener;

    public interface RecipeClickListener {
        void onClick(Recipe recipe);
    }

    public RecipeListAdapter(Context context, RecipeClickListener listener) {
        this.dataUtils = new BakesDataUtils(context);
        this.listener = listener;
    }

    @Override
    public RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View recipeView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recipe_list_item_layout,
                         parent,
                         false);
        return new RecipeHolder(recipeView);
    }

    @Override
    public void onBindViewHolder(RecipeHolder holder, int position) {
        final Recipe recipe = recipeList.get(position);

        String servingsString = getServingsString(holder, recipe.getServings());
        loadRecipeImage(holder, recipe.getImageURLString());

        holder.recipeNameTextView.setText(recipe.getName());
        holder.recipeServings.setText(servingsString);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null ) {
                    listener.onClick(recipe);
                }
            }
        });
    }

    public void loadRecipeImage(RecipeHolder holder, String imageUrl) {
        Context context = holder.itemView.getContext();
        int defaultImageRes = ImageUtils.getRandomDefaultThumbnailRes(context,
                R.array.recipe_placeholder,
                R.drawable.recipe_thumb_default_1);

        ImageUtils.loadImage(context,
                holder.recipeImageView,
                imageUrl,
                R.drawable.recipe_image_loading,
                defaultImageRes);
    }

    private String getServingsString(ViewHolder holder, int servings) {
        Resources resources = holder.itemView.getResources();
        return resources.getQuantityString(R.plurals.recipe_servings_plural,
                                           servings,
                                           servings);
    }


    @Override
    public int getItemCount() {
        if(recipeList != null) {
            return recipeList.size();
        }
        return 0;
    }

    /**
     * updates recipe list by deserialising the cursor
     * injected as its parameter
     */
    public void swapCursor(Cursor cursor) {
        if(cursor != null && cursor.getCount() > 0) {
            recipeList = dataUtils.getRecipeDataObjects(cursor);
        } else {
            recipeList = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    public class RecipeHolder extends ViewHolder{
        public ImageView recipeImageView;
        public TextView recipeNameTextView;
        public TextView recipeServings;


        public RecipeHolder(View itemView) {
            super(itemView);
            recipeImageView =
                    itemView.findViewById(R.id.recipe_item_image_view);
            recipeNameTextView =
                    itemView.findViewById(R.id.recipe_item_name_text_view);
            recipeServings =
                    itemView.findViewById(R.id.recipe_item_servings_text_view);
        }
    }
}
