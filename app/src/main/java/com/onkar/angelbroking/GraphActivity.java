package com.onkar.angelbroking;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.onkar.dto.MonthSpendingDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GraphActivity extends AppCompatActivity {

    List<String> months = new ArrayList<>();
    private int start = -1;

    private void fetchDataAndRenderGraph(final Intent intent) {
        int id = intent.getIntExtra("ID", 0);
        switch (id) {
            case R.id.currentMonth:
                MonthSpendingDto monthSpendingDto = (MonthSpendingDto) intent.getSerializableExtra("DATA");
                renderCurrentMonthHorizontalBar(monthSpendingDto);
                break;
            case R.id.history:
                List<MonthSpendingDto> monthSpendingDtoList = (List<MonthSpendingDto>) intent.getSerializableExtra("DATA");
                renderHistoryWithVerticalBar(monthSpendingDtoList);
                break;
            case R.id.spending:
                renderSpendingHorizontalBar((MonthSpendingDto) intent.getSerializableExtra("DATA"));
                break;
        }
    }

    private HorizontalBarChart getHorizontalChart() {
        HorizontalBarChart chart = new HorizontalBarChart(this.getApplicationContext());
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);
        chart.getDescription().setEnabled(false);
        chart.setMaxVisibleValueCount(60);
        chart.setDrawGridBackground(false);
        chart.setFitBars(true);
        chart.animateY(2500);
        return chart;
    }

    private void setYAxis(final HorizontalBarChart chart) {
        YAxis yl = chart.getAxisLeft();
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setAxisMinimum(0f);

        YAxis yr = chart.getAxisRight();
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
        yr.setAxisMinimum(0f);
    }

    private void setLegend(final HorizontalBarChart chart) {
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setXEntrySpace(4f);
    }

    private BarData getCurrentMonthBarData(final MonthSpendingDto monthSpendingDto) {
        ArrayList<BarEntry> values = new ArrayList<>();
        values.add(new BarEntry(0, 0));
        values.add(new BarEntry(20, 100000));
        values.add(new BarEntry(40, monthSpendingDto.getTotalSpent().longValue()));
        values.add(new BarEntry(60, 0));

        BarDataSet barDataSet = new BarDataSet(values, "");
        barDataSet.setDrawIcons(false);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barDataSet);

        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        data.setBarWidth(9f);
        return data;
    }

    private BarData getSpendingBarData(final MonthSpendingDto monthSpendingDto) {

        Map<String, BigDecimal> spentOn = monthSpendingDto.getSpentOn();
        final ArrayList<BarEntry> values = new ArrayList<>();
        values.add(new BarEntry(0, 0,""));
        int cnt = 15;
        for(String key: spentOn.keySet()) {
            BigDecimal value = spentOn.get(key);
            values.add(new BarEntry(cnt, value.longValue(), key));
            cnt=cnt+15;
        }
        values.add(new BarEntry(cnt, 0,""));

        BarDataSet barDataSet = new BarDataSet(values, "");
        barDataSet.setDrawIcons(false);
        barDataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return entry.getData().toString();
            }
        });

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barDataSet);

        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        data.setBarWidth(9f);
        return data;
    }

    private void renderCurrentMonthHorizontalBar(final MonthSpendingDto monthSpendingDto) {
        HorizontalBarChart chart = this.getHorizontalChart();
        this.setYAxis(chart);
        this.setLegend(chart);
        chart.setData(this.getCurrentMonthBarData(monthSpendingDto));
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.graph);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        chart.setLayoutParams(layoutParams);
        linearLayout.addView(chart);
    }

    private void renderSpendingHorizontalBar(final MonthSpendingDto monthSpendingDto) {
        HorizontalBarChart chart = this.getHorizontalChart();
        this.setYAxis(chart);
        this.setLegend(chart);
        chart.setData(this.getSpendingBarData(monthSpendingDto));
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.graph);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        chart.setLayoutParams(layoutParams);
        linearLayout.addView(chart);
    }

    private void renderHistoryWithVerticalBar(List<MonthSpendingDto> monthSpendingDtoList) {
        BarChart chart = new BarChart(this.getApplicationContext());
        chart.setPinchZoom(false);
        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(false);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);
        l.setYOffset(0f);
        l.setXOffset(10f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if(value < 0 || value > 11) {
                    return String.valueOf((int) value);
                } else {
                    return months.get((int) value);
                }
            }
        });

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f);

        chart.getAxisRight().setEnabled(false);
        chart.setData(this.getHistoryBarData(monthSpendingDtoList));
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.graph);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        chart.setLayoutParams(layoutParams);
        linearLayout.addView(chart);
        float groupSpace = 0.15f;
        float barSpace = 0.06f;
        float barWidth = 0.2f;
        chart.getBarData().setBarWidth(barWidth);
        chart.getXAxis().setAxisMinimum(start);
        chart.getXAxis().setAxisMaximum(start + chart.getBarData().getGroupWidth(groupSpace, barSpace) * 3);
        chart.groupBars(start+0.1f, groupSpace, barSpace);
        chart.invalidate();
    }

    private BarData getHistoryBarData(List<MonthSpendingDto> monthSpendingDtoList) {
        ArrayList<BarEntry> shopping = new ArrayList<>();
        ArrayList<BarEntry> medicine = new ArrayList<>();
        ArrayList<BarEntry> other = new ArrayList<>();

        for(MonthSpendingDto monthSpendingDto: monthSpendingDtoList) {
            if(start == -1) {
                start = months.indexOf(monthSpendingDto.getMonth());
            }
            Map<String, BigDecimal> spentOn = monthSpendingDto.getSpentOn();
            shopping.add(new BarEntry(months.indexOf(monthSpendingDto.getMonth()),spentOn.get("SHOPPING").longValue()));
            medicine.add(new BarEntry(months.indexOf(monthSpendingDto.getMonth()),spentOn.get("MEDICINE").longValue()));
            other.add(new BarEntry(months.indexOf(monthSpendingDto.getMonth()),spentOn.get("OTHER").longValue()));
        }

        BarDataSet set1, set2, set3;
        set1 = new BarDataSet(shopping, "SHOPPING");
        set1.setColor(Color.rgb(104, 241, 175));
        set2 = new BarDataSet(medicine, "MEDICINE");
        set2.setColor(Color.rgb(164, 228, 251));
        set3 = new BarDataSet(other, "OTHER");
        set3.setColor(Color.rgb(242, 247, 158));
        BarData data = new BarData(set1, set2, set3);
        data.setValueFormatter(new LargeValueFormatter());
        return data;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        months.add("Jan");
        months.add("Feb");
        months.add("Mar");
        months.add("Apr");
        months.add("May");
        months.add("Jun");
        months.add("Jul");
        months.add("Aug");
        months.add("Sep");
        months.add("Oct");
        months.add("Nov");
        months.add("Dec");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        Intent intent = getIntent();
        fetchDataAndRenderGraph(intent);
    }
}
