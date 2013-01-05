/**
 * Map view activity
 * @author huydx 
 * @version 0.9
 */

package com.ritsumei.mobileProject;

import java.util.*;
import org.json.*;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.ritsumei.mobileProject.R;

import android.graphics.drawable.Drawable;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class SubMainActivity extends MapActivity {
	/** Called when the activity is first created. */
	private LocationManager mLocationManager;
	private LocationListener mLocationListener;
	private MapView mMapView;
	private MapController mMapController;
	// private String provider;
	private MyLocationOverlay mMyLocationOverlay;
	private String best;
	private ArrayList<String> mNoticedEvent;
	private ArrayList<JSONObject> mListAllEvent = new ArrayList<JSONObject>();
	
	private void CenterLocation(GeoPoint centerGeoPoint) {
		mMapController.animateTo(centerGeoPoint);
		mMapView.getOverlays().add(mMyLocationOverlay);
	};

	private void AlertNewEvent(final String eventinfo) {
		JSONObject jObject = null;
		JSONArray eventsArray = null;
		Boolean isNew = false;
		try {
			jObject = new JSONObject(eventinfo);
			eventsArray = new JSONArray(jObject.getString("events"));

			for (int i = 0; i < eventsArray.length(); i++) {
				String t_id = eventsArray.getJSONObject(i)
						.getString("event_id").toString();
				if (mNoticedEvent == null) {
					mNoticedEvent = new ArrayList<String>();
					mNoticedEvent.add(t_id);
					isNew = true;
				} else if (!mNoticedEvent.contains(t_id)) {
					mNoticedEvent.add(t_id);
					isNew = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		if (isNew == true) {
			Vibrator v = (Vibrator) getApplicationContext().getSystemService(
					Context.VIBRATOR_SERVICE);
			v.vibrate(1000);

			Dialog dialog = new Dialog(SubMainActivity.this);
			dialog.setContentView(R.layout.mydialog);
			dialog.setTitle("イベント発見");
			dialog.setCancelable(true);

			TextView text = (TextView) dialog.findViewById(R.id.TextView01);
			text.setText(R.string.event_discover);

			ImageView img = (ImageView) dialog.findViewById(R.id.ImageView01);
			img.setImageResource(R.drawable.warning);

			// set up button
			Button button = (Button) dialog.findViewById(R.id.Button01);
			button.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getApplicationContext(),
							EventListActivity.class);
					i.putExtra("eventInfo", eventinfo);
					startActivity(i);
				}
			});
			// now that the dialog is set up, it's time to show it
			dialog.show();
		}
	}

	private String IsNeerEventPoint(GeoPoint centerGeoPoint) {
		// post to server
		String responseBody = "";
		try {
			String lat = Double.toString(centerGeoPoint.getLatitudeE6() / 1E6);
			String lng = Double.toString(centerGeoPoint.getLongitudeE6() / 1E6);
			ASyncRequest request = new ASyncRequest();
			responseBody = request.execute(
					"http://cx0cjz7-afx-app000.c4sa.net/", lat, lng).get();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		return responseBody;
	}

	private void DrawAllEvent() {
		try {
			String responseBody;
			ASyncRequest request = new ASyncRequest();
			responseBody = request.execute("http://cx0cjz7-afx-app000.c4sa.net/getall.php").get();
			if (!responseBody.equals("")) {
				JSONObject jObject = null;
				JSONArray eventsArray = null;
				try {
					jObject = new JSONObject(responseBody);
					eventsArray = new JSONArray(jObject.getString("events"));
					Drawable drawable = this.getResources().getDrawable(R.drawable.marker);				
					CustomItemizedOverlay itemizedoverlay = new CustomItemizedOverlay(drawable, this);
					
					for (int i = 0; i < eventsArray.length(); i++) {
						String latitude = eventsArray.getJSONObject(i).getString("latitude").toString();
						String longtitude = eventsArray.getJSONObject(i).getString("longtitude").toString();
						GeoPoint geopoint = new GeoPoint((int)(Float.parseFloat(latitude) * 1E6), (int)(Float.parseFloat(longtitude) * 1E6));
						OverlayItem overlayitem = new OverlayItem(geopoint, 
								eventsArray.getJSONObject(i).getString("event_name"), 
								eventsArray.getJSONObject(i).getString("event_content"));
						itemizedoverlay.addOverlay(overlayitem);
						mListAllEvent.add(eventsArray.getJSONObject(i));
					}
					mMapView.getOverlays().add(itemizedoverlay);
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button schedule = (Button) findViewById(R.id.schedule);
		schedule.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),
						EventRegActivity.class);
				startActivity(i);
			}
		});

		mMapView = (MapView) findViewById(R.id.mapview);
		mMapView.setBuiltInZoomControls(true);
		mMapView.setSatellite(false);
		mMapController = mMapView.getController();
		mMapController.setZoom(20);

		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mLocationListener = new CustomLocationListener();
		Location curLocation = mLocationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		Criteria crit = new Criteria();
		crit.setAccuracy(Criteria.ACCURACY_FINE);
		best = mLocationManager.getBestProvider(crit, false);
		mLocationManager.requestLocationUpdates(best, 0, 1, mLocationListener);

		// "this" refers to your activity
		mMyLocationOverlay = new MyLocationOverlay(this, mMapView);
		mMyLocationOverlay.enableMyLocation();
		mMapView.getOverlays().add(mMyLocationOverlay);

		mMyLocationOverlay.runOnFirstFix(new Runnable() {
			public void run() {
				mMapView.getController().animateTo(
						mMyLocationOverlay.getMyLocation());
			}
		});

		if (curLocation != null) {
			GeoPoint initGeoPoint = new GeoPoint((int) (mLocationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
					.getLatitude() * 1000000), (int) (mLocationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
					.getLongitude() * 1000000));
			DrawAllEvent();
			CenterLocation(initGeoPoint);
			String ret = IsNeerEventPoint(initGeoPoint);
			if (ret.length() > 0) {
				AlertNewEvent(ret);
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mLocationManager.requestLocationUpdates(best, 10000, 1,
				mLocationListener);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mLocationManager.removeUpdates(mLocationListener);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return true;
	}

	/* Customized class */
	// /////////////////
	private class CustomLocationListener implements LocationListener {
		public void onLocationChanged(Location argLocation) {
			GeoPoint myGeoPoint = new GeoPoint(
					(int) (argLocation.getLatitude() * 1000000),
					(int) (argLocation.getLongitude() * 1000000));
			Toast.makeText(
					getApplicationContext(),
					"location changed" + argLocation.getLatitude() + " "
							+ argLocation.getLongitude(), Toast.LENGTH_LONG)
					.show();
			CenterLocation(myGeoPoint);
			String ret = IsNeerEventPoint(myGeoPoint);
			if (ret.length() > 0) {
				AlertNewEvent(ret);
			}
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}

	public class CustomItemizedOverlay extends ItemizedOverlay {

		private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
		private Context mContext;

		public CustomItemizedOverlay(Drawable defaultMarker, Context context) {
			super(boundCenterBottom(defaultMarker));
			// TODO Auto-generated constructor stub
			mContext = context;
		}

		@Override
		protected OverlayItem createItem(int i) {
			return mOverlays.get(i);
		}

		@Override
		public int size() {
			return mOverlays.size();
		}

		public void addOverlay(OverlayItem overlay) {
			mOverlays.add(overlay);
			populate();
		}

		@Override
		protected boolean onTap(int index) {
			final int idx = index;
			OverlayItem item = mOverlays.get(index);
			AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
			dialog.setTitle(item.getTitle());
			dialog.setMessage(item.getSnippet()).setPositiveButton("予約", new DialogInterface.OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					try {
						JSONObject obj = mListAllEvent.get(idx);
						Intent i = new Intent(getApplicationContext(), EventActivity.class);
						i.putExtra("eventName", obj.getString("event_name"));
						i.putExtra("eventId", obj.getString("event_id"));
						i.putExtra("fromMap", true);
						i.putExtra("eventInfo",obj.toString());
						startActivity(i);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			dialog.show();
			return true;
		}
	}
}
