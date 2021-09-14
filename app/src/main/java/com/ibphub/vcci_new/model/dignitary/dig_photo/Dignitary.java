package com.ibphub.vcci_new.model.dignitary.dig_photo;

import com.google.gson.annotations.SerializedName;

public class Dignitary{

	@SerializedName("data")
	private Data data;

	@SerializedName("title")
	private String title;

	public void setData(Data data){
		this.data = data;
	}

	public Data getData(){
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