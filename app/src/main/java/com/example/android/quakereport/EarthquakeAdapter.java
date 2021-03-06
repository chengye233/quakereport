package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 继承{@link ArrayAdapter}
 * 显示list_item的元素
 */
public class EarthquakeAdapter extends ArrayAdapter {

    /**
     * 为了拆分距离和位置
     */
    private static final String LOCATION_SEPARATOR = " of ";

    /**
     * 构造函数
     *
     * @param context  context
     * @param resource 布局资源id
     * @param objects  数据列表
     */
    public EarthquakeAdapter(Context context, int resource, List<Earthquake> objects) {
        super(context, resource, objects);
    }


    /**
     * 重写getView
     *
     * @param position    当前数据的位置
     * @param convertView 当前布局
     * @param parent      父布局
     * @return View
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.earthquake_list_item, parent, false);
        }

        // 获取当前的数据
        Earthquake earthquake = (Earthquake) getItem(position);

        // 显式震级
        TextView textMagnitude = (TextView) listItemView.findViewById(R.id.text_magnitude);
        double magnitude = earthquake.getMagnitude();
        // 保留1为小数
        String magnitudeToDisplay = formatMagnitude(magnitude);
        // 设置震级文字
        textMagnitude.setText(magnitudeToDisplay);
        // 为震级圆圈设置正确的背景颜色。
        // 从 TextView 获取背景，该背景是一个 GradientDrawable。
        GradientDrawable magnitudeCircle = (GradientDrawable) textMagnitude.getBackground();
        // 根据当前的地震震级获取相应的背景颜色
        int magnitudeColor = getMagnitudeColor(magnitude);

        // 设置震级圆圈的颜色
        magnitudeCircle.setColor(magnitudeColor);

        // 显式地震发生的距离和位置
        TextView textLocation = (TextView) listItemView.findViewById(R.id.text_location);
        TextView textDistance = (TextView) listItemView.findViewById(R.id.text_distance);
        // 拆分距离和位置
        String distanceAndLocation = earthquake.getPlace();
        String distanceToDisplay = null;
        String locationToDisplay = null;
        if (distanceAndLocation.contains(LOCATION_SEPARATOR)) {
            String[] dl = distanceAndLocation.split(LOCATION_SEPARATOR);
            distanceToDisplay = dl[0] + LOCATION_SEPARATOR;
            locationToDisplay = dl[1];
        } else {
            distanceToDisplay = getContext().getString(R.string.near_the);
            locationToDisplay = distanceAndLocation;
        }
        // 设置距离和位置文字
        textDistance.setText(distanceToDisplay);
        textLocation.setText(locationToDisplay);

        // 显式地震发生的日期和时间
        TextView textDate = (TextView) listItemView.findViewById(R.id.text_date);
        TextView textTime = (TextView) listItemView.findViewById(R.id.text_time);
        // 格式化日期和时间
        Date dateObject = new Date(earthquake.getTime());
        String dateToDisplay = formatDate(dateObject);
        String timeToDisplay = formatTime(dateObject);
        // 设置时间和日期文字
        textDate.setText(dateToDisplay);
        textTime.setText(timeToDisplay);

        return listItemView;
    }

    /**
     * 判断震级对应的颜色
     *
     * @param magnitude 震级
     * @return int
     */
    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }


    /**
     * 格式化震级
     * eg: 6.0
     */
    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    /**
     * 格式化时间
     * eg: 12:45 PM
     *
     * @param time 时间
     * @return String
     */
    private String formatTime(Date time) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        String timeToDisplay = timeFormat.format(time);
        return timeToDisplay;
    }

    /**
     * 格式化日期
     * eg: Feb 23, 8102
     *
     * @param date 日期
     * @return String
     */
    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String dateToDisplay = dateFormat.format(date);
        return dateToDisplay;
    }
}
