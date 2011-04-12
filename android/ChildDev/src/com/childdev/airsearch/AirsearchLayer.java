package com.childdev.airsearch;

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

public class AirsearchLayer extends NodeEventLayer {
	CCLabel score;
	CCLabel time;
	
	float remain = 15.0f;
	
	public AirsearchLayer(ChildDevBaseTest test) {
		super(test);
		CGSize s = CCDirector.sharedDirector().winSize();
		
        CCLabel hint = CCLabel.makeLabel("现在有三架飞机，赶快把它找出来吧", "DroidSans", 16);
        hint.setAnchorPoint(0.5f, 1.0f);
        hint.setPosition(CGPoint.make(s.width / 2, s.height - 60));
        hint.setOpacity(0);
        addSimpleAct(0.0f, hint, CCSequence.actions(
        		CCFadeIn.action(1), CCFiniteTimeAction.action(3), CCFadeOut.action(2)));

        addSimpleAct(1.0f, new PlaneSprite(getTest(), "grossini.png", 20, 300, 60, 0, "1"), null);
        addSimpleAct(1.0f, new PlaneSprite(getTest(), "grossini.png", 80, 300, 60, 0, "2"), null);
        addSimpleAct(1.0f, new PlaneSprite(getTest(), "grossini.png", 160, 300, 60, 0, "3"), null);
        
        score = CCLabel.makeLabel("得分: 0", "DroidSans", 24);
        score.setAnchorPoint(1.0f, 1.0f);
        score.setPosition(CGPoint.make(s.width - 20, s.height - 20));
        addChild(score);
        
        time = CCLabel.makeLabel("时间剩余: 15秒", "DroidSans", 24);
        time.setAnchorPoint(0.0f, 1.0f);
        time.setPosition(CGPoint.make(20, s.height - 20));
        addChild(time);
	}

	@Override
	public void onEnter() {
		super.onEnter();
		remain = 15.0f;
	}
	
	@Override
	public void update(float d) {
		super.update(d);
		score.setString("得分: " + getTest().score);	
		
		remain -= d;
		if (remain <= 0)
			remain = 0;
		time.setString("时间剩余: "+(int)remain+"秒");
		
		if (getTest().score >= 3) {
			if (remain > 1.0f)
				remain = 1.0f;
		}
		
		if (remain <= 0.0f) {
			getTest().getBundle().finishCurrentScene();
		}
	}
}
