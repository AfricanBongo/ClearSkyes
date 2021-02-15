package com.africanbongo.clearskyes.controller.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;

import com.africanbongo.clearskyes.R;
import com.africanbongo.clearskyes.controller.customviews.WeatherDetailItemView;
import com.africanbongo.clearskyes.model.WeatherCondition;
import com.africanbongo.clearskyes.model.WeatherDay;
import com.africanbongo.clearskyes.model.WeatherHour;
import com.africanbongo.clearskyes.model.WeatherLocation;
import com.africanbongo.clearskyes.model.WeatherMappable;
import com.africanbongo.clearskyes.model.WeatherMisc;
import com.africanbongo.clearskyes.model.WeatherObject;
import com.africanbongo.clearskyes.model.WeatherTemp;
import com.africanbongo.clearskyes.model.WeatherToday;
import com.africanbongo.clearskyes.util.WeatherJsonUtil;
import com.africanbongo.clearskyes.util.WeatherLocationUtil;
import com.africanbongo.clearskyes.util.WeatherTimeUtil;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * An activity that displays the weather in detail.
 * <p>
 *     Always include only one of the following as the calling {@link Intent}'s {@link android.os.Parcel} Extra:
 *     <ul>
 *         <li>{@link com.africanbongo.clearskyes.model.WeatherHour}</li>
 *         <li>{@link com.africanbongo.clearskyes.model.WeatherToday}</li>
 *         <li>{@link com.africanbongo.clearskyes.model.WeatherDay}</li>
 *     </ul>
 * </p>
 *
 */
public class WeatherDetailActivity extends AppCompatActivity {

    private WeatherMappable mappableObject;
    private LinearLayout nestedScrollViewChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_detail);
        nestedScrollViewChild = findViewById(R.id.detail_nested_scroll_child);
        MaterialToolbar materialToolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(materialToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);

        // Get degree and measurement type
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        String activeLocation = preferences.getString(WeatherLocationUtil.SP_ACTIVE_LOCATION, null);
        WeatherLocation weatherLocation = WeatherLocationUtil.deserialize(activeLocation);
        String location = weatherLocation.getShortStringLocation();
        String measurementKey = getString(R.string.measurements_key);
        String meausurementDefault = getString(R.string.measurements_default);
        String degreesKey = getString(R.string.degrees_key);
        String degreesDefault = getString(R.string.degrees_default);
        String measurementType = preferences.getString(measurementKey, meausurementDefault);
        String degreesType = preferences.getString(degreesKey, degreesDefault);
        WeatherMisc.Measurement measurement = WeatherMisc.Measurement.getMeasurement(measurementType);
        WeatherTemp.Degree degree = WeatherTemp.Degree.getDegree(degreesType);

        mappableObject = getIntent().getParcelableExtra(WeatherJsonUtil.INTENT_EXTRA);
        loadWeatherSummary(getIntent(), location, degree);
        loadDetailViews(degree, measurement);
    }

    // Load image, temperature and location of the weather details
    public void loadWeatherSummary(Intent intent, String location, WeatherTemp.Degree degree) {
        if (location != null) {
            WeatherCondition condition = null;
            WeatherTemp temp = null;
            String tempDescription = null;
            String timeDescription = "Time title";
            String time = intent.getStringExtra(WeatherTimeUtil.INTENT_EXTRA);

            ImageView imageView = findViewById(R.id.detail_weather_icon);
            TextView timeTextView = findViewById(R.id.detail_weather_time);
            TextView tempTextView = findViewById(R.id.detail_temp);
            TextView conditionTextView = findViewById(R.id.detail_weather_condition_text);

            switch (intent.getAction()) {
                case WeatherHour.INTENT_ACTION: {
                    WeatherHour weatherHour = (WeatherHour) mappableObject;
                    condition = weatherHour.getConditions();
                    temp = weatherHour.getActualTemp();
                    tempDescription = WeatherObject.ACTUAL_TEMP_KEY;
                    break;
                }

                case WeatherDay.INTENT_ACTION: {
                    WeatherDay weatherDay = (WeatherDay) mappableObject;
                    condition = weatherDay.getConditions();
                    temp = weatherDay.getAvgTemp();
                    tempDescription = WeatherDay.AVG_TEMP_KEY;
                    break;
                }

                case WeatherToday.INTENT_ACTION: {
                    WeatherToday weatherToday = (WeatherToday) mappableObject;
                    condition = weatherToday.getNowWeather().getConditions();
                    temp = weatherToday.getNowWeather().getActualTemp();
                    tempDescription = WeatherObject.ACTUAL_TEMP_KEY;
                    break;
                }
            }

            getSupportActionBar().setTitle(location);
            String tempText = temp.getTemp(degree) + WeatherTemp.Degree.DEGREE_SIGN;
            timeTextView.setText(time);
            tempTextView.setText(tempText);
            condition.loadConditionImage(imageView);
            conditionTextView.setText(condition.getConditionText());
            timeTextView.setTooltipText(timeDescription);
            timeTextView.setContentDescription(timeDescription + time);
            tempTextView.setTooltipText(tempDescription);
            tempTextView.setContentDescription(tempDescription + tempText);
        }

    }


    // Load data into weather detail bes
    public void loadDetailViews(WeatherTemp.Degree degree, WeatherMisc.Measurement measurement) {
        LinkedHashMap<String, String> hashMap = mappableObject.getMapping(degree, measurement);

        Set<String> keySet = hashMap.keySet();
        Iterator<String> mapIterator = keySet.iterator();

        while (mapIterator.hasNext()) {
            float smallGap = getResources().getDimension(R.dimen.small_gap);
            LinearLayout categoryView = new LinearLayout(this);
            categoryView.setOrientation(LinearLayout.VERTICAL);
            categoryView.setElevation(smallGap);
            categoryView.setTranslationZ(smallGap);
            categoryView.setBackgroundResource(R.drawable.secondary_color_curved_background);

            for (int i = 0; i < mappableObject.categorySize(); i++) {
                if (mapIterator.hasNext()) {
                    String detailTitle = mapIterator.next();
                    String detailContent = hashMap.get(detailTitle);

                    WeatherDetailItemView detailItemView = new WeatherDetailItemView(this);
                    detailItemView.setDetailTitle(detailTitle);
                    detailItemView.setDetailContent(detailContent);
                    categoryView.addView(detailItemView);
                    continue;
                }

                break;
            }

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);

            params.topMargin = (int) smallGap;
            params.bottomMargin = (int) smallGap;

            // Add WeatherCategory to nested scroll view child which is a linear layout
            nestedScrollViewChild.addView(categoryView, params);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ActivityCompat.finishAfterTransition(this);
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        ActivityCompat.finishAfterTransition(this);
    }
}