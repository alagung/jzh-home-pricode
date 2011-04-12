package com.childdev.airsearch;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCIntervalAction;
import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.events.CCTouchDispatcher;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.protocols.CCTouchDelegateProtocol;
import org.cocos2d.types.CGPoint;
import org.cocos2d.utils.CCFormatter;

import com.childdev.ChildDevMain;
import com.childdev.base.ChildDevBaseTest;
import com.childdev.base.NodeEventSprite;

import android.os.Handler;
import android.os.Looper;
import android.test.UiThreadTest;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

public class PlaneSprite extends NodeEventSprite{

	private ChildDevBaseTest _test = null;
	private float x;
	private float y;
	private float xt;
	private float yt;
	private String name;
	
	@Override
	public boolean ccTouchesBegan(MotionEvent event) {
		_test.getBundle().postToUI(new Runnable() {			
			@Override
			public void run() {
				Toast.makeText(CCDirector.sharedDirector().getActivity(), "ѡ���˷ɻ� "+name, 30).show();
			}
		});
		_test.score += 1;
		stopAllActions();
		removeAllChildren(true);
		getParent().removeChild(this, true);
		return true;
	}

	@Override
	public void onEnter() {
		super.onEnter();
		setPosition(CGPoint.make(x, y));
		CCIntervalAction act = CCMoveBy.action(10, CGPoint.make(xt, yt));
		runAction(CCRepeatForever.action(CCSequence.actions(act, act.reverse())));
		
        CCAnimation animation = CCAnimation.animation("dance", 0.2f);
        for (int i = 5; i < 7; i++) {
            animation.addFrame(CCFormatter.format("grossini_dance_%02d.png", i));
        }
        
        runAction(CCRepeatForever.action(CCAnimate.action(animation)));
	}

	public PlaneSprite(ChildDevBaseTest test, String filename, float x, float y, float xt, float yt, String name) {
		super(filename);
		this.x = x;
		this.y = y;
		this.xt = xt;
		this.yt = yt;	
		this.name = name;
		this._test = test;
	}

	@Override
	public boolean ccTouchesCancelled(MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean ccTouchesEnded(MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean ccTouchesMoved(MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

}
