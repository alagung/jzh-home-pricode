package com.yypie;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class PhoneStatReceiver extends BroadcastReceiver {

	TelephonyManager tm = null;
	ITelephony iTelephony = null;
	
	PhoneStateListener listener = new PhoneStateListener() {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			// state ��ǰ״̬ incomingNumber,ò��û��ȥ���API
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
				System.out.println("�Ҷ�");
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				System.out.println("����");
				endCall(null);
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				System.out.println("����:�������" + incomingNumber);
				endCall(null);
				// ����������
				break;
			}
		}

	};

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
			// �����ȥ�磨������
			System.out.println("����");
			endCall(context);
		} else {
			System.out.println("����");
			getTM(context)
			.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		}
	}
	
	TelephonyManager getTM(Context context) {
		if (tm == null) {
			tm = (TelephonyManager) context
					.getSystemService(Service.TELEPHONY_SERVICE);
		}
		return tm;
	}
	
	ITelephony getIT(Context context) {
		if (iTelephony != null)
			return iTelephony;
		Class<TelephonyManager> c = TelephonyManager.class;
		Method getITelephonyMethod = null;
		try {
			getITelephonyMethod = c.getDeclaredMethod("getITelephony",
					(Class[]) null);
			getITelephonyMethod.setAccessible(true);
			iTelephony = (ITelephony) getITelephonyMethod.invoke(
					getTM(context), (Object[]) null);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iTelephony;
	}
	
	void endCall(Context context) {
		try {
			getIT(context).endCall();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}