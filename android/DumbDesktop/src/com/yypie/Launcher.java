package com.yypie;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.app.ListActivity;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Launcher extends ListActivity {
	private static final String[][] gItems = {
			{ "��ЯʽWifi�ȵ�����", "com.android.settings",
					"com.android.settings.TetherSettings" },
			{ "�������", "com.yypie", "com.yypie.Manager" } };
	private WatchDog  mMyService;  
	
	//������Ҫ�õ�ServiceConnection��Context.bindService��context.unBindService()���õ�  
	private ServiceConnection mServiceConnection = new ServiceConnection() {
		// ����bindServiceʱ����TextView��ʾMyService��getSystemTime()�����ķ���ֵ
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			mMyService = ((WatchDog.MyBinder) service).getService();
		}

		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub

		}
	};
 
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initListAdapter();
		Intent i  = new Intent();  
		i.setClass(this, WatchDog.class);
		this.startService(i);
	}

	private void initListAdapter() {

		List<String> items = new ArrayList<String>();
		for (String[] item : gItems) {
			items.add(item[0]);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, items);
		this.setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent();
		intent.setClassName(gItems[position][1], gItems[position][2]);
		startActivityForResult(intent, 1);
		super.onListItemClick(l, v, position, id);
	}

	protected void clearAll() {
		NotificationManager notiManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notiManager.cancelAll();
		//ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		clearAll();
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event); 
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return super.onKeyUp(keyCode, event);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}

	@Override
	protected void onPostResume() {
		super.onPostResume();
	}

	@Override
	protected void onResume() {
		new AlertDialog.Builder(this).setTitle("ȷ��").setMessage("ȷ����")
				.setPositiveButton("��", null).setNegativeButton("��", null)
				.show();

		super.onResume();
	}
}
