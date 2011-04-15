package com.raral.childdev.base;

import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.nodes.CCSpriteSheet;
import org.cocos2d.opengl.CCTexture2D;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;

import android.graphics.Bitmap;

import com.raral.childdev.Config;
import com.raral.childdev.util.MyLog;

public class ChildBaseSprite  extends CCSprite {
	private static final String LOG_TAG = "ChildBaseSprite";
	
	public ChildBaseSprite() {
		super();
		super.setScale(getCGScale());
	}

	public ChildBaseSprite(CCTexture2D texture) {
		super(texture);
		super.setScale(getCGScale());
	}

	public ChildBaseSprite(CCSpriteFrame spriteFrame) {
		super(spriteFrame);
		super.setScale(getCGScale());
	}

	public ChildBaseSprite(String filename) {
		super(filename);
		super.setScale(getCGScale());
	}

	public ChildBaseSprite(CCTexture2D texture, CGRect rect) {
		super(texture, rect);
		super.setScale(getCGScale());
	}

	public ChildBaseSprite(String spriteFrameName, boolean isFrame) {
		super(spriteFrameName, isFrame);
		super.setScale(getCGScale());
	}

	public ChildBaseSprite(String filename, CGRect rect) {
		super(filename, rect);
		super.setScale(getCGScale());
	}

	public ChildBaseSprite(Bitmap image, String key) {
		super(image, key);
		super.setScale(getCGScale());
	}

	public ChildBaseSprite(CCSpriteSheet spritesheet, CGRect rect) {
		super(spritesheet, rect);
		super.setScale(getCGScale());
	}
	
	private float getCGScale() {
		MyLog.v(LOG_TAG, Thread.currentThread().getStackTrace()[2].getMethodName());
		CGSize s = CCDirector.sharedDirector().winSize();
		float wScale = s.width / Config.DEFAULT_SCREEN_WIDTH;
		float hScale = s.height / Config.DEFAULT_SCREEN_HEIGHT;
		float scale = 1.0f;
		if(wScale > hScale)
			scale = wScale;
		else
			scale = hScale;
		MyLog.v(LOG_TAG, "scale:" + scale + " (CGSize.width:" + s.width + " CGSize.height:" + s.height);
		return scale;
	}
}
