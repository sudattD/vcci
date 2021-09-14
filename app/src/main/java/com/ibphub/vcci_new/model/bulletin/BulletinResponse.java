package com.ibphub.vcci_new.model.bulletin;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class BulletinResponse{

	@SerializedName("bulletins")
	private List<BulletinsItem> bulletins;

	@SerializedName("error")
	private int error;

	public void setBulletins(List<BulletinsItem> bulletins){
		this.bulletins = bulletins;
	}

	public List<BulletinsItem> getBulletins(){
		return bulletins;
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
			"BulletinResponse{" + 
			"bulletins = '" + bulletins + '\'' + 
			",error = '" + error + '\'' + 
			"}";
		}
}