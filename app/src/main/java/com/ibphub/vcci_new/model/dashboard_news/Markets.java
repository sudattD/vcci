package com.ibphub.vcci_new.model.dashboard_news;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Markets{

	@SerializedName("news")
	private List<NewsItem> news;

	@SerializedName("category_id")
	private String categoryId;

	@SerializedName("title")
	private String title;

	public void setNews(List<NewsItem> news){
		this.news = news;
	}

	public List<NewsItem> getNews(){
		return news;
	}

	public void setCategoryId(String categoryId){
		this.categoryId = categoryId;
	}

	public String getCategoryId(){
		return categoryId;
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
			"Markets{" + 
			"news = '" + news + '\'' + 
			",category_id = '" + categoryId + '\'' + 
			",title = '" + title + '\'' + 
			"}";
		}
}