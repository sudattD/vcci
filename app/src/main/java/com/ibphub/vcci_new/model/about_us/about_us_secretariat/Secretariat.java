package com.ibphub.vcci_new.model.about_us.about_us_secretariat;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Secretariat {

	@SerializedName("data")
	private String data;

	@SerializedName("members")
	private List<SecretariatItem> members;

	@SerializedName("title")
	private String title;

	public void setData(String data){
		this.data = data;
	}

	public String getData(){
		return data;
	}

	public void setMembers(List<SecretariatItem> members){
		this.members = members;
	}

	public List<SecretariatItem> getMembers(){
		return members;
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
			"Secretariat{" +
			"data = '" + data + '\'' + 
			",members = '" + members + '\'' + 
			",title = '" + title + '\'' + 
			"}";
		}
}