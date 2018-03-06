package uk.me.desiderio.mimsbakes;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Asserts {@link RecyclerView} based on its adapter item count
 */

public class RecyclerViewCountAssertion implements ViewAssertion {

    private Matcher<Integer> countMatcher;

    public RecyclerViewCountAssertion(int expectedCount) {
        this.countMatcher = is(expectedCount);
    }

    @Override
    public void check(View view, NoMatchingViewException noViewFoundException) {
        if (noViewFoundException != null) {
            throw noViewFoundException;
        }

        RecyclerView.Adapter adapter = ((RecyclerView) view).getAdapter();
        assertThat(adapter.getItemCount(), countMatcher);
    }
}
