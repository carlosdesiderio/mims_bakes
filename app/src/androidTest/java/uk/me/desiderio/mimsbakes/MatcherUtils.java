package uk.me.desiderio.mimsbakes;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import uk.me.desiderio.mimsbakes.data.model.Recipe;

import static android.support.test.espresso.core.internal.deps.guava.base.Preconditions.checkNotNull;

/**
 * Provides matchers to be used in expresso testing
 */

public class MatcherUtils {

    /**
     * Returns a matcher that matches  {@link Recipe} based on its name
     */
    public static Matcher<Recipe> withRecipeName(final String name) {
        return new TypeSafeDiagnosingMatcher<Recipe>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("Recipe name should be ").appendValue(name);
            }

            @Override
            protected boolean matchesSafely(final Recipe recipe,
                                            final Description mismatchDescription) {
                mismatchDescription.appendText(" was ").appendValue(recipe.getName());
                return recipe.getName().equals(name);
            }
        };
    }

    /**
     * Matcher to be used in with {@link RecyclerView} descendants
     * Returns a matcher that matches Views based on position
     * and matcher provided as parameter
     */
    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> matcher) {
        checkNotNull(matcher);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText(
                        "RecyclerView should have item at position " + position + ": ");
                matcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    return false;
                }
                return matcher.matches(viewHolder.itemView);
            }
        };
    }

    /**
     * Returns a matcher that matches ImageView based on whether
     * the view has a {@link Drawable} attached
     */
    public static Matcher<View> hasDrawable() {
        return new BoundedMatcher<View, ImageView>(ImageView.class) {
            @Override
            public void describeTo(final Description description) {
                description.appendText("ImageView should have a drawable ");
            }

            @Override
            protected boolean matchesSafely(final ImageView imageview) {
                return imageview.getDrawable() != null;
            }
        };
    }
}
