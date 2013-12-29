package com.kgrieger.yogaclass;



import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		View.OnClickListener ocl1 = new View.OnClickListener(){
			//add happy birthday message here!
			//April 23rd
			//Happy 33rd birthday Vicki!!!
			@Override
			public void onClick(View v) {
				changeActivity((Button) v);

			}
		};
		Button btnEditPoses = (Button) findViewById(R.id.btnEditPoses);
		Button btnEditClasses = (Button) findViewById(R.id.btnEditClasses);
		Button btnStartClass = (Button) findViewById(R.id.btnStartClass);
		
		
		btnEditPoses.setOnClickListener(ocl1);
		btnEditClasses.setOnClickListener(ocl1);
		btnStartClass.setOnClickListener(ocl1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	void changeActivity(Button b){
		String buttonText = b.getText().toString();
		Log.d("YogaClass",buttonText);
		if(buttonText.equals("Edit Poses")){
		    Intent intent = new Intent(this,  EditPose.class);
		    startActivity(intent);
		} else if(buttonText.equals("Edit Classes")){
				Intent intent = new Intent(this,  EditSession.class);
			    startActivity(intent);	
		} else {
			Intent intent = new Intent(this,  SelectSession.class);
		    startActivity(intent);	
		}


	}

}
