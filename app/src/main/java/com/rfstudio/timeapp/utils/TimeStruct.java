package com.rfstudio.timeapp.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 注意：虽然展示结构有显示秒数，但在设定时不显示秒数
 * 秒数默认为 00
 */
public class TimeStruct  {
    // 只表示当天,从纪元到设置的时长,可直接用来定时服务
    private long startTimeMillis;     // 开始时间 毫秒 （只读）
    private long endTimeMillis;       // 结束时间 毫秒  （只读）
    private long duringMillis;        // 开始到结束的 时长毫秒 (只读）

    private String startTime;
    private String endTime;

    private int requestCode;          // 用于作为PendingItent 的标识(只读)

    /**
     * 传入 开始与结束 时间的字符串
     * @param startTime 格式 00:00:00
     * @param endTime  格式 00:00:00
     */
    public TimeStruct(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * 获取 从0点到设置的时间 的毫秒数
     * @param timeString 格式为 00：00：00 ，否则输出错误
     * @return 返回相应 以当前为标准，返回毫秒
     */

    public static long StringToDuringMillis(String timeString){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(timeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.e("此时的转化Date",date.toString());
        return date.getTime() + 8*60*60*1000;
    }

    /**
     * 获取 从纪元到所填时间 的毫秒数 。
     *
     * 理解为 当天这个时间段的毫秒数
     * @param timeString HH:mm:ss  的时间string
     * @return 当天这个时间的毫秒数
     */
    public static long StringToCurrentMillis(String timeString){
        long mills = StringToDuringMillis(timeString) + getTodayMillis();

        return mills;
    }



    /**
     *  获取这一天0点时的 毫秒数（system标准)
     * @return 返回今天0点的毫秒数
     */
    public static long getTodayMillis(){
        Date date = new Date();
        // 一天24小时的毫秒数
        long daymills = 24*60*60*1000;
        // 考虑到 东八区，减去8小时
        // 当天时间 - 从零点到现在的毫秒数(当前时间 % 一天时间)
        return (date.getTime() - (date.getTime() % daymills) - 8*60*60*1000 );
    }

    /**
     *  根据毫秒，生成 时间字符串。
     *
     * 关于格式化时间文本：
     *          yyyy-MM-dd  用于表示 年-月-日  如 2020-4-10
     *                  注意 ： MM 表示月，必须大写
     *          HH:mm:ss    用于表示 小时：分钟：秒
     *                  注意 ： HH 表示24小时 hh 表示12小时制
     * @param time      毫秒数
     * @param pattern   时间输出格式 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getTimeString(long time,String pattern){
        SimpleDateFormat format = new SimpleDateFormat(pattern);

        Date d = new Date(time);
        return format.format(d);
    }

    /**
     *  填入 时，分，秒(String) 生成 时：分：秒 格式字符串
     *
     *  某一项为 null 将默认为 00
     * @param hour
     * @param minute
     * @param seconds
     * @return HH:mm:ss 格式字符串
     */
    public static String timeToString(String hour , String minute , String seconds){
        StringBuilder timeString = new StringBuilder();
        if (hour != null){
            timeString.append(hour);
            timeString.append(":");
        }else {
            timeString.append("00");
            timeString.append(":");
        }

        if (minute != null){
            timeString.append(minute);
            timeString.append(":");
        }else {
            timeString.append("00");
            timeString.append(":");
        }

        if (seconds != null){
            timeString.append(seconds);
        }else {
            timeString.append("00");
        }

        return timeString.toString();
    }

    /**
     *  提供目标时间millis(从纪元)，将会返回 到目前时间剩余的时间
     * @param targetTimeMillis 目标时间
     * @return 返回还剩的时间的毫秒数, 返回-1 表示目标时间已经过期
     */
    public static long getRemainMillis(long targetTimeMillis){
        if (targetTimeMillis > System.currentTimeMillis())
            return -1;
        long remainTime = System.currentTimeMillis() - targetTimeMillis;

        return remainTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setStartTime(String startTime) {
        if (startTime.split(":").length != 3)
            // 表示输入格式有问题,给个默认值
            startTime = "00:00:00";
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        if (endTime.split(":").length != 3)
            // 表示输入格式有问题,给个默认值
            endTime = "00:00:00";
        this.endTime = endTime;
    }

    public long getStartTimeMillis() {
        this.startTimeMillis = StringToCurrentMillis(this.startTime);
        return startTimeMillis;
    }

    public long getEndTimeMillis() {
        this.endTimeMillis = StringToCurrentMillis(this.endTime);
        return endTimeMillis;
    }

    /**
     *  获取标识 requestCode，用于对PengdingIntent的设定识别,;
     *  原理为 时分组合，如：12：13 则返回 1213 ;
     *  以开始时间为基准;
     * @return
     */
    public int getRequestCode() {
        // 分割
        String[] part = startTime.split(":");
        int hourInt = Integer.parseInt(part[0])*100;
        int minuteInt = Integer.parseInt(part[1]);

        requestCode = hourInt + minuteInt;
        return requestCode;
    }


    public long getDuringMillis() {
        duringMillis = this.startTimeMillis - this.endTimeMillis;
        return duringMillis;
    }
}
