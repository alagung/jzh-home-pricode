package com.childdev.airsearch;

import org.cocos2d.actions.base.CCFiniteTimeAction;
import org.cocos2d.actions.interval.CCFadeIn;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.events.CCTouchDispatcher;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;

import android.view.MotionEvent;

import com.childdev.base.NodeEventLayer;

public class AirsearchLayer extends NodeEventLayer {
	@Override
	public boolean ccTouchesBeganPro(MotionEvent event, CCNode item) {
    	return CCTouchDispatcher.kEventIgnored;
    }

    @Override
	public boolean ccTouchesEndedPro(MotionEvent event, CCNode item) {
    	return CCTouchDispatcher.kEventIgnored;
    }

    @Override
	public boolean ccTouchesCancelledPro(MotionEvent event, CCNode item) {
    	return CCTouchDispatcher.kEventIgnored;
    }

    @Override
	public boolean ccTouchesMovedPro(MotionEvent event, CCNode item) {
    	return CCTouchDispatcher.kEventIgnored;
    }

    CCLabel hint;
	public AirsearchLayer() {
		super();
		CGSize s = CCDirector.sharedDirector().winSize();

		
        hint = CCLabel.makeLabel("现在有三架飞机，赶快把它找出来吧", "DroidSans", 16);
        hint.setAnchorPoint(0.5f, 1.0f);
        hint.setPosition(CGPoint.make(s.width / 2, s.height - 60));
        hint.setOpacity(0);
        addChild(hint);

        addChild(new PlaneSprite("grossini.png", 20, 300, 60, 0, "1"));
        addChild(new PlaneSprite("grossini.png", 60, 300, 60, 0, "2"));
        addChild(new PlaneSprite("grossini.png", 100, 300, 60, 0, "3"));
	}

	@Override
	public void onEnter() {
		super.onEnter();
		hint.runAction(CCSequence.actions(CCFadeIn.action(2), 
				CCFiniteTimeAction.action(5),
				CCFadeOut.action(2)));
	}
}
