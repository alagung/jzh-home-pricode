package com.raral.childdev;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.types.CGPoint;

import com.raral.childdev.airsearch.AirsearchTest;
import com.raral.childdev.animalmemory.AnimalmemoryTest;
import com.raral.childdev.base.ChildDevBaseChapter;

public class ShowChapters {
	private ChildDevBaseChapter mChapters[] = null;
	private int mChaptersIndex = 0;
	private ChildDevBaseChapter mCurrentChapter = null;

	static class SingletonHolder {     
		static ShowChapters instance = new ShowChapters();
	}

	/** 
	 * returns a singleton instance 
	 */
	public static ShowChapters getInstance() {
		return SingletonHolder.instance;
	}
	
	protected ShowChapters() {
		super();
		mChapters = new ChildDevBaseChapter[] {
				new AnimalmemoryTest(),
				new AirsearchTest(),
		};
		setCurrent(0);
	}
	
	private void setCurrent(int i) {
		mCurrentChapter = mChapters[mChaptersIndex = i];
	}

	private void runScene(CCScene scene) {
		scene.setAnchorPoint(CGPoint.make(0, 0));
		if (CCDirector.sharedDirector().getRunningScene() == null)
			CCDirector.sharedDirector().runWithScene(scene);
		else
			CCDirector.sharedDirector().replaceScene(scene);
	}

	public void start() {
		mCurrentChapter.load();
		mCurrentChapter.resetScene();
		runScene(mCurrentChapter.nextScene());
	}

	public void start(int chaptersIndex) {
		setCurrent(chaptersIndex);
		start();
	}

	public ChildDevBaseChapter getCurrentChapter() {
		return mCurrentChapter;
	}

	public int getCurrentScore() {
		if (mCurrentChapter == null)
			return 0;
		return mCurrentChapter.mScore;
	}

	public void setCurrentScore(int s) {
		if (mCurrentChapter != null)
			mCurrentChapter.mScore = s;
	}

	public int getTotalScore() {
		int s = 0;
		for (int i = 0; i < mChapters.length; ++i) {
			if (mChapters[i].mScore > 0)
				s += mChapters[i].mScore;
		}
		return s;
	}

	public void finishCurrentChapter() {
		if (mCurrentChapter != null)
			mCurrentChapter.onTestFinish();
		if (mChaptersIndex < mChapters.length - 1) {
			setCurrent(mChaptersIndex + 1);
			mCurrentChapter.load();
			mCurrentChapter.resetScene();
			runScene(mCurrentChapter.nextScene());
		} else {
			// All test done
			CCScene object;		
			object = CCScene.node();
			object.addChild(new TerminalLayer());
			runScene(object);
		}
	}

	public void finishCurrentScene() {
		if (mCurrentChapter == null || !mCurrentChapter.hasNextScene())
			finishCurrentChapter();
		else {
			mCurrentChapter.onSceneFinish();
			runScene(mCurrentChapter.nextScene());
		}
	}

	public boolean isRunning(ChildDevBaseChapter test) {
		return test == mCurrentChapter;
	}

	public final boolean postToUI(Runnable r) {
		ChildDevMain act = (ChildDevMain) CCDirector.sharedDirector().getActivity();
		return act.getHandler().post(r);
	}
	
	public ChildDevBaseChapter[] getChapters() {
		return mChapters;
	}	
}
