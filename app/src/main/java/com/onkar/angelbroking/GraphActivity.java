package com.onkar.angelbroking;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphActivity extends AppCompatActivity {

    private int progressStatus = 0;
    private Handler handler = new Handler();

    private void fetchDataAndRenderGraph(final Intent intent) {
        int id = intent.getIntExtra("ID", 0);
        switch (id) {
            case R.id.currentMonth:
                MonthSpendingDto monthSpendingDto = (MonthSpendingDto) intent.getSerializableExtra("DATA");
                renderHorizontalBarForCurrentMonth(monthSpendingDto);
                break;
            case R.id.history:
                List<MonthSpendingDto> monthSpendingDtoList = (List<MonthSpendingDto>) intent.getSerializableExtra("DATA");
                //verticleBar(monthSpendingDtoList);
                break;
            case R.id.spending:
                renderHorizontalBarForCurrentMonthSpending((MonthSpendingDto) intent.getSerializableExtra("DATA"));
                break;
        }
    }

    private void verticleBar(List<MonthSpendingDto> monthSpendingDtoList) {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.graph);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        for(MonthSpendingDto monthSpendingDto: monthSpendingDtoList) {
            Map<String, BigDecimal> spentOn = monthSpendingDto.getSpentOn();
            for(final Map.Entry<String, BigDecimal> entry: spentOn.entrySet()) {
                final ProgressBar progressBar = new ProgressBar(this.getApplicationContext(), null, android.R.attr.progressBarStyleHorizontal);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(40,ViewGroup.LayoutParams.MATCH_PARENT);
                params.setMargins(8, 8, 8, 8);
                progressBar.setLayoutParams(params);
                progressBar.setMax(100000);
                progressBar.setProgressDrawable(this.getResources().getDrawable(R.drawable.verticle_bar));
                progressBar.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (progressStatus < entry.getValue().intValue()) {
                            progressStatus += 1000;
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(progressStatus);
                                }
                            });
                        }
                    }
                }).start();
                linearLayout.addView(progressBar);
            }
        }
    }

    private void renderHorizontalBarForCurrentMonthSpending(final MonthSpendingDto monthSpendingDto) {

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.graph);
        linearLayout.setGravity(Gravity.CENTER_VERTICAL);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        Map<String, BigDecimal> spentOn = monthSpendingDto.getSpentOn();

        List<ProgressBar> progressBarList = new ArrayList<>(3);
        for(final Map.Entry<String, BigDecimal> entry: spentOn.entrySet()) {
            final ProgressBar progressBar = new ProgressBar(this.getApplicationContext(), null, android.R.attr.progressBarStyleHorizontal);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50);
            params.setMargins(8,40,8,8);
            progressBar.setLayoutParams(params);
            progressBar.setMax(100000);
            progressBar.setProgressDrawable(this.getResources().getDrawable(R.drawable.gradient_progress));
            progressBar.setVisibility(View.VISIBLE);
            progressBarList.add(progressBar);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(progressStatus < entry.getValue().intValue()){
                        progressStatus +=1000;
                        try{
                            Thread.sleep(100);
                        }catch(InterruptedException e){
                            e.printStackTrace();
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(progressStatus);
                            }
                        });
                    }
                }
            }).start();

            TextView textView1 = new TextView(this.getApplicationContext());
            textView1.setText(entry.getValue().intValue()+"");
            textView1.setTextColor(Color.BLACK);
            textView1.setTextSize(16);

            TextView textView2 = new TextView(this.getApplicationContext());
            textView2.setText("100000");
            textView2.setTextColor(Color.BLACK);
            textView2.setTextSize(16);

            LinearLayout linearLayoutForText = new LinearLayout(this.getApplicationContext());
            linearLayoutForText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
            linearLayoutForText.setOrientation(LinearLayout.HORIZONTAL);
            linearLayoutForText.addView(textView1);

            TextView textView = new TextView(this.getApplicationContext());
            textView.setText(entry.getKey());
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(16);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
            linearLayoutForText.addView(textView);
            linearLayoutForText.addView(textView2);
            linearLayout.addView(progressBar);
            linearLayout.addView(linearLayoutForText);
        }



    }


    private void renderHorizontalBarForCurrentMonth(final MonthSpendingDto monthSpendingDto) {
        final ProgressBar progressBar = new ProgressBar(this.getApplicationContext(), null, android.R.attr.progressBarStyleHorizontal);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50);
        params.setMargins(8,40,8,8);
        progressBar.setLayoutParams(params);
        progressBar.setMax(100000);
        progressBar.setProgressDrawable(this.getResources().getDrawable(R.drawable.gradient_progress));
        progressBar.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(progressStatus < monthSpendingDto.getTotalSpent().intValue()){
                    progressStatus +=1500;
                    try{
                        Thread.sleep(50);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progressStatus);
                        }
                    });
                }
            }
        }).start();

        TextView textView1 = new TextView(this.getApplicationContext());
        textView1.setText(monthSpendingDto.getTotalSpent().intValue()+"");
        textView1.setTextColor(Color.BLACK);
        textView1.setTextSize(14);

        TextView textView2 = new TextView(this.getApplicationContext());
        textView2.setText("100000");
        textView2.setTextColor(Color.BLACK);
        textView2.setTextSize(14);

        LinearLayout linearLayoutForText = new LinearLayout(this.getApplicationContext());
        linearLayoutForText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
        linearLayoutForText.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutForText.addView(textView1);
        TextView textView = new TextView(this.getApplicationContext());
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
        linearLayoutForText.addView(textView);
        linearLayoutForText.addView(textView2);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.graph);
        linearLayout.setGravity(Gravity.CENTER_VERTICAL);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(progressBar);
        linearLayout.addView(linearLayoutForText);

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        Intent intent = getIntent();
        fetchDataAndRenderGraph(intent);
    }
}
