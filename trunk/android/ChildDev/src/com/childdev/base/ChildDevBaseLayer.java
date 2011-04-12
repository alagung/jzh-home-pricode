package com.childdev.base;

import java.util.ArrayList;

import org.cocos2d.actions.UpdateCallback;
import org.cocos2d.actions.base.CCAction;
import org.cocos2d.actions.base.CCFiniteTimeAction;
import org.cocos2d.actions.interval.CCFadeIn;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.util.CGPointUtil;
import org.cocos2d.types.util.PoolHolder;
import org.cocos2d.utils.pool.OneClassPool;

public class ChildDevBaseLayer extends CCLayer implements UpdateCallback {
	public class SimpleActor {
		public float sTime;
		public CCNode node;
		public float eTime;
		public CCAction act;
		public boolean running;
		public SimpleActor(float sTime, CCNode node, float eTime, CCAction act) {
			super();
			this.sTime = sTime;
			this.node = node;
			this.eTime = eTime;
			this.act = act;
			this.running = false;
		}
		public SimpleActor(float sTime, CCNode node, CCAction act) {
			super();
			this.sTime = sTime;
			this.node = node;
			this.act = act;
			this.eTime = 0.0f;
			this.running = false;
		}
		
	}
	ArrayList<SimpleActor> _acts = new ArrayList<SimpleActor>();
	
	private ChildDevBaseTest _test = null;
	private float tEl = 0.0f;

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

	public ChildDevBaseTest getTest() {
		return _test;
	}
	
	private void processActs() {
		for (int i = 0; i < _acts.size(); ++i) {
			SimpleActor act = _acts.get(i);
			if (!act.running && act.sTime <= tEl) {
				act.running = true;
				addChild(act.node);
				if (act.act != null)
					act.node.runAction(act.act);
			}
			if (act.running && (act.eTime > 0.0f && act.eTime <= tEl)) {
				act.node.stopAllActions();
				removeChild(act.node, true);
				act.running = false;				
			}
		}
	}
		
	public void addSimpleAct(SimpleActor actor) {
		_acts.add(actor);
	}
	
	public SimpleActor addSimpleAct(float sTime, CCNode node, CCAction act) {
		SimpleActor a = new SimpleActor(sTime, node, act);
		_acts.add(a);
		return a;	
	}

	public float getTimeElapsed() {
		return tEl;
	}

	public ChildDevBaseLayer(ChildDevBaseTest test) {
		super();
		_test = test;
		_acts.clear();
	}
	
	@Override
	public void onEnter() {
		super.onEnter();
		schedule(this);
		tEl = 0.0f;
		processActs();		
	}

	@Override
	public void onExit() {
		super.onExit();
		unschedule(this);
	}

	@Override
	public void update(float d) {
		tEl += d;
		processActs();
	}
}