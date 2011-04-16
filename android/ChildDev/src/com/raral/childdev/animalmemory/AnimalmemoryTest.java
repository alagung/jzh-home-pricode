package com.raral.childdev.animalmemory;

import java.util.ArrayList;
import java.util.List;

import org.cocos2d.actions.base.CCFiniteTimeAction;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCFadeIn;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCRotateBy;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItemFont;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.transitions.CCFlipXTransition;
import org.cocos2d.transitions.CCTransitionScene;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;


import com.raral.childdev.Language;
import com.raral.childdev.TestBundle;
import com.raral.childdev.base.ChildBaseSprite;
import com.raral.childdev.base.ChildDevBaseLayer;
import com.raral.childdev.base.ChildDevBaseTest;
import com.raral.childdev.util.MyLog;

public class AnimalmemoryTest extends ChildDevBaseTest {
	private static final String LOG_TAG = "AnimalmemoryTest";
	private AnimalmemoryData memoryData = new AnimalmemoryData();
	
	public AnimalmemoryTest(TestBundle bundle) {
		super(bundle);
		// TODO Auto-generated constructor stub
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
		object.addChild(new Instruction(this));
		s.add(object);
		
		object = CCScene.node();
		object.addChild(new ShowAnimals());
		s.add(object);

		tested = true;
		return s;
	}
	
	private class Instruction extends ChildDevBaseLayer {
		float yy = 0;

		public Instruction(ChildDevBaseTest test) {
			super(test);
			isTouchEnabled_ = true;
			MyLog.v(LOG_TAG, Thread.currentThread().getStackTrace()[2].getMethodName());
			
			CGSize s = CCDirector.sharedDirector().winSize();
			addHint(0.0f, Language.string.animalmemory_test_instruction_select);
			addHint(4.0f, Language.string.animalmemory_test_instruction_select);
			addHint(4.0f, Language.string.animalmemory_test_instruction_select);

			ChildBaseSprite sample = new ChildBaseSprite("grossini.png");
			sample.setPosition(s.width / 2, s.height / 2);
			sample.setOpacity(0);
			addSimpleAct(2.0f, sample, CCFadeIn.action(1.5f));
		}
		
		@Override
		public void onEnter() {
			super.onEnter();		
		}
		
		@Override
		public void update(float d) {
			super.update(d);
			if (getTimeElapsed() >= 8.0f)
				getBundle().finishCurrentScene();
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
	}	
	

	private class ShowAnimals extends CCLayer {
    	
        public ShowAnimals() {
			isTouchEnabled_ = true;
        	MyLog.v(LOG_TAG, Thread.currentThread().getStackTrace()[2].getMethodName());
        	
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
            
			CCAnimation animation = CCAnimation.animation("animals",2);
			
	        List<String> step1ShowPictureList = memoryData.getStep1ShowPictureList();
			for( int i=0; i<step1ShowPictureList.size(); i++) {
				String pic = step1ShowPictureList.get(i);
				MyLog.v(LOG_TAG, "pic: " + pic);
				animation.addFrame(pic);
			}

			CCAnimate action = CCAnimate.action(animation, false);
			animates.runAction(CCSequence.actions(action));
        }
        
    }


}
