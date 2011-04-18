package com.raral.childdev.animalmemory;

import java.util.ArrayList;
import java.util.List;

import org.cocos2d.actions.base.CCFiniteTimeAction;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCFadeIn;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;


import com.raral.childdev.R;
import com.raral.childdev.Report;
import com.raral.childdev.TestBundle;
import com.raral.childdev.base.ChildBaseSprite;
import com.raral.childdev.base.ChildDevBaseLayer;
import com.raral.childdev.base.ChildDevBaseTest;

import com.raral.childdev.util.MyLog;
import com.raral.childdev.util.Tools;

public class AnimalmemoryTest extends ChildDevBaseTest {
	private static final String LOG_TAG = "AnimalmemoryTest";
	private AnimalmemoryData memoryData = new AnimalmemoryData();
	
	public AnimalmemoryTest(TestBundle bundle) {
		super(bundle);
		initData();
		// TODO Auto-generated constructor stub
	}
	
	private void initData() {
		Report.animalmemory_score = 0;
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
		ArrayList<CCScene> s = new ArrayList<CCScene>();
		CCScene object;
		
		object = CCScene.node();
		object.addChild(new Instruction1(this));
		s.add(object);
		
		object = CCScene.node();
		object.addChild(new AnimalsShow(this));
		s.add(object);
		
		object = CCScene.node();
		object.addChild(new Instruction2(this));
		s.add(object);
		
		object = CCScene.node();
		object.addChild(new AnimalsSelectLayer(this, memoryData));
		s.add(object);
		
		tested = true;
		return s;
	}
	
	private class Instruction1 extends ChildDevBaseLayer {
		float yy = 0;

		public Instruction1(ChildDevBaseTest test) {
			super(test);
			isTouchEnabled_ = true;
			MyLog.v(LOG_TAG, Thread.currentThread().getStackTrace()[2].getMethodName());

			addHint(0.0f, Tools.getString(R.string.animalmemory_test_instruction_show1));
			addHint(8.0f, Tools.getString(R.string.animalmemory_test_instruction_show2));
//			
//			CGSize s = CCDirector.sharedDirector().winSize();
//			ChildBaseSprite sample = new ChildBaseSprite("grossini.png");
//			sample.setPosition(s.width / 2, s.height / 2);
//			sample.setOpacity(0);
//			addSimpleAct(2.0f, sample, CCFadeIn.action(1.5f));
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
	        addSimpleAct(sTime, hint, CCSequence.actions(
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
			if (getTimeElapsed() >= 8.0f)
				getBundle().finishCurrentScene();
		}
	}	
	

	private class AnimalsShow extends ChildDevBaseLayer {
		List<String> step1ShowPictureList;
		float sleepTime = 2; //unit is second
        public AnimalsShow(ChildDevBaseTest test) {
			super(test);
			isTouchEnabled_ = true;
        	MyLog.v(LOG_TAG, Thread.currentThread().getStackTrace()[2].getMethodName());
        	step1ShowPictureList = memoryData.getStep1ShowPictureList();
        	
        	//background
        	String bgPath = memoryData.getBackgroupPicture();
			ChildBaseSprite background = new ChildBaseSprite(bgPath);
			// change the transform anchor point (optional)
			background.setAnchorPoint(CGPoint.make(0, 0));
			addChild(background, 0);
             
        	//animate 
	        ChildBaseSprite animates = new ChildBaseSprite("grossini.png");
	        addChild(animates, 1);

	        animates.setPosition(CGPoint.make(CCDirector.sharedDirector().winSize().width / 2, CCDirector.sharedDirector().winSize().height / 3));
            
			CCAnimation animation = CCAnimation.animation("animals", sleepTime);			
	        
			for( int i=0; i<step1ShowPictureList.size(); i++) {
				String pic = step1ShowPictureList.get(i);
				MyLog.v(LOG_TAG, "pic: " + pic);
				animation.addFrame(pic);
			}

			CCAnimate action = CCAnimate.action(animation, false);
			animates.runAction(CCSequence.actions(action));
        }
        
		
		@Override
		public void onEnter() {
			super.onEnter();		
		}
		
		@Override
		public void update(float d) {
			super.update(d);
			if (getTimeElapsed() >= step1ShowPictureList.size() * sleepTime)
				getBundle().finishCurrentScene();
		}
        
    }

	private class Instruction2 extends ChildDevBaseLayer {
		float yy = 0;

		public Instruction2(ChildDevBaseTest test) {
			super(test);
			isTouchEnabled_ = true;
			MyLog.v(LOG_TAG, Thread.currentThread().getStackTrace()[2].getMethodName());

			addHint(0.0f, Tools.getString(R.string.animalmemory_test_instruction_select1));
			addHint(4.0f, Tools.getString(R.string.animalmemory_test_instruction_select2));
//			
//			CGSize s = CCDirector.sharedDirector().winSize();
//			ChildBaseSprite sample = new ChildBaseSprite("grossini.png");
//			sample.setPosition(s.width / 2, s.height / 2);
//			sample.setOpacity(0);
//			addSimpleAct(2.0f, sample, CCFadeIn.action(1.5f));
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
	        addSimpleAct(sTime, hint, CCSequence.actions(
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
			if (getTimeElapsed() >= 4.0f)
				getBundle().finishCurrentScene();
		}
	}	

}
