package com.raral.childdev.base;

import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.nodes.CCSpriteSheet;
import org.cocos2d.opengl.CCTexture2D;
import org.cocos2d.types.CGRect;

import android.graphics.Bitmap;

import com.raral.childdev.util.Tools;

public class ChildBaseSprite  extends CCSprite {

	public ChildBaseSprite() {
		super();
		setCGScale();
	}

	public ChildBaseSprite(CCTexture2D texture) {
		super(texture);
		setCGScale();
	}

	public ChildBaseSprite(CCSpriteFrame spriteFrame) {
		super(spriteFrame);
		setCGScale();
	}

	public ChildBaseSprite(String filename) {
		super(filename);
		setCGScale();
	}

	public ChildBaseSprite(String filename, float scale) {
		super(filename);
		setCGScale(scale);
	}
	
	public ChildBaseSprite(CCTexture2D texture, CGRect rect) {
		super(texture, rect);
		setCGScale();
	}

	public ChildBaseSprite(String spriteFrameName, boolean isFrame) {
		super(spriteFrameName, isFrame);
		setCGScale();
	}

	public ChildBaseSprite(String filename, CGRect rect) {
		super(filename, rect);
		setCGScale();
	}

	public ChildBaseSprite(Bitmap image, String key) {
		super(image, key);
		setCGScale();
	}

	public ChildBaseSprite(CCSpriteSheet spritesheet, CGRect rect) {
		super(spritesheet, rect);
		setCGScale();
	}
	
	private void setCGScale() {
		super.setScale(Tools.getSizeScale());
	}

	private void setCGScale(float scale) {
		super.setScale(scale);
	}

}
