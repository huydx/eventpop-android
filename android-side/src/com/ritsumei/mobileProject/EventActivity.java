/**
* Event detail display activity
* @author huydx 
* @version 0.9
*/
package com.ritsumei.mobileProject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ritsumei.mobileProject.R;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class EventActivity extends Activity {
	private JSONObject mThisEvent = null;
	private AlarmManager mAlarmManager;

	public void setAlarm(String date) {
		try {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.JAPAN);
			Date alarmDate = fmt.parse(date);
			Calendar calNow = Calendar.getInstance();
			Calendar calSet = (Calendar) calNow.clone();

			calSet.setTimeInMillis(System.currentTimeMillis());
			
			calSet.set(Calendar.MONTH, alarmDate.getMonth());
			calSet.set(Calendar.DAY_OF_MONTH, alarmDate.getDay()-1);
			calSet.set(Calendar.HOUR_OF_DAY, alarmDate.getHours());
			calSet.set(Calendar.MINUTE, alarmDate.getMinutes());
			calSet.set(Calendar.SECOND, 0);
			calSet.set(Calendar.MILLISECOND, 0);
			
			Intent activate = new Intent(this, AlarmService.class);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 1, activate, 0);
			mAlarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
			mAlarmManager.set(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), pendingIntent);
			
			//add to list alarm list
			MyApplication myapp = (MyApplication)getApplicationContext();
			myapp.addAlarm(pendingIntent);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event);
		JSONObject jObject = null;
		JSONArray eventsArray = null;
		
		try {
			Bundle extras = getIntent().getExtras();
			String eventId = extras.getString("eventId");
			String eventInfo = extras.getString("eventInfo");
			
			TextView infoView = (TextView)findViewById(R.id.event_name);
			TextView contentView = (TextView)findViewById(R.id.event_content);
			Button alarmButton = (Button)findViewById(R.id.touroku);
			Button gotoReservedList = (Button)findViewById(R.id.schedule);
			//decode json		
			jObject=new JSONObject(eventInfo);
			eventsArray = new JSONArray(jObject.getString("events"));
			    
			//find event by clicked id
			for (int i = 0; i<eventsArray.length(); i++) {
			    String t_id = eventsArray.getJSONObject(i).getString("event_id");
			    if (t_id.equals(eventId))  mThisEvent = eventsArray.getJSONObject(i); 
			}
				
			if (mThisEvent != null) {
				infoView.setText(mThisEvent.getString("event_name"));
				String text = "";
				text = mThisEvent.getString("event_content") + 
						"\n" + "\n" + "\n" +
						"開催時刻"+
						mThisEvent.getString("event_date");
				contentView.setText(text);
			}
			gotoReservedList.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getApplicationContext(), EventRegActivity.class);
					startActivity(i);
				}
			});
			
			
			//alarm setting button
			alarmButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					MyApplication app = (MyApplication)getApplicationContext();
					if (!app.contains(mThisEvent)) {
						app.addEvent(mThisEvent);
						try {
							String time = mThisEvent.getString("event_date");
							setAlarm(time);
						} catch (JSONException e) {
							e.printStackTrace();
							return;
						} catch (Exception e) {
							e.printStackTrace();
							return;
						}
					}	
				}
			});
			
			Button gotoTop = (Button)findViewById(R.id.TOP);
	        gotoTop.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getApplicationContext(), SubMainActivity.class);
					startActivity(i);
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
