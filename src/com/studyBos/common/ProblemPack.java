package com.studyBos.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ProblemPack implements Serializable{
	private String problem;
	private String askerId;
	private byte[]  imageBytes;
	private int proHash;
	private String asktime;
	private String profession;
	private String project;
	
	public String getAskerId() {
		return askerId;
	}

	public void setAskerId(String askerId) {
		this.askerId = askerId;
	}

	public int getProHash() {
		return proHash;
	}

	public void setProHash(int proHash) {
		this.proHash = proHash;
	}

	public String getAsktime() {
		return asktime;
	}

	public void setAsktime(String asktime) {
		this.asktime = asktime;
	}

	public byte[] getImageBytes() {
		return imageBytes;
	}

	public void setImageBytes(byte[] imageBytes) {
		this.imageBytes = imageBytes;
	}
	
	public String getProblem() {
		return problem;
	}
	public void setProblem(String problem) {
		this.problem = problem;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}
	
	public String getProject() {
		return project;
	}
	
	public void setProject(String project) {
		this.project = project;
	}
}
