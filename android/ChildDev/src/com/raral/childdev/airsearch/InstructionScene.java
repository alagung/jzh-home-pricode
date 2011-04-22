package com.raral.childdev.airsearch;

import org.cocos2d.actions.base.CCFiniteTimeAction;
import org.cocos2d.actions.interval.CCFadeIn;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;

import com.raral.childdev.ShowChapters;
import com.raral.childdev.base.ChildDevBaseLayer;
import com.raral.childdev.util.Tools;


public class InstructionScene extends ChildDevBaseLayer {
	CCSprite sample = null;
	float yy = 0;
	private void addHint( float sTime, String text)
	{
		if (yy == 0)
			yy = Tools.getScreenHeight() - 60;
		
		CCLabel hint = CCLabel.makeLabel(text, "DroidSans", 16);
		hint.setAnchorPoint(0.5f, 1.0f);
		hint.setPosition(CGPoint.make(Tools.getScreenWidth() / 2, yy));
        hint.setOpacity(0);
        addSimpleAct(hint, sTime, CCSequence.actions(
        		CCFadeIn.action(1), CCFiniteTimeAction.action(3), CCFadeOut.action(2)));
        yy = hint.getPositionRef().y - hint.getContentSizeRef().height - 20;
	}
	public InstructionScene() {
		super();
		isTouchEnabled_ = true;
		
		addHint(0.0f, "小朋友，天空中有许许多多的飞机，就像这样");
		addHint(4.0f, "接下来你要做的就是在天空中找出飞机哦！");
		addHint(4.0f, "先来试一下吧！");

		sample = CCSprite.sprite("grossini.png");
		sample.setPosition(Tools.getScreenWidth() / 2, Tools.getScreenHeight() / 2);
		sample.setOpacity(0);
		addSimpleAct(sample, 2.0f, CCFadeIn.action(1.5f));
	}
	
	@Override
	public void onEnter() {
		super.onEnter();		
	}
	
	@Override
	public void update(float d) {
		super.update(d);
		if (getElapsedTime() >= 8.0f)
			ShowChapters.getInstance().finishCurrentScene();
	}
}	