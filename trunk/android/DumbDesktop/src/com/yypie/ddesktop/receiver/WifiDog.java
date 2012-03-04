package com.yypie.ddesktop.receiver;

import com.yypie.ddesktop.service.ServiceProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.TrafficStats;
import android.net.wifi.WifiManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class WifiDog extends BroadcastReceiver {
	public static final String WIFI_AP_STATE_CHANGED_ACTION = "android.net.wifi.WIFI_AP_STATE_CHANGED";
    public static final String EXTRA_WIFI_AP_STATE = "wifi_state";
    
	ServiceProvider provider;

	WifiManager mWifiManager = null;
	ConnectivityManager mConnectivityManager = null;
	
	public WifiDog(ServiceProvider provider) {
		super();
		this.provider = provider;
		this.mWifiManager = provider.wifiManager;
		this.mConnectivityManager = provider.connectivityManager;
		
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		 String action = intent.getAction();
		 int state = 0;
         if (WIFI_AP_STATE_CHANGED_ACTION.equals(action)) {
             state = intent.getIntExtra(EXTRA_WIFI_AP_STATE, 0);
         } else if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
        	 state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
         }
         Log.e(ServiceProvider.TAG, intent.getAction() + " State: " + state);
	}
	
	// Public interface
	public void startDog() {
		IntentFilter filter = new IntentFilter();
		filter.setPriority(1000);
		filter.addAction(WIFI_AP_STATE_CHANGED_ACTION);
		filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		filter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);

		provider.registerReceiver(this, filter);
	}

	public void stopDog() {
		provider.unregisterReceiver(this);
	}
}
