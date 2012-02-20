package com.yypie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;

public class Launcher extends ListActivity {
	private static final String[][] gItems = {
		{
	    	"便携式Wifi热点设置",
	    	"com.android.settings", 
	    	"com.android.settings.TetherSettings"
		},
		{
			"密码管理",
			"com.yypie",
			"com.yypie.Manager"
		}
    };
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initListAdapter();        
    }
    
	private void initListAdapter() {

		List<String> items = new ArrayList<String>();
		for (String[] item : gItems) {
			items.add(item[0]);
    	}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, items);
		this.setListAdapter(adapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent();
		intent.setClassName(gItems[position][1],gItems[position][2]);
		startActivity(intent);
		super.onListItemClick(l, v, position, id);
	}
}
