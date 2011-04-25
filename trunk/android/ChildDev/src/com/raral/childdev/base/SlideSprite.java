package com.raral.childdev.base;

import java.util.ArrayList;

import org.cocos2d.actions.UpdateCallback;
import org.cocos2d.actions.base.CCAction;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.types.CGPoint;

import com.raral.childdev.util.MyLog;


public class SlideSprite extends CCNode implements UpdateCallback {
	private static String LOG_TAG = "SlideSprite";
	private float mX = 0;
	private float mY = 0;
	private int mZ = 0;
	private float mDuration = 0.0f;
	private float mElapsedTime = 0.0f;
	private int mActorIndex = 0;
	private ChildBaseSprite pictureSprite = null;
	
	public CCNode mNode = null;
	public boolean mRunning = false;

	ArrayList<Actor> mActors = new ArrayList<Actor>();
	
	public SlideSprite(CCNode node, float x, float y, int z, float duration) {
		mNode = node;
		mX = x;
		mY = y;
		mZ = z;
		mDuration = duration;
	}
	
	@Override
	public void update(float duration) {
		mElapsedTime += duration;
		processActors();
	}
	
	public void addActor(String picture) {
		addActor(picture, null);
	}
	
	public void addActor(String picture, CCAction action) {
		Actor a = new Actor(picture, action);
		a.mEndTime = (mActors.size()+1)*mDuration;
		mActors.add(a);
	}
	
	public void clean() {
		if(pictureSprite != null) {
    		pictureSprite.stopAllActions();
    		mNode.removeChild(pictureSprite, true);
    	}
		mActors.clear();
	}

	private void processActors() {
		if(mActorIndex > mActors.size()-1)
    		return;
		
		Actor act = mActors.get(mActorIndex);
		if(!mRunning || mElapsedTime >= act.mEndTime) {
			mRunning = true;
			MyLog.v(LOG_TAG, "pic: " + act.mPicture);
			if(pictureSprite != null) {
	    		pictureSprite.stopAllActions();
	    		mNode.removeChild(pictureSprite, true);
	    	}

        	pictureSprite = new ChildBaseSprite(act.mPicture);
        	mNode.addChild(pictureSprite, mZ);
			pictureSprite.setPosition(CGPoint.make(mX, mY));
			if (act.mAction != null)
				pictureSprite.runAction(act.mAction);
			
			mActorIndex++;
		}
		
	}

	public class Actor {
		public String mPicture = "";
		public float mEndTime = 0.0f;
		public CCAction mAction = null;

		public Actor(String picture, CCAction action) {
			mPicture = picture;
			mAction = action;
		}
		
		public Actor(String picture) {
			this(picture, null);
		}
		
	}
}

