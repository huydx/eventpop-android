/**
 * Broadcast implement for alarm service.
 * @author huydx 
 * @version 0.9
 */
package com.ritsumei.mobileProject;

import java.util.*;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.*;
import android.net.Uri;
import android.os.Vibrator;
import android.widget.Toast;

public class AlarmService extends BroadcastReceiver {
	MediaPlayer mMediaPlayer;

	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "Alarm worked.", Toast.LENGTH_LONG).show();
		Vibrator v = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(2000);
		long ringDelay = 3500;
		Uri notification = RingtoneManager
		                           .getDefaultUri(RingtoneManager.TYPE_ALARM);
		final Ringtone alarmRingtone = RingtoneManager
		                   .getRingtone(context, notification);
		alarmRingtone.play();
		TimerTask task = new TimerTask() {
		    @Override
		    public void run() {
		        alarmRingtone.stop();
		    }
		};
		Timer timer = new Timer();
		timer.schedule(task, ringDelay);
	}
}