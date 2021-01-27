package com.africanbongo.clearskyes.controller.animations;

import android.view.View;

import androidx.annotation.NonNull;

/**
 * Class used to switch views using a fade animation
 * Makes uses of {@link android.view.ViewPropertyAnimator} class
 */
public class SwitchFadeAnimation {

    public static final long NORMAL_DURATION = 1500L;
    public static final long LONG_DURATION = 4000L;

    /**
     * Switches views by fading one out and the other in
     * @param viewBeingRemoved {@link View} being faded out of focus
     * @param viewToBeViewed {@link View} being faded in into focus
     * @param duration The entire duration of the animation
     */
    public void switchViews(@NonNull View viewBeingRemoved, @NonNull View viewToBeViewed, long duration) {
        // Set the alpha of the viewToBeViewed to initially being 0.
        viewToBeViewed.setAlpha(0f);
        viewToBeViewed.setVisibility(View.VISIBLE);

        // Fade the unwanted view out of focus and make it disappear
        viewBeingRemoved
                .animate()
                .alpha(0f)
                .setDuration(duration / 2)
                .start();

        viewBeingRemoved.setVisibility(View.GONE);

        // Fade in the wanted view
        viewToBeViewed
                .animate()
                .alpha(1f)
                .setDuration(duration / 2)
                .start();

    }
}
