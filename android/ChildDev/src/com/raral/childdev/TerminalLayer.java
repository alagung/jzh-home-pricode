package com.raral.childdev;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGSize;
import org.cocos2d.utils.CCFormatter;

import android.view.MotionEvent;

import com.raral.childdev.base.ChildDevBaseLayer;
import com.raral.childdev.base.ChildDevBaseChapter;

public class TerminalLayer extends ChildDevBaseLayer {

	public TerminalLayer() {
		super();
		isTouchEnabled_ = true;

		ChildDevBaseChapter tests[] = ShowChapters.getInstance().getChapters();
		
		CGSize s = CCDirector.sharedDirector().winSize();
		float yy = s.height - 20;
		for (int i=0;i<tests.length;++i)
		{
			ChildDevBaseChapter t = tests[i];
			CCLabel line = CCLabel.makeLabel(CCFormatter.format("%s: %4d", t
					.getName(), t.getScore()), "DroidSans", 16);
			line.setAnchorPoint(0.5f, 1.0f);
	        line.setPosition(s.width / 2, yy);
	        addChild(line);
	        yy = line.getPosition().y - line.getContentSizeRef().height - 20;	        
		}
		CCLabel score = CCLabel.makeLabel(CCFormatter.format("×ÜµÃ·Ö: %d", ShowChapters.getInstance().getTotalScore()), "DroidSans", 36);
		score.setAnchorPoint(0.5f, 1.0f);
		score.setPosition(s.width / 2, yy);
		addChild(score);
		yy = score.getPosition().y - score.getContentSizeRef().height - 20;	
//		
//		CCAnimation animation = CCAnimation.animation("report", 0.2f);
//        for (int i = 1; i < 10; i++) {
//            animation.addFrame(CCFormatter.format("grossini_dance_%02d.png", i));
//        }
//        
//        CCSprite report = new CCSprite("grossini.png");
//        report.setAnchorPoint(0.5f, 1.0f);
//        report.setPosition(s.width / 2, yy);
//		addSimpleAct(report, 1.0f, CCRepeatForever.action(CCAnimate.action(animation)));
	}

	@Override
	public boolean ccTouchesEnded(MotionEvent event) {
		ShowChapters.getInstance().postToUI(new Runnable() {			
			@Override
			public void run() {
				CCDirector.sharedDirector().getActivity().finish();
			}
		});
		
		return true;
	}
}
