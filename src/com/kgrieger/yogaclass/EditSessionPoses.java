package com.kgrieger.yogaclass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class EditSessionPoses extends Activity {
	
	int session_id;
	String name;
	ArrayAdapter<SessionPose> arrAdapter;
	SqlOps db;
	SessionPose spSelected;
	CheckedTextView ctvSelected;
	Integer lvSelected =null;
	ListView lvSessionPoses;
	
	SeekBar sbDuration;
	
	Button btnRemove;
	Button btnMoveUp;
	Button btnMoveDown;
	
	TextView tvTotal;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_session_poses);
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			session_id = extras.getInt("session_id");
			name = extras.getString("name");
			setTitle(name);
		}
		// Show the Up button in the action bar.
		setupActionBar();
		
		db = new SqlOps(this, "YogaClass", null, 2);
		
		Button btnSaveExit = (Button) findViewById(R.id.btnSaveExit);
		Button btnAddPose = (Button) findViewById(R.id.btnAddPose);
		btnRemove = (Button) findViewById(R.id.btnRemove);
		btnMoveUp = (Button) findViewById(R.id.btnMoveUp);
		btnMoveDown = (Button) findViewById(R.id.btnMoveDown);
		
		sbDuration = (SeekBar) findViewById(R.id.sbDuration);
		TextView tvNameDescription = (TextView) findViewById(R.id.tvNameDescription);
		tvTotal = (TextView) findViewById(R.id.tvTotal);
		TextView tvDuration = (TextView) findViewById(R.id.tvDuration);
		
		tvNameDescription.setText("");
		

		
		sbDuration.setMax(60);
		sbDuration.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if(spSelected != null && ctvSelected != null){
					spSelected.setDuration(progress);
					ctvSelected.setText(spSelected.toString());
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				setTotal();
				
			}
			
		});
		
		lvSessionPoses = (ListView) findViewById(R.id.lvSessionPoses);
		arrAdapter = new ArrayAdapter<SessionPose>(
				this,
				android.R.layout.simple_list_item_single_choice,
				android.R.id.text1,
				db.sessionPoseList(session_id)
				);
		lvSessionPoses.setAdapter(arrAdapter);
		lvSessionPoses.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				spSelected = arrAdapter.getItem(arg2);
				ctvSelected = (CheckedTextView) arg1;
				lvSelected = arg2;
				bigEventHandler(arg1,arg2);
			}
		});
		
		
		View.OnClickListener l1 = new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bigEventHandler(v,-1);
			}
		};
		btnSaveExit.setOnClickListener(l1);
		btnAddPose.setOnClickListener(l1);
		btnRemove.setOnClickListener(l1);
		btnMoveUp.setOnClickListener(l1);
		btnMoveDown.setOnClickListener(l1);
		

		setClearedState();
		setTotal();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);  
		if(requestCode==2)  
        {  
           int pose_id=data.getIntExtra("pose_id",-1); 
           if(pose_id != -1){
	           Pose pose = db.poseRead(pose_id);
	           SessionPose sp = new SessionPose(pose, session_id, arrAdapter.getCount()-1);
	           arrAdapter.add(sp);
	           setTotal();
           }
        }  
		
	}
	
	public void setClearedState(){
		spSelected=null;
		ctvSelected=null;
		lvSelected=null;
		btnRemove.setEnabled(false);
		btnMoveUp.setEnabled(false);
		btnMoveDown.setEnabled(false);
		sbDuration.setProgress(0);
		lvSessionPoses.clearChoices();
	}
	
	public ArrayList<SessionPose> getArrayList(){
		ArrayList<SessionPose> spArray = new ArrayList<SessionPose>();
		for(int a = 0; a<arrAdapter.getCount();a++){
			SessionPose sp = arrAdapter.getItem(a);
			sp.setPosition(a);
			spArray.add(sp);
		}
		return spArray;
	}
	
	public void setTotal(){
		int total =0;
		for(int a=0; a< arrAdapter.getCount(); a++){
			total += arrAdapter.getItem(a).getDuration();
		}
		tvTotal.setText(String.format("Total: %d minutes", total));
	}
	
	public void swapValues(int index1, int index2){
		ArrayList<SessionPose> spArray = getArrayList();
		Collections.swap(spArray, index1, index2);
		arrAdapter.clear();
		arrAdapter.addAll(spArray);
		lvSessionPoses.clearChoices();
		lvSessionPoses.setItemChecked(index2, true);
		spSelected = arrAdapter.getItem(index2);
		ctvSelected = (CheckedTextView) lvSessionPoses.getChildAt(index2);
		lvSelected = index2;
	}
	

	
	public void bigEventHandler(View view, int lvPosition){
		String className = view.getClass().getName();
		String buttonText = "";
		if(className.equals("android.widget.Button")){
			buttonText = ((Button) view).getText().toString();
		}
		
		if(buttonText.equals("Add Pose")){
			Intent intent = new Intent(this,  SelectPose.class);
		    startActivityForResult(intent,2);
		}
		
		if(buttonText.equals("Save & Exit")){
			db.sessionPoseUpdateAll(getArrayList());
			finish();
		}
		
		if(buttonText.equals("Move UP")){
			if(lvSelected!=null && lvSelected !=0 && arrAdapter.getCount() > 1){
				swapValues(lvSelected,lvSelected-1);
			}
		}
		
		if(buttonText.equals("Move Down")){
			if(lvSelected!=null && lvSelected != (arrAdapter.getCount()-1) && arrAdapter.getCount() > 1){
				swapValues(lvSelected,lvSelected+1);
			}
		}
		
		if(buttonText.equals("Remove")){
			ArrayList<SessionPose> spArray = getArrayList();
			spArray.remove((int)lvSelected);
			arrAdapter.clear();
			arrAdapter.addAll(spArray);
			setClearedState();
			setTotal();
		}
		
		if(lvPosition != -1){
			btnRemove.setEnabled(true);
			btnMoveUp.setEnabled(true);
			btnMoveDown.setEnabled(true);
			sbDuration.setProgress(spSelected.getDuration());
		}
		
		
	}
	


	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_session_poses, menu);
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

class SessionPose{
	private int session_pose_id, session_id, position, duration;
	private Pose pose;
	
	public SessionPose(Pose p, int spid, int sid, int pos, int dur){
		this.pose = p;
		this.session_pose_id = spid;
		this.session_id = sid;
		this.position = pos;
		this.duration = dur;
	}
	
	public SessionPose(Pose p, int sid, int pos){
		this.pose = p;
		this.session_pose_id = -1;
		this.session_id = sid;
		this.position = pos;
		this.duration = 5;
	}

	public String toString(){
		return String.format("%s  (%d min)", this.pose.getName(), this.duration);
	}
	
	public Pose getPose(){
		return this.pose;
	}
	
	public int getSessionPoseId(){
		return this.session_pose_id;
	}
	
	public int getSessionId(){
		return this.session_id;
	}
	
	public int getPosition(){
		return this.position;
	}
	
	public int getDuration(){
		return this.duration;
	}

	public void setPose(Pose p){
		this.pose = p;
	}
	
	public void setSessionPoseId(int spid){
		this.session_pose_id = spid;
	}
	
	public void setSessionId(int sid){
		this.session_id = sid;
	}
	
	public void setPosition(int pos){
		this.position = pos;
	}
	
	public void setDuration(int dur){
		this.duration = dur;
	}
	
	
	
}


