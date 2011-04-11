package com.childdev.airsearch;

import java.util.ArrayList;

import org.cocos2d.actions.base.CCFiniteTimeAction;
import org.cocos2d.actions.interval.CCFadeIn;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;

import android.view.MotionEvent;

import com.childdev.TestBundle;
import com.childdev.base.ChildDevBaseTest;

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
		object.addChild(new Instruction());
		s.add(object);
		
		object = CCScene.node();
		object.addChild(new AirsearchLayer(this));
		s.add(object);

		tested = true;
		return s;
	}
	
	private class Instruction extends CCLayer {
		ArrayList<CCLabel> hints = new ArrayList<CCLabel>(4);
		CCSprite sample = null;
		private void addHint(String text)
		{
			CGSize s = CCDirector.sharedDirector().winSize();
			float yy = s.height - 60;
			if (hints.size() > 0) {
				CCLabel last = hints.get(hints.size()-1);
				yy = last.getPositionRef().y - last.getContentSizeRef().height - 20;
			}
			CCLabel hint = CCLabel.makeLabel(text, "DroidSans", 16);
			hint.setAnchorPoint(0.5f, 1.0f);
			hint.setPosition(CGPoint.make(s.width / 2, yy));
	        hint.setOpacity(0);
	        addChild(hint);
		}
		public Instruction() {
			super();
			isTouchEnabled_ = true;
			
			CGSize s = CCDirector.sharedDirector().winSize();
			addHint("С���ѣ��������������ķɻ�����������");
			addHint("��������Ҫ���ľ�����������ҳ��ɻ�Ŷ��");
			addHint("������һ�°ɣ�");

			sample = CCSprite.sprite("grossini.png");
			sample.setPosition(s.width / 2, s.height / 2);
			sample.setOpacity(0);
		}
		
		@Override
		public void onEnter() {
			super.onEnter();
			for (int i = 0; i < hints.size(); ++i) {
				hints.get(i).runAction(
						CCSequence.actions(CCFadeIn.action(1),
								CCFiniteTimeAction.action(4), CCFadeOut
										.action(2)));
			}
			sample.runAction(CCSequence.actions(CCFiniteTimeAction.action(2), CCFadeIn.action(1)));
		}
		

		@Override
		public boolean ccTouchesEnded(MotionEvent event) {
			getBundle().finishCurrentScene();
			return true;
		}
	}	
}
