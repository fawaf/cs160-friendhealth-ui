package edu.berkeley.cs160.teamk;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class AddTaskActivity extends Activity {
	
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addtask);
		
		//---get the Add Task button---
		Button btn_addTask = (Button) findViewById(R.id.btn_addTask);
		
		//---event handler for the Add Task button---
		btn_addTask.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent data = new Intent();
				Bundle extras = new Bundle();
				
				//---get the Task name---
				EditText txt_taskName =
						(EditText) findViewById(R.id.taskName);
				String taskName = txt_taskName.getText().toString();
				Log.d("friendHealthATA", "Task name: " + taskName);
				//---set the data to pass back---
				extras.putString("name", taskName);
				
				// Get Tags.
				EditText txt_taskTags =
						(EditText) findViewById(R.id.taskTags);
				String taskTags = txt_taskTags.getText().toString(); 
				Log.d("friendHealthATA", "Task tag: " + taskTags);
				// Put tags into the Bundle.
				extras.putString("tags", taskTags);
				// Only adding task rather taking picture.
				extras.putString("result", "only_add");
				
				data.putExtras(extras);
				setResult(RESULT_OK, data);
				
				// Close the activity.
				finish();
			}
		});
		
		//---get the Add Task button---
		Button btn_takePicture= (Button) findViewById(R.id.btn_takePicture);
		
		//---event handler for the Add Task button---
		btn_takePicture.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Log.d("friendHealthATA", "WHY YOU NO WORK");
			}
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case Utility.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
			onCameraActivityResult(resultCode, data);
			return;
		case Utility.RC_ACTIVITYSUBMISSION:
			onActivitySubmissionResult(resultCode, data);
			return;
		}
	}
	
	
	private void onCameraActivityResult(int resultCode, Intent data) {
		
	}
	
	
	private void onActivitySubmissionResult(int resultCode, Intent data) {
		
	}
	
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	OptionsMenu om = new OptionsMenu();
    	om.CreateMenu(menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	OptionsMenu om = new OptionsMenu();
    	return om.MenuChoice(this, item);
    }

}