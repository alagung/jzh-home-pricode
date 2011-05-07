package com.raral.childdev.base;

import org.cocos2d.events.CCTouchDispatcher;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.menus.CCMenuItem;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.protocols.CCTouchDelegateProtocol;
import org.cocos2d.types.ccColor4B;

import android.view.MotionEvent;

public abstract class NodeEventLayer extends ChildDevBaseLayer{
	public NodeEventLayer() {
		super();
		isTouchEnabled_ = true;
	}
	
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

	public boolean ccTouchesBeganPro(MotionEvent event, CCNode item) {
		return CCTouchDispatcher.kEventIgnored;
	}

	public boolean ccTouchesEndedPro(MotionEvent event, CCNode item) {
		return CCTouchDispatcher.kEventIgnored;
	}

	public boolean ccTouchesCancelledPro(MotionEvent event, CCNode item) {
		return CCTouchDispatcher.kEventIgnored;
	}

	public boolean ccTouchesMovedPro(MotionEvent event, CCNode item) {
		return CCTouchDispatcher.kEventIgnored;
	}

}