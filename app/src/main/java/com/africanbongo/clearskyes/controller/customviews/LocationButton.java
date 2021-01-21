package com.africanbongo.clearskyes.controller.customviews;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.africanbongo.clearskyes.R;
import com.google.android.material.button.MaterialButton;

/**
 * Button linked to state adapter to show weather for a certain location
 * Upon installation of the app, the current location is taken to get a weather location
 */
public class LocationButton extends MaterialButton {
    private String location;

    private static final int WHITE_COLOR = Color.WHITE;
    private static final int PURPLE_COLOR = R.color.purple_dark;
    private static final int MARGIN = R.dimen.medium_gap;
    private static final int INSET_TOP_BOTTOM = 5;
    private static final float SCALE_NORMAL = 1f;
    private static final float SCALE_EXTEND = 1.2f;
    private static final long EXTENSION_DURATION = 500L;

    public LocationButton(Context context, String location) {
        this(context);

        // Always make the location a title string, eg. Harare, Gold, Reef
        // Make sure to remove whitespaces too
        this.location = location
                .substring(0, 1)
                .toUpperCase()
                .concat(location.substring(1).toLowerCase())
                .trim();

        init();
    }
    public LocationButton(Context context) {
        super(context);
        init();
    }

    public LocationButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LocationButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        // Label the button
        setText(location);

        // Change view colors
        int color = ResourcesCompat.getColor(getResources(), PURPLE_COLOR, null);
        setBackgroundTintList(ColorStateList.valueOf(color));
        setTextColor(WHITE_COLOR);

        // Configure the view insets
        setInsetTop(INSET_TOP_BOTTOM);
        setInsetBottom(INSET_TOP_BOTTOM);

        // Change view scale and colors when checked and unchecked
        addOnCheckedChangeListener((MaterialButton button, boolean isChecked) -> {
                int textColor;
                int backgroundColor;
                float scaleTo;
                float scaleFrom;

                if (isChecked()) {
                    textColor = PURPLE_COLOR;
                    backgroundColor = WHITE_COLOR;

                    // Extend the button's scaleX
                    scaleFrom = SCALE_NORMAL;
                    scaleTo = SCALE_EXTEND;

                } else {
                    textColor = WHITE_COLOR;
                    backgroundColor = ResourcesCompat.getColor(getResources(), PURPLE_COLOR, null);

                    // Return the button's scaleX to normal
                    scaleFrom = SCALE_EXTEND;
                    scaleTo = SCALE_NORMAL;
                }

                // Set the button attributes
                setTextColor(textColor);
                setBackgroundTintList(ColorStateList.valueOf(backgroundColor));


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

    /**
     * Get the location of the weather data associated with this button
     * @return {@link String} location eg. Harare
     */
    @NonNull
    public String getLocation() {
        return location;
    }
}
