package com.ibphub.vcci_new.model.circulars.circular_list;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Circulars{

	@SerializedName("data")
	private List<DataItem> data;

	@SerializedName("title")
	private String title;

	public void setData(List<DataItem> data){
		this.data = data;
	}

	public List<DataItem> getData(){
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
			"Circulars{" + 
			"data = '" + data + '\'' + 
			",title = '" + title + '\'' + 
			"}";
		}
}