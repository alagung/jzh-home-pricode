package com.yypie.ddesktop.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.TelephonyManager;

import com.yypie.ddesktop.receiver.PhoneDog;
import com.yypie.ddesktop.receiver.PhoneStatReceiver;

public class ServiceProvider extends Service {
	static final String ACTION_NAME = ServiceProvider.class.getName() + ".MAIN";
	public static final String TAG = "YYPIE";
	
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
	ActivityManager activityManager;
	TelephonyManager telephonyManager;


	private IServiceProvider.Stub mBinder = new ServiceProviderBinder();

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

		telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

		// Initialize global settings
		pd = new PhoneDog(this, telephonyManager);
		pd.startDog();
		
		ad = new ActivityDog(this, activityManager);
		ad.startDog();
		super.onCreate();
	}

	public void onDestroy() {
		pd.stopDog();
		pd = null;
		ad.stopDog();
		ad = null;
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
