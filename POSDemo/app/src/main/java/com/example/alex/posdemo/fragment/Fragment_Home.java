package com.example.alex.posdemo.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.posdemo.GlobalVariable.UserInfo;
import com.example.alex.posdemo.R;
import com.example.alex.posdemo.adapter.recylclerview.SimpleTextAdapter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import Utils.AsyncTaskUtils;
import Utils.IDataCallBack;
import library.AnalyzeJSON.APIpojo.HomeHeaderPojo;
import library.AnalyzeJSON.Analyze_UserInfo;
import library.JsonApi.RegistAndLoginApi;

/**
 * Created by user on 2017/5/30.
 */

public class Fragment_home extends Fragment {
    View v;
    private View top_itme1, top_itme2, top_itme3, top_itme4;
    private RecyclerView recyclerView_announce, recyclerView_viersion;
    private SimpleTextAdapter adapter_announce, adapter_viersion;
    private PieChart salecompare_chart;
    private LineChart home_todaysale_chart;
    private UserInfo userInfo;
    private TextView today_sale, yesterday_sale, today_order, yesterday_order;
    private WebView luntanListview;
    private RegistAndLoginApi registAndLoginApi;
    private Analyze_UserInfo analyze_userInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home_layout, container, false);
        userInfo = (UserInfo) getContext().getApplicationContext();
        registAndLoginApi = new RegistAndLoginApi();
        analyze_userInfo = new Analyze_UserInfo();
        initTopItem();
        initWebView();
        initRecylcerView();
        initPieChart();
        initLineChart();
        return v;
    }

    private void initWebView() {
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                return registAndLoginApi.board();
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                showWebView(analyze_userInfo.getBoard(jsonObject));
            }
        });

    }

    private void showWebView(String html) {
        luntanListview = v.findViewById(R.id.home_webview);
        luntanListview.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
        luntanListview.setWebViewClient(new WebViewClient());
    }

    public void clearWebViewResource(ViewGroup container, WebView webView) {
        if (webView != null) {
            webView.removeAllViews();
            // in android 5.1(sdk:21) we should invoke this to avoid memory leak
            // see (https://coolpers.github.io/webview/memory/leak/2015/07/16/
            // android-5.1-webview-memory-leak.html)
            if (container != null)
                container.removeView(webView);
            webView.setTag(null);
            webView.clearHistory();
            webView.destroy();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clearWebViewResource((ViewGroup) v, luntanListview);
    }

    private void initLineChart() {
        home_todaysale_chart = v.findViewById(R.id.home_todaysale_chart);

//创建描述信息
        Description description = new Description();
        description.setText("测试图表");
        description.setTextColor(Color.RED);
        description.setTextSize(20);
        home_todaysale_chart.setDescription(description);//设置图表描述信息
        home_todaysale_chart.setNoDataText("没有数据熬");//没有数据时显示的文字
        home_todaysale_chart.setNoDataTextColor(Color.BLUE);//没有数据时显示文字的颜色
        home_todaysale_chart.setDrawGridBackground(false);//chart 绘图区后面的背景矩形将绘制
        home_todaysale_chart.setDrawBorders(false);//禁止绘制图表边框的线
        //home_todaysale_chart.setBorderColor(); //设置 chart 边框线的颜色。
        //home_todaysale_chart.setBorderWidth(); //设置 chart 边界线的宽度，单位 dp。
        //home_todaysale_chart.setLogEnabled(true);//打印日志
        //home_todaysale_chart.notifyDataSetChanged();//刷新数据
        //home_todaysale_chart.invalidate();//重绘

/**
 * Entry 坐标点对象  构造函数 第一个参数为x点坐标 第二个为y点
 */
        ArrayList<Entry> values1 = new ArrayList<>();
        ArrayList<Entry> values2 = new ArrayList<>();

        values1.add(new Entry(4, 10));
        values1.add(new Entry(6, 15));
        values1.add(new Entry(9, 20));
        values1.add(new Entry(12, 5));
        values1.add(new Entry(15, 30));

        values2.add(new Entry(3, 110));
        values2.add(new Entry(6, 115));
        values2.add(new Entry(9, 130));
        values2.add(new Entry(12, 85));
        values2.add(new Entry(15, 90));

        //LineDataSet每一个对象就是一条连接线
        LineDataSet set1;
        LineDataSet set2;

        //判断图表中原来是否有数据
        if (home_todaysale_chart.getData() != null &&
                home_todaysale_chart.getData().getDataSetCount() > 0) {
            //获取数据1
            set1 = (LineDataSet) home_todaysale_chart.getData().getDataSetByIndex(0);
            set1.setValues(values1);
            set2 = (LineDataSet) home_todaysale_chart.getData().getDataSetByIndex(1);
            set2.setValues(values2);
            //刷新数据
            home_todaysale_chart.getData().notifyDataChanged();
            home_todaysale_chart.notifyDataSetChanged();
        } else {
            //设置数据1  参数1：数据源 参数2：图例名称
            set1 = new LineDataSet(values1, "测试数据1");
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(1f);//设置线宽
            set1.setCircleRadius(3f);//设置焦点圆心的大小
            set1.enableDashedHighlightLine(10f, 5f, 0f);//点击后的高亮线的显示样式
            set1.setHighlightLineWidth(2f);//设置点击交点后显示高亮线宽
            set1.setHighlightEnabled(true);//是否禁用点击高亮线
            set1.setHighLightColor(Color.RED);//设置点击交点后显示交高亮线的颜色
            set1.setValueTextSize(9f);//设置显示值的文字大小
            set1.setDrawFilled(false);//设置禁用范围背景填充

            //格式化显示数据
            final DecimalFormat mFormat = new DecimalFormat("###,###,##0");
            set1.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return mFormat.format(value);
                }
            });
            /*
            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                set1.setFillDrawable(drawable);//设置范围背景填充
            } else {
                set1.setFillColor(Color.BLACK);
            }
            */

            //设置数据2
            set2 = new LineDataSet(values2, "测试数据2");
            set2.setColor(Color.GRAY);
            set2.setCircleColor(Color.GRAY);
            set2.setLineWidth(1f);
            set2.setCircleRadius(3f);
            set2.setValueTextSize(10f);

            //保存LineDataSet集合
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the datasets
            dataSets.add(set2);
            //创建LineData对象 属于LineChart折线图的数据集合
            LineData data = new LineData(dataSets);
            // 添加到图表中
            home_todaysale_chart.setData(data);
            //绘制图表
            home_todaysale_chart.invalidate();

            //获取此图表的x轴
            XAxis xAxis = home_todaysale_chart.getXAxis();
            xAxis.setEnabled(true);//设置轴启用或禁用 如果禁用以下的设置全部不生效
            xAxis.setDrawAxisLine(true);//是否绘制轴线
            xAxis.setDrawGridLines(true);//设置x轴上每个点对应的线
            xAxis.setDrawLabels(true);//绘制标签  指x轴上的对应数值
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴的显示位置
            //xAxis.setTextSize(20f);//设置字体
            //xAxis.setTextColor(Color.BLACK);//设置字体颜色
            //设置竖线的显示样式为虚线
            //lineLength控制虚线段的长度
            //spaceLength控制线之间的空间
            xAxis.enableGridDashedLine(10f, 10f, 0f);
//        xAxis.setAxisMinimum(0f);//设置x轴的最小值
//        xAxis.setAxisMaximum(10f);//设置最大值
            xAxis.setAvoidFirstLastClipping(true);//图表将避免第一个和最后一个标签条目被减掉在图表或屏幕的边缘
            xAxis.setLabelRotationAngle(10f);//设置x轴标签的旋转角度
//        设置x轴显示标签数量  还有一个重载方法第二个参数为布尔值强制设置数量 如果启用会导致绘制点出现偏差
//        xAxis.setLabelCount(10);
//        xAxis.setTextColor(Color.BLUE);//设置轴标签的颜色
//        xAxis.setTextSize(24f);//设置轴标签的大小
//        xAxis.setGridLineWidth(10f);//设置竖线大小
//        xAxis.setGridColor(Color.RED);//设置竖线颜色
//        xAxis.setAxisLineColor(Color.GREEN);//设置x轴线颜色
//        xAxis.setAxisLineWidth(5f);//设置x轴线宽度
//        xAxis.setValueFormatter();//格式化x轴标签显示字符

            /**
             * Y轴默认显示左右两个轴线
             */
            //获取右边的轴线
            YAxis rightAxis = home_todaysale_chart.getAxisRight();
            //设置图表右边的y轴禁用
            rightAxis.setEnabled(false);
            //获取左边的轴线
            YAxis leftAxis = home_todaysale_chart.getAxisLeft();
            //设置网格线为虚线效果
            leftAxis.enableGridDashedLine(10f, 10f, 0f);
            //是否绘制0所在的网格线
            leftAxis.setDrawZeroLine(false);

            home_todaysale_chart.setTouchEnabled(true); // 设置是否可以触摸
            home_todaysale_chart.setDragEnabled(true);// 是否可以拖拽
            home_todaysale_chart.setScaleEnabled(false);// 是否可以缩放 x和y轴, 默认是true
            home_todaysale_chart.setScaleXEnabled(true); //是否可以缩放 仅x轴
            home_todaysale_chart.setScaleYEnabled(true); //是否可以缩放 仅y轴
            home_todaysale_chart.setPinchZoom(true);  //设置x轴和y轴能否同时缩放。默认是否
            home_todaysale_chart.setDoubleTapToZoomEnabled(true);//设置是否可以通过双击屏幕放大图表。默认是true
            home_todaysale_chart.setHighlightPerDragEnabled(true);//能否拖拽高亮线(数据点与坐标的提示线)，默认是true
            home_todaysale_chart.setDragDecelerationEnabled(true);//拖拽滚动时，手放开是否会持续滚动，默认是true（false是拖到哪是哪，true拖拽之后还会有缓冲）
            home_todaysale_chart.setDragDecelerationFrictionCoef(0.99f);//与上面那个属性配合，持续滚动时的速度快慢，[0,1) 0代表立即停止。

            Legend l = home_todaysale_chart.getLegend();//图例
            l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_INSIDE);//设置图例的位置
            l.setTextSize(10f);//设置文字大小
            l.setForm(Legend.LegendForm.CIRCLE);//正方形，圆形或线
            l.setFormSize(10f); // 设置Form的大小
            l.setWordWrapEnabled(true);//是否支持自动换行 目前只支持BelowChartLeft, BelowChartRight, BelowChartCenter
            l.setFormLineWidth(10f);//设置Form的宽度
        }
    }

    private void initPieChart() {
        salecompare_chart = v.findViewById(R.id.home_salecompare_chart);
// 设置 pieChart 图表基本属性
        salecompare_chart.setUsePercentValues(false);            //使用百分比显示
        salecompare_chart.getDescription().setEnabled(false);    //设置pieChart图表的描述
        salecompare_chart.setBackgroundColor(Color.WHITE);      //设置pieChart图表背景色
        salecompare_chart.setExtraOffsets(5, 5, 60, 0);        //设置pieChart图表上下左右的偏移，类似于外边距
        salecompare_chart.setDragDecelerationFrictionCoef(0.95f);//设置pieChart图表转动阻力摩擦系数[0,1]
        salecompare_chart.setRotationAngle(0);                   //设置pieChart图表起始角度
        salecompare_chart.setRotationEnabled(true);              //设置pieChart图表是否可以手动旋转
        salecompare_chart.setHighlightPerTapEnabled(true);       //设置piecahrt图表点击Item高亮是否可用
        salecompare_chart.animateY(1400, Easing.EasingOption.EaseInOutQuad);// 设置pieChart图表展示动画效果

// 设置 pieChart 图表Item文本属性
        salecompare_chart.setDrawEntryLabels(true);              //设置pieChart是否只显示饼图上百分比不显示文字（true：下面属性才有效果）
        salecompare_chart.setEntryLabelColor(Color.WHITE);       //设置pieChart图表文本字体颜色
        //   salecompare_chart.setEntryLabelTypeface(mTfRegular);     //设置pieChart图表文本字体样式
        salecompare_chart.setEntryLabelTextSize(10);            //设置pieChart图表文本字体大小

// 设置 pieChart 内部圆环属性
        salecompare_chart.setDrawHoleEnabled(true);              //是否显示PieChart内部圆环(true:下面属性才有意义)
        salecompare_chart.setHoleRadius(28f);                    //设置PieChart内部圆的半径(这里设置28.0f)
        salecompare_chart.setTransparentCircleRadius(31f);       //设置PieChart内部透明圆的半径(这里设置31.0f)
        salecompare_chart.setTransparentCircleColor(Color.BLACK);//设置PieChart内部透明圆与内部圆间距(31f-28f)填充颜色
        salecompare_chart.setTransparentCircleAlpha(50);         //设置PieChart内部透明圆与内部圆间距(31f-28f)透明度[0~255]数值越小越透明
        salecompare_chart.setHoleColor(Color.WHITE);             //设置PieChart内部圆的颜色
        salecompare_chart.setDrawCenterText(true);               //是否绘制PieChart内部中心文本（true：下面属性才有意义）
        //  salecompare_chart.setCenterTextTypeface(mTfLight);       //设置PieChart内部圆文字的字体样式
        salecompare_chart.setCenterText("NT$\n257898");                 //设置PieChart内部圆文字的内容
        salecompare_chart.setCenterTextSize(10f);                //设置PieChart内部圆文字的大小
        salecompare_chart.setCenterTextColor(Color.RED);         //设置PieChart内部圆文字的颜色

// pieChart添加数据
        setData();

// 获取pieCahrt图列
        Legend l = salecompare_chart.getLegend();
        l.setEnabled(true);                    //是否启用图列（true：下面属性才有意义）
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setForm(Legend.LegendForm.DEFAULT); //设置图例的形状
        l.setFormSize(10);                      //设置图例的大小
        l.setFormToTextSpace(10f);              //设置每个图例实体中标签和形状之间的间距
        l.setDrawInside(false);
        l.setWordWrapEnabled(true);              //设置图列换行(注意使用影响性能,仅适用legend位于图表下面)
        l.setXEntrySpace(10f);                  //设置图例实体之间延X轴的间距（setOrientation = HORIZONTAL有效）
        l.setYEntrySpace(8f);                  //设置图例实体之间延Y轴的间距（setOrientation = VERTICAL 有效）
        l.setYOffset(0f);                      //设置比例块Y轴偏移量
        l.setTextSize(14f);                      //设置图例标签文本的大小
        l.setTextColor(Color.parseColor("#ff9933"));//设置图例标签文本的颜色


    }

    private void setData() {
        ArrayList<PieEntry> pieEntryList = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#f17548"));
        colors.add(Color.parseColor("#FF9933"));
        colors.add(Color.parseColor("#FF5533"));
        colors.add(Color.parseColor("#FF4923"));
        colors.add(Color.parseColor("#FF9573"));


        int a = 25345;
        int b = 13345;
        int c = 5345;
        int d = 3570;
        int e = 15860;
        //饼图实体 PieEntry
        PieEntry pieEntry = new PieEntry(a * 100 / (a + b + c + d + e), "耳機/音響$" + a);
        pieEntryList.add(pieEntry);
        pieEntry = new PieEntry(b * 100 / (a + b + c + d + e), "電競商品$" + b);
        pieEntryList.add(pieEntry);
        pieEntry = new PieEntry(c * 100 / (a + b + c + d + e), "手機周邊$" + c);
        pieEntryList.add(pieEntry);
        pieEntry = new PieEntry(d * 100 / (a + b + c + d + e), "五金雜貨$" + d);
        pieEntryList.add(pieEntry);
        pieEntry = new PieEntry(e * 100 / (a + b + c + d + e), "液晶螢幕$" + e);
        pieEntryList.add(pieEntry);
        pieEntryList.add(pieEntry);
        //饼状图数据集 PieDataSet
        PieDataSet pieDataSet = new PieDataSet(pieEntryList, "銷售比例評估");
        pieDataSet.setSliceSpace(3f);           //设置饼状Item之间的间隙
        pieDataSet.setSelectionShift(10f);      //设置饼状Item被选中时变化的距离
        pieDataSet.setColors(colors);           //为DataSet中的数据匹配上颜色集(饼图Item颜色)
        //最终数据 PieData
        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(true);            //设置是否显示数据实体(百分比，true:以下属性才有意义)
        pieData.setValueTextColor(Color.BLUE);  //设置所有DataSet内数据实体（百分比）的文本颜色
        pieData.setValueTextSize(10);          //设置所有DataSet内数据实体（百分比）的文本字体大小
        // pieData.setValueTypeface(mTfLight);     //设置所有DataSet内数据实体（百分比）的文本字体样式
        pieData.setValueFormatter(new PercentFormatter());//设置所有DataSet内数据实体（百分比）的文本字体格式
        salecompare_chart.setData(pieData);
        salecompare_chart.highlightValues(null);
        salecompare_chart.invalidate();                    //将图表重绘以显示设置的属性和数据
    }

    private void initRecylcerView() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            arrayList.add("2018/4/11 系統更新 v1.28版本");
        }


        //sub
        recyclerView_viersion = v.findViewById(R.id.home_version_recyclerview);
        adapter_viersion = new SimpleTextAdapter(getContext(), arrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView_viersion.setLayoutManager(layoutManager);
        recyclerView_viersion.setAdapter(adapter_viersion);
    }

    private void initTopItem() {
        //layout
        top_itme1 = v.findViewById(R.id.home_top_item1);
        top_itme2 = v.findViewById(R.id.home_top_item2);
        top_itme3 = v.findViewById(R.id.home_top_item3);
        top_itme4 = v.findViewById(R.id.home_top_item4);
        //textc
        today_sale = v.findViewById(R.id.home_txt_todaysale);
        yesterday_sale = v.findViewById(R.id.home_txt_yesterdaysale);
        today_order = v.findViewById(R.id.home_txt_todayorder);
        yesterday_order = v.findViewById(R.id.home_txt_yesterdayorder);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.home_top_item1:
                        Toast.makeText(getContext(), "1", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.home_top_item2:
                        Toast.makeText(getContext(), "2", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.home_top_item3:
                        Toast.makeText(getContext(), "3", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.home_top_item4:
                        Toast.makeText(getContext(), "4", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        top_itme1.setOnClickListener(onClickListener);
        top_itme2.setOnClickListener(onClickListener);
        top_itme3.setOnClickListener(onClickListener);
        top_itme4.setOnClickListener(onClickListener);
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {

            @Override
            public JSONObject onTasking(Void... params) {
                return registAndLoginApi.home_header(userInfo.getS_no());
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                HomeHeaderPojo homeHeaderPojo = analyze_userInfo.getHomeHeader(jsonObject);
                today_sale.setText(homeHeaderPojo.getN_sale());
                yesterday_sale.setText(homeHeaderPojo.getN_order());
                today_order.setText(homeHeaderPojo.getY_sale());
                yesterday_order.setText(homeHeaderPojo.getY_orde());
            }
        });


    }

}
