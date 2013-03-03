package com.edyhl.wateringplants;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

 
public class myService extends IntentService 
{
	private static final int NOTIF_ID = 1;

	//1
	public myService()
	{
	    super("myService");
	}

	//2
	@Override
	protected void onHandleIntent(Intent intent) 
	{
	    DbAdapter mDbHelperService;
	    mDbHelperService = new DbAdapter(this);
	    mDbHelperService.open();
        final String sharedName = "WateringPlantsPrefs";
        final String IGNOREMINUTESKEY = "IgnoreMinutesKey";
        final String SOUNDCHECKKEY = "SoundCheckKey";
        final String VIBRATIONCHECKKEY = "VibrationCheckKey";
        long[] vibration = new long[] {500,500,500};
        SharedPreferences mWateringPlants = getSharedPreferences(sharedName, Context.MODE_PRIVATE);//PreferenceManager.getDefaultSharedPreferences(this);
        long ignoreMinutesLong = mWateringPlants.getLong(IGNOREMINUTESKEY, 0);
        String nextAlarmTime = mDbHelperService.getSmallestValue(DbAdapter.NEXTDATEDB, ignoreMinutesLong);
	        
	      
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        long when = System.currentTimeMillis();
	        
        Calendar mycal = Calendar.getInstance();
        mycal.setTimeInMillis(when);
	        
        Notification notification = new Notification(R.drawable.ic_launcher, "Watering Reminder", when);
	        
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if(alarmSound == null)
        {
            alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if(alarmSound == null)
            {
                alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        if(mWateringPlants.getInt(SOUNDCHECKKEY, 0) == 1)
        	 notification.sound = alarmSound;
	      
        if(mWateringPlants.getInt(VIBRATIONCHECKKEY, 1) == 1)
        	 vibration = new long[] {1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000};
	         
        notification.flags |= Notification.FLAG_AUTO_CANCEL; //FLAG_AUTO_CANCEL;
        notification.vibrate = vibration;
	      
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent , 0);
        notification.setLatestEventInfo(getApplicationContext(),  "Water Plants","Click here to manage your plants", contentIntent);
        nm.notify(NOTIF_ID, notification);
	        
	        
	    if(nextAlarmTime != null)
	    {
	     	long whenService = Long.parseLong(nextAlarmTime);
	     	if(whenService >= System.currentTimeMillis()-2000)
	     	{
	    		AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
	            Intent intentService = new Intent(this, myService.class);
	            PendingIntent pendingIntent = PendingIntent.getService(this, 0, intentService, 0);
	            alarmManager.set(AlarmManager.RTC_WAKEUP, whenService, pendingIntent);
	     	}
	    }
	    mDbHelperService.close();
	}
	
	//3
	@Override
	public IBinder onBind(Intent intent) 
	{
		return null;
	}

	//4
}
	

 
