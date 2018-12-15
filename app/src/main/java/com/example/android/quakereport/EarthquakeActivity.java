/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

/**
 * MainActivity
 */
public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // 后台线程获取数据
        EarthquakeAsyncTask earthquakeAsyncTask = new EarthquakeAsyncTask();
        earthquakeAsyncTask.execute(USGS_REQUEST_URL);

    }

    /**
     * {@link AsyncTask} 用于在后台线程上执行网络请求，然后
     * 使用响应中的第一个地震更新 UI。
     */
    private class EarthquakeAsyncTask extends AsyncTask<String, Void, List<Earthquake>> {

        /**
         * 此方法在后台线程上激活（调用），因此我们可以执行
         * 诸如做出网络请求等长时间运行操作。
         * <p>
         * 因为不能从后台线程更新 UI，所以我们仅返回
         * {@link List<Earthquake>} 对象作为结果。
         */
        protected List<Earthquake> doInBackground(String... urls) {
            // 如果不存在任何 URL 或第一个 URL 为空，切勿执行请求。
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<Earthquake> result = QueryUtils.fetchEarthquakeData(urls[0]);
            return result;
        }

        /**
         * 此方法是在完成后台工作后，在主 UI 线程上
         * 激活的。
         * <p>
         * 可以在此方法内修改 UI。我们得到 {@link List<Earthquake>} 对象
         * （该对象从 doInBackground() 方法返回），并更新屏幕上的视图。
         */
        protected void onPostExecute(List<Earthquake> result) {
            // 如果不存在任何结果，则不执行任何操作。
            if (result == null) {
                return;
            }

            updateUi(result);
        }
    }

    /**
     * 更新布局
     */
    private void updateUi(List<Earthquake> earthquakes) {
        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new {@link EarthquakeAdapter} of earthquakes
        final EarthquakeAdapter earthquakeAdapter = new EarthquakeAdapter(
                this, R.layout.earthquake_list_item, earthquakes);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(earthquakeAdapter);

        // ListView设置监听器 点击发送intent 跳转浏览器
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 从Adapter获取当前Earthquake
                Earthquake earthquake = (Earthquake) earthquakeAdapter.getItem(position);

                // 将字符串 URL 转换为 URI 对象（以传递至 Intent 中 constructor)
                Uri earthquakeUri = Uri.parse(earthquake.getUrl());

                // 创建一个新的 Intent 以查看地震 URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // 发送 Intent 以启动新活动
                startActivity(websiteIntent);
            }
        });
    }
}
