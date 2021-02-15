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
import androidx.preference.PreferenceManager;

import com.africanbongo.clearskyes.R;
import com.africanbongo.clearskyes.controller.activities.FeedbackActivity;
import com.africanbongo.clearskyes.controller.activities.LocationsActivity;
import com.africanbongo.clearskyes.controller.activities.SettingsActivity;
import com.africanbongo.clearskyes.model.WeatherLocation;
import com.africanbongo.clearskyes.util.WeatherLocationUtil;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class CustomNavigationView extends NavigationView
    implements View.OnClickListener {

    private MaterialButtonToggleGroup locationsGroup;
    private MaterialButton manageLocationsButton;
    private View drawerHeader;

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
        drawerHeader = navigationView.findViewById(R.id.nav_header);

        // Load up credit image to WeatherAPI
        ImageView creditImage =
                navigationView
                        .findViewById(R.id.poweredby_view)
                        .findViewById(R.id.credit_image);

        // Open settings activity when settings menu item clicked
        navigationView.findViewById(R.id.settings_button).setOnClickListener(this);
        // Open feedback activity when feedback menu item clicked
        navigationView.findViewById(R.id.feedback_button).setOnClickListener(this);
        // If the manageLocationsButton is clicked open LocationsActivity
        manageLocationsButton.setOnClickListener(this);
        // Rotate sun and open github page when the drawer header is clicked
        drawerHeader.setOnClickListener(this);
        // Open site upon clicking image
        creditImage.setOnClickListener(this);
    }

    /**
     * Loads all the location toggle buttons into their toggle group
     * Return a {@link String} location,
     * If location returned is null means that no location exists.
     * @return {@link String} location of which location data which should be displayed.
     */
    public WeatherLocation loadLocations() {
        SharedPreferences locationPreferences =
                PreferenceManager.getDefaultSharedPreferences(getContext());


        if (locationPreferences != null) {
            Set<String> locations =
                    locationPreferences.getStringSet(WeatherLocationUtil.SP_LOCATION_SET, null);

            if (locations != null) {
                if (!locations.isEmpty()) {
                    ArrayList<WeatherLocation> weatherLocations =
                            new ArrayList<>(WeatherLocationUtil.deserializeAll(locations));

                    // Loop through the set add a button to the toggle group
                    weatherLocations.forEach(this::addLocationButton);

                    String preferredLocation =
                            locationPreferences.getString(WeatherLocationUtil.SP_FAV_LOCATION, null);
                    LocationButton locationButton = null;

                    if (preferredLocation != null) {
                        WeatherLocation location = WeatherLocationUtil.deserialize(preferredLocation);

                        // Load in location and show the weather info for the location
                        for (int i = 0; i < locationsGroup.getChildCount(); i++) {
                            locationButton = (LocationButton) locationsGroup.getChildAt(i);

                            if (locationButton.getLocation().equals(location)) {
                                break;
                            }
                        }
                    } else {
                        locationButton = (LocationButton) locationsGroup.getChildAt(0);

                        locationPreferences
                                .edit()
                                .putString(WeatherLocationUtil.SP_FAV_LOCATION, WeatherLocationUtil.serialize(locationButton.getLocation()))
                                .apply();
                    }

                    WeatherLocation activeLocation = locationButton.getLocation();

                    // Save as the active location
                    locationPreferences
                            .edit()
                            .putString(WeatherLocationUtil.SP_ACTIVE_LOCATION, WeatherLocationUtil.serialize(activeLocation))
                            .apply();
                    
                    locationButton.setChecked(true);
                    return activeLocation;
                }
            }

        }

        // Initialize the string set
        locationPreferences
                .edit()
                .putStringSet(WeatherLocationUtil.SP_LOCATION_SET, new LinkedHashSet<>())
                .apply();

        return null;
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

        // Open the drawer and prompt user to add a location
        parentDrawerLayout.openDrawer(GravityCompat.START);

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
                        PreferenceManager.getDefaultSharedPreferences(getContext());

                Set<String> locations =
                        sharedPreferences.getStringSet(WeatherLocationUtil.SP_LOCATION_SET, null);

                if (locations.isEmpty()) {
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

    @Override
    public void onClick(View v) {

        Intent intent = null;

        switch (v.getId()) {
            // If the manageLocationsButton is clicked open LocationsActivity
            case R.id.manage_locations_button: {
                intent = new Intent(getContext(), LocationsActivity.class);
                break;
            }

            // Rotate sun and open github page when the drawer header is clicked
            case R.id.nav_header: {
                ImageView sunImage = drawerHeader.findViewById(R.id.nav_header_image);
                Drawable drawable = sunImage.getDrawable();

                if (drawable instanceof AnimatedVectorDrawable) {
                    ((AnimatedVectorDrawable) drawable).start();

                    // Stop animation and open github page
                    new Handler().postDelayed(() -> {
                        String githubPage = "https://github.com/AfricanBongo/ClearSkyes";
                        Intent gitIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(githubPage));
                        getContext().startActivity(gitIntent);

                        ((AnimatedVectorDrawable) drawable).stop();

                    }, 2000L);
                }
                break;
            }

            // Open site upon clicking image
            case R.id.credit_image: {
                String websiteURL = "https://www.weatherapi.com";
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteURL));
                break;
            }

            // Open settings activity when clicked
            case R.id.settings_button: {
                intent = new Intent(getContext(), SettingsActivity.class);
                break;
            }

            // Open feedback activity when clicked
            case R.id.feedback_button: {
                intent = new Intent(getContext(), FeedbackActivity.class);
                break;
            }
        }

        if (intent != null) {
            getContext().startActivity(intent);
        }
    }
}
