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
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.widget.Toast;

public class AlarmService extends BroadcastReceiver {
	MediaPlayer mMediaPlayer;

	@Override
	public void onReceive(Context context, Intent intent) {
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        final WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
        wakeLock.acquire();
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
		        wakeLock.release();
		    }
		};
		Timer timer = new Timer();
		timer.schedule(task, ringDelay);
	}
}