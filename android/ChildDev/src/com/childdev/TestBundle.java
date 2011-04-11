package com.childdev;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.types.CGPoint;

import android.widget.Toast;

import com.childdev.airsearch.AirsearchLayer;
import com.childdev.airsearch.AirsearchTest;
import com.childdev.base.ChildDevBaseTest;

public class TestBundle {
	private ChildDevBaseTest _tests[] = null;
	private int _tIndex = 0;
	private ChildDevBaseTest _t = null;

	private void setCurrent(int i) {
		_t = _tests[_tIndex = i];
	}

	private void runScene(CCScene scene) {
		scene.setAnchorPoint(CGPoint.make(0, 0));
		if (CCDirector.sharedDirector().getRunningScene() == null)
			CCDirector.sharedDirector().runWithScene(scene);
		else
			CCDirector.sharedDirector().replaceScene(scene);
	}

	public TestBundle() {
		super();
		_tests = new ChildDevBaseTest[] {
				new AirsearchTest(this),
		};
	}

	public void start() {
		setCurrent(0);
		_t.load();
		_t.resetScene();
		runScene(_t.nextScene());
	}

	public int getCurrentScore() {
		if (_t == null)
			return 0;
		return _t.score;
	}

	public void setCurrentScore(int s) {
		if (_t != null)
			_t.score = s;
	}

	public int getTotalScore() {
		int s = 0;
		for (int i = 0; i < _tests.length; ++i) {
			if (_tests[i].score > 0)
				s += _tests[i].score;
		}
		return s;
	}

	public void finishCurrentTest() {
		if (_t != null)
			_t.onTestFinish();
		if (_tIndex < _tests.length - 1) {
			setCurrent(_tIndex + 1);
			_t.load();
			_t.resetScene();
			runScene(_t.nextScene());
		} else {
			// All test done
			CCScene object;		
			object = CCScene.node();
			object.addChild(new TerminalLayer(this));
			runScene(object);
		}
	}

	public void finishCurrentScene() {
		if (_t == null || !_t.hasNextScene())
			finishCurrentTest();
		else {
			_t.onSceneFinish();
			runScene(_t.nextScene());
		}
	}

	public boolean isRunning(ChildDevBaseTest test) {
		return test == _t;
	}

	public final boolean postToUI(Runnable r) {
		ChildDevMain act = (ChildDevMain) CCDirector.sharedDirector()
				.getActivity();
		return act.getHandler().post(r);
	}
}
