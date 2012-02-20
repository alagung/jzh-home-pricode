package com.yypie;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class WatchDog extends Service {
	private static final String TAG = "WatchDog"; 
	ActivityManager activityManager;
	Intent intent;
	
	//这里定义吧一个Binder类，用在onBind()有方法里，这样Activity那边可以获取到 
	private MyBinder mBinder = new MyBinder();  
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}

	@Override
	public void onCreate() {  
	    //dao  
	    //lockAppDao = new LockAppDao(getApplicationContext());  
	    //从数据库获取到程序锁的集合  
	    //blockapppacks = lockAppDao.findAll();  
	    //获取程序锁集合和程序锁当前状态是否已经打开  
	    //blockappinfos = new ArrayList<LockAppInfo>();  
	    //fillData(blockapppacks);  
	    // 得到activity的管理器  
		activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);  
		intent = new Intent(this, Manager.class);  
	    // 在新的任务栈中创建 activity的实例  
	    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
	    new Thread() {  
	        @Override  
	        public void run() {  
	            // 看门狗, 不停的查看当前activity任务栈的栈顶  
	            while (true) {  
	                // 首先获取到最上面的任务栈, get(0) 获取到任务栈栈顶的activity  
	                String packname = activityManager.getRunningTasks(1).get(0).topActivity  
	                        .getPackageName();
	                /*for (LockAppInfo apppackinfo : blockappinfos) {  
	                    if (packname.equals(apppackinfo.getPackname())) {  
	                        //防止重复弹出密码对话框  
	                        if (!apppackinfo.isFlagcanstart()) {  
	                            // 弹出密码对话框,弹出新的activity 覆盖当前要启动的activity  
	                            intent.putExtra("packagename", packname);  
	                            startActivity(intent);  
	                        }  
	                    }  
	                }*/
	                if (packname != "com.yypie")
		                startActivity(intent);  
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
