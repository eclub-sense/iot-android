package com.eclubprague.iot.android.weissmydeweiss.ui.charts;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.eclubprague.iot.android.weissmydeweiss.R;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities.SensorAndData;
import com.eclubprague.iot.android.weissmydeweiss.tasks.GetSensorDataByIdTask;
import com.eclubprague.iot.android.weissmydeweiss.ui.charts.components.CustomMarkerView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Dat on 24.8.2015.
 */
public class OneValueChartActivity extends ActionBarActivity implements
        OnChartValueSelectedListener, GetSensorDataByIdTask.TaskDelegate {

    private LineChart mChart;

    private String title;

    private String datasetDesc;

    private String token;

    private String uuid;

    //Activity ovveride
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_realtime_linechart);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);

        token = getIntent().getStringExtra("token");
        uuid = getIntent().getStringExtra("uuid");

        title = getIntent().getStringExtra("title");

        actionBar.setTitle(title);

        datasetDesc = getIntent().getStringExtra("datasetDesc");

        mChart = (LineChart) findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);

        // no description text
        mChart.setDescription("");
        mChart.setNoDataTextDescription("No data available yet.");

        CustomMarkerView mv = new CustomMarkerView (this, R.layout.tv_content_layout);
        mChart.setMarkerView(mv);

        // enable value highlighting
        mChart.setHighlightEnabled(true);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        mChart.setBackgroundColor(Color.WHITE);

        LineData data = new LineData();
        data.setValueTextColor(Color.BLACK);

        // add empty data
        mChart.setData(data);

        Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        // get the legend (only possible after setting data)
        Legend legend = mChart.getLegend();

        // modify the legend ...
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setTypeface(tf);
        legend.setTextColor(Color.BLACK);

        XAxis xl = mChart.getXAxis();
        xl.setTypeface(tf);
        xl.setTextColor(Color.BLACK);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setSpaceBetweenLabels(5);
        xl.setEnabled(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(tf);
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setAxisMaxValue(/*100f*/getIntent().getFloatExtra("upperBound", 100));
        leftAxis.setAxisMinValue(/*0f*/-1 * getIntent().getFloatExtra("upperBound", 100));
        leftAxis.setStartAtZero(false);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

        delegateRef.add(this);

        //todo start timer task
        startTimer();

    }

    //Activity override

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //TODO stop timer task
        stopTimerTask();
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    }

    //onChartValueSelectedListener override

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        Toast.makeText(this, "value : " + Float.toString(e.getVal()) + ", time : " +
                e.getData().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected() {

    }

    //------------------------------------------------------------------
    private void addEntry(float val) {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        //System.out.println( sdf.format(cal.getTime()) );

        LineData data = mChart.getData();

        if (data != null) {

            LineDataSet set = data.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            // add a new x-value first
            String time = sdf.format(cal.getTime());
            data.addXValue(time);
            data.addEntry(new Entry(val, set.getEntryCount(), time), 0);

            // let the chart know it's data has changed
            mChart.notifyDataSetChanged();

            // limit the number of visible entries
            mChart.setVisibleXRangeMaximum(10);
            // mChart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            mChart.moveViewToX(data.getXValCount() - 11);

            // this automatically refreshes the chart (calls invalidate())
            // mChart.moveViewTo(data.getXValCount()-7, 55f,
            // AxisDependency.LEFT);
        }
    }

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, datasetDesc);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.RED);
        set.setLineWidth(2f);
        set.setCircleSize(2f);
        set.setFillAlpha(65);
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.BLACK);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chart_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_pause:
                try {
                    //TODO stop timer task
                    stopTimerTask();
                } catch (Exception e) {
                    Log.e("UNREG", e.toString());
                }
                break;
            case R.id.action_continue:
                try {
                    //TODO start timer task
                    startTimer();
                } catch (Exception e) {
                    Log.e("REG", e.toString());
                }
        }
        return true;
    }


    @Override
    public void onGetSensorDataByIdTaskCompleted(SensorAndData sData) {
        try {
//            float val = Float.parseFloat(sData.getMeasured().get(
//                    sData.getMeasured().size() - 1
//            ).getValue());
//            addEntry(val);
        } catch (Exception e) {
            Log.e("onTaskCompleted", e.toString());
        }
    }

    @Override
    public void onGetSensorDataByIdTaskWithFlageCompleted(SensorAndData sData) {

    }

    //----------------------------------------------------------------
    // TIMER TASK
    // DO SOME WORKS PERIODICALLY
    //----------------------------------------------------------------

    private Timer timer;
    private TimerTask timerTask;
    final Handler handler = new Handler();


    public void startTimer() {
        if(timer != null) return;
        //set a new Timer
        timer = new Timer();
        //initialize the TimerTask's job
        initializeTimerTask();
        //schedule the timer, after the first 3000ms the TimerTask will run every 2000ms
        timer.schedule(timerTask, 3000, 2000); //
    }

    public void stopTimerTask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private ArrayList<GetSensorDataByIdTask.TaskDelegate> delegateRef = new ArrayList<>();

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {

                        new GetSensorDataByIdTask(delegateRef, token, uuid, false).execute();
                    }
                });
            }
        };
    }
}
