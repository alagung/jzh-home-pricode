package com.raral.childdev;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSpriteFrameCache;
import org.cocos2d.nodes.CCTextureCache;

import com.raral.childdev.R;
import com.raral.childdev.util.MyLog;



import android.app.ExpandableListActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;


public class Launcher extends ExpandableListActivity {
	private static final String LOG_TAG = "Launcher";
    private static final String KEY = "KEY";
    
	private ExpandableListAdapter adapter;
    private List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
    private List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
    
    private static final String[] groupNames = {
    	"幼儿发展",
    };
    
    private static final String[][] childNames = {
    	{
    		"动物记忆",
    	},    	
    };
    private static final Object[][] activities = {
    	{
    		ChildDevMain.class,  		
    	},
    };
    
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyLog.i(LOG_TAG, "Contact Email: yitercel@msn.com");
		this.requestFullScreen();
		startActivity(new Intent(Launcher.this,ChildDevMain.class));  
//		initListAdapter();
		this.setContentView(R.layout.main);
	}
	
	private void initListAdapter() {

		for (int i = 0; i < groupNames.length; i++) {
			String groupName = groupNames[i];
            Map<String, String> curGroupMap = new HashMap<String, String>();
            groupData.add(curGroupMap);
            curGroupMap.put(KEY, groupName);
            
        	List<Map<String, String>> children = new ArrayList<Map<String, String>>();
        	
        	String[] childName = childNames[i];
        	for (String name : childName) {
                Map<String, String> curChildMap = new HashMap<String, String>();
        		curChildMap.put(KEY, name);
        		children.add(curChildMap);
        	}
        	childData.add(children);
        }
				
		adapter = new SimpleExpandableListAdapter(
				this, 
                groupData,
                android.R.layout.simple_expandable_list_item_1,
                new String[] { KEY },
                new int[] { android.R.id.text1 },
                childData,
                android.R.layout.simple_expandable_list_item_2,
                new String[] { KEY },
                new int[] { android.R.id.text1 }
		);
		this.setListAdapter(adapter);
	}
	
	@Override
	public boolean onChildClick(ExpandableListView parent, View view, 
			int groupPosition, int childPosition, final long id) {
		Intent intent = new Intent(this, (Class<?>)activities[groupPosition][childPosition]);
		startActivity(intent);
		return super.onChildClick(parent, view, groupPosition, childPosition, id);
	}
	
    @Override
    public void onDestroy() {
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0); 
    }

	private void requestFullScreen() {
		Window window = this.getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		window.requestFeature(Window.FEATURE_NO_TITLE);
		
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		MyLog.v(LOG_TAG, String.format("onCreate-getMetrics, w:%d, h:%d", metric.widthPixels, metric.heightPixels));
//		Display display = getWindowManager().getDefaultDisplay();
//		MyLog.v(LOG_TAG, String.format("onCreate-getDefaultDisplay, w:%d, h:%d", display.getWidth(), display.getHeight()));
		if(metric.heightPixels > metric.widthPixels) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		
	}
}
