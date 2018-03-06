package uk.me.desiderio.mimsbakes.espresso;

import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

import uk.me.desiderio.mimsbakes.MainActivity;

/**
 * {@link IdlingResource} for espresso test at the {@link MainActivity}
 */

public class RecipeIdlingResource implements IdlingResource {

    @Nullable
    private volatile ResourceCallback resourceCallback;

    private AtomicBoolean idleNow = new AtomicBoolean(true);


    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return idleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.resourceCallback = callback;
    }

    /**
     * set idle state for espresso testing
     * setting to true will allow espresso to continue with testing procedure
     */
    public void setIdleState(boolean isIdleNow) {
        idleNow.set(isIdleNow);
        if (isIdleNow && resourceCallback != null) {
            resourceCallback.onTransitionToIdle();
        }
    }
}
