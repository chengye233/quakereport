package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * 加载器{@link AsyncTaskLoader} 在后台从USGS获取地震数据
 * 管理{@link android.os.AsyncTask}
 * 避免每次旋转屏幕会创建新的{@link android.app.Activity}和{@link android.os.AsyncTask}
 * 通过使用 AsyncTask 执行
 * 给定 URL 的网络请求，加载地震列表。
 */
public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    /**
     * 日志消息标签
     */
    private static final String LOG_TAG = EarthquakeLoader.class.getName();

    /**
     * 查询 URL
     */
    private String url;

    /**
     * 构建新 {@link EarthquakeLoader}。
     * <p>
     * 活动的 @param 上下文
     * 要从中加载数据的 @param url
     */
    public EarthquakeLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        Log.v(LOG_TAG, "调用onStartLoading");


        forceLoad();
    }

    /**
     * 这位于后台线程上。
     */
    @Override
    public List<Earthquake> loadInBackground() {
        Log.v(LOG_TAG, "调用loadInBackground");



        if (url == null) {
            return null;
        }

        // 执行网络请求、解析响应和提取地震列表。
        List<Earthquake> earthquakes = QueryUtils.fetchEarthquakeData(url);
        return earthquakes;
    }
}
