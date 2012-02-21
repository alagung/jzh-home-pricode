package com.yypie;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;

public class WatchDog extends Service {
	final static long termOfValidity = 15000000; // 15s -> m second
	ActivityManager activityManager;
	Intent intent;
	ConcurrentHashMap<String, Long> credential;
	HashSet<String> needPassword;
	HashSet<String> allows;
	
	//这里定义吧一个Binder类，用在onBind()有方法里，这样Activity那边可以获取到 
	private MyBinder mBinder = new MyBinder();  
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}

	@Override
	public void onCreate() {  
	    activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);  
		// Create new indent for password
	    intent = new Intent(this, Manager.class);  
	    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
	    
		final AlertDialog dia = new AlertDialog.Builder(this)
		.setTitle("Message Box")
		.setMessage("Sure to exit?")
		.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		})
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		}).create();
	    
	    // Initialize global settings
	    needPassword = new HashSet<String>();
	    allows = new HashSet<String>();
	    
	    needPassword.add("com.android.settings/com.android.settings.TetherSettings");
	    
	    new Thread() {  
	        @Override  
	        public void run() { 
	        	// every time renew the cred
	        	credential = new ConcurrentHashMap<String, Long>();
	        	String desk = Launcher.class.getPackage() + "/" + Launcher.class.getName();
	    	    
	        	// 看门狗, 不停的查看当前activity任务栈的栈顶  
	            while (true) {  
	            	// Allow it go
	            	boolean allow = false;
	            	// No password
	            	boolean skip = false;
	            	
	                // 首先获取到最上面的任务栈, get(0) 获取到任务栈栈顶的activity 
	            	RunningTaskInfo task = activityManager.getRunningTasks(1).get(0);
	            	
	                String packname = task.topActivity.getPackageName();
	                String clsName = task.topActivity.getClassName();
	                String full = task.topActivity.flattenToString();
	                
	                long timestamp = System.currentTimeMillis();
	                
	                if (allows.contains(full) || Launcher.class.getPackage().equals(packname)) {
	                	allow = true;
	                	skip = true;
	                	if (desk.equals(full)) {
	                		// We are back to desk
	                		// Clean cred
	                		credential.clear();
	                	}
	                }
	                
	                if (needPassword.contains(full)) {
	                	allow = true;
	                	if (credential.contains(full)) {
							long ex = credential.get(full).longValue();
							if ((timestamp - ex) < termOfValidity) {
								skip = true;
							} else {
								credential.remove(full);
							}
						}
	                }
					
	                if (!allow) {
	                	// Restart it
	                	// activityManager.restartPackage(packname);
	                } else if (!skip) {
						// startActivity(intent);
	                	dia.show();
	                	credential.put(full, System.currentTimeMillis());
					}
	                try {  
	                    Thread.sleep(200);  
	                } catch (InterruptedException e) {  
	                    e.printStackTrace();  
	                }  
	            }  
	        }  
	    }.start();  
	    super.onCreate();  
	}  

	public class MyBinder extends Binder {
		WatchDog getService() {
			return WatchDog.this;
		}
	}
}
