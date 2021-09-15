package com.ibphub.vcci_new.model.category;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class CategoriesResponse{

	@SerializedName("categories")
	private List<CategoriesItem> categories;

	@SerializedName("error")
	private int error;

	public void setCategories(List<CategoriesItem> categories){
		this.categories = categories;
	}

	public List<CategoriesItem> getCategories(){
		return categories;
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
			"CategoriesResponse{" + 
			"categories = '" + categories + '\'' + 
			",error = '" + error + '\'' + 
			"}";
		}
}