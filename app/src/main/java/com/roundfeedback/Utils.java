package com.roundfeedback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by hussam on 08/06/2018.
 */

public  class Utils {

    public static long parseTimeToMillis(String ts) throws Exception
    {
            // Convert timestamp to  Milliseconds format

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));


            Date date = sdf.parse("1970-01-01 " + ts);
            System.out.println("in milliseconds: " + date.getTime());

            return date.getTime();

    }




}
