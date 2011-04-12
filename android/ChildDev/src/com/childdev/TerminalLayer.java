package com.childdev;

import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;

import android.view.MotionEvent;
import android.widget.Toast;

import com.childdev.base.ChildDevBaseLayer;

public class TerminalLayer extends ChildDevBaseLayer {
	TestBundle _bundle;

	public TerminalLayer(TestBundle bundle) {
		super(null);
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
						"退出", 1).show();
				CCDirector.sharedDirector().getActivity().finish();
			}
		});
		
		return true;
	}
}
