package com.africanbongo.clearskyes.controller.customviews;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.africanbongo.clearskyes.R;
import com.africanbongo.clearskyes.model.WeatherLocation;
import com.google.android.material.button.MaterialButton;

/**
 * Button linked to state adapter to show weather for a certain location
 * Upon installation of the app, the current location is taken to get a weather location
 */
public class LocationButton extends MaterialButton
    implements View.OnTouchListener {
    private final WeatherLocation location;

    private static final int INSET_TOP_BOTTOM = 5;
    private static final float SCALE_NORMAL = 1f;
    private static final float SCALE_EXTEND = 1.2f;
    private static final long EXTENSION_DURATION = 500L;

    public LocationButton(Context context, WeatherLocation location) {
        super(context);
        this.location = location;
        init();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void init() {
        // Label the button
        String buttonLabel = location.getShortStringLocation();
        String buttonToolTipText = location.getLongStringLocation();
        setText(buttonLabel);
        setTooltipText(buttonToolTipText);

        // Set view property colors
        ColorStateList textColorList = ResourcesCompat
                .getColorStateList(getResources(), R.color.location_button_text_color, null);
        ColorStateList backgroundColorList = ResourcesCompat
                .getColorStateList(getResources(), R.color.location_button_background_color, null);
        setTextColor(textColorList);
        setBackgroundTintList(backgroundColorList);

        // Configure the view insets
        setInsetTop(INSET_TOP_BOTTOM);
        setInsetBottom(INSET_TOP_BOTTOM);

        // Don't allow button to lose checked state if touched again
        setOnTouchListener(this);

        // Change view scale and colors when checked and unchecked
        addOnCheckedChangeListener((MaterialButton button, boolean isChecked) -> {
            float scaleTo;
            float scaleFrom;

            if (isChecked()) {
                // Extend the button's scaleX
                scaleFrom = SCALE_NORMAL;
                scaleTo = SCALE_EXTEND;

            } else {
                // Return the button's scaleX to normal
                scaleFrom = SCALE_EXTEND;
                scaleTo = SCALE_NORMAL;

            }

            ValueAnimator extensionAnimator = ValueAnimator.ofFloat(scaleFrom, scaleTo);
            extensionAnimator.setDuration(EXTENSION_DURATION);

            extensionAnimator.addUpdateListener((ValueAnimator valueAnimator) -> {
                float animatedValue = (float) valueAnimator.getAnimatedValue();
                // Extend the scaleX of the button gradually
                setScaleX(animatedValue);
                setScaleY(animatedValue);
            });

            extensionAnimator.start();
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        setChecked(true);
        return true;
    }

    /**
     * Get the location of the weather data associated with this button
     * @return {@link WeatherLocation} object eg. for Harare
     */
    @NonNull
    public WeatherLocation getLocation() {
        return location;
    }
}
