package com.raral.childdev.base;

import java.util.ArrayList;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;

import com.raral.childdev.util.MyLog;

abstract public class ChildDevBaseChapter {
	private static final String LOG_TAG = "ChildDevBaseChapter";
	private int mNextScene = 0;
	public ArrayList<Class<?>> mScenes = null;
	public int mScore = 0;
	public boolean mIsLoaded = false;
	
	public boolean isLoaded() {
		return mIsLoaded;
	}

	public ChildDevBaseChapter() {
		super();
	}

	public int getScore() {
		if (mScore < 0)
			return 0;
		else
			return mScore;
	}

	abstract public String getName();

	abstract public String getDesc();

	abstract public ArrayList<Class<?>> loadScenes();

	public void load() {
		mScenes = loadScenes();
		mIsLoaded = true;
	}

	public boolean hasNextScene() {
		if (mNextScene < mScenes.size()) {
			return true;
		} else {
			return false;
		}
	}

	public CCScene nextScene() {
		MyLog.v(LOG_TAG, Thread.currentThread().getStackTrace()[2].getMethodName());
		try {
            Class<?> c = mScenes.get(mNextScene++);
            CCScene s = CCScene.node();
    		s.addChild((ChildDevBaseLayer) c.newInstance());
    		return s;
        } catch (Exception e) {
            return null;
        }
	}

	public void resetScene() {
		mNextScene = 0;
	}

	public void onSceneFinish() {
		// Override to impl
	}

	public void onTestFinish() {
		// Override to impl
	}
}
