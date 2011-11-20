package edu.berkeley.cs160.teamk;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
//import android.widget.Toast;
import android.util.Log;


public class ActivitySubmission extends Activity {
	
	String act_name = "";
	String img_filename = "";
	int score = 0;
	Button submit_button;
	String origVal = "";
	EditText edt_Caption;
	Bundle bundle = new Bundle();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activitysubmission);
		
		Log.d("friendHealthAS", "ActivitySubmission drawn");
		
		//---display the image taken---
		ImageView imageView = (ImageView) findViewById(R.id.img_submit);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			act_name = extras.getString("name");
			img_filename = extras.getString("filename");
			score = extras.getInt("score");
			
			Log.d("friendHealthAS", "Full name: " + img_filename);
			String shortname = img_filename.substring(11);
			Log.d("friendHealthAS", "Display name: " + shortname);
			

			Bitmap myBitmap = null;
			myBitmap = BitmapFactory.decodeFile(shortname);
			/*try {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 8;
				myBitmap = BitmapFactory.decodeFile(shortname, options);
			}
			catch (OutOfMemoryError e) {
				Log.d("friendHealthAS", e.toString());
				Toast.makeText(getBaseContext(),
						"OutOfMemoryError: " + e.toString(),
						Toast.LENGTH_LONG).show();
				return;
			}*/
			
			Log.d("friendHealthAS", "Displaying image");
			imageView.setImageBitmap(myBitmap);
			Log.d("friendHealthAS", "Image displayed");

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			myBitmap.compress(CompressFormat.JPEG, 75, bos);
			bundle.putByteArray("picture", bos.toByteArray());
			Utility.mPrefs = getSharedPreferences("FHActivitySelector", MODE_PRIVATE);
			bundle.putString(Facebook.TOKEN, Utility.mPrefs.getString("access_token", null));
			Log.d("friendHealthAS", "access_token: " + Utility.mPrefs.getString("access_token", null));
			
			origVal = "Photo for " + act_name;
				
			edt_Caption = (EditText) findViewById(R.id.edt_Caption);
			edt_Caption.setText(origVal);
			edt_Caption.setOnClickListener(new View.OnClickListener() {
				String message = edt_Caption.getText().toString();

				@Override
				public void onClick(View v) {
						if(message.equals(origVal)) {
							edt_Caption.setText("");
						}
					}
				});
			
			//---get the Submit button---
			submit_button = (Button) findViewById(R.id.btn_Submit);
	        
	        //---event handler for the Submit button---
	        submit_button.setOnClickListener(new View.OnClickListener() {
	        	public void onClick(View view) {
	        		try {
	        			edt_Caption = (EditText) findViewById(R.id.edt_Caption);
	        			String caption = edt_Caption.getText().toString();
	        			if (caption.equals("")) {
	        				bundle.putString("message", "Photo for " + act_name);
	        			} else {
	        				bundle.putString("message", caption);
	        			}
	        			String response = Utility.facebook.request("me/photos", bundle, "POST");
	        			
	        			if (response.indexOf("OAuthException") > -1) {
	        				Log.d("friendHealthBA", "Response: " + response);
	        				response = "Submission Failed";
	        			} else {
	        				Log.d("friendHealthBA", "Response: " + response);
	        				response = "Submission Successful";
	        			}
	        			
	        			if (response == "Submission Successful"){
	        				String jsonUser = Utility.facebook.request("me");
	        				JSONObject obj;
	        				obj = Util.parseJson(jsonUser);
	        				String facebookId = obj.optString("id");
	        				
	        				Utility.mPrefs = getSharedPreferences("FHActivitySelector", MODE_PRIVATE);
	            			Bundle bundle = new Bundle();
	            			bundle.putInt("score", 1);
	            			bundle.putString(Facebook.TOKEN, Utility.mPrefs.getString("access_token", null));
	            			Log.d("friendHealthBA", "Access_token: " + Utility.mPrefs.getString("access_token", null));
	            			String score_response = Utility.facebook.request(facebookId+"/feed", bundle, "POST");
	            			JSONObject score_obj = Util.parseJson(score_response);
	            			String message = score_obj.optString("message");
	            			Log.d("friendHealthAS_Score", message);
	            			
	        			}
	        			
	        			Intent intent = new Intent("edu.berkeley.cs160.teamk.FHActivity");
	        			Bundle extras = getIntent().getExtras();
	        			extras.putString("response", response);
	        			intent.putExtras(extras);
	        			startActivity(intent);
	        		} catch (MalformedURLException e) {
	        		} catch (IOException e) {
	        		} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (FacebookError e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        	}
	        });
		}
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
