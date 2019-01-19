package com.onkar.angelbroking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onkar.api.ApiClient;
import com.onkar.api.ApiInterface;
import com.onkar.dto.MonthSpendingDto;
import com.onkar.dto.TransactionDetailsDto;
import com.onkar.helper.Constants;
import com.onkar.helper.NumberFormatter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener, Callback<TransactionDetailsDto> {

    private SharedPreferences sharedPreferences;
    static int animationSequence = 1;
    private MonthSpendingDto currentMonthDto;
    private List<MonthSpendingDto> history = new ArrayList<>(3);;

    private void renderUIData() {
        TextView currentMonth = (TextView) findViewById(R.id.currentMonthName);
        currentMonth.setText(currentMonthDto.getMonth().toUpperCase());

        TextView currentYear = (TextView) findViewById(R.id.currentYear);
        currentYear.setText(currentMonthDto.getYear()+"");

        TextView currentMonthTotal = (TextView) findViewById(R.id.currentMonthTotal);
        currentMonthTotal.setText(NumberFormatter.formateNumberAndAppendRupee(currentMonthDto.getTotalSpent()));

        Map<String, BigDecimal> spentOn = currentMonthDto.getSpentOn();

        TextView currentMonthShopping = (TextView) findViewById(R.id.currentMonthShopping);
        currentMonthShopping.setText(NumberFormatter.formateNumberAndAppendRupee(spentOn.get("SHOPPING")));

        TextView currentMonthMedicine = (TextView) findViewById(R.id.currentMonthMedicine);
        currentMonthMedicine.setText(NumberFormatter.formateNumberAndAppendRupee(spentOn.get("MEDICINE")));

        TextView currentMonthOther = (TextView) findViewById(R.id.currentMonthOther);
        currentMonthOther.setText(NumberFormatter.formateNumberAndAppendRupee(spentOn.get("OTHER")));

        int[][] historyIds = new int[][]{{R.id.month1, R.id.month1text}, {R.id.month2, R.id.month2text},{R.id.month3, R.id.month3text}};

        for(int i =0;i<historyIds.length;i++) {
            TextView monthTotal = (TextView) findViewById(historyIds[i][0]);
            monthTotal.setText(NumberFormatter.formateNumberAndAppendRupee(history.get(i).getTotalSpent()));

            TextView monthText = (TextView) findViewById(historyIds[i][1]);
            monthText.setText(history.get(i).getMonth()+"`"+history.get(i).getYear()%100);
        }
    }


    private void setAnimation(final DashboardActivity dashboardActivity) {
        final LinearLayout l1 = findViewById(R.id.currentMonth);
        final LinearLayout l2 = findViewById(R.id.spending);
        final LinearLayout l3 = findViewById(R.id.history);

        l1.setOnClickListener(dashboardActivity);
        l2.setOnClickListener(dashboardActivity);
        l3.setOnClickListener(dashboardActivity);

        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bottom_to_original);
        l1.setAnimation(animation1);
        Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bottom_to_original);
        animation2.setDuration(2000);
        l2.setAnimation(animation2);
        Animation animation3 = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bottom_to_original);
        animation3.setDuration(2500);
        l3.setAnimation(animation3);
    }

    private void renderLogout(final DashboardActivity dashboardActivity) {
        Button logout = (Button) findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Constants.USERNAME, null);
                editor.commit();

                finish();
                Intent intent = new Intent(dashboardActivity, LoginActivity.class);
                startActivity(intent);
            }
        });
        Animation animationFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        logout.setAnimation(animationFadeIn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final DashboardActivity dashboardActivity = this;

        sharedPreferences = getSharedPreferences(Constants.ANGEL, Context.MODE_PRIVATE);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        if(currentMonthDto == null || history.isEmpty()) {
            Call<TransactionDetailsDto> call = apiService.getTopRatedMovies(sharedPreferences.getString(Constants.USERNAME, "user1"));
            call.enqueue(this);
        }

    }

    @Override
    public void onClick(View view) {
        Intent intent= new Intent(this, GraphActivity.class);
        switch (view.getId()) {
            case R.id.currentMonth:
                intent.putExtra("ID",view.getId());
                intent.putExtra("DATA", currentMonthDto);
                break;
            case R.id.spending:
                intent.putExtra("ID",view.getId());
                intent.putExtra("DATA", currentMonthDto);
                startActivity(intent);
                break;
            case R.id.history:
                intent.putExtra("ID",view.getId());
                intent.putExtra("DATA", (Serializable) history);
                break;
        }
        startActivity(intent);
    }

    @Override
    public void onResponse(Call<TransactionDetailsDto> call, Response<TransactionDetailsDto> response) {
        for(MonthSpendingDto monthSpendingDto : response.body().getHistory()) {
            if(monthSpendingDto.isCurrentMonth()) {
                currentMonthDto = monthSpendingDto;
            } else {
                history.add(monthSpendingDto);
            }
        }
        setContentView(R.layout.activity_dashboard);
        renderUIData();
        setAnimation(this);
        renderLogout(this);
    }

    @Override
    public void onFailure(Call<TransactionDetailsDto> call, Throwable t) {
        Log.d("Onkar", t.toString());
    }
}
