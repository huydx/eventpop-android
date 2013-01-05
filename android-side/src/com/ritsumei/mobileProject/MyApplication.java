/**
* Customized application class to store information
* @author huydx 
* @version 0.9
*/

package com.ritsumei.mobileProject;

import android.app.*;
import java.util.*;
import org.json.*;

public class MyApplication extends Application {

	private ArrayList<Event> mReservedEvent = new ArrayList<Event>();
	private ArrayList<PendingIntent> mReservedAlarm = new ArrayList<PendingIntent>();

	private Event JsonToEvent(JSONObject o) {
		Event e = new Event();
		try {
			e.setId(o.getString("event_id"));
			e.setName(o.getString("event_name"));
			e.setContent(o.getString("event_content"));
			e.setTime(o.getString("event_date"));
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
		return e;
	}

	public void addEvent(JSONObject o) {
		Event e = JsonToEvent(o);
		mReservedEvent.add(e);
	}

	public boolean contains(JSONObject o) {
		Event e = JsonToEvent(o);
		return mReservedEvent.contains(e);
	}

	public void removeEvent(Event e) {
		mReservedEvent.remove(e);
	}
	
	public ArrayList<Event> getReservedEvent() {
		return mReservedEvent;
	}
	
	public void addAlarm(PendingIntent intent) {
		mReservedAlarm.add(intent);
	}
	
	public void removeAlarm(PendingIntent intent) {
		mReservedAlarm.remove(intent);
	}
	
	public PendingIntent getAlarm(int index) {
		return mReservedAlarm.get(index);
	}
	
	public int getEventIdx(Event e) {
		return mReservedEvent.indexOf(e);
	}
}
