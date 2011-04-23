package com.raral.childdev.util;

import org.cocos2d.nodes.CCDirector;
import org.cocos2d.types.CGSize;

import com.raral.childdev.Config;

public class Tools {
	private static final String LOG_TAG = "Tools";
	private static CGSize mSize = null;
	private static float mSizeScale = 0.0f;
	
	public static int getFullInteger(float number) {
		return (int)Math.round(number+0.5); 
	}
	
	public static String getString(int resId){
		return CCDirector.sharedDirector().getActivity().getString(resId);
	}
	
	public static float getScreenWidth(){
		getSize();
		return mSize.width;
	}

	public static float getScreenHeight(){
		getSize();
		return mSize.height;
	}

	/**
	 * get real screen scale to DEFAULT_SCREEN
	 * @return
	 */
	public static float getSizeScale() {
		if(!(mSizeScale > 0.0f)) {
			getSize();
			float wScale = mSize.width / Config.DEFAULT_SCREEN_WIDTH;
			float hScale = mSize.height / Config.DEFAULT_SCREEN_HEIGHT;
			mSizeScale = (wScale > hScale) ? wScale : hScale;
			MyLog.v(LOG_TAG, String.format("getSizeScale, scale:%f, w:%f, h:%f", mSizeScale,  mSize.width, mSize.height));
		}
		return mSizeScale;
	}
	
	private static void getSize() {
		if(mSize == null) {
			mSize = CCDirector.sharedDirector().winSize();
		}
	}
	
}
