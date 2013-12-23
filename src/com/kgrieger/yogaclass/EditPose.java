package com.kgrieger.yogaclass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.os.Build;

public class EditPose extends Activity {

	ArrayAdapter<Pose> arrAdapter;
	ListView posesListView;
	EditText etName, etmDescription;
	Button btnCreate, btnSave, btnDelete;
	SqlOps db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_pose);
		// Show the Up button in the action bar.
		setupActionBar();
		setTitle("Edit Poses");
		
		db = new SqlOps(this, "YogaClass", null, 1);
		
		etName = (EditText) findViewById(R.id.etName);
		etmDescription = (EditText) findViewById(R.id.etmDescription);
		btnCreate = (Button) findViewById(R.id.btnCreate);
		btnSave = (Button) findViewById(R.id.btnSave);
		btnDelete = (Button) findViewById(R.id.btnDelete);
		
		View.OnClickListener l1 = new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bigEventHandler(v,-1);
			}
		};
		
		btnCreate.setOnClickListener(l1);
		btnSave.setOnClickListener(l1);
		btnDelete.setOnClickListener(l1);
		
		
		setCreateState();
		
		
		posesListView = (ListView) findViewById(R.id.lvPoses);

		arrAdapter = new ArrayAdapter<Pose>(
				this,
				android.R.layout.simple_list_item_single_choice,
				android.R.id.text1,
				db.poseList()
				);
		posesListView.setAdapter(arrAdapter);

		posesListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) { 
				bigEventHandler(view,arg2);
			}
		});
	}
	

	public void setCreateState(){
		btnCreate.setEnabled(false);
		btnDelete.setEnabled(false);
		etName.setText("");
		etmDescription.setText("");
	}
	
	public int getSelectedChildIndex(){
		for(int a = 0; a< posesListView.getChildCount(); a++){
			CheckedTextView ctv = (CheckedTextView) posesListView.getChildAt(a);
			if(ctv.isChecked())
				return a;
		}
		return -1;
	}

	public void bigEventHandler(View view, int position){
		String className = view.getClass().getName();
		String buttonText = "";
		if(className.equals("android.widget.Button")){
			buttonText = ((Button) view).getText().toString();
		}
		
		//create
		if((!btnCreate.isEnabled()) && (!btnDelete.isEnabled())
				&& buttonText.equals("Save")){
			Pose pose = new Pose(etName.getText().toString(), 
					etmDescription.getText().toString());
			db.poseCreate(pose);
			arrAdapter.clear();
			arrAdapter.addAll(db.poseList());
		}
		
		//read
		if(position != -1 && buttonText.equals("")){
			Pose pose = arrAdapter.getItem(position);
			etName.setText(pose.getName());
			etmDescription.setText(pose.getDescription());
			btnCreate.setEnabled(true);
			btnDelete.setEnabled(true);
		}
		
		//update
		if(btnCreate.isEnabled() && btnDelete.isEnabled() && buttonText.equals("Save")){
			Pose pose = arrAdapter.getItem(getSelectedChildIndex());
			pose.setName(etName.getText().toString());
			pose.setDescription(etmDescription.getText().toString());
			db.poseUpdate(pose);
			arrAdapter.clear();
			arrAdapter.addAll(db.poseList());
		}
		
		//delete
		if(btnCreate.isEnabled() && btnDelete.isEnabled() && buttonText.equals("Delete")){
			Pose pose = arrAdapter.getItem(getSelectedChildIndex());
			db.poseDelete(pose);
			arrAdapter.clear();
			arrAdapter.addAll(db.poseList());
		}
		
		//go to create mode
		if(btnCreate.isEnabled() && btnDelete.isEnabled() && buttonText.equals("Create")){
			setCreateState();
		}
	}


	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_pose, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}

class Pose{
	private int pose_id;
	private String name, description;
	
	public Pose(int i, String n, String d){
		this.pose_id = i;
		this.name = n;
		this.description = d;
	}
	
	public Pose(String n, String d){
		this.name = n;
		this.description = d;
	}
	
	public String toString(){
		return this.getName();
	}
	
	public String toString2(){
		return String.format(
				"pose_id: %d name: %s description: %s",
				this.getPoseId(),
				this.getName(),
				this.getDescription());
	}
	
	public int getPoseId(){
		return this.pose_id;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public void setPoseId(int pose_id){
		this.pose_id = pose_id;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
}



















