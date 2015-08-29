package com.sspharma.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class User implements Serializable{
	
	private String name;
	@Id
	private String email;
	private Integer age;
	private Boolean isPremiumUser;
	private String profilePic;
	private String gender;
	private String driveFolderId;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Boolean getIsPremiumUser() {
		return isPremiumUser;
	}
	public void setIsPremiumUser(Boolean isPremiumUser) {
		this.isPremiumUser = isPremiumUser;
	}
	public String getProfilePic() {
		return profilePic;
	}
	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}
	
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public String getDriveFolderId() {
		return driveFolderId;
	}
	public void setDriveFolderId(String driveFolderId) {
		this.driveFolderId = driveFolderId;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("User [name=").append(name).append(", email=").append(email).append(", age=").append(age)
				.append(", isPremiumUser=").append(isPremiumUser).append(", profilePic=").append(profilePic)
				.append("]");
		return builder.toString();
	}
	
	@Override
	public boolean equals(Object object) {
		// TODO Auto-generated method stub
		if(object instanceof User)
		{
			User user = (User)object;
			if(user.getEmail().equalsIgnoreCase(this.getEmail()))
			{
				return true;
			}
		}
		return false;
	}
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return email.hashCode();
	}
}
