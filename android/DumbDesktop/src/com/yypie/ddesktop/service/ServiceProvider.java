package com.yypie.ddesktop.service;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.yypie.ddesktop.receiver.PhoneDog;
import com.yypie.ddesktop.receiver.WifiDog;

public class ServiceProvider extends Service {
	static final String ACTION_NAME = ServiceProvider.class.getName() + ".MAIN";
	public static final String TAG = "YYPIE";
	static final String PACKAGE_NAME = "com.yypie.ddesktop";
	
	// Helper controllers
	public static ComponentName StartMe(Context context) {
		return context.startService(new Intent(ACTION_NAME));
	}
	public static boolean StopMe(Context context) {
		return context.stopService(new Intent(ACTION_NAME));
	}
	public static ServiceProviderConn bindMe(Context context,
			ServiceProviderConn conn) {
		if (conn == null)
			conn = new ServiceProviderConn();
		context.bindService(new Intent(ACTION_NAME), conn, Context.BIND_AUTO_CREATE);
		return conn;
	}
	public static void unBindMe(Context context, ServiceProviderConn conn) {
		context.unbindService(conn);
	}

	ActivityDog ad = null;
	PhoneDog pd = null;
	WifiDog wd = null;
	WifiAPDog wad = null;
	
	public ActivityManager activityManager;
	public TelephonyManager telephonyManager;
	public NotificationManager notificationManager;
	public WifiManager wifiManager;
	public ConnectivityManager connectivityManager;
	public PackageManager packageManager;


	private IServiceProvider.Stub mBinder = new ServiceProviderBinder();

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationManager.cancelAll();
		wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
		connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
		packageManager = getPackageManager();
		
		// TODO: Enable this block
		// Block ADB and set my launcher default
		try {
			Settings.Secure.putInt(getContentResolver(), Settings.Secure.ADB_ENABLED, 1);			
		} catch (SecurityException e) {
			// We need root and be put in /system/app
			Log.e(TAG, "Failed to disable ADB, we may need root. " + e.getMessage());
		}
	
		// Set us default
        List<ComponentName> prefActList = new ArrayList<ComponentName>();
        
        // Intent list cannot be null. so pass empty list
        List<IntentFilter> intentList = new ArrayList<IntentFilter>();
        packageManager.getPreferredActivities(intentList, prefActList, PACKAGE_NAME);
        Log.i(TAG, "Have "+prefActList.size()+" number of activities in prefered list");
        packageManager.clearPackagePreferredActivities(PACKAGE_NAME);        
        // packageManager.addPreferredActivity(filter, IntentFilter.MATCH_CATEGORY_EMPTY, components, component); 
		
		
		// Initialize global settings
		pd = new PhoneDog(this, telephonyManager);
		pd.startDog();
		
		wd = new WifiDog(this);
		wd.startDog();
		
		wad = new WifiAPDog(this);
		wad.startDog();
		
		ad = new ActivityDog(this, activityManager);
		ad.startDog();
		super.onCreate();
	}

	public void onDestroy() {
		Log.e(TAG, "YYPIE Exiting...");
		
		ad.stopDog();
		ad = null;
		
		pd.stopDog();
		pd = null;
		wd.stopDog();
		wd = null;
		
		wad.stopDog();
		wad = null;
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	// Interfaces
	public void registCred(String fullname) {
		ad.registCred(fullname);
	}



	public class ServiceProviderBinder extends IServiceProvider.Stub {

		@Deprecated
		ServiceProvider getService() {
			return ServiceProvider.this;
		}

		@Override
		public void registCred(String fullname) throws RemoteException {
			ServiceProvider.this.registCred(fullname);
		}
	}

	static public class ServiceProviderConn implements ServiceConnection {
		public IServiceProvider mService = null;

		public void onServiceConnected(ComponentName className, IBinder service) {
			mService = IServiceProvider.Stub.asInterface(service);
		}

		public void onServiceDisconnected(ComponentName className) {
			mService = null;
		}
	}
}
