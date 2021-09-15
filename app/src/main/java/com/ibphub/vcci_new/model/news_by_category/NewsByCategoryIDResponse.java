package com.ibphub.vcci_new.model.news_by_category;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class NewsByCategoryIDResponse{

	@SerializedName("news")
	private List<NewsItem> news;

	@SerializedName("error")
	private int error;

	public void setNews(List<NewsItem> news){
		this.news = news;
	}

	public List<NewsItem> getNews(){
		return news;
	}

	public void setError(int error){
		this.error = error;
	}

	public int getError(){
		return error;
	}

	@Override
 	public String toString(){
		return 
			"NewsByCategoryIDResponse{" + 
			"news = '" + news + '\'' + 
			",error = '" + error + '\'' + 
			"}";
		}
}