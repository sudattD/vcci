package com.ibphub.vcci_new.model.committee;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Committees{

	@SerializedName("data")
	private List<CommitteeDataItem> data;

	@SerializedName("title")
	private String title;

	public void setData(List<CommitteeDataItem> data){
		this.data = data;
	}

	public List<CommitteeDataItem> getData(){
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
			"Committees{" + 
			"data = '" + data + '\'' + 
			",title = '" + title + '\'' + 
			"}";
		}
}