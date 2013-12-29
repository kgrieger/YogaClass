package com.kgrieger.yogaclass;

import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class YogaClass extends Activity {
	
	int session_id;
	String name;
	List<SessionPose> sessionPoseList;
	TextView tvName;
	TextView tvDescription;
	TextView tvPoseTime;
	TextView tvTotalTime;
	
	SeekBar sbSessionTime;
	
	Button btnPlayPause;
	
	Handler timerHandler;
	Runnable timerRunnable;
	
	long startTime = 0;
	long offset = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yoga_class);
		// Show the Up button in the action bar.
		setupActionBar();
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			session_id = extras.getInt("session_id");
			name = extras.getString("name");
			setTitle(name);
		}
		
		SqlOps db = new SqlOps(this, "YogaClass", null, 2);
		
		sessionPoseList = db.sessionPoseList(session_id);
		
		int sessionLength = 0;
		for(SessionPose sp : sessionPoseList){
			sessionLength += sp.getDuration();
		}
		
		tvName = (TextView) findViewById(R.id.tvName);
		tvDescription = (TextView) findViewById(R.id.tvDescription);
		tvPoseTime = (TextView) findViewById(R.id.tvPoseTime);
		tvTotalTime = (TextView) findViewById(R.id.tvTotalTime);
		
		sbSessionTime = (SeekBar) findViewById(R.id.sbSessionTime);
		
		sbSessionTime.setMax(sessionLength);
		
		btnPlayPause = (Button) findViewById(R.id.btnPlayPause);
		
		btnPlayPause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                if (b.getText().equals("Pause")) {
                    timerHandler.removeCallbacks(timerRunnable);
                    b.setText("Play");
                } else {
                	if(startTime==0){
                		startTime = System.currentTimeMillis();
                	}else{
                		startTime = System.currentTimeMillis() - offset;
                	}
                    timerHandler.postDelayed(timerRunnable, 0);
                    b.setText("Pause");
                }
            }
        });
		
		timerHandler = new Handler();
	    timerRunnable = new Runnable() {

	        @Override
	        public void run() {
	            offset = System.currentTimeMillis() - startTime;
	            int seconds = (int) (offset / 1000);
	            int minutes = seconds / 60;
	            seconds = seconds % 60;
	           
	            tvPoseTime.setText(String.format("%d:%02d", minutes, seconds));
	            
	            Pose currentPose = getSessionPoseForTime(seconds).getPose();
	            tvName.setText(currentPose.getName());
	            tvDescription.setText(currentPose.getDescription());

	            if(sbSessionTime.getProgress() < sbSessionTime.getMax()){
	            	sbSessionTime.setProgress(seconds);
	            	timerHandler.postDelayed(this, 500);
	            }else
	            {
	            	tvPoseTime.setText("Yooure done!!!");
	            }
	            
	        }
	    };
	}
	
	  @Override
	    public void onPause() {
	        super.onPause();
	        timerHandler.removeCallbacks(timerRunnable);
	        btnPlayPause.setText("Play");
	    }
	  
	public SessionPose getSessionPoseForTime(int secondsPast){
		int secondsMore = 0;
		for(int a = 0; a < sessionPoseList.size(); a++){
			secondsMore += sessionPoseList.get(a).getDuration();
			if(secondsPast < secondsMore){
				return sessionPoseList.get(a);
			}
		}
		return sessionPoseList.get(sessionPoseList.size()-1);
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
		getMenuInflater().inflate(R.menu.yoga_class, menu);
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
