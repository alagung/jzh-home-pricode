package com.raral.childdev.util;

import android.util.Log;


public class MyLog {
	public static String LOG_LEVEL = "VERBOSE";
    public static enum Level {   
    	ERROR, WARN, INFO, DEBUG, VERBOSE   
    }; 
    
    private static boolean isLog(Level l){
    	String levelStr = "INFO";
    	for(Level ll : Level.values()){
    		if(ll.toString().equals(LOG_LEVEL)){
    			levelStr = LOG_LEVEL;
    			break;
    		}
    	}
    	return Level.valueOf(levelStr).ordinal() >= l.ordinal() == true;
    }

	public static void v(String LOG_TAG, String msg) {
		if(isLog(Level.VERBOSE))
		{
			Log.v(LOG_TAG, msg);
		}
	}

	public static void d(String LOG_TAG, String msg) {
		if(isLog(Level.DEBUG))
			Log.d(LOG_TAG, msg);
	}

	public static void i(String LOG_TAG, String msg) {
		if(isLog(Level.INFO))
			Log.i(LOG_TAG, msg);
	}

	public static void w(String LOG_TAG, String msg) {
		if(isLog(Level.WARN))
			Log.w(LOG_TAG, msg);
	}

	public static void e(String LOG_TAG, String msg) {
		if(isLog(Level.ERROR))
			Log.e(LOG_TAG, msg);
	}
}
