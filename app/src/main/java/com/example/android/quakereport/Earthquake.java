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
    private String location;

    /**
     * 地震发生时间
     */
    private String time;

    /**
     * 构造函数
     * @param magnitude 震级
     * @param location 地点
     * @param time 时间
     */
    public Earthquake(String magnitude, String location, String time) {
        this.magnitude = magnitude;
        this.location = location;
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
    public String getLocation() {
        return location;
    }

    /**
     * 获取地震发生时间
     * @return String
     */
    public String getTime() {
        return time;
    }
}
