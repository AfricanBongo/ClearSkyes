package com.africanbongo.clearskyes.controller.customviews;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.africanbongo.clearskyes.R;
import com.africanbongo.clearskyes.controller.activities.LocationsActivity;
import com.africanbongo.clearskyes.model.location.WeatherLocation;
import com.africanbongo.clearskyes.model.util.LocationUtil;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class CustomNavigationView extends NavigationView {

    private DrawerLayout mDrawerLayout = null;
    private MaterialButtonToggleGroup locationsGroup;
    private MaterialButton manageLocationsButton;
    public static final String NO_LOCATION_FOUND = "No location found";

    public CustomNavigationView(@NonNull Context context) {
        super(context);
        init();
    }

    public CustomNavigationView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomNavigationView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        Context localContext = getContext();

        NavigationView navigationView =
                (NavigationView) LayoutInflater
                .from(localContext)
                .inflate(R.layout.custom_navigation_view, this, true);

        manageLocationsButton = navigationView.findViewById(R.id.manage_locations_button);
        locationsGroup = navigationView.findViewById(R.id.locations_toggle_group);
        locationsGroup.setSingleSelection(true);

        View drawerHeader = navigationView.findViewById(R.id.nav_header);
        ImageView sunImage = drawerHeader.findViewById(R.id.nav_header_image);

        // If the manageLocationsButton is clicked open LocationsActivity
        manageLocationsButton.setOnClickListener(e -> {
            Intent intent = new Intent(localContext, LocationsActivity.class);
            getContext().startActivity(intent);
        });

        // Rotate sun and open github page when the drawer header is clicked
        drawerHeader.setOnClickListener(e -> {

            Drawable drawable = sunImage.getDrawable();

            if (drawable instanceof AnimatedVectorDrawable) {
                ((AnimatedVectorDrawable) drawable).start();

                // Stop animation and open github page
                new Handler().postDelayed(() -> {
                    String githubPage = "https://github.com/AfricanBongo/ClearSkyes";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(githubPage));
                    localContext.startActivity(intent);

                    ((AnimatedVectorDrawable) drawable).stop();

                }, 2000L);
            }
        });

        // Load up credit image to WeatherAPI
        ImageView creditImage =
                navigationView
                        .findViewById(R.id.poweredby_view)
                        .findViewById(R.id.credit_image);

        // Open site upon clicking image
        creditImage.setOnClickListener(e -> {
            String websiteURL = "https://www.weatherapi.com";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteURL));
            localContext.startActivity(intent);
        });
    }


    /**
     * Attach the parent drawer layout
     * @param parentDrawer {@link DrawerLayout} containing this {@link CustomNavigationView}
     */
    public void setParentDrawer(@NonNull DrawerLayout parentDrawer) {
        mDrawerLayout = parentDrawer;
    }

    /**
     * Loads all the location toggle buttons into their toggle group
     * Return a {@link String} location,
     * If location returned is {@value NO_LOCATION_FOUND} means that no location exists.
     * @return {@link String} location of which location data which should be displayed.
     */
    public String loadLocations() {
        SharedPreferences locationPreferences =
                getContext().getSharedPreferences("locations", MODE_PRIVATE);

        Set<String> locations = locationPreferences.getStringSet("locationSet", null);

        if (locations != null) {

            ArrayList<WeatherLocation> weatherLocations =
                    new ArrayList<>(LocationUtil.deserializeAll(locations));

            // Loop through the set add a button to the toggle group
            weatherLocations.forEach(this::addLocationButton);

            int preferredLocation = locationPreferences.getInt("preferredLocation", -1);
            LocationButton locationButton;

            if (preferredLocation != -1) {
                locationButton = (LocationButton) locationsGroup.getChildAt(preferredLocation);
            } else {
                locationButton = (LocationButton) locationsGroup.getChildAt(0);
            }

            // Load in location and show the weather info for the location
            String location = locationButton.getLocation().getUrlLocation();
            locationButton.setChecked(true);

            return location;
        }

        return NO_LOCATION_FOUND;
    }

    /**
     * Animation played when the program wants user to add locations
     * @param parentDrawerLayout {@link DrawerLayout} required to show the animation
     */
    public void promptForLocations(@NonNull DrawerLayout parentDrawerLayout, boolean untilLocationAdded) {

        float blinkButtonAlphaVisible = 1f;
        float blinkButtonAlphaInvisible = 0.2f;
        long blinkDuration = 1500L;
        int repeatCount = 5;
        int repeatMode = ValueAnimator.REVERSE;

        // That means repeat the prompt until the user add a location
        if (untilLocationAdded) {
            repeatCount = 1;
        }

        if (mDrawerLayout == null) {
            mDrawerLayout = parentDrawerLayout;
        }

        // Open the drawer and prompt user to add a location
        mDrawerLayout.openDrawer(GravityCompat.START);

        // Animate the view to catch attention
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(manageLocationsButton, "alpha",
                blinkButtonAlphaVisible, blinkButtonAlphaInvisible);

        alphaAnimator.setRepeatCount(repeatCount);
        alphaAnimator.setRepeatMode(repeatMode);
        alphaAnimator.setDuration(blinkDuration);


        Runnable runBlinkAnim = new Runnable() {
            @Override
            public void run() {
                alphaAnimator.start();

                // if no new locations have been added keep running the animation
                SharedPreferences sharedPreferences =
                        getContext().getSharedPreferences(LocationUtil.SP_LOCATIONS, MODE_PRIVATE);

                Set<String> locations =
                        sharedPreferences.getStringSet(LocationUtil.SP_LOCATION_SET, null);

                if (locations == null) {
                    new Handler().postDelayed(this, blinkDuration * 2);
                } else {
                    loadLocations();
                }
            }
        };

        new Handler().post(runBlinkAnim);
    }

    /**
     * Add a location button to the view's location buttons toggle group
     * @param location {@link WeatherLocation} used to identify the location's weather info being displayed
     */
    public void addLocationButton(WeatherLocation location){
        if (location != null) {
            LocationButton newButton = new LocationButton(getContext(), location);

            // Add the button to the toggle group
            final int insertPos = locationsGroup.getChildCount();
            locationsGroup.addView(newButton, insertPos);

        }
    }

    /**
     * Add {@link com.google.android.material.button.MaterialButtonToggleGroup.OnButtonCheckedListener} to the view's {@link MaterialButtonToggleGroup}
     * @see MaterialButtonToggleGroup#addOnButtonCheckedListener(MaterialButtonToggleGroup.OnButtonCheckedListener)
     * @param checkedListener {@link com.google.android.material.button.MaterialButtonToggleGroup.OnButtonCheckedListener} invoked when a button within the toggle group is checked
     */
    public void addOnLocationButtonCheckedListener(@NonNull MaterialButtonToggleGroup.OnButtonCheckedListener checkedListener) {
        locationsGroup.addOnButtonCheckedListener(checkedListener);
    }
}
