package com.yypie.ddesktop.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.util.Log;

public class WifiAPDog extends Thread {

	ServiceProvider provider;
	Process mLogCat;
	BufferedReader mLogReader;
	
	boolean running = false;

	public WifiAPDog(ServiceProvider provider) {
		super();
		this.provider = provider;
	}

	public void startDog() {
		running = true;
		this.start();
	}

	public void stopDog() {
		// Close the process first
		if (mLogCat != null)
			mLogCat.destroy();
		
		// Clear reader
		if (mLogReader != null) {
			try {
				mLogReader.close();
			} catch (IOException e) {
				Log.e(ServiceProvider.TAG, "Throw: " + Log.getStackTraceString(e));
			}
		}
		
		this.interrupt();		
		running = false;
	}
	
	@Override
	public void run() {
		try {
			Log.e(ServiceProvider.TAG, "WifiAPDog started");
			
			Runtime runtime = Runtime.getRuntime();
			mLogCat = runtime.exec("logcat -c");
			mLogCat.waitFor();			
			mLogCat = runtime.exec("logcat -v raw dnsmasq:I *:S");
			if (mLogCat != null) {
				InputStream is = null;
				InputStreamReader isr = null;

				is = mLogCat.getInputStream();
				isr = new InputStreamReader(is);
				mLogReader = new BufferedReader(isr, 0x400);
			}
			
			if (mLogCat == null || mLogReader == null)
			{
				// Not started
				running = false;
			}
			
			String line = mLogReader.readLine();			
			while (running && line != null) {
				if (line.startsWith("DHCP"))
					Log.e(ServiceProvider.TAG, line);
				line = mLogReader.readLine();
			}
		} catch (Exception e) {
			Log.e(ServiceProvider.TAG, "Throw: " + Log.getStackTraceString(e));
		}

		Log.e(ServiceProvider.TAG, "WifiAPDog stopped");
	}
}
