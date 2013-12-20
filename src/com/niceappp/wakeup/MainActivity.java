package com.niceappp.wakeup;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";
	private DevicePolicyManager devicePolicyManager;
    private ComponentName deviceAdmin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Intent service = new Intent(this, WakeupService.class);
		startService(service);
		enableAdmin();
	}

	private void enableAdmin() {
		devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        deviceAdmin = new ComponentName(this, Admin.class);
        boolean active = devicePolicyManager.isAdminActive(deviceAdmin);
        if (active) return;
        
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, deviceAdmin);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "Enable admin");
        startActivityForResult(intent, 1);
        Log.d(TAG, "Started?");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
