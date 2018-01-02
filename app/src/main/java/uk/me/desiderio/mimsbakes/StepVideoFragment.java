package uk.me.desiderio.mimsbakes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import uk.me.desiderio.mimsbakes.data.model.Step;

/**
 * Fragment to show video and description of the current {@link Step}
 *
 * The class will be attached to either {@link StepVideoActivity} and
 * {@link RecipeDetailsActivity} depending on the device and rotation.
 */
public class StepVideoFragment extends Fragment {

    private Step step;
    private TextView descriptionTextView;

    public StepVideoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_step_video, container, false);
        descriptionTextView = rootView.findViewById(R.id.step_description_text_view);
        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    public void swapData(Step step) {
        this.step = step;
        updateViewOnDataChange(step);
    }

    private void updateViewOnDataChange(Step step) {
        descriptionTextView.setText(step.getDescription());
    }
}
