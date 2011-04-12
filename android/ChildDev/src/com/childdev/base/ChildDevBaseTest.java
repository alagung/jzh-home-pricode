package com.childdev.base;

import java.util.ArrayList;

import org.cocos2d.layers.CCScene;

import com.childdev.TestBundle;

abstract public class ChildDevBaseTest {
	protected TestBundle bundle = null;
	private int _nextI = 0;
	public ArrayList<CCScene> _scenes = null;
	public int score = 0;
	public boolean tested = false;
	
	public boolean isTested() {
		return tested;
	}

	public ChildDevBaseTest(TestBundle bundle) {
		super();
		this.bundle = bundle;
	}

	public TestBundle getBundle() {
		return bundle;
	}

	public void setBundle(TestBundle bundle) {
		this.bundle = bundle;
	}

	public int getScore() {
		if (score < 0)
			return 0;
		else
			return score;
	}

	abstract public String getName();

	abstract public String getDesc();

	abstract public ArrayList<CCScene> loadScenes();

	public void load() {
		_scenes = loadScenes();
		tested = true;
	}

	public boolean hasNextScene() {
		if (_nextI < _scenes.size()) {
			return true;
		} else {
			return false;
		}
	}

	public CCScene nextScene() {
		return _scenes.get(_nextI++);
	}

	public void resetScene() {
		_nextI = 0;
	}

	public void onSceneFinish() {
		// Override to impl
	}

	public void onTestFinish() {
		// Override to impl
	}
}
