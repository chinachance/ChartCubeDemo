package com.haohao.framwork.chartcubedemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.LineCircleChart;
import com.github.mikephil.charting.renderer.LineChartCircleRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * 设置第几个x坐标对应的小圆点是否可见
 */
public class MainActivity extends AppCompatActivity {

    private LineCircleChart mChart;
    private Toolbar mToolbar;
    private LineCircleChart chart_main;
    private List<String> mXValue;
    private List<Float> mYValue;
    private List<List<Float>> mYValues;
    private List<Integer> mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mChart = (LineCircleChart) findViewById(R.id.chart_main);
        initChart(mChart);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar_main);
        setSupportActionBar(mToolbar);
    }

    private void initChart(LineCircleChart mChart) {
        //模拟数据
        mXValue = new ArrayList<>();
        mYValue = new ArrayList<>();
        mYValues = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            mXValue.add(i + "");
            mYValue.add(i % 2 == 0 ? i * 10f : i * 1f);
        }
        mYValues.add(mYValue);

        //设置那几个位置的圆点显示
        mPosition = new ArrayList<>();
        mPosition.add(0);
        mPosition.add(3);
        mPosition.add(6);
        LineChartCircleRenderer.setCirclePoints(mPosition);

        //设置图表
        HaoChartStyleFour.setLinesChart(this, mChart, mXValue, mYValues, new int[]{R.color.red});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_action1 ://不渐变图表
                //设置图表
                LineChartCircleRenderer.isSetColorGradient(false);
                HaoChartStyleFour.setLinesChart(this, mChart, mXValue, mYValues, new int[]{R.color.red});
                break;
            case R.id.toolbar_action2 ://渐变图表
                //设置颜色渐变
                LineChartCircleRenderer.isSetColorGradient(true);
                LineChartCircleRenderer.setGradientColors(new int[]{Color.RED, Color.BLUE});
                //设置图表
                HaoChartStyleFour.setLinesChart(this, mChart, mXValue, mYValues, new int[]{R.color.red});
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
