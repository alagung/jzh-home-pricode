package com.raral.childdev.animalmemory;


import org.cocos2d.types.CGPoint;

import com.raral.childdev.ShowChapters;
import com.raral.childdev.base.ChildBaseSprite;
import com.raral.childdev.base.ChildDevBaseLayer;
import com.raral.childdev.util.MyLog;
import com.raral.childdev.util.Tools;


public class Instruction1Scene extends ChildDevBaseLayer {
	private static final String LOG_TAG = "Instruction1Scene";
	float yy = 0;
	float mRemainTime = 6.0f;

	public Instruction1Scene() {
		super();
		isTouchEnabled_ = true;
		MyLog.v(LOG_TAG, Thread.currentThread().getStackTrace()[2].getMethodName());

		//background
    	String bgPath = AnimalmemoryTest.mAnimalMemoryData.getBackgroupPicture();
		ChildBaseSprite background = new ChildBaseSprite(bgPath);
		// change the transform anchor point (optional)
		background.setAnchorPoint(CGPoint.make(0, 0));
		addChild(background, 0);
		

		//instruction
    	String instPath = AnimalmemoryTest.mAnimalMemoryData.getPath() + "/instruction1.png";
		ChildBaseSprite instruction = new ChildBaseSprite(instPath);
		instruction.setAnchorPoint(0, 1);
		instruction.setPosition(CGPoint.make(0, Tools.getScreenHeight()));
		addChild(instruction, 1);

	}
	
	@Override
	public void onEnter() {
		super.onEnter();
	}
	
	@Override
	public void update(float d) {
		super.update(d);
		mRemainTime -= d;
		if (mRemainTime <= 0.0f) {
			ShowChapters.getInstance().finishCurrentScene();
		}
	}
}	
