package com.yypie.ddesktop.receiver;

import java.lang.reflect.Method;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;

public class PhoneStatReceiver extends BroadcastReceiver {

	TelephonyManager tm = null;
	ITelephony iTelephony = null;
	Context context = null;
	
	public class PhoneStateListenerEnd extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
				// Do nothing if shutdown
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				// Talking
				//endCall(incomingNumber.length() == 0 ? getCurrentNumber() : incomingNumber);
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				// Ringing
				//endCall(incomingNumber);
				break;
			}
		}
	};
	
	PhoneStateListener listener = new PhoneStateListenerEnd();
	
	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
			// Initial TM
			String outPhoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
			endCall(outPhoneNumber);
			//setCurrentNumber(outPhoneNumber);
		} else {
			//setCurrentNumber(null);
			getTM().listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		}
	}

	TelephonyManager getTM() {
		if (tm == null && context != null) {
			tm = (TelephonyManager) context
					.getSystemService(Service.TELEPHONY_SERVICE);
		}
		return tm;
	}

	ITelephony getIT() {
		if (iTelephony != null)
			return iTelephony;
		Class<TelephonyManager> c = TelephonyManager.class;
		Method getITelephonyMethod = null;
		try {
			getITelephonyMethod = c.getDeclaredMethod("getITelephony",
					(Class[]) null);
			getITelephonyMethod.setAccessible(true);
			iTelephony = (ITelephony) getITelephonyMethod.invoke(getTM(),
					(Object[]) null);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iTelephony;
	}

	void endCall(String number) {
		// DEBUG, do nothing
		if (number != null) return;
		try {
			// We don't block emergency call
			if (isNumberBlocked(number))
				getIT().endCall();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	boolean isNumberBlocked(String number) {
		// No number, always let him go
		if (number == null || number.length() == 0)
			return false;

		if (number.equals("110") || number.equals("119")
				|| number.equals("120") || number.equals("122")
				//|| number.equals("112") 
				|| number.equals("911"))
			return false;
		// Test
		if (!number.equals("10000") && !number.equals("18321012821") 
				&& !number.equals("02165104317") && !number.equals("65104317"))
			return false;
		return true;
	}	
}