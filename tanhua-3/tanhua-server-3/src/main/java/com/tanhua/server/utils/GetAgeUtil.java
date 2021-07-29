package com.tanhua.server.utils;

import org.joda.time.DateTime;
import org.joda.time.Years;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GetAgeUtil {

    public static int getAge(String yearMonthDay){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date birthday = sdf.parse(yearMonthDay);
            Years years = Years.yearsBetween(new DateTime(birthday),DateTime.now());
            return years.getYears();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

}
