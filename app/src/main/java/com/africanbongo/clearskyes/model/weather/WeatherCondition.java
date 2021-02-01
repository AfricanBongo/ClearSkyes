package com.africanbongo.clearskyes.model.weather;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.africanbongo.clearskyes.R;
import com.africanbongo.clearskyes.model.weatherapi.util.WeatherAVDS;
import com.bumptech.glide.Glide;

/**
Base class for holding base conditions and loading the weather icon into a ImageView
 */
public class WeatherCondition {

    private final String protocol = "https:";
    private final String conditionText;
    private final String conditionIcon;
    private final int conditionCode;
    private final boolean day;

    // Resource id for when a weather icon has not been found
    public static final int IMAGE_NOT_FOUND = R.drawable.image_not_found;

    public WeatherCondition(@NonNull String conditionText, @NonNull String conditionIcon, int conditionCode, boolean day) {
        this.conditionText = conditionText;
        this.conditionIcon = protocol + conditionIcon;
        this.conditionCode = conditionCode;
        this.day = day;
    }

    /**
     * Loads an image or drawable into the ImageView object given depending availability of either resource
     * @param view {@link ImageView} to load resource into
     */
    public void loadConditionImage(@NonNull ImageView view) {

        // Set the tooltip and description of the image view
        view.setTooltipText(conditionText);
        view.setContentDescription(conditionText);

        // Load AnimatedVectorDrawable from resources, if it exists
        for (WeatherAVDS avd : WeatherAVDS.values()) {
            if (conditionCode == avd.getAvdID()) {
                int drawableId;

                // Retrieve drawable id in respect to time of weather report, i.e night or day
                if (day) {
                    drawableId = avd.getDayResourceId();
                } else {
                    drawableId = avd.getNightResourceId();
                }

                // Load up the AVD
                Drawable drawable = ResourcesCompat
                        .getDrawable(view.getResources(), drawableId, null);

                if (drawable != null) {
                    Glide
                            .with(view)
                            .load(drawable)
                            .error(IMAGE_NOT_FOUND)
                            .into(view);

                    // Start animation if the drawable is a AnimatedVectorDrawable object
                    if (drawable instanceof AnimatedVectorDrawable) {
                        ((AnimatedVectorDrawable) drawable).start();
                    }
                }

                return;
            }
        }

        // Else load up image from internet
        Glide
                .with(view.getContext())
                .load(conditionIcon)
                .error(IMAGE_NOT_FOUND)
                .into(view);
    }



    public String getConditionText() {
        return conditionText;
    }
}