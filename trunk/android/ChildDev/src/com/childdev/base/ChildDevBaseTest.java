package com.childdev.base;

import org.cocos2d.layers.CCScene;

import com.childdev.TestBundle;

abstract public class ChildDevBaseTest {
	protected TestBundle bundle = null;
	
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
	abstract public void init();
	abstract public CCScene nextScene();
	abstract public boolean hasNextScene();
	abstract public CCScene resetScene();
	
	public void onSceneFinish() {
		// Override to impl
	}
	
	public void onTestFinish() {
		// Override to impl
	}
}
