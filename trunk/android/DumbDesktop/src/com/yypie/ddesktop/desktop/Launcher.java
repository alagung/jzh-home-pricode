package com.yypie.ddesktop.desktop;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.yypie.ddesktop.R;
import com.yypie.ddesktop.service.ServiceProvider;

public class Launcher extends ListActivity {
	private static final String[][] gItems = {
			{ "便携式Wifi热点设置", "com.android.settings",
					"com.android.settings.wifi.WifiApSettings" },
			{ "紧急呼叫", "com.android.phone", "com.android.phone.EmergencyDialer" },
			// TODO: test
			{ "测试-打电话", "com.android.contacts",
					"com.android.contacts.DialtactsActivity" },
			{ "密码管理", "com.yypie.ddesktop", "com.yypie.ddesktop.desktop.Manager" },
			{ "完全退出", null, null } };

	private static final int lastExit = (gItems.length - 1);

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initListAdapter();
		ServiceProvider.StartMe(this);
	}

	@Override
	protected void onDestroy() {
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
		if (position == lastExit) {
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
							ServiceProvider.StopMe(Launcher.this);
							android.os.Process.killProcess(android.os.Process
									.myPid());
						}
					}).show();
		} else {
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			intent.setClassName(gItems[position][1], gItems[position][2]);
			startActivityForResult(intent, 1);
		}

		super.onListItemClick(l, v, position, id);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			// Block back
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
