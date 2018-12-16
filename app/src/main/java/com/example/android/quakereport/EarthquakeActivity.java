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

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * MainActivity
 * 实现{@link LoaderManager.LoaderCallbacks}当显式数据后进行回调
 */
public class EarthquakeActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    /**
     * {@link EarthquakeAdapter}用来显式数据
     */
    private EarthquakeAdapter earthquakeAdapter;

    // LOG_TAG
    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    // url
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    /**
     * 地震 loader ID 的常量值。我们可选择任意整数。
     * 仅当使用多个 loader 时该设置才起作用。
     */
    private static final int EARTHQUAKE_LOADER_ID = 1;

    // 空视图 在没有地震数据时显式
    private TextView emptyStateTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // 设置empty list
        emptyStateTextView = (TextView) findViewById(R.id.empty);
        earthquakeListView.setEmptyView(emptyStateTextView);

        // 设置{@link Adapter}
        earthquakeAdapter = new EarthquakeAdapter(
                this, R.layout.earthquake_list_item, new ArrayList<Earthquake>());

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

        // 引用 LoaderManager，以便与 loader 进行交互。
        LoaderManager loaderManager = getLoaderManager();

        // 初始化 loader。传递上面定义的整数 ID 常量并为为捆绑
        // 传递 null。为 LoaderCallbacks 参数（由于
        // 此活动实现了 LoaderCallbacks 接口而有效）传递此活动(onCreateLoader)。
        loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
    }

    /**
     * 创建{@link EarthquakeLoader}
     */
    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "调用onCreateLoader");

        // 为给定 URL 创建新 loader
        return new EarthquakeLoader(this, USGS_REQUEST_URL);
    }


    /**
     * 相当于{@link AsyncTask#onPostExecute(Object)}
     * 在{@link AsyncTaskLoader#loadInBackground()}方法执行后调用
     */
    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        // 设置空视图text
        emptyStateTextView.setText(R.string.no_earthquakes);

        Log.v(LOG_TAG, "调用onLoadFinished");


        // 清除之前地震数据的适配器
        earthquakeAdapter.clear();

        // 如果存在 {@link Earthquake} 的有效列表，则将其添加到适配器的
        // 数据集。这将触发 ListView 执行更新。
        if (earthquakes != null && !earthquakes.isEmpty()) {
            earthquakeAdapter.addAll(earthquakes);
        }
    }

    /**
     * 清除无效数据
     */
    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        Log.v(LOG_TAG, "调用onLoaderReset");


        earthquakeAdapter.clear();
    }

}
