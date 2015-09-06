package com.example.photogallery;

import android.os.Parcel;
import android.os.Parcelable;


public class PhotoInfo implements Parcelable, Comparable<PhotoInfo>{
String title,url;
Integer views;
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public String getUrl() {
	return url;
}
public void setUrl(String url) {
	this.url = url;
}
public Integer getViews() {
	return views;
}

public void setViews(Integer views) {
	this.views = views;
}
@Override
public String toString() {
	return "PhotoInfo [title=" + title + ", url=" + url + ", views=" + views
			+ "]";
}
@Override
public int describeContents() {
	// TODO Auto-generated method stub
	return 0;
}
@Override
public void writeToParcel(Parcel p, int arg1) {
	// TODO Auto-generated method stub
	p.writeStringArray(new String[]{this.title,this.url,String.valueOf(this.views)});
//	p.writeString(title);
//	p.writeString(url);
//	p.writeInt(views);
}

public static final Parcelable.Creator<PhotoInfo> CREATOR= new Parcelable.Creator<PhotoInfo>() {
public PhotoInfo createFromParcel(Parcel in) {
return new PhotoInfo(in);
}

public PhotoInfo[] newArray(int size) {
return new PhotoInfo[size];
}
};

private PhotoInfo(Parcel in) {
	String[] data= new String[3];
	 
	in.readStringArray(data);
	this.title = data[0];
	this.url = data[1];
	this.views = Integer.parseInt(data[2]);
}
public PhotoInfo() {
	// TODO Auto-generated constructor stub
}
@Override
public int compareTo(PhotoInfo another) {
	// TODO Auto-generated method stub
	return another.getViews() - this.getViews();
}

}
