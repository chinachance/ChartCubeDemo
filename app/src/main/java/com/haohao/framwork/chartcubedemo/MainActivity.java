package com.haohao.framwork.chartcubedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineCircleChart;
import com.github.mikephil.charting.renderer.LineChartCircleRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * 设置第几个x坐标对应的小圆点是否可见
 */
public class MainActivity extends AppCompatActivity {

    private LineCircleChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mChart = (LineCircleChart) findViewById(R.id.chart_main);
        initChart(mChart);
    }

    private void initChart(LineCircleChart mChart) {
        //模拟数据
        List<String> xValue = new ArrayList<>();
        List<Float> yValue = new ArrayList<>();
        List<List<Float>> yValues = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            xValue.add(i + "");
            yValue.add(i % 2 == 0 ? i * 10f : i * 1f);
        }
        yValues.add(yValue);

        //设置那几个位置的圆点显示
        List<Integer> position = new ArrayList<>();
        position.add(0);
        position.add(3);
        position.add(6);
        LineChartCircleRenderer.setCirclePoints(position);

        //设置图表
        HaoChartStyleFour.setLinesChart(this,mChart,xValue,yValues,new int[]{R.color.red});
    }
}
