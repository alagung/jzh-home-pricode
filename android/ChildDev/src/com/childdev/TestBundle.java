package com.childdev;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.types.CGPoint;

import android.widget.Toast;

import com.childdev.airsearch.AirsearchLayer;
import com.childdev.base.ChildDevBaseTest;

public class TestBundle {
	private ChildDevBaseTest tests[] = null;
	private int _tIndex = 0;
	private ChildDevBaseTest _t = null;

	private void setCurrent(int i)
	{
		_t = tests[_tIndex = i];
	}
	
	private void runScene(CCScene scene) {
		scene.setAnchorPoint(CGPoint.make(0, 0));
		CCDirector.sharedDirector().runWithScene(scene);
	}
	
	public TestBundle() {
		super();	
		tests = new ChildDevBaseTest[]{};		
	}
	
	public void start() {
		setCurrent(0);
		_t.init();
		_t.resetScene();
		runScene(_t.nextScene());
	}
	
	public void finishCurrentTest() {
		if (_t != null)
			_t.onTestFinish();
		if (_tIndex < tests.length - 1)
			setCurrent(_tIndex+1);
		_t.init();
		_t.resetScene();
		runScene(_t.nextScene());
	}
	
	public void finishCurrentScene() {
		if (_t == null || !_t.hasNextScene())
			finishCurrentTest();
		else {
			_t.onSceneFinish();
			runScene(_t.nextScene());
		}
	}
	
	public final boolean postToUI(Runnable r) {
		ChildDevMain act = (ChildDevMain)CCDirector.sharedDirector().getActivity();
		return act.getHandler().post(r);
	}
}
