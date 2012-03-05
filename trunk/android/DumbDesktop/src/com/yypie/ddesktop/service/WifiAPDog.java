package com.yypie.ddesktop.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Intent;
import android.util.Log;

import com.yypie.ddesktop.desktop.Launcher;
import com.yypie.ddesktop.desktop.LockDog;

public class WifiAPDog extends Thread {

	ServiceProvider provider;
	Runtime runtime;
	Process mLogCat;
	BufferedReader mLogReader;
	
	boolean running = false;

	public WifiAPDog(ServiceProvider provider) {
		super();
		this.provider = provider;
		this.runtime = Runtime.getRuntime();
	}

	public void startDog() {
		try {
			mLogCat = runtime.exec("logcat -c");
			mLogCat = runtime.exec("logcat -v raw dnsmasq:I *:S");
			if (mLogCat != null) {
				InputStream is = null;
				InputStreamReader isr = null;

				is = mLogCat.getInputStream();
				isr = new InputStreamReader(is);
				mLogReader = new BufferedReader(isr, 0x400);

				Log.e(ServiceProvider.TAG, "WifiAPDog started");
				running = true;
				this.start();
			}
		} catch (IOException e) {
			Log.e(ServiceProvider.TAG, "Throw: " + Log.getStackTraceString(e));
		}
	}

	public void stopDog() {
		if (mLogReader != null) {
			try {
				mLogReader.close();
				mLogCat.destroy();
			} catch (IOException e) {
				Log.e(ServiceProvider.TAG, "Throw: " + Log.getStackTraceString(e));
			}
		}
		this.interrupt();		
		running = false;
	}
	
	@Override
	public void run() {
		while (running) {
			try {
				String line = mLogReader.readLine();
				//if (!line.startsWith("DHCP"))					continue;
				if (line != null)
					Log.e(ServiceProvider.TAG, line);
			} catch (Exception e) {
				Log.e(ServiceProvider.TAG, "Throw: " + Log.getStackTraceString(e));
				break;
			}
		}
	}
}
