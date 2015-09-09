package com.sspharma.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class HealthTip implements Serializable{

	@Id
	private Long id;
	private String tip;
	@Index
	private String postedBy;
	public HealthTip() {
	}
	public HealthTip(String tip, String postedBy) {
		super();
		this.tip = tip;
		this.postedBy = postedBy;
	}
	public HealthTip(long id, String tip, String postedBy) {
		super();
		this.id = id;
		this.tip = tip;
		this.postedBy = postedBy;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	public String getPostedBy() {
		return postedBy;
	}
	public void setPostedBy(String postedBy) {
		this.postedBy = postedBy;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HealthTip [id=").append(id).append(", tip=")
				.append(tip).append(", postedBy=").append(postedBy).append("]");
		return builder.toString();
	}
	
}
