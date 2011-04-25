package com.raral.childdev.animalmemory;

import org.cocos2d.actions.base.CCFiniteTimeAction;
import org.cocos2d.actions.interval.CCFadeIn;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;

import com.raral.childdev.R;
import com.raral.childdev.ShowChapters;
import com.raral.childdev.base.ChildDevBaseLayer;
import com.raral.childdev.util.MyLog;
import com.raral.childdev.util.Tools;


public class Instruction1Scene extends ChildDevBaseLayer {
	private static final String LOG_TAG = "Instruction1Scene";
	float yy = 0;

	public Instruction1Scene() {
		super();
		isTouchEnabled_ = true;
		MyLog.v(LOG_TAG, Thread.currentThread().getStackTrace()[2].getMethodName());

		addHint(0.0f, Tools.getString(R.string.animalmemory_test_instruction_show1));
		addHint(8.0f, Tools.getString(R.string.animalmemory_test_instruction_show2));

	}
	
	private void addHint( float sTime, String text)
	{
		CGSize s = CCDirector.sharedDirector().winSize();
		if (yy == 0)
			yy = s.height - 60;
		
		CCLabel hint = CCLabel.makeLabel(text, "DroidSans", Tools.getFontSize(22));
		hint.setAnchorPoint(0.5f, 1.0f);
		hint.setPosition(CGPoint.make(s.width / 2, yy));
        hint.setOpacity(0);
        addSimpleAct(hint, sTime, CCSequence.actions(
        		CCFadeIn.action(1), CCFiniteTimeAction.action(3), CCFadeOut.action(2)));
        yy = hint.getPositionRef().y - hint.getContentSizeRef().height - 20;
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
