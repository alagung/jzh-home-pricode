package com.raral.childdev.base;

import java.util.ArrayList;

import org.cocos2d.layers.CCScene;

abstract public class ChildDevBaseChapter {
	private int mNextScene = 0;
	public ArrayList<CCScene> mScenes = null;
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

	abstract public ArrayList<CCScene> loadScenes();

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
		return mScenes.get(mNextScene++);
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
