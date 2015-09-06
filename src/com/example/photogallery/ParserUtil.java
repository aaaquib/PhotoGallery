package com.example.photogallery;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ParserUtil {
	
	static public class JSONParser{
		static ArrayList<PhotoInfo> parsePhotos(String in) throws JSONException{
			
			ArrayList<PhotoInfo> photoList = new ArrayList<PhotoInfo>();
			
			JSONObject root = new JSONObject(in);
			JSONObject photos = root.getJSONObject("photos");
			JSONArray photoarray = photos.getJSONArray("photo");
			
			for(int i=0; i<photoarray.length(); i++){
				JSONObject photoObject = photoarray.getJSONObject(i);
				PhotoInfo photo = new PhotoInfo();
				photo.setTitle(photoObject.getString("title"));
				photo.setViews(photoObject.getInt("views"));
				photo.setUrl(photoObject.getString("url_m"));
				photoList.add(photo);
			}
			return photoList;
		}
	}

}
