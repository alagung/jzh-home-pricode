package com.raral.childdev.airsearch;

import org.cocos2d.actions.base.CCFiniteTimeAction;
import org.cocos2d.actions.interval.CCFadeIn;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;

import com.raral.childdev.ShowChapters;
import com.raral.childdev.base.NodeEventLayer;

public class AirsearchScene extends NodeEventLayer {
	CCLabel score;
	CCLabel time;
	
	float remain = 15.0f;
	
	public AirsearchScene() {
		super();
		CGSize s = CCDirector.sharedDirector().winSize();
		
        CCLabel hint = CCLabel.makeLabel("现在有三架飞机，赶快把它找出来吧", "DroidSans", 16);
        hint.setAnchorPoint(0.5f, 1.0f);
        hint.setPosition(CGPoint.make(s.width / 2, s.height - 60));
        hint.setOpacity(0);
        addSimpleAct(hint, 0.0f, CCSequence.actions(
        		CCFadeIn.action(1), CCFiniteTimeAction.action(3), CCFadeOut.action(2)));

        addSimpleAct(new PlaneSprite("grossini.png", 20, s.height/2, 60, 0, "1"), 1.0f, null);
        addSimpleAct(new PlaneSprite("grossini.png", 80, s.height/2, 60, 0, "2"), 1.0f, null);
        addSimpleAct(new PlaneSprite("grossini.png", 160, s.height/2, 60, 0, "3"), 1.0f, null);
        
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
		int curScore = ShowChapters.getInstance().getCurrentScore();
		score.setString("得分: " + curScore);	
		
		remain -= d;
		if (remain <= 0)
			remain = 0;
		time.setString("时间剩余: "+(int)remain+"秒");
		
		if (curScore >= 3) {
			if (remain > 1.0f)
				remain = 1.0f;
		}
		
		if (remain <= 0.0f) {
			ShowChapters.getInstance().finishCurrentScene();
		}
	}
}
