package com.coder520.common.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    private static Calendar calendar = Calendar.getInstance();

    /**
    *@Author Yusui
    *@Date 2018/5/23 8:35
    *@Description 获取星期
    */
    public static int getTadayWeek(){


        calendar.setTime(new Date());
        int week = calendar.get(Calendar.DAY_OF_WEEK)-1;
        if(week<0){
            week=7;
        }
        return week;
    }

    /**
    *@Author Yusui
    *@Date 2018/5/23 8:35
    *@Description 计算上下午打卡时间差
    */
    public static int getMinute(Date startDate,Date endDate){
        long start = startDate.getTime();
        long end = endDate.getTime();
        int minute= (int) ((end - start)/(1000*60));
        return minute;
    }

    /**
    *@Author Yusui
    *@Date 2018/5/23 8:42
    *@Description 获取当天的某个时间
    */
    public static Date getDate(int hour,int minute){
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);
        return calendar.getTime();
    }
}
