package com.niceappp.wakeup;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

public class WakeupService extends Service implements SensorEventListener {

	private static final String TAG = "WakeupService";
	SensorManager sManager;
	Sensor acceSensor;
	private float mAccel; // acceleration apart from gravity
	private float mAccelCurrent; // current acceleration including gravity
	private float mAccelLast; // last acceleration including gravity
	private long lapsedTime = System.currentTimeMillis();

	@Override
	public void onCreate() {
		super.onCreate();
		Toast.makeText(this, "Service Created", Toast.LENGTH_LONG).show();
		Log.d(TAG, "Started");
		sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		acceSensor = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		sManager.registerListener(this, acceSensor,
				SensorManager.SENSOR_DELAY_NORMAL);

		mAccel = 0.00f;
		mAccelCurrent = SensorManager.GRAVITY_EARTH;
		mAccelLast = SensorManager.GRAVITY_EARTH;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		sManager.unregisterListener(this, acceSensor);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
//		Log.d(TAG, "onAccuracyChanged = sensor: " + sensor + ", accuracy: "+ accuracy);

	}

	@Override
	public void onSensorChanged(SensorEvent se) {
		//Log.d(TAG, "onSensorChanged = sensor=" + se.sensor + " >>> " + se.values);
		
		float x = se.values[0];
		float y = se.values[1];
		float z = se.values[2];
		mAccelLast = mAccelCurrent;
		mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
		float delta = mAccelCurrent - mAccelLast;
		mAccel = mAccel * 0.9f + delta; // perform low-cut filter
		long t = System.currentTimeMillis() - lapsedTime;
		//Log.d(TAG, " t =" + t + ", lapsedtime =" + lapsedTime);
		if (mAccel > 14 && t > 1300 ) {
			Log.d(TAG, " X=" + mAccel + ", t=" + t);
			toggleScreen();
			lapsedTime = System.currentTimeMillis();
		}
		
	}

	private void toggleScreen() {
		PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
//		WindowManager window = (WindowManager) getSystemService(WINDOW_SERVICE);
//		int orientation = window.getDefaultDisplay().getOrientation();
		
		boolean isScreenOn = pm.isScreenOn();
		if (!isScreenOn ) {
			PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "Your Tag");
			wl.acquire();
			wl.release();
			Log.d(TAG, " Turn on");
			
			Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(100);

		} else {
			DevicePolicyManager mDPM =
				    (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
			mDPM.lockNow();

			Log.d(TAG, " Turn off");
		}
	}
}
