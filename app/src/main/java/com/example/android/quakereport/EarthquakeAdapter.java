package com.example.android.quakereport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 继承{@link ArrayAdapter}
 * 显示list_item的元素
 */
public class EarthquakeAdapter extends ArrayAdapter {

    /**
     * 构造函数
     * @param context context
     * @param resource 布局资源id
     * @param objects 数据列表
     */
    public EarthquakeAdapter(Context context, int resource, List<Earthquake> objects) {
        super(context, resource, objects);
    }


    /**
     * 重写getView
     * @param position 当前数据的位置
     * @param convertView 当前布局
     * @param parent 父布局
     * @return View
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.earthquake_list_item, parent, false);
        }

        // 获取当前位置的数据
        Earthquake earthquake = (Earthquake) getItem(position);

        // 显式震级
        TextView textMagnitude = (TextView) listItemView.findViewById(R.id.text_magnitude);
        textMagnitude.setText(earthquake.getMagnitude());

        // 显式位置
        TextView textLocation = (TextView) listItemView.findViewById(R.id.text_location);
        textLocation.setText(earthquake.getPlace());

        // 显式日期和时间
        TextView textDate = (TextView) listItemView.findViewById(R.id.text_date);
        TextView textTime = (TextView) listItemView.findViewById(R.id.text_time);
        // 格式化日期和时间
        Date dateObject = new Date(earthquake.getTime());
        String dateToDisplay = formatDate(dateObject);
        String timeToDisplay = formatTime(dateObject);
        // 设置
        textDate.setText(dateToDisplay);
        textTime.setText(timeToDisplay);

        return listItemView;
    }

    /**
     * 格式化时间
     * eg: 12:45 PM
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
     * @param date 日期
     * @return String
     */
    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String dateToDisplay = dateFormat.format(date);
        return dateToDisplay;
    }
}
