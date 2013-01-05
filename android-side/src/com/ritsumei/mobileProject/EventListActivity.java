/**
* Event list display activity
* @author huydx 
* @version 0.9
*/

package com.ritsumei.mobileProject;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ritsumei.mobileProject.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class EventListActivity extends Activity {
	private String[] listItems;
	private String[] listId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		JSONObject jObject = null;
		JSONArray eventsArray = null;
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eventlist);
		Bundle extras = getIntent().getExtras();
		final String eventinfo = extras.getString("eventInfo");
		
		//decode json object
		try {	
	        jObject=new JSONObject(eventinfo);
	        eventsArray = new JSONArray(jObject.getString("events"));
	        listItems = new String[eventsArray.length()];
	        listId = new String[eventsArray.length()];
	        
	        for (int i = 0; i<eventsArray.length(); i++) {
	        	listItems[i] = eventsArray.getJSONObject(i).getString("event_name").toString();
	        	listId[i] = eventsArray.getJSONObject(i).getString("event_id").toString();
	        }
		}
		catch(Exception e){
			e.printStackTrace();
			return;
		}		
		
        ListView eventList = (ListView) findViewById(R.id.event_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, listItems);
        eventList.setAdapter(adapter);

        eventList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				String item = ((TextView)view).getText().toString();
                Intent i = new Intent(getApplicationContext(), EventActivity.class);
				i.putExtra("eventName", item);
				i.putExtra("eventId", listId[(int)id]);
				i.putExtra("eventInfo", eventinfo);
				startActivity(i);
			}
        });
        
        Button gotoReservedList = (Button)findViewById(R.id.schedule);
        gotoReservedList.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), EventRegActivity.class);
				startActivity(i);
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
        
	}
}
