package com.ibphub.vcci_new.model.dignitary.dig_photo;

import com.google.gson.annotations.SerializedName;
import com.ibphub.vcci_new.model.dignitary_new.dignitary_photos.DataItemDignitaryPhoto;

public class DignitaryPhotoResponse{

	@SerializedName("dignitary")
	private DataItemDignitaryPhoto dignitary;

	@SerializedName("error")
	private int error;

	public void setDignitary(DataItemDignitaryPhoto dignitary){
		this.dignitary = dignitary;
	}

	public DataItemDignitaryPhoto getDignitary(){
		return dignitary;
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
			"DignitaryPhotoResponse{" + 
			"dignitary = '" + dignitary + '\'' + 
			",error = '" + error + '\'' + 
			"}";
		}
}