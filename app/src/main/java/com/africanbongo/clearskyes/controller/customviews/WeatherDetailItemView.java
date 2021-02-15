package com.africanbongo.clearskyes.controller.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.africanbongo.clearskyes.R;


/**
 * {@link RelativeLayout} that contains information about a certain weather detail.
 */
public class WeatherDetailItemView extends RelativeLayout {

    private TextView detailTitle;
    private TextView detailContent;

    public WeatherDetailItemView(Context context) {
        super(context);
        init(context);
    }

    public WeatherDetailItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WeatherDetailItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public WeatherDetailItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }


    public void init(Context context) {
        RelativeLayout layout = (RelativeLayout)
                LayoutInflater
                .from(context)
                .inflate(R.layout.weather_detail_item, this, true);

        detailTitle = layout.findViewById(R.id.detail_title);
        detailContent = layout.findViewById(R.id.detail_content);
    }

    public void setDetailContent(String detailContent) {
        this.detailContent.setText(detailContent);
        this.detailContent.setContentDescription(detailContent);
    }

    public void setDetailTitle(String detailTitle) {
        this.detailTitle.setText(detailTitle);
        this.detailTitle.setContentDescription(detailTitle);
    }
}
