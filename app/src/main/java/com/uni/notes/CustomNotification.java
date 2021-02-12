package com.uni.notes;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;

//διαχειριζεται τις ειδοποιησεις
class CustomNotification {
    private int code;       // ο κωδικος της ειδοποιησης
    private Context context;
    private String name;    //το ονομα του μαθηματος
    private int day;        //η μερα του

    CustomNotification(int code,Context context,String name,int day){
        this.code = code;
        this.context = context;
        this.name = name;
        this.day = day;
    }

    void setCode(int c) {
        code = c;
    }

    void setDay(int d){
        day=d;
    }

    void makeNotification(){

        Intent intent = new Intent(context, Receiver.class);
        intent.putExtra("name_of_sub", name);         //στελνει το ονομα του μαθηματος στον receiver
        intent.putExtra("code", code);

        //το update_current ανανεωνει το υπαρχων intent για να στελνει το σωστο μαθημα. (αντι 0 να βαλω αλλο uniqueInt)
        PendingIntent pIntent = PendingIntent.getBroadcast(context, code, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar now = Calendar.getInstance();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, day+2);
        calendar.set(Calendar.HOUR, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.AM_PM, 1);

        if(calendar.before(now)){
            calendar.add(Calendar.DAY_OF_WEEK , 7);
        }

        Long alarmTime = calendar.getTimeInMillis();

        AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        am.setRepeating(am.RTC, alarmTime , 7*24*60*60*1000, pIntent);
    }

    void deleteNotification(){
        Intent intent = new Intent(context, Receiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, code, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

        alarmManager.cancel(pIntent);
    }
}
