package com.africanbongo.clearskyes.controller.animations;

import android.content.Context;
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
    private final SwitchFadeAnimation switchFadeAnimation;
    private final View loadingLayout;
    private final View mainLayout;
    private boolean animationRan = false;

    public LoadingLayoutAnimation(@NonNull View loadingLayout, @NonNull View mainLayout) {
        switchFadeAnimation = new SwitchFadeAnimation();
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
        // Get image  view and start the animation
        loadingLayout.setVisibility(View.VISIBLE);
        ImageView loadingImage = loadingLayout.findViewById(R.id.loading_image);

        AnimatedVectorDrawable vectorDrawable =
                (AnimatedVectorDrawable) ResourcesCompat.getDrawable(
                        loadingLayout.getContext().getResources(),
                        R.drawable.avd_loading_purple,
                        null
                );

        if (vectorDrawable != null) {
            loadingImage.setImageDrawable(vectorDrawable);
            vectorDrawable.start();
            animationRan = true;
        }
    }

    /**
     * <p>
     * Stops the loading animation and switches views
     * </p>
     * <p>
     *     However, if animation wasn't started, the stop method won't be run
     * </p>
     * Always runs animations on UI Thread
     * in case they're being called from Non-UI threads
     */
    public void stop() {
        if (animationRan) {
            stopNow();
            // Switch the loading view with the normal weather view
            switchFadeAnimation.switchViews(loadingLayout, mainLayout, SwitchFadeAnimation.LONG_DURATION);
            animationRan = false;
        }

    }

    /**
     * Stop the loading animation immediately without any switching of views
     */
    public void stopNow() {
        if (animationRan) {
            // Get image  view and start the animation
            ImageView loadingImage = loadingLayout.findViewById(R.id.loading_image);

            AnimatedVectorDrawable vectorDrawable =
                    (AnimatedVectorDrawable) loadingImage.getDrawable();

            if (vectorDrawable != null) {vectorDrawable.stop();}
        }
    }

    public Context getContext() {
        return loadingLayout.getContext();
    }
}
