/**Homework 4 - Filename: MainActivity.java
 * Team members:	
 * Sriram Padavala
 * Abdul Aaquib
 * 
 * 10/04/2013
 */

package com.example.photogallery;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;

import android.os.AsyncTask;
import android.os.Bundle;
//import android.os.Parcelable;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

public class MainActivity extends Activity {
	Intent intent;// = new Intent(getApplicationContext(), PhotosActivity.class);
    
	RadioGroup radio;
	Button b1;
	Button b2;
	ProgressDialog pd;
	final Context context = this;
	
	int FORMAT;		//0 = XML , 1 = JSON
	String FlickrQuery_url = "http://api.flickr.com/services/rest/?method=flickr.photos.search";
	String FlickrQuery_per_page = "&per_page=100";
	String FlickrQuery_nojsoncallback = "&nojsoncallback=1";
	String FlickrQuery_format = "&format=";
	String FlickrQuery_tag = "&tags=";
	String FlickrQuery_extras = "&extras=views,url_m";
	String FlickrQuery_key = "&api_key=";
	String qResult = null;
	
	String FlickrApiKey = "a44b0f2b662d63e0b286d122f6a14e03";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		intent = new Intent(this, PhotosActivity.class);
//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//	    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
		
		b1 = (Button)findViewById(R.id.button1);
		b2 = (Button)findViewById(R.id.button2);
		
		radio = (RadioGroup)findViewById(R.id.radioGroup1);
		
		b1.setOnClickListener(new ButtonClick(b1));
		b2.setOnClickListener(new ButtonClick(b2));
		
		radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				
				switch(radio.getCheckedRadioButtonId()){
				
				case R.id.radio0:
					FORMAT = 0;
					
					break;
				case R.id.radio1:
					FORMAT = 1;
					
					break;
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public class ButtonClick implements View.OnClickListener{
		Button b;
		
		ButtonClick(Button b){
			this.b = b;
		}
		@Override
		public void onClick(View v) {
			
			if(b.getId() == R.id.button1){
				intent.putExtra("type", "photos");
				
				//startActivity(intent);
			}
			else if(b.getId() == R.id.button2){
				intent.putExtra("type", "slide");
				//startActivity(intent);
			}
            if(FORMAT == 0){
//
//          	  String qString =
//          	    FlickrQuery_url
//          	    + FlickrQuery_per_page
//          	    + FlickrQuery_nojsoncallback
//          	    + FlickrQuery_format + "xml"
//          	    + FlickrQuery_tag + "uncc"
//          	    + FlickrQuery_key + FlickrApiKey;
            }
            else if(FORMAT == 1){
            	String qString =
	            	    FlickrQuery_url
	            	    + FlickrQuery_per_page
	            	    + FlickrQuery_nojsoncallback
	            	    + FlickrQuery_format + "json"
	            	    + FlickrQuery_tag + "uncc"
	            	    + FlickrQuery_extras
	            	    + FlickrQuery_key + FlickrApiKey;
	            	  
	            	  new RetrieveImagesInfo().execute(qString);
//            	  
            }
            
		}
		
	}
	
	public class RetrieveImagesInfo extends AsyncTask<String, Void, ArrayList<PhotoInfo>> {

		@Override
		protected ArrayList<PhotoInfo> doInBackground(String... params) {
			// TODO Auto-generated method stub
			StringBuilder sb = new StringBuilder();
			try {
				URL url = new URL(params[0]);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("GET");
				con.connect();
				int statusCode = con.getResponseCode();
				if(statusCode == HttpURLConnection.HTTP_OK){
					BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String line = reader.readLine();
					while(line!=null){
						sb.append(line);
						line = reader.readLine();
					}
					return ParserUtil.JSONParser.parsePhotos(sb.toString());
				}
				
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
//			HttpClient httpClient = new DefaultHttpClient();
//		     HttpGet httpGet = new HttpGet(params[0]);
//		  
//		     try {
//		 HttpEntity httpEntity = httpClient.execute(httpGet).getEntity();
//
//		 if (httpEntity != null){
//		  InputStream inputStream = httpEntity.getContent();
//		  Reader in = new InputStreamReader(inputStream);
//		  BufferedReader bufferedreader = new BufferedReader(in);
//		  StringBuilder stringBuilder = new StringBuilder();
//
//		  String stringReadLine = null;
//
//		  while ((stringReadLine = bufferedreader.readLine()) != null) {
//		   stringBuilder.append(stringReadLine + "\n");
//		   }
//
//		  qResult = sb.toString();
//
//		 }
//
//		} catch (ClientProtocolException e) {
//		 // TODO Auto-generated catch block
//		 e.printStackTrace();
//		} catch (IOException e) {
//		 // TODO Auto-generated catch block
//		 e.printStackTrace();
//		}
		     
		  
		return null;
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pd = new ProgressDialog(MainActivity.this);
			pd.setCancelable(false);
			pd.setMessage("Retrieving Images Info");
			pd.show();
			super.onPreExecute();
		}
		
		@Override
		protected void onPostExecute(ArrayList<PhotoInfo> result) {
			// TODO Auto-generated method stub
			pd.cancel();
			Log.d("demo", result.toString());
			intent.putParcelableArrayListExtra("photoList", result);
			startActivity(intent);
			super.onPostExecute(result);
		}
		
	}

}
