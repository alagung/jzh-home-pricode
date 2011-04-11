package com.childdev.base;

import org.cocos2d.events.CCTouchDispatcher;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItem;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.protocols.CCTouchDelegateProtocol;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.util.CGPointUtil;
import org.cocos2d.types.util.PoolHolder;
import org.cocos2d.utils.pool.OneClassPool;

import android.view.MotionEvent;

public abstract class NodeEventLayer extends CCLayer {

	/**
	 * Determine whether a UIKit coordinate is in Node. Will convert to GL coordinate first
	 * @param item CCNode
	 * @param x UIKit coordinate x
	 * @param y UIKit coordinate y
	 * @return
	 */
	static public boolean uiXYInNode(CCNode item, float x, float y)
	{
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

	public NodeEventLayer() {
		super();
		isTouchEnabled_ = true;
	}

	public abstract boolean ccTouchesMovedPro(MotionEvent event, CCNode item);

	public abstract boolean ccTouchesCancelledPro(MotionEvent event, CCNode item);

	public abstract boolean ccTouchesEndedPro(MotionEvent event, CCNode item);

	public abstract boolean ccTouchesBeganPro(MotionEvent event, CCNode item);
	
	private boolean itemEventImpl(MotionEvent event, CCNode item) {
		int action = event.getAction();
		int actionCode = action & MotionEvent.ACTION_MASK;
		
		switch (actionCode) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_POINTER_DOWN:
			if (item instanceof CCTouchDelegateProtocol)
				return ((CCTouchDelegateProtocol)item).ccTouchesBegan(event);
			else
				return ccTouchesBeganPro(event, item);
		case MotionEvent.ACTION_CANCEL:
			if (item instanceof CCTouchDelegateProtocol)
				return ((CCTouchDelegateProtocol)item).ccTouchesCancelled(event);
			else
				return ccTouchesCancelledPro(event, item);
		case MotionEvent.ACTION_MOVE:
			if (item instanceof CCTouchDelegateProtocol)
				return ((CCTouchDelegateProtocol)item).ccTouchesMoved(event);
			else
				return ccTouchesMovedPro(event, item);
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			if (item instanceof CCTouchDelegateProtocol)
				return ((CCTouchDelegateProtocol)item).ccTouchesEnded(event);
			else
				return ccTouchesEndedPro(event, item);
		}   
		return CCTouchDispatcher.kEventIgnored;
	}

	private boolean itemEvent(MotionEvent event, CCNode item) {
		// Ignore Menu item, it take over by Menu
		if (item instanceof CCMenuItem)
			return CCTouchDispatcher.kEventIgnored;
		
		if (!item.isRunning())
			return CCTouchDispatcher.kEventIgnored;
	    
		// Go through all children, reverse to draw order
		
	    if (item.getChildren() != null) {
	    	for (int i=item.getChildren().size()-1; i>=0; --i) {
	    		CCNode child = item.getChildren().get(i);
	    		if (child.getZOrder() >= 0) {
	    			if (itemEvent(event, child) == CCTouchDispatcher.kEventHandled)
	    				return CCTouchDispatcher.kEventHandled;
	    		} else
	    			continue;
	    	}
	    }	
	    
	    // Ignore Layer who take over itself 
	    if (!(item instanceof CCLayer && ((CCLayer)item).isTouchEnabled())) {
	    	if (uiXYInNode(item, event.getX(), event.getY())) {
	        	if(itemEventImpl(event, item) == CCTouchDispatcher.kEventHandled) {
					return CCTouchDispatcher.kEventHandled;
				}
	    	}
	    }

	    if (item.getChildren() != null) {
	    	for (int i=item.getChildren().size()-1; i>=0; --i) {
	    		CCNode child = item.getChildren().get(i);
	    		if (child.getZOrder() < 0) {
	    			if (itemEvent(event, child) == CCTouchDispatcher.kEventHandled)
	    				return CCTouchDispatcher.kEventHandled;
	    		} else
	    			continue;
	    	}
	    }
	
	    return CCTouchDispatcher.kEventIgnored;
	}

	@Override
	final public boolean ccTouchesBegan(MotionEvent event) {
	    return itemEvent(event, this);
	}

	@Override
	final public boolean ccTouchesEnded(MotionEvent event) {
		return itemEvent(event, this);
	}

	@Override
	final public boolean ccTouchesCancelled(MotionEvent event) {
		return itemEvent(event, this);
	}

	@Override
	final public boolean ccTouchesMoved(MotionEvent event) {
		return itemEvent(event, this);
	}

}