package com.africanbongo.clearskyes.model.weatherobjects;

import android.content.Context;
import android.widget.ImageView;

import com.africanbongo.clearskyes.R;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

@RunWith(RobolectricTestRunner.class)
public class WeatherConditionTest {

    WeatherCondition condition;

    @Before
    public void before() {
        condition =
                new WeatherCondition("Cloudy",
                        "https://img.icons8.com/doodle/48/000000/partly-cloudy-day.png",
                        1000, true);
    }

    // Test if a drawable is loaded into an ImageView
    // Returns null
    @Test
    public void loadConditionImage() {

        Context context = RuntimeEnvironment.application.getApplicationContext();
        ImageView testView = new ImageView(context);

        condition.loadConditionImage(testView);

        Assert.assertNull(testView.getDrawable());
    }
}