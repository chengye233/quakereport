package com.example.android.quakereport;

/**
 * 用来存储地震{@link Earthquake}的信息
 */
public class Earthquake {

    /**
     * 地震的震级
     */
    private String magnitude;

    /**
     * 地震发生位置
     */
    private String place;

    /**
     * 地震发生时间
     */
    private String time;

    /**
     * 构造函数
     * @param magnitude 震级
     * @param place 地点
     * @param time 时间
     */
    public Earthquake(String magnitude, String place, String time) {
        this.magnitude = magnitude;
        this.place = place;
        this.time = time;
    }

    /**
     * 获取震级
     * @return String
     */
    public String getMagnitude() {
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
     * @return String
     */
    public String getTime() {
        return time;
    }
}
