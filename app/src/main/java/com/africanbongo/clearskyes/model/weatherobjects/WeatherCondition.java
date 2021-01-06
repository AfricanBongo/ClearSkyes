package com.africanbongo.clearskyes.model.weatherobjects;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.africanbongo.clearskyes.R;
import com.bumptech.glide.Glide;

/**
Base class for holding base conditions and loading the weather icon into a ImageView
 */
public class WeatherCondition {

    // TODO Wire up AVDs and write enum to hold avd ids
    enum AVD {
        PARTLY_CLOUDY(1000, R.drawable.avd_cloudy_day, R.drawable.avd_cloudy_night);

        private final int avdID;
        private final int dayResourceId;
        private final int nightResourceId;

        AVD(int avdID, int dayResourceId, int nightResourceId) {
            this.avdID = avdID;
            this.dayResourceId = dayResourceId;
            this.nightResourceId = nightResourceId;
        }

        public int getAvdID() {
            return avdID;
        }

        public int getDayResourceId() {
            return dayResourceId;
        }

        public int getNightResourceId() {
            return nightResourceId;
        }
    }

    private final String conditionText;
    private final String conditionIcon;
    private final int conditionCode;
    private final boolean day;

    // Resource id for when a weather icon has not been found
    public static final int IMAGE_NOT_FOUND = R.drawable.image_not_found;

    public WeatherCondition(@NonNull String conditionText, @NonNull String conditionIcon, int conditionCode, boolean day) {
        this.conditionText = conditionText;
        this.conditionIcon = conditionIcon;
        this.conditionCode = conditionCode;
        this.day = day;
    }

    /**
     * Loads an image or drawable into the ImageView object given depending availability of either resource
     * @param view {@link ImageView} to load resource into
     */
    public void loadConditionImage(ImageView view) {

        // Load AnimatedVectorDrawable from resources, if it exists
        for (AVD avd : AVD.values()) {
            if (conditionCode == avd.avdID) {
                int drawableId;

                // Retrieve drawable id in respect to time of weather report, i.e night or day
                if (day) {
                    drawableId = avd.dayResourceId;
                } else {
                    drawableId = avd.nightResourceId;
                }

                // Load up the AVD
                view.setImageResource(drawableId);

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

    public String getConditionIcon() {
        return conditionIcon;
    }

    public int getConditionCode() {
        return conditionCode;
    }

    public boolean isDay() {
        return day;
    }
}
