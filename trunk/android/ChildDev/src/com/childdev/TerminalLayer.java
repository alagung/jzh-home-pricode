package com.childdev;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGSize;
import org.cocos2d.utils.CCFormatter;

import android.view.MotionEvent;

import com.childdev.base.ChildDevBaseLayer;
import com.childdev.base.ChildDevBaseTest;

public class TerminalLayer extends ChildDevBaseLayer {
	TestBundle _bundle;

	public TerminalLayer(TestBundle bundle) {
		super(null);
		isTouchEnabled_ = true;
		_bundle = bundle;

		ChildDevBaseTest tests[] = _bundle.getTests();
		
		CGSize s = CCDirector.sharedDirector().winSize();
		float yy = s.height - 20;
		for (int i=0;i<tests.length;++i)
		{
			ChildDevBaseTest t = tests[i];
			CCLabel line = CCLabel.makeLabel(CCFormatter.format("%s: %4d", t
					.getName(), t.getScore()), "DroidSans", 16);
			line.setAnchorPoint(0.5f, 1.0f);
	        line.setPosition(s.width / 2, yy);
	        addChild(line);
	        yy = line.getPosition().y - line.getContentSizeRef().height - 20;	        
		}
		CCLabel score = CCLabel.makeLabel(CCFormatter.format("×ÜµÃ·Ö: %d", bundle
				.getTotalScore()), "DroidSans", 36);
		score.setAnchorPoint(0.5f, 1.0f);
		score.setPosition(s.width / 2, yy);
		addChild(score);
		yy = score.getPosition().y - score.getContentSizeRef().height - 20;	
		
		CCAnimation animation = CCAnimation.animation("report", 0.2f);
        for (int i = 1; i < 10; i++) {
            animation.addFrame(CCFormatter.format("grossini_dance_%02d.png", i));
        }
        
        CCSprite report = new CCSprite("grossini.png");
        report.setAnchorPoint(0.5f, 1.0f);
        report.setPosition(s.width / 2, yy);
		addSimpleAct(1.0f, report, CCRepeatForever.action(CCAnimate.action(animation)));
	}

	@Override
	public boolean ccTouchesEnded(MotionEvent event) {
		_bundle.postToUI(new Runnable() {			
			@Override
			public void run() {
				CCDirector.sharedDirector().getActivity().finish();
			}
		});
		
		return true;
	}
}
