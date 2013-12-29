package com.kgrieger.yogaclass;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SelectPose extends Activity {
	ArrayAdapter<Pose> 	arrAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_pose);
		
		setTitle("Select Pose");
		
		ListView lvSelectPose = (ListView) findViewById(R.id.lvSelectPose);
		SqlOps db = new SqlOps(this, "YogaClass", null, 2);
		
		arrAdapter = new ArrayAdapter<Pose>(
				this,
				android.R.layout.simple_list_item_1,
				android.R.id.text1,
				db.poseList()
				);
		lvSelectPose.setAdapter(arrAdapter);
		
		lvSelectPose.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				Intent intent = new Intent();
				intent.putExtra("pose_id", arrAdapter.getItem(arg2).getPoseId());
				setResult(2,intent);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_pose, menu);
		return true;
	}

}
