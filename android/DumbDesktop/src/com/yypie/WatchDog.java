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
	
	//���ﶨ���һ��Binder�࣬����onBind()�з��������Activity�Ǳ߿��Ի�ȡ�� 
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
	    //�����ݿ��ȡ���������ļ���  
	    //blockapppacks = lockAppDao.findAll();  
	    //��ȡ���������Ϻͳ�������ǰ״̬�Ƿ��Ѿ���  
	    //blockappinfos = new ArrayList<LockAppInfo>();  
	    //fillData(blockapppacks);  
	    // �õ�activity�Ĺ�����  
		activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);  
		intent = new Intent(this, Manager.class);  
	    // ���µ�����ջ�д��� activity��ʵ��  
	    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
	    new Thread() {  
	        @Override  
	        public void run() {  
	            // ���Ź�, ��ͣ�Ĳ鿴��ǰactivity����ջ��ջ��  
	            while (true) {  
	                // ���Ȼ�ȡ�������������ջ, get(0) ��ȡ������ջջ����activity  
	                String packname = activityManager.getRunningTasks(1).get(0).topActivity  
	                        .getPackageName();
	                /*for (LockAppInfo apppackinfo : blockappinfos) {  
	                    if (packname.equals(apppackinfo.getPackname())) {  
	                        //��ֹ�ظ���������Ի���  
	                        if (!apppackinfo.isFlagcanstart()) {  
	                            // ��������Ի���,�����µ�activity ���ǵ�ǰҪ������activity  
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
