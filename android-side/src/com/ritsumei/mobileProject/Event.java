/**
 * Event struct
 * @author huydx 
 * @version 0.9
 */
package com.ritsumei.mobileProject;

import org.json.*;

//event struct
public class Event {

	private String _id;
	private String _content;
	private String _name;
	private String _time;
	private boolean _selected;

	//constructor
	public Event() {
		this._id = "";
		this._content = "";
		this._name = "";
		this._time = "";
		this._selected = false;
	}
	
	public Event(JSONObject o) {
		try {
			this._id = o.getString("event_id");
			this._content = o.getString("event_content");
			this._name = o.getString("event_name");
			this._time = o.getString("event_date");
			this._selected = false;
		} catch(Exception e) {
			//do nothing
		}
	}
	
	public String getId() {
		return _id;
	}

	public void setId(String id) {
		_id = id;
	}

	public String getContent() {
		return _content;
	}

	public void setContent(String content) {
		_content = content;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public String getTime() {
		return _time;
	}

	public void setTime(String time) {
		_time = time;
	}

	public boolean isSelected() {
		return _selected;
	}

	public void setSelected(boolean selected) {
		this._selected = selected;
	}

	@Override
	public boolean equals(Object _other) {
		if (_other == null || this.getClass() != _other.getClass()) {
			return false;
		}

		final Event other = (Event) _other;
		if (other._id == null)
			return false;
		else {
			if (other._id.equals(this._id)) {
				return true;
			}
		}
		return false;
	}
}
