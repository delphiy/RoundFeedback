package com.roundfeedback;

import android.content.Context;
import android.content.res.AssetManager;


import com.jjoe64.graphview.series.DataPoint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import java.util.LinkedHashMap;

import java.util.StringTokenizer;

/**
 * Created by hussam on 07/06/2018.
 */

public class Helper {

    public static Round  readRoundFile(Context mContext,int roundNo)throws Exception
    {
        /*
        *  This function read round file from rounds folder and return Round object that contain the following variables
        *  punchCount , averageSpeed , averagePower , leftPunchesJab , leftPunchesHook , leftPunchesUppercut , rightPunchescross , rightPunchesHook ,rightPunchesUppercut and dataPoints

        *
        * */
        LinkedHashMap<String,String> dataPointsGraph=new LinkedHashMap<String,String>();
        int punchesCount = 0;
        int leftPunchesJab = 0;
        int leftPunchesHook = 0;
        int leftPunchesUppercut = 0;
        int rightPunchesCross = 0;
        int rightPunchesHook = 0;
        int rightPunchesUppercut = 0;
        int sumPunchs = 0;
        float sumSpeed = 0;
        float sumPower = 0;
        float averageSpeed = 0;
        float averagePower = 0;
        long timeMillis;
        long targetTimesBins;



        AssetManager assetManager = mContext.getAssets();
        InputStream is = null;
        String roundFileName="rounds/round"+String.valueOf(roundNo)+".csv";
        is = assetManager.open(roundFileName);


        BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

        String line = "";
        StringTokenizer st = null;
        int i=0;
        Round roundInfo = new Round();
        targetTimesBins = Settings.secondBins;   // set target to Initial value (15 seconds)

        while ((line = reader.readLine()) != null)  //Read line by line from .csv file
        {
            String[] roundRow = line.split(",");    // Split the line by comma ,i.e., 00:00:57.31 , 3 , 11.9, 10.1 will be array of strings.

            if(!roundRow[0].equals("ts") && roundRow.length==4)     // Ignore first row
            {
                sumSpeed =  sumSpeed+Float.parseFloat(roundRow[2]);
                sumPower =  sumPower+Float.parseFloat(roundRow[3]);
                int punch_type_id=Integer.parseInt(roundRow[1]);

                switch(punch_type_id)
                {
                    case 0:
                        leftPunchesJab = leftPunchesJab+ 1 ;
                        break;
                    case 1:
                        leftPunchesHook = leftPunchesHook+ 1 ;
                        break;
                    case 2:
                        leftPunchesUppercut = leftPunchesUppercut+ 1 ;
                        break;
                    case 3:
                        rightPunchesCross = rightPunchesCross+ 1 ;
                        break;
                    case 4:
                        rightPunchesHook = rightPunchesHook+ 1 ;
                        break;
                    case 5:
                        rightPunchesUppercut = rightPunchesUppercut+ 1 ;
                        break;
                }




                //Every Settings.secondBins (15 seconds ) , the punches are summed.
                        timeMillis = Utils.parseTimeToMillis(roundRow[0]);     //Convert time to Milliseconds format
                        if(timeMillis <= targetTimesBins)
                        {
                            sumPunchs = sumPunchs + 1;
                        }
                        else
                        {
                            dataPointsGraph.put(String.valueOf(targetTimesBins),String.valueOf(sumPunchs)); //hash map to save key time:  and value: Sum of  punches in 15 second bin
                            roundInfo.dataPoints.add(new DataPoint(targetTimesBins, sumPunchs));
                            sumPunchs = 1 ;
                            targetTimesBins =  timeMillis + Settings.secondBins;

                        }
                //***
                i++;

            }

        }


        //******
        averageSpeed = sumSpeed / i;
        averagePower = sumPower / i;
        punchesCount = i;
        //****** save daata into roundInfo object
        roundInfo.punchCount = punchesCount;
        roundInfo.averageSpeed = averageSpeed;
        roundInfo.averagePower = averagePower;
        //***

        roundInfo.leftPunchesJab = leftPunchesJab;
        roundInfo.leftPunchesHook = leftPunchesHook;
        roundInfo.leftPunchesUppercut = leftPunchesUppercut;
        //**
        roundInfo.rightPunchescross = rightPunchesCross;
        roundInfo.rightPunchesHook = rightPunchesHook;
        roundInfo.rightPunchesUppercut = rightPunchesUppercut;
        //***




        return roundInfo;
    }


    public static int getLastRound(Context mContext)
   {
      /*
        *   To get last round number. For example , number between 1 and 12
        */
      final AssetManager assetManager = mContext.getAssets();
      int lastRound=0;
      try
      {
          String[] filelistInSubfolder = assetManager.list("rounds");

          for (int i=0; i<filelistInSubfolder.length; i++)
          {
              String filename = filelistInSubfolder[i];
              int number = Integer.parseInt(filename.replaceAll("\\D+",""));
              if(number>lastRound)
                  lastRound=number;
          }



      }
      catch (IOException e)
      {
          e.printStackTrace();
      }

      return lastRound;
  }

    public static int getRoundsCount(Context mContext)
    {
        /*
        *   To get rounds count from rounds folder
        */
        final AssetManager assetManager = mContext.getAssets();
        int roundsCount=0;
        try
        {
            String[] filelistInSubfolder = assetManager.list("rounds");
            roundsCount = filelistInSubfolder.length;




        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return roundsCount;
    }



}
