package com.ibphub.vcci_new.model.events.event_list;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Events{

	@SerializedName("data")
	private List<EventItem> data;

	@SerializedName("title")
	private String title;

	public void setData(List<EventItem> data){
		this.data = data;
	}

	public List<EventItem> getData(){
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
			"Events{" + 
			"data = '" + data + '\'' + 
			",title = '" + title + '\'' + 
			"}";
		}
}