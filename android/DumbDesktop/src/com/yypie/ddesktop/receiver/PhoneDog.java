package com.yypie.ddesktop.receiver;

import java.lang.reflect.Method;
import java.util.HashSet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.yypie.ddesktop.service.ServiceProvider;

public class PhoneDog extends BroadcastReceiver {
    public static final int CALL_STATE_UNKNOWN = -1;
	
	ServiceProvider provider;

	TelephonyManager mTelephonyManager = null;
	ITelephony mITelephony = null;
	PhoneStateListener mListener = null;

	HashSet<String> allows = null;

	String outNumber = "";

	public PhoneDog(ServiceProvider provider, TelephonyManager tm) {
		super();
		this.provider = provider;
		this.mTelephonyManager = tm;

		// Init allow list
		allows = new HashSet<String>();
		allows.add("110");
		allows.add("119");
		allows.add("120");
		allows.add("122");
		allows.add("911");
		allows.add("112");

		// TODO: test
		allows.add("13310023525");
		allows.add("13918114849");
		allows.add("13062651555");

		// Init ITelephony
		if (tm != null) {
			Class<TelephonyManager> c = TelephonyManager.class;
			Method getITelephonyMethod = null;
			try {
				getITelephonyMethod = c.getDeclaredMethod("getITelephony",
						(Class[]) null);
				getITelephonyMethod.setAccessible(true);
				mITelephony = (ITelephony) getITelephonyMethod.invoke(tm,
						(Object[]) null);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String number = null;
		if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
			number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
			if (number == null)
				number = "";
			
			Log.e(ServiceProvider.TAG, intent.getAction() + " Ph: " + number);

			newOutGoing(number);
			endCall(number);
		} else {
			// STATE Change
			number = intent
					.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
			if (number == null)
				number = "";
			
			int state = CALL_STATE_UNKNOWN;
			try {
				state = mITelephony.getCallState();
			} catch (RemoteException e) {
				// Ignore
			}
			
			Log.e(ServiceProvider.TAG, intent.getAction() + " State: " + state
					+ " Ph: " + number);
			
			handleCallState(state, number);
		}
	}

	// Public interface
	public void startDog() {
		IntentFilter filter = new IntentFilter();
		filter.setPriority(1000);
		filter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
		filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);

		provider.registerReceiver(this, filter);

		mListener = new PhoneStateListener() {
			@Override
			public void onCallStateChanged(int state, String incomingNumber) {
				Log.e(ServiceProvider.TAG, "LISTENER State: " + state + " Ph: "
						+ incomingNumber);
				super.onCallStateChanged(state, incomingNumber);
				handleCallState(state, incomingNumber);
			}
		};

		mTelephonyManager.listen(mListener,
				PhoneStateListener.LISTEN_CALL_STATE);
	}

	public void stopDog() {
		mTelephonyManager.listen(mListener, PhoneStateListener.LISTEN_NONE);
		provider.unregisterReceiver(this);
	}

	// Internal helpers
	
	public void handleCallState(int state, String incomingNumber) {
		String number = getPhoneNumber(incomingNumber);
		Log.e(ServiceProvider.TAG, "handleCallState State: " + state + " Ph: "
				+ number);
		
		switch (state) {
		case CALL_STATE_UNKNOWN:
			// We failed to get state
			// We just call end call for safe
			endCall(number);
			break;
		case TelephonyManager.CALL_STATE_IDLE:
			// Clear saved outgoing number
			clearOutGoing();
			break;
		case TelephonyManager.CALL_STATE_OFFHOOK:
			// Talking, stop!
			endCall(number);
			break;
		case TelephonyManager.CALL_STATE_RINGING:
			// Ringing, stop!
			endCall(number);
			break;
		}
	}
	
	void endCall(String number) {
		if (mITelephony == null)
			return;

		// We don't block emergency call
		if (isNumberBlocked(number)) {
			Log.e(ServiceProvider.TAG, "DOG end call");
			try {
				mITelephony.endCall();
			} catch (RemoteException e) {				
			}
		}
	}

	void newOutGoing(String outNumber) {
		this.outNumber = outNumber;
	}

	void clearOutGoing() {
		this.outNumber = "";
	}

	String getPhoneNumber(String incomingNumber) {
		// Incoming empty means out going call. Use saved out number
		if ((incomingNumber == null || incomingNumber.length() == 0))
			return outNumber;
		else
			return incomingNumber;
	}

	boolean isNumberBlocked(String number) {
		// No number, who are you?
		if (number == null || number.length() == 0)
			return true;

		if (allows.contains(number))
			return false;
		// Maybe we have 021
		if (number.length() > 6 && allows.contains(number.substring(3)))
			return false;

		return true;
	}

}
