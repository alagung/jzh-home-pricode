package com.yypie;

import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import android.app.ActivityManager;
import android.app.Service;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class WatchDog extends Service {
	final static long termOfValidity = 15000000; // 15s -> m second
	ActivityManager activityManager;
	Intent intent = null;
	Intent intentDesk = null;
	ConcurrentHashMap<String, Long> credential;
	HashSet<String> needPassword;
	HashSet<String> allows;
	public Launcher m = null;
	
	//这里定义吧一个Binder类，用在onBind()有方法里，这样Activity那边可以获取到 
	private MyBinder mBinder = new MyBinder();  
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	@Override
	public void onCreate() {  
	    activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);  
		// Create new indent for password
	    intent = new Intent(this, Manager.class);  
	    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
	    intentDesk = new Intent(this, Launcher.class);  
	    intentDesk.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
	    intentDesk.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
	    
		// Initialize global settings
	    needPassword = new HashSet<String>();
	    allows = new HashSet<String>();
	    
	    needPassword.add("com.android.settings/com.android.settings.wifi.WifiApSettings");
	    allows.add("com.android.phone/com.android.phone.EmergencyDialer");
	    
	    new Thread() {  
	        @Override  
	        public void run() { 
	        	// every time renew the cred
	        	credential = new ConcurrentHashMap<String, Long>();
	        	String desk = Launcher.class.getPackage() + "/" + Launcher.class.getName();
	    	    
	        	while (true) {  
	            	// Allow it go
	            	boolean allow = false;
	            	// No password
	            	boolean skip = false;
	            	
	                // 首先获取到最上面的任务栈, get(0) 获取到任务栈栈顶的activity 
	            	RunningTaskInfo task = activityManager.getRunningTasks(1).get(0);
	            	
	                String packname = task.topActivity.getPackageName();
	                String full = task.topActivity.flattenToString();
	                
	                long timestamp = System.currentTimeMillis();
	                
	                if (allows.contains(full) || Launcher.class.getPackage().getName().equals(packname)) {
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
	                	//activityManager.restartPackage(intentDesk.getPackage());
	                	startActivity(intentDesk);
	                } else if (!skip) {
						startActivity(intent);
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
