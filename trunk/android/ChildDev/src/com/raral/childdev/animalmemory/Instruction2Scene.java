package com.raral.childdev.animalmemory;

import org.cocos2d.actions.base.CCFiniteTimeAction;
import org.cocos2d.actions.interval.CCFadeIn;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.types.CGPoint;

import com.raral.childdev.R;
import com.raral.childdev.ShowChapters;
import com.raral.childdev.base.ChildDevBaseLayer;
import com.raral.childdev.util.MyLog;
import com.raral.childdev.util.Tools;


public class Instruction2Scene extends ChildDevBaseLayer {
	private static final String LOG_TAG = "Instruction2Scene";
	float yy = 0;

	public Instruction2Scene() {
		super();
		isTouchEnabled_ = true;
		MyLog.v(LOG_TAG, Thread.currentThread().getStackTrace()[2].getMethodName());

		addHint(0.0f, Tools.getString(R.string.animalmemory_test_instruction_select1));
		addHint(4.0f, Tools.getString(R.string.animalmemory_test_instruction_select2));
	}
	
	private void addHint( float sTime, String text)
	{
		if (yy == 0)
			yy = Tools.getScreenHeight() - 60;
		
		CCLabel hint = CCLabel.makeLabel(text, "DroidSans", Tools.getFontSize(28));
		hint.setAnchorPoint(0.5f, 1.0f);
		hint.setPosition(CGPoint.make(Tools.getScreenWidth() / 2, yy));
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
		if (getElapsedTime() >= 4.0f)
			ShowChapters.getInstance().finishCurrentScene();
	}
}	
