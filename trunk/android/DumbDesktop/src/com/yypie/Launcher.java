package com.yypie;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.app.ListActivity;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.DialogInterface;
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
			{ "便携式Wifi热点设置", "com.android.settings",
					"com.android.settings.wifi.WifiApSettings" },
			{ "紧急呼叫", "com.android.phone", "com.android.phone.EmergencyDialer" },
			{ "密码管理", "com.yypie", "com.yypie.Manager" } };
	private WatchDog mMyService;
	private ServiceConnection mConn = new ServiceConnection() {
		public void onServiceConnected(ComponentName name, IBinder service) {
			mMyService = ((WatchDog.WatchDogBinder) service).getService();
		}

		public void onServiceDisconnected(ComponentName name) {
			mMyService = null;
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initListAdapter();
		// this.startService(new Intent(this, WatchDog.class));
		this.bindService(new Intent(this, WatchDog.class), mConn,
				BIND_AUTO_CREATE);
	}

	@Override
	protected void onDestroy() {
		this.unbindService(mConn);
		super.onDestroy();
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
		// ActivityManager am = (ActivityManager)
		// getSystemService(ACTIVITY_SERVICE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		clearAll();
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(this).setTitle("Message Box").setMessage(
					"Sure to exit?").setNegativeButton("No",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						}
					}).setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							finish();
							Launcher.this.stopService(new Intent(Launcher.this,
									WatchDog.class));
							android.os.Process.killProcess(android.os.Process
									.myPid());
						}
					}).show();
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
		super.onResume();
	}
}
