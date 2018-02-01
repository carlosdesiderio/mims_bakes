package uk.me.desiderio.mimsbakes;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import uk.me.desiderio.mimsbakes.data.model.Ingredient;
import uk.me.desiderio.mimsbakes.data.model.Recipe;
import uk.me.desiderio.mimsbakes.data.model.Step;

/**
 * Shows list of ingredient and execution steps of current recipe
 */


public class RecipeDetailsFragment extends Fragment implements RecipeDetailsAdapter.OnClickListItemListener {

    private OnRecipeDetailsFragmentItemClickListener listener;
    private RecipeDetailsAdapter adapter;
    private TextView titleTextView;

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }

    public void setData(Recipe recipe) {
        titleTextView.setText(recipe.getName());
        adapter.swapRecipe(recipe);

    }

    public void updateIngredients(List<Ingredient> ingredientList) {
        adapter.updateIngredientList(ingredientList);
    }

    public Recipe getData() {
        return adapter.getData();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_recipe_details, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.recipe_details_recycler_view);
        titleTextView = rootView.findViewById(R.id.recipe_details_title_text_view);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);

        adapter = new RecipeDetailsAdapter(this);
        recyclerView.setAdapter(adapter);

        recyclerView.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRecipeDetailsFragmentItemClickListener) {
            listener = (OnRecipeDetailsFragmentItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRecipeDetailsFragmentItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onStepSelected(Step step) {
        if (listener != null) {
            listener.onRecipeStepSelected(step);
        }
    }

    @Override
    public void onIngredientSelected(Ingredient ingredient, int recipeId) {
        if(listener != null){
            listener.onRecipeIngredientSelected(ingredient, recipeId);
        }
    }

    public interface OnRecipeDetailsFragmentItemClickListener {
        void onRecipeStepSelected(Step step);
        void onRecipeIngredientSelected(Ingredient ingredient, int recipeId);
    }
}
