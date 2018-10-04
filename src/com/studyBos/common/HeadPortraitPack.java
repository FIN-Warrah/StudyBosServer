package com.studyBos.common;

import java.awt.List;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

public class HeadPortraitPack implements Serializable{
	
	private String userId;
	private String portraitName;
	private String format;
	//存储图片数据的数组
	byte[] imageByte ;
	
	
	public byte[] getImageByte() {
		return imageByte;
	}
	public void setImageByte(byte[] imageByte) {
		this.imageByte = imageByte;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPortraitName() {
		return portraitName;
	}
	public void setPortraitName(String portraitName) {
		this.portraitName = portraitName;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
}
