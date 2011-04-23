package com.raral.childdev.animalmemory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Initialize memory test data
 * 
 *  Step 1: 
 *     34 pictures a group, 4 groups.
 *     There are 16 pictures will randomly appear from a set of pictures, and appear for 2 seconds.
 *          
 *  Step 2: 
 *     There are 9 pictures which are appeared in step 1; there are 9 picture which are not appeared in step 1.
 */

public class AnimalmemoryData {
	private static final String ASSETS_PATH_PICTURE = "animalmemory_test";
	private static final String PICTURE_SUFFIX = ".png";
	private static final String BACKGROUND_PICTURE = "bg.png";
	private static final int TOTAL_PICTUREGROUPS = 1;
	private static final int TOTAL_PICTURES = 20;
	private static final int STEP1_SLEEPTIME = 2; //unit second.
	private static final int STEP1_SHOWPICTURES = 8;
	private static final int STEP2_APPEARED = 5;
	private static final int STEP2_UNAPPEARED = 5;
	private static final int PICTURE_WIDTH = 350;
	private static final int PICTURE_HEIGHT = 350;
	private List<Integer> pictureGroupList = new ArrayList<Integer>();
	private List<String> PictureList = new ArrayList<String>();
	private List<String> step1ShowPictureList = new ArrayList<String>();
	private List<String> step1NoShowPictureList = new ArrayList<String>();
	private List<String> step2ShowPictureList = new ArrayList<String>();
	List<String> appearPictures = new ArrayList<String>();
	List<String> unappearPictures = new ArrayList<String>();
	
	public String langGuidanceShow = "";
	public String langGuidanceSelect = "";
	
	public AnimalmemoryData(){
		initData();
	}
	
	public int getStep2InStep1Number() {
		return STEP2_APPEARED;
	}
		
	public int getPictureWidth() {
		return PICTURE_WIDTH;
	}
	
	public int getPictureHeight() {
		return PICTURE_HEIGHT;
	}
	
	public List<String> getStep1ShowPictureList() {
		return step1ShowPictureList;
	}
	
	public List<String> getStep2ShowPictureList() {
		return step2ShowPictureList;
	}
	
	public String getBackgroupPicture() {
		return String.format("%s/%s", ASSETS_PATH_PICTURE, BACKGROUND_PICTURE);
	}
	
	public int getTotalTime() {
		return STEP1_SLEEPTIME * STEP1_SHOWPICTURES;
	}
	
	
	public void initData() {
		assert (TOTAL_PICTUREGROUPS > 0 && TOTAL_PICTURES > 0 && TOTAL_PICTURES >= STEP1_SHOWPICTURES );
		// initialize picture groups
		for(int i=1; i<=TOTAL_PICTUREGROUPS; i++){
			pictureGroupList.add(i);
		}
		Collections.shuffle(pictureGroupList);
		
		// initialize pictures
		for(int i=1; i<=TOTAL_PICTURES; i++){
			PictureList.add(String.format("%s/%d/%d%s", ASSETS_PATH_PICTURE, pictureGroupList.get(0), i, PICTURE_SUFFIX));
		}
		Collections.shuffle(PictureList);
		
		// initialize step 1 pictures
		for(int i=0; i<PictureList.size(); i++){
			if(i < STEP1_SHOWPICTURES) {
				step1ShowPictureList.add(PictureList.get(i));
				appearPictures.add(PictureList.get(i));
			} else {
				step1NoShowPictureList.add(PictureList.get(i));
				unappearPictures.add(PictureList.get(i));
			}
		}
		Collections.shuffle(step1ShowPictureList);
		
		// initialize step 2 pictures
		Collections.shuffle(appearPictures);
		Collections.shuffle(unappearPictures);

		for (int i = 0; i < appearPictures.size(); i++) {
			if (i > STEP2_APPEARED - 1) 
				break;
			step2ShowPictureList.add(appearPictures.get(i));
		}
		for (int i = 0; i < unappearPictures.size(); i++) {
			if (i > STEP2_UNAPPEARED - 1)
				break;
			step2ShowPictureList.add(unappearPictures.get(i));
		}
		Collections.shuffle(step2ShowPictureList);
	}


}
