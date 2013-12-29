package com.kgrieger.yogaclass;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v4.app.NavUtils;

public class SelectSession extends Activity {
	ArrayAdapter<Session> 	arrAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_session);
		// Show the Up button in the action bar.
		setupActionBar();
		
		setTitle("Select Session");
		ListView lvSelectSession = (ListView) findViewById(R.id.lvSelectSession);
		SqlOps db = new SqlOps(this, "YogaClass", null, 2);
		
		arrAdapter = new ArrayAdapter<Session>(
				this,
				android.R.layout.simple_list_item_1,
				android.R.id.text1,
				db.sessionList()
				);
		lvSelectSession.setAdapter(arrAdapter);
		
		lvSelectSession.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				startClass(arg2);
			}
		});
	}
	
	public void startClass(int index){
	    Intent intent = new Intent(this,  YogaClass.class);
	    Session session = arrAdapter.getItem(index);
	    intent.putExtra("session_id",session.getSessionId());
	    intent.putExtra("name", session.getName());
	    startActivity(intent);
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
		getMenuInflater().inflate(R.menu.select_session, menu);
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
