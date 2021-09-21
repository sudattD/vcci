package vcci.android.consumer.model.dignitary.dig_video;

import com.google.gson.annotations.SerializedName;

import vcci.android.consumer.model.dignitary_new.dignitary_video.DignitaryVideo;

public class DignitaryVideoResponse{

	@SerializedName("dignitary")
	private DignitaryVideo dignitary;

	@SerializedName("error")
	private int error;

	public void setDignitaryVideo(DignitaryVideo dignitary){
		this.dignitary = dignitary;
	}

	public DignitaryVideo getDignitaryVideo(){
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
			"DignitaryVideoResponse{" + 
			"dignitary = '" + dignitary + '\'' + 
			",error = '" + error + '\'' + 
			"}";
		}
}