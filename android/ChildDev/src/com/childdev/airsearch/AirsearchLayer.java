package com.childdev.airsearch;

import org.cocos2d.actions.UpdateCallback;
import org.cocos2d.actions.base.CCFiniteTimeAction;
import org.cocos2d.actions.interval.CCFadeIn;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;

import com.childdev.base.ChildDevBaseTest;
import com.childdev.base.NodeEventLayer;

public class AirsearchLayer extends NodeEventLayer implements UpdateCallback {
	CCLabel hint;
	CCLabel score;
	CCLabel time;
	
	float tEl = 30.0f;
	
	public AirsearchLayer(ChildDevBaseTest test) {
		super(test);
		CGSize s = CCDirector.sharedDirector().winSize();
		
        hint = CCLabel.makeLabel("现在有三架飞机，赶快把它找出来吧", "DroidSans", 16);
        hint.setAnchorPoint(0.5f, 1.0f);
        hint.setPosition(CGPoint.make(s.width / 2, s.height - 60));
        hint.setOpacity(0);
        addChild(hint);

        addChild(new PlaneSprite(getTest(), "grossini.png", 20, 300, 60, 0, "1"));
        addChild(new PlaneSprite(getTest(), "grossini.png", 80, 300, 60, 0, "2"));
        addChild(new PlaneSprite(getTest(), "grossini.png", 160, 300, 60, 0, "3"));
        
        score = CCLabel.makeLabel("得分: 0", "DroidSans", 24);
        score.setAnchorPoint(1.0f, 1.0f);
        score.setPosition(CGPoint.make(s.width - 20, s.height - 20));
        addChild(score);
        
        time = CCLabel.makeLabel("时间剩余: 30秒", "DroidSans", 24);
        time.setAnchorPoint(0.0f, 1.0f);
        time.setPosition(CGPoint.make(20, s.height - 20));
        addChild(time);
	}

	@Override
	public void onExit() {
		// TODO Auto-generated method stub
		super.onExit();
		unschedule(this);
	}

	@Override
	public void onEnter() {
		super.onEnter();
		schedule(this);
		hint.runAction(CCSequence.actions(CCFadeIn.action(2), 
				CCFiniteTimeAction.action(5),
				CCFadeOut.action(2)));

		tEl = 30.0f;
	}

	@Override
	public void update(float d) {
		score.setString("得分: " + getTest().score);	
		
		tEl -= d;
		if (tEl <= 0)
			tEl = 0;
		time.setString("时间剩余: "+(int)tEl+"秒");
		
		if (getTest().score >= 3) {
			if (tEl > 1.0f)
				tEl = 1.0f;
		}
		
		if (tEl <= 0.0f) {
			getTest().getBundle().finishCurrentScene();
		}
	}
}
