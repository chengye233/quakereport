package com.example.android.quakereport;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * 从USGS获取 {@link Earthquake} 列表
 */
public final class QueryUtils {

    /**
     * LOG_TAG
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * 单例
     */
    private QueryUtils() {
    }

    /**
     * 通过链查询USGS返回 {@link Earthquake} 列表
     */
    public static List<Earthquake> fetchEarthquakeData(String requestUrl) {

        // 等待2s
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Log.e(LOG_TAG, "fetchEarthquakeData等待2s", e);
        }

        // 创建URL对象
        URL url = createUrl(requestUrl);

        // 构建JSON数据响应
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "fetchEarthquakeData出错", e);
        }

        // 解析JSON数据为{@link Earthquake}列表
        List<Earthquake> earthquakeList = extractEarthquakes(jsonResponse);

        // 返回{@link Earthquake}列表
        return earthquakeList;
    }

    /**
     * 用url{@link String} 创建 {@link URL} 对象
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "createUrl出错", e);
        }
        return url;
    }

    /**
     * 用{@link URL} 创建含有JSON的{@link String}
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // 保证URL非空
        if (url == null) {
            return jsonResponse;
        }

        // 获取JSON
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            // 创建连接
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            // GET
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // 200则成功
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "makeHttpRequest出错", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * 转换 {@link InputStream} 为JSON字符串
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * 解析JSON为 {@link Earthquake} 列表
     */
    private static List<Earthquake> extractEarthquakes(String jsonResponse) {

        // 创建List
        List<Earthquake> earthquakes = new ArrayList<>();

        // 转换
        try {
            // 创建JSONObject
            JSONObject jsonObject = new JSONObject(jsonResponse);
            // 获取features数组
            JSONArray features = jsonObject.getJSONArray("features");
            // 遍历features数组并填充Earthquake的属性
            for (int i = 0; i < features.length(); i++) {
                // 获取i位置的feature
                JSONObject feature = features.getJSONObject(i);
                // 获取feature的properties属性
                JSONObject properties = feature.getJSONObject("properties");
                // 提取properties的mag属性
                double mag = properties.getDouble("mag");
                // 提取properties的place属性
                String place = properties.getString("place");
                // 提取properties的time属性
                long time = properties.getLong("time");
                // 提取url
                String url = properties.getString("url");

                // 创建{@link Earthquake}对象
                Earthquake earthquake = new Earthquake(mag, place, time, url);
                // 放入{@link List}earthquakes
                earthquakes.add(earthquake);
            }
        } catch (JSONException e) {
            // 错误日志
            Log.e("QueryUtils", "转换JSON为List<Earthquake>出问题", e);
        }

        // 返回{@link List<Earthquake>}
        return earthquakes;
    }
}