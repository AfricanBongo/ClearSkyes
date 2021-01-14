package com.africanbongo.clearskyes.controller.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.africanbongo.clearskyes.R;

/**
 * Class with TextViews that hold the date of the weather info
 */
public class CustomDateView extends RelativeLayout {

    private RelativeLayout layout;
    private TextView relativeDay;
    private TextView exactDate;

    public CustomDateView(Context context) {
        super(context);
        init();
    }

    public CustomDateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomDateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CustomDateView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        layout =
                (RelativeLayout) LayoutInflater
                .from(getContext())
                .inflate(R.layout.custom_date_view, this, true);

        // Get the views within the view group
        relativeDay = layout.findViewById(R.id.today_relative_day);
    }

    public void setDate(String relativeDayString) {
        relativeDay.setText(relativeDayString);
    }
}
