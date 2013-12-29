package com.kgrieger.yogaclass;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class EditSession extends Activity {

	ArrayAdapter<Session> arrAdapter;
	ListView sessionsListView;
	EditText etName, etmDescription;
	Button btnCreate, btnSave, btnDelete, btnEditPoses;
	SqlOps db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_session);
		// Show the Up button in the action bar.
		setupActionBar();
		setTitle("Edit Sessions");
		
		db = new SqlOps(this, "YogaClass", null, 2);
		
		etName = (EditText) findViewById(R.id.etName);
		etmDescription = (EditText) findViewById(R.id.etmDescription);
		btnCreate = (Button) findViewById(R.id.btnCreate);
		btnSave = (Button) findViewById(R.id.btnSave);
		btnDelete = (Button) findViewById(R.id.btnDelete);
		btnEditPoses = (Button) findViewById(R.id.btnEditPoses);
		
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
		btnEditPoses.setOnClickListener(l1);
		
		setCreateState();
		
		sessionsListView = (ListView) findViewById(R.id.lvClasses);

		arrAdapter = new ArrayAdapter<Session>(
				this,
				android.R.layout.simple_list_item_single_choice,
				android.R.id.text1,
				db.sessionList()
				);
		sessionsListView.setAdapter(arrAdapter);

		sessionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

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
		btnEditPoses.setEnabled(false);
		etName.setText("");
		etmDescription.setText("");
	}
	
	public int getSelectedChildIndex(){
		for(int a = 0; a< sessionsListView.getChildCount(); a++){
			CheckedTextView ctv = (CheckedTextView) sessionsListView.getChildAt(a);
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
			Session session = new Session(etName.getText().toString(), 
					etmDescription.getText().toString());
			db.sessionCreate(session);
			arrAdapter.clear();
			arrAdapter.addAll(db.sessionList());
			setCreateState();
		}
		
		//read
		if(position != -1 && buttonText.equals("")){
			Session session = arrAdapter.getItem(position);
			etName.setText(session.getName());
			etmDescription.setText(session.getDescription());
			btnCreate.setEnabled(true);
			btnDelete.setEnabled(true);
			btnEditPoses.setEnabled(true);
		}
		
		//update
		if(btnCreate.isEnabled() && btnDelete.isEnabled() && buttonText.equals("Save")){
			Session session = arrAdapter.getItem(getSelectedChildIndex());
			session.setName(etName.getText().toString());
			session.setDescription(etmDescription.getText().toString());
			db.sessionUpdate(session);
			arrAdapter.clear();
			arrAdapter.addAll(db.sessionList());
			setCreateState();
		}
		
		//delete
		if(btnCreate.isEnabled() && btnDelete.isEnabled() && buttonText.equals("Delete")){
			Session session = arrAdapter.getItem(getSelectedChildIndex());
			db.sessionDelete(session);
			arrAdapter.clear();
			arrAdapter.addAll(db.sessionList());
			setCreateState();
		}
		
		//go to create mode
		if(btnCreate.isEnabled() && btnDelete.isEnabled() && buttonText.equals("Create")){
			setCreateState();
		}
		
		//go to EditSessionPoses Activity
		if(buttonText.equals("Edit Poses")){
			Intent intent = new Intent(this,  EditSessionPoses.class);
			Session session = arrAdapter.getItem(getSelectedChildIndex());
			intent.putExtra("session_id",session.getSessionId());
			intent.putExtra("name", session.getName());
		    startActivity(intent);	
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
		getMenuInflater().inflate(R.menu.edit_class, menu);
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

class Session{
	private int session_id;
	private String name, description;
	
	public Session(int i, String n, String d){
		this.session_id = i;
		this.name = n;
		this.description = d;
	}
	
	public Session(String n, String d){
		this.name = n;
		this.description = d;
	}
	
	public String toString(){
		return this.getName();
	}
	
	public String toString2(){
		return String.format(
				"session_id: %d name: %s description: %s",
				this.getSessionId(),
				this.getName(),
				this.getDescription());
	}
	
	public int getSessionId(){
		return this.session_id;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public void setSessionId(int session_id){
		this.session_id = session_id;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
}