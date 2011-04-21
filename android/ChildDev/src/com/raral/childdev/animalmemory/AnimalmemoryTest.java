package com.raral.childdev.animalmemory;

import java.util.ArrayList;
import java.util.List;

import org.cocos2d.actions.base.CCAction;
import org.cocos2d.actions.base.CCFiniteTimeAction;
import org.cocos2d.actions.interval.CCFadeIn;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;


import com.raral.childdev.R;
import com.raral.childdev.Report;
import com.raral.childdev.ShowChapters;
import com.raral.childdev.base.ChildBaseSprite;
import com.raral.childdev.base.ChildDevBaseLayer;
import com.raral.childdev.base.ChildDevBaseChapter;

import com.raral.childdev.util.MyLog;
import com.raral.childdev.util.Tools;

public class AnimalmemoryTest extends ChildDevBaseChapter {
	private static final String LOG_TAG = "AnimalmemoryTest";
	public static AnimalmemoryData mAnimalMemoryData;
	
	public AnimalmemoryTest() {
		super();
		initData();
		// TODO Auto-generated constructor stub
	}
	
	private void initData() {
		Report.animalmemory_score = 0;
		mAnimalMemoryData = new AnimalmemoryData();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "动物记忆";
	}

	@Override
	public String getDesc() {
		// TODO Auto-generated method stub
		return "找出看到过的动物";
	}

	@Override
	public ArrayList<CCScene> loadScenes() {
		// TODO Auto-generated method stub
		ArrayList<CCScene> sceneList = new ArrayList<CCScene>();
		CCScene scene;
		
		scene = CCScene.node();
		scene.addChild(new Instruction1());
		sceneList.add(scene);
		
		scene = CCScene.node();
		scene.addChild(new AnimalsShow());
		sceneList.add(scene);
		
		scene = CCScene.node();
		scene.addChild(new Instruction2());
		sceneList.add(scene);
		
		scene = CCScene.node();
		scene.addChild(new AnimalsSelectLayer());
		sceneList.add(scene);
		
		mIsLoaded = true;
		return sceneList;
	}
	
	public class SimpleSlide {
		public String mPicture;
		public float mX = 0;
		public float mY = 0;
		public float mStartTime = 0.0f;
		public float mEndTime = 0.0f;
		public CCAction mAction = null;
		public boolean mRunning = false;
		
		public SimpleSlide(String picture, float x, float y, float startTime, float endTime, CCAction action) {
			this(picture, x, y, startTime, action);
			mEndTime = endTime;
		}
		public SimpleSlide(String picture, float x, float y, float startTime, CCAction action) {
			this(picture, x, y, startTime);
			mAction = action;
		}
		
		public SimpleSlide(String picture, float x, float y, float startTime) {
			super();
			mPicture = picture;
			mStartTime = startTime;
			mX = x;
			mY = y;
		}
	}
	
	private class Instruction1 extends ChildDevBaseLayer {
		float yy = 0;

		public Instruction1() {
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
			
			CCLabel hint = CCLabel.makeLabel(text, "DroidSans", 16);
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
			MyLog.v(LOG_TAG, Thread.currentThread().getStackTrace()[2].getMethodName());
		}
		
		@Override
		public void update(float d) {
			super.update(d);
			MyLog.v(LOG_TAG, Thread.currentThread().getStackTrace()[2].getMethodName());
			if (getElapsedTime() >= 8.0f)
				ShowChapters.getInstance().finishCurrentScene();
		}
	}	
	

	private class AnimalsShow extends ChildDevBaseLayer {
		List<String> step1ShowPictureList;
		int pictureNumber = 0;
		int pictureIndex = 0;
		float sleepTime = 2; //unit is second
		float endTime = 0;
		ChildBaseSprite animalPicture = null;
		float animalPictureX = 0;
		float animalPictureY = 0;
		
        public AnimalsShow() {
			super();
        	MyLog.v(LOG_TAG, Thread.currentThread().getStackTrace()[2].getMethodName());
        	
        	isTouchEnabled_ = true;
        	step1ShowPictureList = mAnimalMemoryData.getStep1ShowPictureList();
        	pictureNumber = step1ShowPictureList.size();
        	endTime = (pictureNumber+1)*sleepTime;
        	animalPictureX = CCDirector.sharedDirector().winSize().width / 2;
    		animalPictureY = CCDirector.sharedDirector().winSize().height / 3;
        	
        	//background
        	String bgPath = mAnimalMemoryData.getBackgroupPicture();
			ChildBaseSprite background = new ChildBaseSprite(bgPath);
			// change the transform anchor point (optional)
			background.setAnchorPoint(CGPoint.make(0, 0));
			addChild(background, 0);
			
			for(int i=0; i<pictureNumber; i++){
				new SimpleSlide(step1ShowPictureList.get(i), animalPictureX, animalPictureY, (i+1)*sleepTime);
			}

        }
        
        private void animalSlide(){
        	MyLog.v(LOG_TAG, "pictureIndex: " + pictureIndex + ", pictureNumber:" + pictureNumber);
        	if(pictureIndex > pictureNumber-1)
        		return;

			if(getElapsedTime() > (pictureIndex+1)*sleepTime) {
	        	if(animalPicture != null) {
	        		removeChild(animalPicture, true);
	        	}
	        	
	        	String pic = step1ShowPictureList.get(pictureIndex);
	        	MyLog.v(LOG_TAG, "pic: " + pic);
	        	
	        	animalPicture = new ChildBaseSprite(pic);
				addChild(animalPicture, 1);
				animalPicture.setPosition(CGPoint.make(animalPictureX, animalPictureY));
				
				pictureIndex++;
			}
        }
        
		
		@Override
		public void onEnter() {
			super.onEnter();		
		}
		
		@Override
		public void update(float d) {
			super.update(d);
			animalSlide();

			if (getElapsedTime() > endTime)
				ShowChapters.getInstance().finishCurrentScene();
		}
        
    }

	private class Instruction2 extends ChildDevBaseLayer {
		float yy = 0;

		public Instruction2() {
			super();
			isTouchEnabled_ = true;
			MyLog.v(LOG_TAG, Thread.currentThread().getStackTrace()[2].getMethodName());

			addHint(0.0f, Tools.getString(R.string.animalmemory_test_instruction_select1));
			addHint(4.0f, Tools.getString(R.string.animalmemory_test_instruction_select2));
		}
		
		private void addHint( float sTime, String text)
		{
			CGSize s = CCDirector.sharedDirector().winSize();
			if (yy == 0)
				yy = s.height - 60;
			
			CCLabel hint = CCLabel.makeLabel(text, "DroidSans", 16);
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
			if (getElapsedTime() >= 4.0f)
				ShowChapters.getInstance().finishCurrentScene();
		}
	}	

}
