package com.raral.childdev.animalmemory;

import java.util.ArrayList;
import com.raral.childdev.Report;
import com.raral.childdev.base.ChildDevBaseChapter;

import com.raral.childdev.util.MyLog;


public class AnimalmemoryTest extends ChildDevBaseChapter {
	private static final String LOG_TAG = "AnimalmemoryTest";
	public static AnimalmemoryData mAnimalMemoryData;
	
	public AnimalmemoryTest() {
		super();
		MyLog.v(LOG_TAG, Thread.currentThread().getStackTrace()[2].getMethodName());
		initData();
		// TODO Auto-generated constructor stub
	}
	
	private void initData() {
		Report.animalmemory_score = 0;
		mAnimalMemoryData = new AnimalmemoryData();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "动物记忆";
	}

	@Override
	public String getDesc() {
		// TODO Auto-generated method stub
		return "找出看到过的动物";
	}

	@Override
	public ArrayList<Class<?>> loadScenes() {
		// TODO Auto-generated method stub
		ArrayList<Class<?>> sceneList = new ArrayList<Class<?>>();
		sceneList.add(com.raral.childdev.animalmemory.Instruction1Scene.class);
		sceneList.add(com.raral.childdev.animalmemory.AnimalsShowScene.class);
		sceneList.add(com.raral.childdev.animalmemory.Instruction2Scene.class);
		sceneList.add(com.raral.childdev.animalmemory.AnimalsSelectScene.class);
		mIsLoaded = true;
		return sceneList;
	}
	

}
