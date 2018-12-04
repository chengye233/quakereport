package com.example.android.quakereport;

/**
 * 用来存储地震{@link Earthquake}的信息
 */
public class Earthquake {

    /**
     * 地震的震级
     */
    private double magnitude;

    /**
     * 地震发生位置
     */
    private String place;

    /**
     * 地震发生时间
     * 毫秒
     */
    private long time;

    /**
     * USGS的地震信息的url
     */
    private String url;

    /**
     * 构造函数
     * @param magnitude 震级
     * @param place 地点
     * @param time 时间
     * @param url
     */
    public Earthquake(double magnitude, String place, long time, String url) {
        this.magnitude = magnitude;
        this.place = place;
        this.time = time;
        this.url = url;
    }

    /**
     * 获取震级
     * @return double
     */
    public double getMagnitude() {
        return magnitude;
    }

    /**
     * 获取地震发生位置
     * @return String
     */
    public String getPlace() {
        return place;
    }

    /**
     * 获取地震发生时间
     * 毫秒
     * @return long
     */
    public long getTime() {
        return time;
    }

    /**
     * USGS地震信息的url
     * @return String
     */
    public String getUrl() {
        return url;
    }
}
