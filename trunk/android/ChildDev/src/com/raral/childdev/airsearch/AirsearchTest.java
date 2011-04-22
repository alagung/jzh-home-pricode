package com.raral.childdev.airsearch;

import java.util.ArrayList;
import com.raral.childdev.base.ChildDevBaseChapter;


public class AirsearchTest extends ChildDevBaseChapter {
	
	public AirsearchTest() {
		super();
	}

	@Override
	public String getDesc() {
		return "��������ҳ��ɻ�";
	}

	@Override
	public String getName() {
		return "�������";
	}

	@Override
	public ArrayList<Class<?>> loadScenes() {
		ArrayList<Class<?>> sceneList = new ArrayList<Class<?>>();
		sceneList.add(com.raral.childdev.airsearch.InstructionScene.class);
		sceneList.add(com.raral.childdev.airsearch.AirsearchScene.class);
		mIsLoaded = true;
		return sceneList;
	}

}
