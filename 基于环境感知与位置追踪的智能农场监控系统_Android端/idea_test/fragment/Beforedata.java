package com.example.idea_test.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.idea_test.Handler.DBHelper;
import com.example.idea_test.Handler.MySQLDataFetcher;
import com.example.idea_test.R;
import com.example.idea_test.mysql.*;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.*;


public class Beforedata extends Fragment {

    private static final String TAG = "Beforedata";
    private LineChart tempChart, humiChart, flameChart, airChart, lightChart, soilChart;
    private Handler handler = new Handler();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beforedata, container, false);

        // 初始化图表
        tempChart = view.findViewById(R.id.tempChart);
        humiChart = view.findViewById(R.id.humiChart);
        flameChart = view.findViewById(R.id.flameChart);
        airChart = view.findViewById(R.id.airChart);
        lightChart = view.findViewById(R.id.lightChart);
        soilChart = view.findViewById(R.id.soilChart);

        // 设置定期刷新
        handler.postDelayed(dataRefreshRunnable, 1000);

        return view;
    }
private Runnable dataRefreshRunnable = new Runnable() {
        @Override
        public void run() {
            new Thread(() -> {
                // 从MySQL获取数据
                List<TempData> tempData = MySQLDataFetcher.getAllTempData();
                List<HumiData> humiData = MySQLDataFetcher.getAllHumiData();
                List<AirData> airData = MySQLDataFetcher.getAllAirData();
                List<FlameData> flameData = MySQLDataFetcher.getAllFlameData();
                List<LightData> lightData = MySQLDataFetcher.getAllLightData();
                List<SoilData> soilData = MySQLDataFetcher.getAllSoilData();

                // 更新UI
                requireActivity().runOnUiThread(() -> {
                    drawTempChart(tempData);
                    drawHumiChart(humiData);
                    drawAirChart(airData);
                    drawFlameChart(flameData);
                    drawLightChart(lightData);
                    drawSoilChart(soilData);
                });

                // 5秒后再次刷新
                handler.postDelayed(this, 5000);
            }).start();
        }
    };

    private void drawTempChart(List<TempData> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            tempChart.setNoDataText("没有温度数据可用");
            return;
        }

        List<Entry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (int i = 0; i < dataList.size(); i++) {
            TempData data = dataList.get(i);
            entries.add(new Entry(i, data.getValue()));
            labels.add(i % 5 == 0 || i == dataList.size() - 1 ?
                    formatTimeLabel(data.getTime()) : "");
        }

        LineDataSet dataSet = new LineDataSet(entries, "温度(°C)");
        dataSet.setColor(Color.RED);
        dataSet.setLineWidth(2f);
        dataSet.setCircleColor(Color.RED);
        dataSet.setCircleRadius(3f);
        dataSet.setDrawValues(false);

        LineData lineData = new LineData(dataSet);
        tempChart.setData(lineData);

        // 配置图表...
        tempChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        tempChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        tempChart.getXAxis().setLabelRotationAngle(-45);
        tempChart.invalidate();
    }

    private void drawHumiChart(List<HumiData> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            humiChart.setNoDataText("没有湿度数据可用");
            return;
        }

        List<Entry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (int i = 0; i < dataList.size(); i++) {
            HumiData data = dataList.get(i);
            entries.add(new Entry(i, data.getValue()));
            labels.add(i % 5 == 0 || i == dataList.size() - 1 ?
                    formatTimeLabel(data.getTime()) : "");
        }

        LineDataSet dataSet = new LineDataSet(entries, "湿度(%)");
        dataSet.setColor(Color.BLUE);
        dataSet.setLineWidth(2f);
        dataSet.setCircleColor(Color.BLUE);
        dataSet.setCircleRadius(3f);
        dataSet.setDrawValues(false);

        LineData lineData = new LineData(dataSet);
        humiChart.setData(lineData);

        // 配置图表...
        humiChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        humiChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        humiChart.getXAxis().setLabelRotationAngle(-45);
        humiChart.invalidate();
    }
    private void drawAirChart(List<AirData> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            airChart.setNoDataText("没有空气质量数据可用");
            return;
        }

        List<Entry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (int i = 0; i < dataList.size(); i++) {
            AirData data = dataList.get(i);
            entries.add(new Entry(i, data.getValue()));
            labels.add(i % 5 == 0 || i == dataList.size() - 1 ?
                    formatTimeLabel(data.getTime()) : "");
        }

        LineDataSet dataSet = new LineDataSet(entries, "空气质量(ppm)");
        dataSet.setColor(Color.parseColor("#9C27B0"));
        dataSet.setCircleColor(Color.parseColor("#9C27B0"));
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(3f);
        dataSet.setDrawValues(false);

        LineData lineData = new LineData(dataSet);
        airChart.setData(lineData);

        // 配置图表...
        airChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        airChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        airChart.getXAxis().setLabelRotationAngle(-45);
        airChart.invalidate();
    }
    private void drawFlameChart(List<FlameData> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            flameChart.setNoDataText("没有火焰值数据可用");
            return;
        }

        List<Entry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (int i = 0; i < dataList.size(); i++) {
            FlameData data = dataList.get(i);
            entries.add(new Entry(i, data.getValue()));
            labels.add(i % 5 == 0 || i == dataList.size() - 1 ?
                    formatTimeLabel(data.getTime()) : "");
        }

        LineDataSet dataSet = new LineDataSet(entries, "火焰值：");
        dataSet.setColor(Color.parseColor("#FF9800"));
        dataSet.setCircleColor(Color.parseColor("#FF9800"));
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(3f);
        dataSet.setDrawValues(false);

        LineData lineData = new LineData(dataSet);
        flameChart.setData(lineData);

        // 配置图表...
        flameChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        flameChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        flameChart.getXAxis().setLabelRotationAngle(-45);
        flameChart.invalidate();
    }
    private void drawLightChart(List<LightData> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            lightChart.setNoDataText("没有光照数据可用");
            return;
        }

        List<Entry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (int i = 0; i < dataList.size(); i++) {
            LightData data = dataList.get(i);
            entries.add(new Entry(i, data.getValue()));
            labels.add(i % 5 == 0 || i == dataList.size() - 1 ?
                    formatTimeLabel(data.getTime()) : "");
        }

        LineDataSet dataSet = new LineDataSet(entries, "光照值(lve)");
        dataSet.setColor(Color.YELLOW);
        dataSet.setCircleColor(Color.YELLOW);
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(3f);
        dataSet.setDrawValues(false);

        LineData lineData = new LineData(dataSet);
        lightChart.setData(lineData);

        // 配置图表...
        lightChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        lightChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lightChart.getXAxis().setLabelRotationAngle(-45);
        lightChart.invalidate();
    }
    private void drawSoilChart(List<SoilData> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            soilChart.setNoDataText("没有土壤湿度数据可用");
            return;
        }

        List<Entry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (int i = 0; i < dataList.size(); i++) {
            SoilData data = dataList.get(i);
            entries.add(new Entry(i, data.getValue()));
            labels.add(i % 5 == 0 || i == dataList.size() - 1 ?
                    formatTimeLabel(data.getTime()) : "");
        }

        LineDataSet dataSet = new LineDataSet(entries, "土壤湿度(%");
        dataSet.setColor(Color.parseColor("#795548"));
        dataSet.setCircleColor(Color.parseColor("#795548"));
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(3f);
        dataSet.setDrawValues(false);

        LineData lineData = new LineData(dataSet);
        soilChart.setData(lineData);

        // 配置图表...
        soilChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        soilChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        soilChart.getXAxis().setLabelRotationAngle(-45);
        soilChart.invalidate();
    }


    private String formatTimeLabel(String time) {
        try {
            // 时间格式化逻辑...
            return time.substring(11, 16); // 简单截取HH:mm部分
        } catch (Exception e) {
            return time;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(dataRefreshRunnable);
    }
    private void configureCommonChart(LineChart chart, List<String> labels) {
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setLabelRotationAngle(-45);
        chart.getXAxis().setGranularity(1f);

        chart.getAxisRight().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(true);

        chart.invalidate();
    }
}