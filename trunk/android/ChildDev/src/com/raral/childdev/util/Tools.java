package com.raral.childdev.util;

import org.cocos2d.nodes.CCDirector;

public class Tools {
	public static int getFullInteger(float number) {
		return (int)Math.round(number+0.5); 
	}
	
	public static String getString(int resId){
		return CCDirector.sharedDirector().getActivity().getString(resId);
	}
}
