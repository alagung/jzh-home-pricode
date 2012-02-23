package com.yypie;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;

public class Manager extends Activity {
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
        setContentView(R.layout.manager);
        this.bindService(new Intent(this, WatchDog.class), mConn,
				0);
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
							mMyService.registCred(getIntent().getStringExtra(WatchDog.extraApp));
							finish();
						}
					}).show();
			return true;
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		this.unbindService(mConn);
		super.onDestroy();
	}
	
}
