package com.raral.childdev.airsearch;

import java.util.ArrayList;

import org.cocos2d.actions.base.CCFiniteTimeAction;
import org.cocos2d.actions.interval.CCFadeIn;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;

import com.raral.childdev.TestBundle;
import com.raral.childdev.base.ChildDevBaseLayer;
import com.raral.childdev.base.ChildDevBaseTest;

public class AirsearchTest extends ChildDevBaseTest {
	
	public AirsearchTest(TestBundle bundle) {
		super(bundle);
	}

	@Override
	public String getDesc() {
		return "��������ҳ��ɻ�";
	}

	@Override
	public String getName() {
		return "�������";
	}

	@Override
	public ArrayList<CCScene> loadScenes() {
		ArrayList<CCScene> s = new ArrayList<CCScene>();
		CCScene object;
		
		object = CCScene.node();
		object.addChild(new Instruction(this));
		s.add(object);
		
		object = CCScene.node();
		object.addChild(new AirsearchLayer(this));
		s.add(object);

		tested = true;
		return s;
	}
	
	private class Instruction extends ChildDevBaseLayer {
		CCSprite sample = null;
		float yy = 0;
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
		public Instruction(ChildDevBaseTest test) {
			super(test);
			isTouchEnabled_ = true;
			
			CGSize s = CCDirector.sharedDirector().winSize();
			addHint(0.0f, "С���ѣ��������������ķɻ�����������");
			addHint(4.0f, "��������Ҫ���ľ�����������ҳ��ɻ�Ŷ��");
			addHint(4.0f, "������һ�°ɣ�");

			sample = CCSprite.sprite("grossini.png");
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
	}	
}
