package com.raral.childdev.base;

import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.nodes.CCSpriteSheet;
import org.cocos2d.opengl.CCTexture2D;
import org.cocos2d.protocols.CCTouchDelegateProtocol;
import org.cocos2d.types.CGRect;

import android.graphics.Bitmap;

abstract public class NodeEventSprite extends ChildBaseSprite implements CCTouchDelegateProtocol{

	public NodeEventSprite() {
		super();
	}

	public NodeEventSprite(CCTexture2D texture) {
		super(texture);
	}

	public NodeEventSprite(CCSpriteFrame spriteFrame) {
		super(spriteFrame);
	}

	public NodeEventSprite(String filename) {
		super(filename);
	}

	public NodeEventSprite(String filename, float scale) {
		super(filename, scale);
	}

	public NodeEventSprite(CCTexture2D texture, CGRect rect) {
		super(texture, rect);
	}

	public NodeEventSprite(String spriteFrameName, boolean isFrame) {
		super(spriteFrameName, isFrame);
	}

	public NodeEventSprite(String filename, CGRect rect) {
		super(filename, rect);
	}

	public NodeEventSprite(Bitmap image, String key) {
		super(image, key);
	}

	public NodeEventSprite(CCSpriteSheet spritesheet, CGRect rect) {
		super(spritesheet, rect);
	}

}