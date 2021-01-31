package com.africanbongo.clearskyes.controller.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.preference.DialogPreference;

import com.africanbongo.clearskyes.R;
import com.africanbongo.clearskyes.model.weatherapi.util.WeatherTimeUtil;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Show dialog with a {@link android.widget.TimePicker} object,
 * credit to
 * <a href="https://stackoverflow.com/questions/59060442/timepicker-custom-preference-for-androidx-preference-dialogpreference">StackOverflow solution</a>
 */
public class TimePreference extends DialogPreference {

    private DateTimeFormatter dateTimeFormatter;
    private LocalTime notificationTime;
    private int mHour;
    private int mMinute;
    private static final int DEFAULT_HOUR = 7;
    private static final int DEFAULT_MINUTE = 0;
    private static final String POSITIVE_TEXT = "Set";
    private static final String NEGATIVE_TEXT = "Cancel";

    public TimePreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public TimePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public TimePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TimePreference(Context context) {
        super(context);
        init();
    }

    public void init() {
        setPositiveButtonText(POSITIVE_TEXT);
        setNegativeButtonText(NEGATIVE_TEXT);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(@Nullable Object defaultValue) {
        String defaultTime = convertTime(DEFAULT_HOUR, DEFAULT_MINUTE);
        String time = getPersistedString(defaultTime);

        mHour = DEFAULT_HOUR;
        mMinute = DEFAULT_MINUTE;

        if (!time.equals(defaultTime)) {
            mHour = parseHour(time);
            mMinute = parseMinute(time);
        }
        setTime(mHour, mMinute);
    }

    public int parseHour(String time) {
        String[] array = time.split(":");
        return Integer.parseInt(array[0]);
    }

    public int parseMinute(String time) {
        String[] array = time.split(":");
        return Integer.parseInt(array[1]);
    }
    public String convertTime(int hour, int minute) {
        dateTimeFormatter = DateTimeFormatter.ofPattern(WeatherTimeUtil.TIME_FORMAT);
        notificationTime = LocalTime.of(hour, minute);
        return dateTimeFormatter.format(notificationTime);
    }

    public String setTime(int hour, int minute) {
        String time = convertTime(hour, minute);
        mHour = hour;
        mMinute = minute;
        persistString(time);
        setSummary(time);
        notifyChanged();

        return time;
    }


    public int getHour() {
        return mHour;
    }

    public int getMinute() {
        return mMinute;
    }
}
