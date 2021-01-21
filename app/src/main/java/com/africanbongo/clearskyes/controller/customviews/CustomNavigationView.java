package com.africanbongo.clearskyes.controller.customviews;

import android.content.Context;
import android.content.Intent;
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

import com.africanbongo.clearskyes.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.navigation.NavigationView;

public class CustomNavigationView extends NavigationView {

    private MaterialButtonToggleGroup locationsGroup;
    private MaterialButton manageLocationsButton;

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
        NavigationView navigationView =
                (NavigationView) LayoutInflater
                .from(getContext())
                .inflate(R.layout.custom_navigation_view, this, true);

        manageLocationsButton = navigationView.findViewById(R.id.manage_locations_button);
        locationsGroup = navigationView.findViewById(R.id.locations_toggle_group);
        locationsGroup.setSingleSelection(true);

        View drawerHeader = navigationView.findViewById(R.id.nav_header);
        ImageView sunImage = drawerHeader.findViewById(R.id.nav_header_image);

        // Rotate sun and open github page when the drawer header is clicked
        drawerHeader.setOnClickListener(e -> {

            Drawable drawable = sunImage.getDrawable();

            if (drawable instanceof AnimatedVectorDrawable) {
                ((AnimatedVectorDrawable) drawable).start();

                // Stop animation and open github page
                new Handler().postDelayed(() -> {
                    String githubPage = "https://github.com/AfricanBongo/ClearSkyes";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(githubPage));
                    getContext().startActivity(intent);

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
            getContext().startActivity(intent);
        });
    }

    public MaterialButtonToggleGroup getLocationsGroup() {
        return locationsGroup;
    }

    public MaterialButton getManageLocationsButton() {
        return manageLocationsButton;
    }
}
