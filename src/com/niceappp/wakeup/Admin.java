package com.niceappp.wakeup;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class Admin extends DeviceAdminReceiver {
	
	@Override
	public void onEnabled(Context context, Intent intent) {
		super.onEnabled(context, intent);
		Toast.makeText(context, "Admin enabled",
				Toast.LENGTH_LONG).show();
		Log.d("Admin", "onEnabled");
	}
}
