package com.raral.childdev.animalmemory;

import java.util.List;

import org.cocos2d.types.CGPoint;

import com.raral.childdev.ShowChapters;
import com.raral.childdev.base.ChildBaseSprite;
import com.raral.childdev.base.ChildDevBaseLayer;
import com.raral.childdev.base.SlideSprite;
import com.raral.childdev.util.MyLog;
import com.raral.childdev.util.Tools;

public class AnimalsShowScene extends ChildDevBaseLayer {
	private static final String LOG_TAG = "AnimalsShowScene";
	List<String> step1ShowPictureList;
	float duration = 2; //unit is second
	float endTime = 0;
	SlideSprite animals = null;

    public AnimalsShowScene() {
		super();
    	MyLog.v(LOG_TAG, Thread.currentThread().getStackTrace()[2].getMethodName());
    	
    	isTouchEnabled_ = true;
    	step1ShowPictureList = AnimalmemoryTest.mAnimalMemoryData.getStep1ShowPictureList();
    	endTime = (step1ShowPictureList.size()+1)*duration;

    	//background
    	String bgPath = AnimalmemoryTest.mAnimalMemoryData.getBackgroupPicture();
		ChildBaseSprite background = new ChildBaseSprite(bgPath);
		// change the transform anchor point (optional)
		background.setAnchorPoint(CGPoint.make(0, 0));
		addChild(background, 0);
		
		animals = new SlideSprite(this, Tools.getScreenWidth()/2, (Tools.getScreenHeight()/3)+20, 1, duration);
		for(int i=0; i<step1ShowPictureList.size(); i++){
			animals.addActor(step1ShowPictureList.get(i));
		}

    }

	@Override
	public void onEnter() {
		super.onEnter();		
	}
	
	@Override
	public void update(float d) {
		super.update(d);
		animals.update(d);

		if (getElapsedTime() > endTime){
			animals.clean();
			ShowChapters.getInstance().finishCurrentScene();
		}
	}
    
}
