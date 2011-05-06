package com.raral.childdev.animalmemory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.cocos2d.nodes.CCLabel;
import org.cocos2d.types.CGPoint;

import android.view.MotionEvent;

import com.raral.childdev.Report;
import com.raral.childdev.ShowChapters;
import com.raral.childdev.base.ChildBaseSprite;
import com.raral.childdev.base.NodeEventLayer;
import com.raral.childdev.base.NodeEventSprite;
import com.raral.childdev.util.MyLog;
import com.raral.childdev.util.Tools;

public class AnimalsSelectScene extends NodeEventLayer {
	private static final String LOG_TAG = "AnimalSelectLayer";
	CCLabel mScore;
	CCLabel mTime;
	float mRemain = 60.0f;
	List<String> mStep2ShowPictureList;
	int mSelectNumber = 0;
	int mStep2InStep1Number = 0;
	float mScale = 1.0f;  // for drawing picture
	int mPicturesALine = 4; // show 4 pictures a line
	float mPicturesPadding = 30; // pixel
	float mPicDisplayWidth = 0;
	float mPicDisplayHeight = 0;
	int mScoreTextHeight = 20;

	List<String> mSelectPictures = new ArrayList<String>();
	HashMap<String, PicPostion> picturesPostion = new HashMap<String, PicPostion>();

	public AnimalsSelectScene() {
		super();
		MyLog.v(LOG_TAG, Thread.currentThread().getStackTrace()[2].getMethodName());
		
		mStep2ShowPictureList = AnimalmemoryTest.mAnimalMemoryData.getStep2ShowPictureList();
		mStep2InStep1Number = AnimalmemoryTest.mAnimalMemoryData.getStep2InStep1Number();

		getPictureSizeAndScale();

		MyLog.v(LOG_TAG, String.format("mScale:%f, w:%f, h:%f, padding:%f", mScale, mPicDisplayWidth, mPicDisplayHeight, mPicturesPadding));
		// padding+pic+padding+pic+padding
		for( int i=0; i<mStep2ShowPictureList.size(); i++) {
			String pic = mStep2ShowPictureList.get(i);
			float x = (i % mPicturesALine) * mPicDisplayWidth + mPicturesPadding*((i % mPicturesALine)+1);
			float y = (i / mPicturesALine) * mPicDisplayHeight + mPicturesPadding*((i / mPicturesALine)+1);
			MyLog.v(LOG_TAG, String.format("i:%d, x:%f, y:%f", i, x, y));
			picturesPostion.put(pic, new PicPostion(x, y));
//			addSimpleAct(new AnimalSprite(pic, x, y, pic), 1.0f, null);
			AnimalSprite anims = new AnimalSprite(pic, x, y, pic);
			anims.setAnchorPoint(0, 0);
			anims.setPosition(CGPoint.make(x, y));
			addChild(anims, 0);
		}
        
        mScore = CCLabel.makeLabel("得分: 0", "DroidSans", 18);
        mScore.setAnchorPoint(1.0f, 1.0f);
        mScore.setPosition(CGPoint.make(Tools.getScreenWidth() - 20, Tools.getScreenHeight() - mScoreTextHeight));
        addChild(mScore, 0);
        
        mTime = CCLabel.makeLabel("时间剩余: 60秒", "DroidSans", 18);
        mTime.setAnchorPoint(0.0f, 1.0f);
        mTime.setPosition(CGPoint.make(20, Tools.getScreenHeight() - mScoreTextHeight));
        addChild(mTime, 0);
	}

	private void getPictureSizeAndScale() {
		mPicturesPadding *= Tools.getSizeScale();
		if(mStep2ShowPictureList.size() < mPicturesALine)
			mPicturesALine = mStep2ShowPictureList.size();
		
		mPicDisplayWidth = (Tools.getScreenWidth() - mPicturesPadding*(mPicturesALine+1)) / mPicturesALine;
		int pictureLines = Tools.getFullInteger(mStep2ShowPictureList.size() / mPicturesALine);
		mPicDisplayHeight = (Tools.getScreenHeight() - mScoreTextHeight - mPicturesPadding*(pictureLines+1)) / pictureLines;
		
		float wScale = mPicDisplayWidth / AnimalmemoryTest.mAnimalMemoryData.getPictureWidth();
		float hScale = mPicDisplayHeight / AnimalmemoryTest.mAnimalMemoryData.getPictureHeight();	
		MyLog.v(LOG_TAG, String.format("wScale:%f, hScale:%f", wScale, hScale));
		if(wScale < hScale){
			mScale = wScale;
			mPicDisplayHeight = AnimalmemoryTest.mAnimalMemoryData.getPictureHeight() * mScale;
		} else {
			mScale = hScale;
			mPicDisplayWidth = AnimalmemoryTest.mAnimalMemoryData.getPictureWidth() * mScale;
		}
	}
	
	private void drawSelectPicture(String showPic, String overPic) {
		ChildBaseSprite selectSprite = new ChildBaseSprite(overPic, mScale);
		PicPostion picpos = picturesPostion.get(showPic);
//		MyLog.v(LOG_TAG, String.format("ccTouchesBegan: selectPic:%s, x:%f, y:%f", selectPic, x, y));
		selectSprite.setAnchorPoint(0.0f, 0.0f);
		selectSprite.setPosition(CGPoint.make(picpos.x, picpos.y));
		addChild(selectSprite, 1);
	}
	
	@Override
	public void onEnter() {
		super.onEnter();
	}
	
	@Override
	public void update(float d) {
		super.update(d);
		int curScore = ShowChapters.getInstance().getCurrentScore();
		mScore.setString("得分: " + curScore);	
		
		mRemain -= d;
		if (mRemain <= 0)
			mRemain = 0;
		mTime.setString("时间剩余: "+(int)mRemain+"秒");
		
		if (mSelectNumber >= mStep2InStep1Number) {
			if (mRemain > 2.0f)
				mRemain = 2.0f;
		}
		
		if (mRemain <= 0.0f) {
			ShowChapters.getInstance().finishCurrentScene();
		}
	}
	
	private class AnimalSprite extends NodeEventSprite{

		private float x;
		private float y;
		private String name;
		
		@Override
		public boolean ccTouchesBegan(MotionEvent event) {
			
			if(!mSelectPictures.contains(this.name)) {
				mSelectPictures.add(this.name);
				mSelectNumber++;
			}
				
			if(mSelectNumber <= mStep2InStep1Number){
				String overPic = "";
				if(AnimalmemoryTest.mAnimalMemoryData.getStep1ShowPictureList().contains(this.name)) {
					ShowChapters.getInstance().getCurrentChapter().mScore += 1;	
					Report.animalmemory_score++;
					overPic = AnimalmemoryTest.mAnimalMemoryData.getRightPicture();
				} else {
					overPic = AnimalmemoryTest.mAnimalMemoryData.getWrongPicture();
				}
				
				drawSelectPicture(this.name, overPic);
//				stopAllActions();
//				removeAllChildren(true);
//				getParent().removeChild(this, true);
			}
			return true;
		}

		@Override
		public void onEnter() {
			super.onEnter();
			setAnchorPoint(0.0f, 0.0f);
			setPosition(CGPoint.make(x, y));
		}

		public AnimalSprite(String filename, float x, float y, String name) {
			super(filename, mScale);
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
	
	private class PicPostion {
		float x;
		float y;
		
		PicPostion(float x, float y) {
			this.x = x;
			this.y = y;
		}
	}

}
