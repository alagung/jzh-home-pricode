package com.yypie.ddesktop.service;

import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Intent;
import android.util.Log;

import com.yypie.ddesktop.desktop.Launcher;
import com.yypie.ddesktop.desktop.LockDog;

public class ActivityDog extends Thread {

	final static long termOfValidity = 60000; // 60s -> m second

	ServiceProvider provider;

	ActivityManager activityManager;
	Intent intent = null;
	Intent intentDesk = null;

	boolean running = false;

	ConcurrentHashMap<String, Long> credential;
	HashSet<String> needPassword;
	HashSet<String> allows;

	public ActivityDog(ServiceProvider provider, ActivityManager activityManager) {
		super();
		this.provider = provider;
		this.activityManager = activityManager;

		// Create new indent for password
		intent = new Intent(provider, LockDog.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intentDesk = new Intent(provider, Launcher.class);
		intentDesk.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intentDesk.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		needPassword = new HashSet<String>();
		allows = new HashSet<String>();

		needPassword.add("com.android.settings/com.android.settings.wifi.WifiApSettings");
		allows.add("com.android.phone/com.android.phone.EmergencyDialer");
		allows.add("com.android.phone/com.android.phone.EmergencyCallbackModeExitDialog");
		allows.add("com.android.phone/com.android.phone.EmergencyCallHandler");
		allows.add("com.android.phone/com.android.phone.InCallScreen");
		allows.add("com.android.phone/com.android.phone.PrivilegedOutgoingCallBroadcaster");

		// TODO: test
		allows.add("com.android.contacts/com.android.contacts.DialtactsActivity");

		credential = new ConcurrentHashMap<String, Long>();
		credential.clear();
	}

	synchronized public void registCred(String fullname) {
		if (fullname != null)
			credential.put(fullname, System.currentTimeMillis());
	}

	public void startDog() {
		// every time renew the cred
		credential.clear();
		running = true;
		this.start();
	}

	public void stopDog() {
		this.interrupt();
		running = false;
	}

	private void startLockDog(String activityName) {
		intent.putExtra(LockDog.arg1, activityName);
		provider.startActivity(intent);
	}

	private void backToDesktop() {
		// Restart it
		// activityManager.restartPackage(intentDesk.getPackage());
		provider.startActivity(intentDesk);
	}

	private boolean isDesk(String full) {
		return (full.equals(provider.getPackageName() + "/"
				+ Launcher.class.getName()));
	}

	private boolean isMyPackage(String pack) {
		return (pack.equals(provider.getPackageName()));
	}

	@Override
	public void run() {
		while (running) {
			try {
				// Allow it go
				boolean allow = false;
				// No password
				boolean skip = false;

				// 首先获取到最上面的任务栈, get(0) 获取到任务栈栈顶的activity
				RunningTaskInfo task = activityManager.getRunningTasks(1)
						.get(0);

				String packname = task.topActivity.getPackageName();
				String full = task.topActivity.flattenToString();

				long timestamp = System.currentTimeMillis();

				if (allows.contains(full) || isMyPackage(packname)) {
					allow = true;
					skip = true;
					if (isDesk(full)) {
						// We are back to desk
						// Clean cred
						credential.clear();
					}
				}

				if (needPassword.contains(full)) {
					allow = true;
					if (credential.containsKey(full)) {
						long ex = credential.get(full).longValue();
						if ((timestamp - ex) < termOfValidity) {
							skip = true;
						} else {
							credential.remove(full);
						}
					}
				}

				if (!allow) {
					Log.e(ServiceProvider.TAG, "Blocked: " + full);
					backToDesktop();
				} else if (!skip) {
					startLockDog(full);
				} else {
				}

				Thread.sleep(500);
			} catch (InterruptedException e) {
				// We are stopped.
				break;
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
	}

}
