# 指定特定位置的圆点的显示

[TOC]

## 先看效果图

 ![1](1.png)



## 源码修改

我们知道，MPAndroidChart这个依赖库的图表的所有绘制都在renderer中，所有我们找到LineChartRender，找到drawCircles（）方法，这就是绘制圆点的方法，好了，现在开始修改。

1.复制LineChartRender，命名为LineChartCircleRenderer，添加代码：

~~~~JAVA
/**
     * 显示原点的x轴的对应的数组
     */
    private static List<Integer> mCirclePointPositions = new ArrayList<>();

    /**
     * 设置显示哪个x轴对应的原点
     *
     * @param positions 显示原点的数据所在的位置的集合
     */
    public static void setCirclePoints(List<Integer> positions) {
        mCirclePointPositions = positions;
    }
~~~~

修改drawCircles()方法：

~~~~JAVA
/**
     * 画圆点  -- x轴值对应的y值的小圆圈
     *
     * @param c
     */
    protected void drawCircles(Canvas c) {

        mRenderPaint.setStyle(Paint.Style.FILL);

        float phaseY = mAnimator.getPhaseY();

        mCirclesBuffer[0] = 0;
        mCirclesBuffer[1] = 0;

        List<ILineDataSet> dataSets = mChart.getLineData().getDataSets();

        for (int i = 0; i < dataSets.size(); i++) {

                ILineDataSet dataSet = dataSets.get(i);
                if (!dataSet.isVisible() || !dataSet.isDrawCirclesEnabled() ||
                        dataSet.getEntryCount() == 0)
                    continue;

                mCirclePaintInner.setColor(dataSet.getCircleHoleColor());

                Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

                mXBounds.set(mChart, dataSet);

                float circleRadius = dataSet.getCircleRadius();
                float circleHoleRadius = dataSet.getCircleHoleRadius();
                boolean drawCircleHole = dataSet.isDrawCircleHoleEnabled() &&
                        circleHoleRadius < circleRadius &&
                        circleHoleRadius > 0.f;
                boolean drawTransparentCircleHole = drawCircleHole &&
                        dataSet.getCircleHoleColor() == ColorTemplate.COLOR_NONE;

                DataSetImageCache imageCache;

                if (mImageCaches.containsKey(dataSet)) {
                    imageCache = mImageCaches.get(dataSet);
                } else {
                    imageCache = new DataSetImageCache();
                    mImageCaches.put(dataSet, imageCache);
                }

                boolean changeRequired = imageCache.init(dataSet);

                // only fill the cache with new bitmaps if a change is required
                if (changeRequired) {
                    imageCache.fill(dataSet, drawCircleHole, drawTransparentCircleHole);
                }

                int boundsRangeCount = mXBounds.range + mXBounds.min;

                for (int j = mXBounds.min; j <= boundsRangeCount; j++) {
                    // TODO: 2017/11/13 改动位置
                    if (mCirclePointPositions.contains(j)) {
                        Entry e = dataSet.getEntryForIndex(j);
                        if (e == null)
                            break;

                        mCirclesBuffer[0] = e.getX();
                        mCirclesBuffer[1] = e.getY() * phaseY;

                        trans.pointValuesToPixel(mCirclesBuffer);

                        if (!mViewPortHandler.isInBoundsRight(mCirclesBuffer[0]))
                            break;

                        if (!mViewPortHandler.isInBoundsLeft(mCirclesBuffer[0]) ||
                                !mViewPortHandler.isInBoundsY(mCirclesBuffer[1]))
                            continue;

                        Bitmap circleBitmap = imageCache.getBitmap(j);

                        if (circleBitmap != null) {
                            c.drawBitmap(circleBitmap, mCirclesBuffer[0] - circleRadius, mCirclesBuffer[1] - circleRadius, null);
                        }
                    }
                }
            }
        }
~~~~

其实就是在`Entry e = dataSet.getEntryForIndex(j);`外包加了一层我们写的if判断，判断只有当位置是我们传入的列表的位置时，才显示。

2.复制LineChart，命名为LineCircleChart，把所有的LineChartRender替换成LineChartCircleRenderer。

~~~~java
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
~~~~

3.使用线性图的时候使用刚改好的LineCircleChart，并调用我们在renderer中添加的方法。

~~~~JAVA
	//设置那几个位置的圆点显示
        List<Integer> position = new ArrayList<>();
        position.add(0);
        position.add(3);
        position.add(6);
        LineChartCircleRenderer.setCirclePoints(position);
~~~~



注意：使用的时候请注意是否开启绘制圆点：

~~~~JAVA
  	//绘制x轴对应的y轴数据值的圆点
        lineDataSet.setDrawCircles(true);
        //绘制的圆点是否是空心
        lineDataSet.setDrawCircleHole(false);
        //圆点的填充颜色
        lineDataSet.setCircleColor(Color.BLACK);
        //圆点的半径
        lineDataSet.setCircleRadius(5f);
~~~~

