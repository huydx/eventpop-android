/**
* Event registration/remove alarm display activity
* @author huydx 
* @version 0.9
*/

package com.ritsumei.mobileProject;

import java.util.ArrayList;

import com.ritsumei.mobileProject.R;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class EventRegActivity extends Activity {
	private ArrayList<Event> mListEvent = new ArrayList<Event>();
	private MyCustomAdapter dataAdapter = null;
	private ListView	listView = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.regeventlist_main);
		MyApplication myapp = (MyApplication) getApplicationContext();
		mListEvent = myapp.getReservedEvent();
		// Generate list View from ArrayList
		displayListView();
		checkButtonClick();
		
		Button gotoTop = (Button)findViewById(R.id.TOP);
        gotoTop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), SubMainActivity.class);
				startActivity(i);
			}
		});
	}

	private void displayListView() {
		// create an ArrayAdaptar from the String Array
		dataAdapter = new MyCustomAdapter(this, R.layout.regeventlist,
				mListEvent);
		listView = (ListView) findViewById(R.id.listView1);
		// Assign adapter to ListView
		listView.setAdapter(dataAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, show a toast with the TextView text
				Event evenList = (Event) parent.getItemAtPosition(position);
				Toast.makeText(getApplicationContext(),
						"Clicked on Row: " + evenList.getName(),
						Toast.LENGTH_LONG).show();
			}
		});

	}

	private void checkButtonClick() {
		Button myButton = (Button) findViewById(R.id.findSelected);
		myButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				StringBuffer responseText = new StringBuffer();
				responseText.append("The following were selected...\n");

				ArrayList<Event> eventList = mListEvent;
				for (int i = 0; i < eventList.size(); i++) {
					Event event = eventList.get(i);
					if (event.isSelected()) {
						responseText.append("\n" + event.getName());
						removeAlarm(event);
						//remove view item
						dataAdapter.remove(event);
					}
				}
				Toast.makeText(getApplicationContext(), responseText,
						Toast.LENGTH_LONG).show();
			}
		});
		listView.invalidateViews();
	}
	
	public void removeAlarm(Event e) {
		MyApplication myapp = (MyApplication)getApplicationContext();
		int idx = myapp.getEventIdx(e);
		PendingIntent p = myapp.getAlarm(idx);
		AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		
		am.cancel(p);
		myapp.removeAlarm(p);
		myapp.removeEvent(e);
	}
	
	// customized class
	private class MyCustomAdapter extends ArrayAdapter<Event> {
		public MyCustomAdapter(Context context, int textViewResourceId,
				ArrayList<Event> countryList) {
			super(context, textViewResourceId, countryList);
		}

		// holder for a item of view
		private class ViewHolder {
			TextView time;
			CheckBox name;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.regeventlist, null);

				holder = new ViewHolder();
				holder.time = (TextView) convertView.findViewById(R.id.code);
				holder.name = (CheckBox) convertView
						.findViewById(R.id.checkBox1);
				convertView.setTag(holder);

				holder.name.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						CheckBox cb = (CheckBox) v;
						Event e = (Event)v.getTag();
						Toast.makeText(
								getApplicationContext(),
								"Clicked on Checkbox: " + cb.getText() + " is "
										+ cb.isChecked(), Toast.LENGTH_LONG)
								.show();
						e.setSelected(cb.isChecked());
					}
				});
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Event event = mListEvent.get(position);
			holder.time.setText(event.getTime());
			holder.name.setText(event.getName()+"    ");
			holder.name.setTag(event);

			return convertView;
		}
	}
}
