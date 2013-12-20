package com.niceappp.wakeup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Bootreceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent service = new Intent(context, WakeupService.class);
	    context.startService(service);
	}

}
