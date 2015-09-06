/**Homework 4 - Filename: PhotosActivity.java
 * Team members:	
 * Sriram Padavala
 * Abdul Aaquib
 * 
 * 10/04/2013
 */

package com.example.photogallery;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
//import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;

public class PhotosActivity extends Activity {
	private ImageView iv;
	private TextView titleText,viewsText;
	private String[] urls = new String[100];
	private Integer current_index = 0;
	//private Resources res;
	private ProgressDialog pd;
	private String type;
	Context context;
	DownloadImageAsyncTask slideShow;
	ArrayList<PhotoInfo> photoList;
	
	private DiskLruCache imageCache;
	
	private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photos);
		
		imageCache = DiskLruCache.openCache(getApplicationContext(), getCacheDir(), DISK_CACHE_SIZE);
		imageCache.clearCache();
		//res = getResources();
		//urls = res.getStringArray(R.array.photo_urls);
		iv = (ImageView) findViewById(R.id.imageView1);
		titleText = (TextView) findViewById(R.id.textView1);
		viewsText = (TextView) findViewById(R.id.textView2);
		//urls = new String[];
		
		slideShow = new DownloadImageAsyncTask();
		photoList = getIntent().getParcelableArrayListExtra("photoList");
		Collections.sort(photoList);
		
		for(int i=0; i<photoList.size(); i++){
			urls[i] = photoList.get(i).getUrl();
		}
		
		type = this.getIntent().getExtras().getString("type");
		if(type.equalsIgnoreCase("photos")){
			new DownloadImageAsyncTask().execute(urls[current_index]);
			iv.setOnTouchListener(new touchEvent());
		}
		else if(type.equalsIgnoreCase("slide")){
			slideShow.execute(urls);
			}
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.photos, menu);
		return true;
	}

	public class DownloadImageAsyncTask extends AsyncTask<String, Bitmap, Bitmap> {

	    public boolean finished = false;
	    
	    @Override
	    protected Bitmap doInBackground(String... urlParam) {
	    	URL url= null;
	    	Bitmap bmp = null;
			if(type.equalsIgnoreCase("photos")){
				
				if(imageCache.containsKey(current_index.toString())){
					bmp = imageCache.get(current_index.toString());
				}
				else{
					try {
						url = new URL(urlParam[0]);
					}
					 catch (MalformedURLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
						try {
							bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						new putImageTask().execute(bmp);
				}
			}
			else if(type.equalsIgnoreCase("slide")){
		        do { 
		            // This is the bulk of our task, request the data, and put in "bmp"
		        	if(imageCache.containsKey(current_index.toString())){
						bmp = imageCache.get(current_index.toString());
					}
		        	else{
						try {
							url = new URL(urlParam[current_index]);
						} catch (MalformedURLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							}
						try {
							bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
							} catch (IOException e) {
								// TODO Auto-generated catch block
							e.printStackTrace();
							}
						new putImageTask().execute(bmp);
		        	}
		            // Return it to the activity thread using publishProgress()
		            publishProgress(bmp);
		            
		            try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	
		        } while(!finished);
			}
			return bmp;
	    }

	    @Override
	    protected void onProgressUpdate(Bitmap... result) 
	    {
	    	iv.setImageBitmap(result[0]);
	    	titleText.setText(photoList.get(current_index).getTitle());
			viewsText.setText("Views :" + photoList.get(current_index).getViews());
	    	
	    	current_index++;
			if(current_index == urls.length-1){
				current_index =0;
			}
	    }
	    @Override
		protected void onPostExecute(Bitmap result) {
	    	super.onPostExecute(result);
			// TODO Auto-generated method stub
			if(type.equalsIgnoreCase("photos")){
			iv.setImageBitmap(result);
			titleText.setText(photoList.get(current_index).getTitle());
			viewsText.setText("Views :" + photoList.get(current_index).getViews());
			pd.cancel();
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if(type.equalsIgnoreCase("photos")){
			pd = new ProgressDialog(PhotosActivity.this);
			pd.setCancelable(false);
			pd.setMessage("Loading Image");
			pd.show();
			}
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			//super.onCancelled();
			finished=true;
		}
		
	}
	@Override
	public void onBackPressed(){
		slideShow.finished=true;
		//slideShow.cancel(true);
		super.onBackPressed();
	}
	
	public class putImageTask extends AsyncTask<Bitmap, Void, Void>{

		@Override
		protected Void doInBackground(Bitmap... bmp) {
			// TODO Auto-generated method stub
			imageCache.put(current_index.toString(), bmp[0]);
			
			return null;
		}
	}
	
	public class touchEvent implements View.OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN){
                if(event.getX() <= 64){
                	if(current_index == 0){
                		current_index = urls.length-1;
                	}
                	else{
                		current_index = current_index -1;
                	}
                	new DownloadImageAsyncTask().execute(urls[current_index]);
                }
                else if(event.getX() >= 256){
                	if(current_index == urls.length-1){
                		current_index = 0;
                	}
                	else{
                		current_index = current_index +1;
                	}
                	new DownloadImageAsyncTask().execute(urls[current_index]);
                }
            }
			return true;
		}
		
	}

}
