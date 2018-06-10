package com.roundfeedback;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import java.text.SimpleDateFormat;

import java.util.Date;




public class MainActivity extends AppCompatActivity {


    private RoundsPagerAdapter roundsPagerAdapter;
    private ViewPager mViewPager;
    public static int roundNumber;
    public static int roundsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        roundsPagerAdapter = new RoundsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(roundsPagerAdapter);

        roundsCount = Helper.getRoundsCount(this);              // When the app starts ,it will switch tab to last round
        roundNumber = Helper.getLastRound(this);                //Get the last round from   assets/rounds folder.
        ViewPager viewPager=findViewById(R.id.container);
        int indexLast = viewPager.getAdapter().getCount() - 1;
        viewPager.setCurrentItem(indexLast);                             // Switch to last round




    }




    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_ROUND_NUMBER = "round_number";
        private Round currentRound = new Round();
        private Round previousRound = new Round();
        private ProgressBar progressBar;
        private SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        private  TextView lab_time;

        public PlaceholderFragment() {
        }
        public static PlaceholderFragment newInstance(int roundNumber) {

            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_ROUND_NUMBER, roundNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.round_Name);
            textView.setText(getString(R.string.round_name_format, getArguments().getInt(ARG_ROUND_NUMBER)));
            roundNumber = getArguments().getInt(ARG_ROUND_NUMBER);
            updateRoundUI(rootView);
            return rootView;
        }

        public void updateRoundUI(View rootView )
        {

            TextView lab_round_name = rootView.findViewById(R.id.round_Name);
            TextView lab_punch_count = rootView.findViewById(R.id.lab_punch_Count);
            TextView lab_average_speed = rootView.findViewById(R.id.lab_average_speed);
            TextView lab_average_power = rootView.findViewById(R.id.lab_average_power);
            TextView lab_left_punches_jab = rootView.findViewById(R.id.Lab_left_punches_jab);
            TextView lab_left_punches_hook = rootView.findViewById(R.id.Lab_left_punches_hook);
            TextView lab_left_punches_uppercut = rootView.findViewById(R.id.Lab_left_punches_uppercut);
            TextView lab_right_punches_cross = rootView.findViewById(R.id.Lab_right_punches_cross);
            TextView lab_right_punches_hook = rootView.findViewById(R.id.Lab_right_punches_hook);
            TextView lab_right_punches_uppercut = rootView.findViewById(R.id.Lab_right_punches_uppercut);
            ImageView img_punch_count_arrow =rootView.findViewById(R.id.img_punch_Count_arrow);
            ImageView img_average_speed_arrow =rootView.findViewById(R.id.img_average_speed_arrow);
            ImageView img_average_power_arrow =rootView.findViewById(R.id.img_average_power_arrow);
            ProgressBar progress_left_punches_jab =rootView.findViewById(R.id.progress_left_punches_jab);
            ProgressBar progress_right_punches_cross =rootView.findViewById(R.id.progress_right_punches_cross);
            ProgressBar progress_left_punches_hook =rootView.findViewById(R.id.progress_left_punches_hook);
            ProgressBar progress_right_punches_hook =rootView.findViewById(R.id.progress_right_punches_hook);
            ProgressBar progress_left_punches_uppercut =rootView.findViewById(R.id.progress_left_punches_uppercut);
            ProgressBar progress_right_punches_uppercut =rootView.findViewById(R.id.progress_right_punches_uppercut);

            try
            {
                /*
                To read current round from .csv file and update data in GUI. The user will be able to swipe left and right to see all rounds available
                 */
                currentRound=Helper.readRoundFile(getContext(),roundNumber);
                if(roundNumber - 1 !=0)        // To check if there is previous round
                {
                    previousRound = Helper.readRoundFile(getContext(), roundNumber - 1);
                    if(previousRound.punchCount > currentRound.punchCount)
                    {
                        img_punch_count_arrow.setImageResource(R.mipmap.arrow_down);
                    }
                    else
                    {
                        img_punch_count_arrow.setImageResource(R.mipmap.arrow_up);
                    }
                    //*****
                    //** Average Speed arrow up or down

                    if(previousRound.averageSpeed > currentRound.averageSpeed) //if previous punche more than the current
                    {
                        img_average_speed_arrow.setImageResource(R.mipmap.arrow_down);
                    }
                    else
                    {
                        img_average_speed_arrow.setImageResource(R.mipmap.arrow_up);
                    }
                    //*****

                    //** Average Power arrow up or down

                    if(previousRound.averagePower  > currentRound.averagePower) //if previous punche more than the current
                    {
                        img_average_power_arrow.setImageResource(R.mipmap.arrow_down);
                    }
                    else
                    {
                        img_average_power_arrow.setImageResource(R.mipmap.arrow_up);
                    }
                    //*****

                }
                else
                {
                    img_punch_count_arrow.setImageResource(0);
                    img_average_speed_arrow.setImageResource(0);
                    img_average_power_arrow.setImageResource(0);
                }


                //*****
                lab_round_name.setText("Round "+String.valueOf(roundNumber));
                lab_punch_count.setText(String.valueOf(currentRound.punchCount));
                lab_average_speed.setText(String.format("%.2f", currentRound.averageSpeed));
                lab_average_power.setText(String.format("%.2f", currentRound.averagePower));
                lab_left_punches_jab.setText(String.valueOf(currentRound.leftPunchesJab));
                lab_left_punches_hook.setText(String.valueOf(currentRound.leftPunchesHook));
                lab_left_punches_uppercut.setText(String.valueOf(currentRound.leftPunchesUppercut));
                lab_right_punches_cross.setText(String.valueOf(currentRound.rightPunchescross));
                lab_right_punches_hook.setText(String.valueOf(currentRound.rightPunchesHook));
                lab_right_punches_uppercut.setText(String.valueOf(currentRound.rightPunchesUppercut));


                //**Punches into the count of each type of punch
                progress_left_punches_jab.setMax(Settings.maxPuncheType);
                progress_left_punches_jab.setProgress(currentRound.leftPunchesJab);

                progress_right_punches_cross.setMax(Settings.maxPuncheType);
                progress_right_punches_cross.setProgress(currentRound.rightPunchescross);
                //*****
                progress_left_punches_hook.setMax(Settings.maxPuncheType);
                progress_left_punches_hook.setProgress(currentRound.leftPunchesHook);

                progress_right_punches_hook.setMax(Settings.maxPuncheType);
                progress_right_punches_hook.setProgress(currentRound.rightPunchesHook);
                //***
                progress_left_punches_uppercut.setMax(Settings.maxPuncheType);
                progress_left_punches_uppercut.setProgress(currentRound.leftPunchesUppercut);

                progress_right_punches_uppercut.setMax(Settings.maxPuncheType);
                progress_right_punches_uppercut.setProgress(currentRound.rightPunchesUppercut);




                progressBarStart(rootView);    // method: how far through the rest period it is by using progress bar  and textview
                blotGraph(rootView);           // method: plot intensity graph



            }
            catch(Exception ex)
            {
                Log.i("kme","Problem " +ex.getMessage());
            }



        }

        public void progressBarStart(View rootView)
        {
            /*
                This function how far through the rest period it is by using progress bar  and textview
            */
            progressBar = rootView.findViewById(R.id.progressBar);
            lab_time = rootView.findViewById(R.id.lab_time);
            progressBar.setMax(180);
            new CountDownTimer(180000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    //this will be done every 1000 milliseconds ( 1 seconds )
                    long progress = (180000 - millisUntilFinished) / 1000;
                    progressBar.setProgress((int)progress);
                    int seconds = (int) (millisUntilFinished / 1000);
                    int minutes = seconds / 60;
                    seconds = seconds % 60;
                    lab_time.setText("TIME : " + String.format("%02d", minutes)
                            + ":" + String.format("%02d", seconds));
                }

                @Override
                public void onFinish() {
                    lab_time.setText("Go..!");
                }

            }.start();
        }


        public void blotGraph(View rootView)
        {
            /*
            *   This functions plot intensity graph by reading data points from Round.dataPoints
            *   Axis has milliseconds up to 3 minutes . I used LabelFormatter to make readable time for user.
            * */

            GraphView graph = (GraphView) rootView.findViewById(R.id.graph);

            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(currentRound.dataPoints.toArray(new DataPoint[0]));
            graph.getViewport().setXAxisBoundsManual(true);
            series.setColor(Color.BLUE);
            series.setThickness(4);
            series.setDrawBackground(true);
            graph.getGridLabelRenderer().setGridStyle( GridLabelRenderer.GridStyle.NONE );
            series.setBackgroundColor(Color.argb(50, 50, 255, 200));
            series.setDrawDataPoints(true);
            graph.addSeries(series);
            graph.getViewport().setMinX(Settings.minAxis);
            graph.getViewport().setMaxX(Settings.maxAxis);



            graph.getGridLabelRenderer().setVerticalLabelsVisible(false);
            graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
            graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter()
            {

                @Override
                public String formatLabel(double value,boolean isValueX)
                {
                    if(isValueX)
                    {
                        if(value==Settings.minAxis || value==Settings.maxAxis)
                            return sdf.format(new Date((long)value));
                        else
                            return null;
                    }
                    else
                    {
                        if(value==Settings.minAxis || value==Settings.maxAxis)
                            return super.formatLabel(value,isValueX);
                        else
                            return null;
                    }
                }

            }

            );




        }
    }


    public class RoundsPagerAdapter extends FragmentPagerAdapter {

        public RoundsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {

            return Helper.getRoundsCount(MainActivity.this); // Get count of rounds (.csv files)
        }
    }



    public void resetRound(View v)
    {
        /*
          This click event to start realtimeScrolling activity which capture data in real time . It does not complete yet and it needs little work
        * */
        Intent realtimeScrolling = new Intent(this, RealtimeScrolling.class);
        startActivity(realtimeScrolling);

    }

}
