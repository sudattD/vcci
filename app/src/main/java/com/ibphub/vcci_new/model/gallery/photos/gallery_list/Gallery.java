package com.ibphub.vcci_new.model.gallery.photos.gallery_list;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Gallery{

	@SerializedName("data")
	private List<GalleryItem> data;

	@SerializedName("title")
	private String title;

	public void setData(List<GalleryItem> data){
		this.data = data;
	}

	public List<GalleryItem> getData(){
		return data;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	@Override
 	public String toString(){
		return 
			"VideoGallery{" +
			"data = '" + data + '\'' + 
			",title = '" + title + '\'' + 
			"}";
		}
}