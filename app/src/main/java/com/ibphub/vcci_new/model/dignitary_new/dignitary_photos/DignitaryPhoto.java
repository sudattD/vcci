package com.ibphub.vcci_new.model.dignitary_new.dignitary_photos;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DignitaryPhoto {

	@SerializedName("data")
	private List<DataItemDignitaryPhoto> data;

	@SerializedName("title")
	private String title;

	public void setData(List<DataItemDignitaryPhoto> data){
		this.data = data;
	}

	public List<DataItemDignitaryPhoto> getData(){
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
			"Dignitary{" + 
			"data = '" + data + '\'' + 
			",title = '" + title + '\'' + 
			"}";
		}
}