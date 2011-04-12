package com.childdev;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;

import android.view.MotionEvent;
import android.widget.Toast;

public class TerminalLayer extends CCLayer {
	TestBundle _bundle;

	public TerminalLayer(TestBundle bundle) {
		super();
		isTouchEnabled_ = true;
		_bundle = bundle;

		CCLabel score = CCLabel.makeLabel("总得分: " + bundle.getTotalScore(),
				"DroidSans", 36);
		CGSize s = CCDirector.sharedDirector().winSize();
		score.setPosition(CGPoint.make(s.width / 2, s.height / 2));
		addChild(score);
	}

	@Override
	public boolean ccTouchesEnded(MotionEvent event) {
		_bundle.postToUI(new Runnable() {			
			@Override
			public void run() {
				Toast.makeText(CCDirector.sharedDirector().getActivity(),
						"退出", 3).show();
				CCDirector.sharedDirector().getActivity().finish();
			}
		});
		
		return true;
	}
}
