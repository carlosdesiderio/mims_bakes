package uk.me.desiderio.mimsbakes;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.me.desiderio.mimsbakes.data.model.Recipe;
import uk.me.desiderio.mimsbakes.data.model.Step;

/**
 * Shows list of ingredient and execution steps of current recipe
 */


public class RecipeDetailsFragment extends Fragment implements RecipeDetailsAdapter.OnClickListItemListener {

    private OnRecipeDetailsFragmentItemClickListener listener;
    private RecipeDetailsAdapter adapter;

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }

    public void setData(Recipe recipe) {
        adapter.swapRecipe(recipe);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_recipe_details, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.recipe_details_recycler_view);

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
    public void onClick(Step step) {
        if (listener != null) {
            listener.onRecipeStepSelected(step);
        }
    }

    public interface OnRecipeDetailsFragmentItemClickListener {
        void onRecipeStepSelected(Step step);
    }
}
