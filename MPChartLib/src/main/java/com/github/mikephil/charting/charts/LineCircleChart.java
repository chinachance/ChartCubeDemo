
package com.github.mikephil.charting.charts;

import android.content.Context;
import android.util.AttributeSet;

import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.renderer.LineChartCircleRenderer;

/**
 * Chart that draws lines, surfaces, circles, ...
 *
 * @author Philipp Jahoda
 */
public class LineCircleChart extends BarLineChartBase<LineData> implements LineDataProvider {

    public LineCircleChart(Context context) {
        super(context);
    }

    public LineCircleChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineCircleChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();

        mRenderer = new LineChartCircleRenderer(this, mAnimator, mViewPortHandler);
    }

    @Override
    public LineData getLineData() {
        return mData;
    }

    @Override
    protected void onDetachedFromWindow() {
        // releases the bitmap in the renderer to avoid oom error
        if (mRenderer != null && mRenderer instanceof LineChartCircleRenderer) {
            ((LineChartCircleRenderer) mRenderer).releaseBitmap();
        }
        super.onDetachedFromWindow();
    }
}
