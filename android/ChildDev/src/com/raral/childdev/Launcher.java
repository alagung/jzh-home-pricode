package com.raral.childdev;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.raral.childdev.R;



import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;


public class Launcher extends ExpandableListActivity {
	
    private static final String KEY = "KEY";
    
	private ExpandableListAdapter adapter;
    private List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
    private List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
    
    private static final String[] groupNames = {
    	"Ó×¶ù·¢Õ¹",
    };
    
    private static final String[][] childNames = {
    	{
    		"Ìì¿ÕËÑË÷",
    		"²âÊÔ",
    	},    	
    };
    private static final Object[][] activities = {
    	{
    		ChildDevMain.class,
    		TestMain.class,    		
    	},
    };
    
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
		this.requestFullScreen();

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
		this.setContentView(R.layout.main);
	}
	
	private void initData() {
		// will use reflection 
		Language.string.animalmemory_test_instruction_select = getString(R.string.animalmemory_test_instruction_select);
		Language.string.animalmemory_test_instruction_select = getString(R.string.animalmemory_test_instruction_select);
	}
	
	@Override
	public boolean onChildClick(ExpandableListView parent, View view, 
			int groupPosition, int childPosition, final long id) {
		Intent intent = new Intent(this, (Class<?>)activities[groupPosition][childPosition]);
		startActivity(intent);
		return super.onChildClick(parent, view, groupPosition, childPosition, id);
	}

	private void requestFullScreen() {
		Window window = this.getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		window.requestFeature(Window.FEATURE_NO_TITLE);
	}
}
