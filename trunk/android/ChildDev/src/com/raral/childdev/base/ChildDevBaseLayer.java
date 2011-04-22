package com.raral.childdev.base;

import java.util.ArrayList;

import org.cocos2d.actions.UpdateCallback;
import org.cocos2d.actions.base.CCAction;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.util.CGPointUtil;
import org.cocos2d.types.util.PoolHolder;
import org.cocos2d.utils.pool.OneClassPool;

public class ChildDevBaseLayer extends CCLayer implements UpdateCallback {
	public class SimpleActor {
		public CCNode mNode;
		public float mStartTime;
		public float mEndTime = 0.0f;
		public CCAction mAction = null;
		public boolean mRunning = false;
		
		public SimpleActor(CCNode node, float startTime, float endTime, CCAction action) {
			mNode = node;
			mStartTime = startTime;
			mEndTime = endTime;
			mAction = action;
			mEndTime = endTime;
		}
		
		public SimpleActor(CCNode node, float startTime, CCAction action) {
			this(node, startTime, 0.0f, action);
		}
		
		public SimpleActor(CCNode node, float startTime) {
			this(node, startTime, 0.0f, null);
		}
		
	}
	
	ArrayList<SimpleActor> mSimpleActors = new ArrayList<SimpleActor>();
	
	private float mElapsedTime = 0.0f;
	
	public ChildDevBaseLayer() {
		super();
		mSimpleActors.clear();
	}

	/**
	 * Determine whether a UIKit coordinate is in Node. Will convert to GL coordinate first
	 * @param item CCNode
	 * @param x UIKit coordinate x
	 * @param y UIKit coordinate y
	 * @return
	 */
	public static boolean uiXYInNode(CCNode item, float x, float y) {
		PoolHolder holder = PoolHolder.getInstance();
		OneClassPool<CGPoint> pointPool = holder.getCGPointPool();
		OneClassPool<CGRect> rectPool = holder.getCGRectPool();
		CGPoint touchLocation = pointPool.get();
		CGPoint local = pointPool.get();
		CGRect r = rectPool.get();
	
		try {
			CCDirector.sharedDirector().convertToGL(x, y, touchLocation);
			item.convertToNodeSpace(touchLocation.x, touchLocation.y, local);
			CGPoint pos = item.getPositionRef();
			CGPoint pnt = item.getAnchorPointRef();
			CGSize size = item.getContentSizeRef();
			r.set(pos.x - size.width * pnt.x, pos.y - size.height * pnt.y,
					size.width, size.height);
			CGPointUtil.zero(r.origin);
			return CGRect.containsPoint(r, local);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			pointPool.free(touchLocation);
			pointPool.free(local);
			rectPool.free(r);
		}
	}
	
	private void processActors() {
		for (int i = 0; i < mSimpleActors.size(); ++i) {
			SimpleActor act = mSimpleActors.get(i);
			if (!act.mRunning && act.mStartTime <= mElapsedTime) {
				act.mRunning = true;
				addChild(act.mNode);
				if (act.mAction != null)
					act.mNode.runAction(act.mAction);
			}
			if (act.mRunning && (act.mEndTime > 0.0f && act.mEndTime <= mElapsedTime)) {
				act.mNode.stopAllActions();
				removeChild(act.mNode, true);
				act.mRunning = false;				
			}
		}
	}
		
	public void addSimpleAct(SimpleActor actor) {
		mSimpleActors.add(actor);
	}
	
	public SimpleActor addSimpleAct(CCNode node, float sTime, CCAction act) {
		SimpleActor a = new SimpleActor(node, sTime, act);
		mSimpleActors.add(a);
		return a;	
	}

	public float getElapsedTime() {
		return mElapsedTime;
	}
	
	@Override
	public void onEnter() {
		super.onEnter();
		schedule(this);
		mElapsedTime = 0.0f;
		processActors();		
	}

	@Override
	public void onExit() {
		super.onExit();
		unschedule(this);
	}

	@Override
	public void update(float d) {
		mElapsedTime += d;
		processActors();
	}
}