package com.yypie.ddesktop.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yypie.ddesktop.service.ServiceProvider;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		ServiceProvider.StartMe(context);
	}
}
