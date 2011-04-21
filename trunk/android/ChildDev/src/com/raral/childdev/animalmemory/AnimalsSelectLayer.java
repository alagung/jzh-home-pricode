package com.raral.childdev.animalmemory;

import java.util.List;


import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;

import android.view.MotionEvent;

import com.raral.childdev.Report;
import com.raral.childdev.ShowChapters;
import com.raral.childdev.base.ChildDevBaseChapter;
import com.raral.childdev.base.NodeEventLayer;
import com.raral.childdev.base.NodeEventSprite;
import com.raral.childdev.util.MyLog;
import com.raral.childdev.util.Tools;

public class AnimalsSelectLayer extends NodeEventLayer {
	private static final String LOG_TAG = "AnimalSelectLayer";
	CCLabel score;
	CCLabel time;
	float remain = 60.0f;
	List<String> step2ShowPictureList;
	int selectNumber = 0;
	int step2InStep1Number = 0;
	float scale = 0.5f;  // for drawing picture
	int originX = 60;
	int originY = 60;
	int picturesALine = 5; // show 5 pictures a line
	int picturesPadding = 20; // pixel
	int scoreTextHeight = 20;

	public AnimalsSelectLayer() {
		super();
		MyLog.v(LOG_TAG, Thread.currentThread().getStackTrace()[2].getMethodName());
		
		step2ShowPictureList = AnimalmemoryTest.mAnimalMemoryData.getStep2ShowPictureList();
		step2InStep1Number = AnimalmemoryTest.mAnimalMemoryData.getStep2InStep1Number();
		int pictureWidth = AnimalmemoryTest.mAnimalMemoryData.getPictureWidth();
		int pictureHeight = AnimalmemoryTest.mAnimalMemoryData.getPictureHeight();
		
		CGSize s = CCDirector.sharedDirector().winSize();
		
		int pictureLines = Tools.getFullInteger(step2ShowPictureList.size() / picturesALine);
		if(step2ShowPictureList.size() < picturesALine)
			picturesALine = step2ShowPictureList.size();
		
		float wScale = (s.width - originX) / ((pictureWidth+picturesPadding)*picturesALine);
		float hScale = (s.height -  originY - scoreTextHeight) / ((pictureHeight+picturesPadding)*pictureLines);	
		scale = (wScale < hScale)? wScale : hScale;

		int ox = originX;  // begin point
		int oy = originY; 
		int w = Tools.getFullInteger((pictureWidth + picturesPadding) * scale);
		int h = Tools.getFullInteger((pictureHeight + picturesPadding) * scale);
		MyLog.v(LOG_TAG, String.format("scale:%f, ox:%d, oy:%d, w:%d, h:%d, sw:%f, sh:%f", scale, ox, oy, w, h, s.width, s.height));
		
		for( int i=0; i<step2ShowPictureList.size(); i++) {
			String pic = step2ShowPictureList.get(i);
			MyLog.v(LOG_TAG, "pic: " + pic);
			int x = (i % (picturesALine-1)) * w + ox;
			int y = (i / (picturesALine-1)) * h + oy;
			MyLog.v(LOG_TAG, String.format("i:%d, x:%d, y:%d", i, x, y));
			addSimpleAct(new AnimalSprite(pic, x, y, pic), 1.0f, null);
		}
        
        score = CCLabel.makeLabel("得分: 0", "DroidSans", 18);
        score.setAnchorPoint(1.0f, 1.0f);
        score.setPosition(CGPoint.make(s.width - 20, s.height - scoreTextHeight));
        addChild(score);
        
        time = CCLabel.makeLabel("时间剩余: 30秒", "DroidSans", 18);
        time.setAnchorPoint(0.0f, 1.0f);
        time.setPosition(CGPoint.make(20, s.height - scoreTextHeight));
        addChild(time);
	}


	@Override
	public void onEnter() {
		super.onEnter();
	}
	
	@Override
	public void update(float d) {
		super.update(d);
		int curScore = ShowChapters.getInstance().getCurrentScore();
		score.setString("得分: " + curScore);	
		
		remain -= d;
		if (remain <= 0)
			remain = 0;
		time.setString("时间剩余: "+(int)remain+"秒");
		
		if (curScore >= 3) {
			if (remain > 1.0f)
				remain = 1.0f;
		}
		
		if (remain <= 0.0f) {
			ShowChapters.getInstance().finishCurrentScene();
		}
	}
	
	private class AnimalSprite extends NodeEventSprite{

		private float x;
		private float y;
		private String name;
		
		@Override
		public boolean ccTouchesBegan(MotionEvent event) {
			if(selectNumber < step2InStep1Number){
				if(AnimalmemoryTest.mAnimalMemoryData.getStep1ShowPictureList().contains(this.name)) {
					ShowChapters.getInstance().getCurrentChapter().mScore += 1;	
					Report.animalmemory_score++;
				}
				selectNumber++;
				stopAllActions();
				removeAllChildren(true);
				getParent().removeChild(this, true);
			}
			return true;
		}

		@Override
		public void onEnter() {
			super.onEnter();
			setPosition(CGPoint.make(x, y));
		}

		public AnimalSprite(String filename, float x, float y, String name) {
			super(filename, scale);
			this.x = x;
			this.y = y;
			this.name = name;
		}

		@Override
		public boolean ccTouchesCancelled(MotionEvent event) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean ccTouchesEnded(MotionEvent event) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean ccTouchesMoved(MotionEvent event) {
			// TODO Auto-generated method stub
			return false;
		}

	}

}
