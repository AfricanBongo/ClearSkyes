package com.africanbongo.clearskyes.model.weatherapi;

import android.content.Context;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.africanbongo.clearskyes.R;
import com.africanbongo.clearskyes.controller.activities.Reloadable;
import com.africanbongo.clearskyes.controller.animations.LoadingLayoutAnimation;
import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * <p>
     * An implementation of the {@link com.android.volley.Response.ErrorListener} interface.
     * When the {@link #onErrorResponse(VolleyError)} method is invoked
     * the current screen of the app is replaced by an error page.
     * All error pages include a reload feature that will invoke a refresh process
     * to reload data and views needed by the user.
 * </p>
 *
 * <p>
 *     Conditions of invoking {@link #onErrorResponse(VolleyError)} method are:
 *     <ul>
 *         <li> Network error, e.g. there is no internet connection</li>
 *         <li> Server error, e.g. when the limit of api calls has been exceeded</li>
 *     </ul>
 * </p>
 */
public class ErrorPageListener implements Response.ErrorListener {

    // Used for error pages
    public static final String API_ERROR_MESSAGE = "Server Communication Error!";
    public static final String NO_CONNECTION_MESSAGE = "No internet connection";

    private final Reloadable reloadableActivity;
    private final LoadingLayoutAnimation loadingLayoutAnimation;

    public ErrorPageListener(@NonNull Reloadable reloadableActivity, @NonNull LoadingLayoutAnimation loadingLayoutAnimation) {
        this.reloadableActivity = reloadableActivity;
        this.loadingLayoutAnimation = loadingLayoutAnimation;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        View view = reloadableActivity.showError();
        Context context = loadingLayoutAnimation.getContext();

        if (view != null && error != null) {
            // Stop the loading animation first
            loadingLayoutAnimation.stopNow();

            // Get the error message views
            ImageView errorMessageImage = view.findViewById(R.id.error_message_image);
            TextView errorMessage = view.findViewById(R.id.error_message);
            Button reloadButton = view.findViewById(R.id.reload_button);

            // Configure the reload button
            reloadButton.setOnClickListener(e -> reloadableActivity.reload());
            reloadButton.setAlpha(0f);

            String errorMessageText;
            int drawableToDisplay;

            // Or if there is no internet connection
            if (!WeatherRequestQueue.getWeatherRequestQueue(context).isNetworkAvailable()) {
                errorMessageText = NO_CONNECTION_MESSAGE;
                drawableToDisplay = R.drawable.avd_no_connection;
            }
            // If an error to the weather api
            else {
                errorMessageText = API_ERROR_MESSAGE;
                drawableToDisplay = R.drawable.avd_error_warning;
            }

            AlphaAnimation reloadAnimation = new AlphaAnimation(0f, 1f);
            reloadAnimation.setDuration(500);
            reloadAnimation.setInterpolator(context, android.R.interpolator.fast_out_slow_in);

            errorMessage.setText(errorMessageText);

            // Start the AVD animation
            AnimatedVectorDrawable drawable =
                    (AnimatedVectorDrawable) ResourcesCompat.getDrawable(context.getResources(), drawableToDisplay, null);
            errorMessageImage.setImageDrawable(drawable);

            if (drawable != null) {
                drawable.start();

                drawable.registerAnimationCallback(new Animatable2.AnimationCallback() {
                    @Override
                    public void onAnimationEnd(Drawable drawable) {
                        super.onAnimationEnd(drawable);

                        reloadButton
                                .animate()
                                .alpha(1f)
                                .start();
                    }
                });
            }
        }
    }
}
