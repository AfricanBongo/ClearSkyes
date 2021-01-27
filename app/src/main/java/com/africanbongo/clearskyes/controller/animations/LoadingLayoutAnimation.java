package com.africanbongo.clearskyes.controller.animations;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;

import com.africanbongo.clearskyes.R;

/**
 * Class used to switch views with loading layout using {@link SwitchFadeAnimation} class
 * Makes uses of {@link android.view.ViewPropertyAnimator} class
 */
public class LoadingLayoutAnimation {
    private SwitchFadeAnimation switchFadeAnimation;
    private final FragmentActivity activity;
    private final View loadingLayout;
    private final View mainLayout;
    private boolean animationRan = false;

    public LoadingLayoutAnimation(@NonNull FragmentActivity activity, @NonNull View loadingLayout, @NonNull View mainLayout) {
        switchFadeAnimation = new SwitchFadeAnimation();
        this.activity = activity;
        this.loadingLayout = loadingLayout;
        this.mainLayout = mainLayout;
    }

    /**
     * <p>
     *     Starts the loading animation
     * </p>
     * Always runs animations on UI Thread
     *  in case they're being called from Non-UI threads
     */
    public void start() {
        activity.runOnUiThread(() -> {
            // Get image  view and start the animation
            ImageView loadingImage = loadingLayout.findViewById(R.id.loading_image);

            AnimatedVectorDrawable vectorDrawable = (AnimatedVectorDrawable)
                    ResourcesCompat.getDrawable(activity.getResources(), R.drawable.avd_loading_purple, null);

            if (vectorDrawable != null) {
                loadingImage.setImageDrawable(vectorDrawable);
                vectorDrawable.start();
                animationRan = true;
            }
        });
    }

    /**
     * <p>
     * Stops the loading animation
     * </p>
     * <p>
     *     However, if animation wasn't started, the stop method won't be run
     * </p>
     * Always runs animations on UI Thread
     * in case they're being called from Non-UI threads
     */
    public void stop() {
        activity.runOnUiThread(() -> {
            if (animationRan) {
                // Get image  view and start the animation
                ImageView loadingImage = loadingLayout.findViewById(R.id.loading_image);

                AnimatedVectorDrawable vectorDrawable =
                        (AnimatedVectorDrawable) loadingImage.getDrawable();

                if (vectorDrawable != null) {vectorDrawable.stop();}

                // Switch the loading view with the normal weather view
                switchFadeAnimation.switchViews(loadingLayout, mainLayout, SwitchFadeAnimation.LONG_DURATION);

                animationRan = false;
            }
        });

    }
}
