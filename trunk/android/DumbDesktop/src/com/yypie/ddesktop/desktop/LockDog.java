package com.yypie.ddesktop.desktop;

import com.yypie.ddesktop.R;
import com.yypie.ddesktop.service.ServiceProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.KeyEvent;

public class LockDog extends Activity {
	public final static String arg1 = "arg1";

	private ServiceProvider.ServiceProviderConn mConn = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password);
        mConn = ServiceProvider.bindMe(this, mConn);
    }
    
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(this).setTitle("Message Box").setMessage(
					"Press Yes to unlock?").setNegativeButton("No",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						}
					}).setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							if (mConn == null) // Bind haven't done. Try again.
								return;
							
							try {
								mConn.mService.registCred(getIntent().getStringExtra(arg1));
							} catch (RemoteException e) {
								// Ignore
							}
							finish();
						}
					}).show();
			return true;
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		ServiceProvider.unBindMe(this, mConn);
		super.onDestroy();
	}
}
