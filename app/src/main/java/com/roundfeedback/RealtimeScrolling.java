package com.roundfeedback;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;

/**
 * Created by hussam on 09/06/2018.
 */

public class RealtimeScrolling extends AppCompatActivity {
    private final Handler mHandler = new Handler();
    private Runnable mTimer;
    private double graphLastXValue = 5d;
    private LineGraphSeries<DataPoint> mSeries;
    TextView lab_punch_count;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.realtimescrolling);

        /*
          This activity not completed yet but it is good example to show that the app will support Real Time capture .
          This feature is good because it allows coach to see live progress during the training.

        */



        GraphView graph = findViewById(R.id.graph);

        graph.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        graph.getViewport().setXAxisBoundsManual(true);


        TextView roundName = findViewById(R.id.round_Name);
        lab_punch_count  = findViewById(R.id.lab_punch_Count);

        roundName.setText("Realtime Round "+String.valueOf(Helper.getRoundsCount(this) + 1));
        initGraph(graph);
    }


    public void initGraph(GraphView graph) {
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(4);

        graph.getGridLabelRenderer().setLabelVerticalWidth(100);

        // first mSeries is a line
        mSeries = new LineGraphSeries<>();
        mSeries.setDrawDataPoints(true);
        mSeries.setDrawBackground(true);
        mSeries.setColor(Color.BLUE);
        mSeries.setThickness(4);
        mSeries.setDrawBackground(true);
        graph.addSeries(mSeries);
    }

    public void onResume() {
        super.onResume();
        mTimer = new Runnable() {
            @Override
            public void run() {
                graphLastXValue += 0.25d;
                mSeries.appendData(new DataPoint(graphLastXValue, getRandom()), true, 22);
                mHandler.postDelayed(this, 330);
            }
        };
        mHandler.postDelayed(mTimer, 1500);
    }

    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mTimer);
    }

    double mLastRandom = 2;
    Random mRand = new Random();
    private double getRandom() {
        int a;
        int test = 0;
        a = Integer.parseInt(lab_punch_count.getText().toString());

        test =a + 1;
        lab_punch_count.setText(String.valueOf(test));
        return mLastRandom += mRand.nextDouble()*0.5 - 0.25;
    }
}
