package com.sspharma.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class VoiceMessage implements Serializable
{
	private enum VoiceMessageType{
		PATIENT_TO_DOCTOR,DOCTOR_TO_PATIENT;
	}
	private String localFileUlr;
	@Id
	private String driveFileId;
	private String title;
	@Index
	private String doctorEmail;
	@Index
	private String patientEmail;
	private VoiceMessageType type; // P-D or D-P
	
	public String getLocalFileUlr() {
		return localFileUlr;
	}
	public void setLocalFileUlr(String localFileUlr) {
		this.localFileUlr = localFileUlr;
	}
	public String getDriveFileId() {
		return driveFileId;
	}
	public void setDriveFileId(String driveFileId) {
		this.driveFileId = driveFileId;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDoctorEmail() {
		return doctorEmail;
	}
	public void setDoctorEmail(String sender) {
		this.doctorEmail = sender;
	}
	public String getPatientEmail() {
		return patientEmail;
	}
	public void setPatientEmail(String receiver) {
		this.patientEmail = receiver;
	}
	public VoiceMessageType getType() {
		return type;
	}
	public void setType(VoiceMessageType type) {
		this.type = type;
	}
	
	
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("VoiceMessage [localFileUlr=").append(localFileUlr)
				.append(", driveFileId=").append(driveFileId).append(", title=").append(title).append(", doctorEmail=")
				.append(doctorEmail).append(", patientEmail=").append(patientEmail).append(", type=").append(type)
				.append("]");
		return builder.toString();
	}
	@Override
	public boolean equals(Object object) {
		if(object instanceof VoiceMessage)
		{
			VoiceMessage voiceMessage = (VoiceMessage)object;
			if(voiceMessage.getDriveFileId().equalsIgnoreCase(this.driveFileId))
			{
				return true;
			}
		}
		return false;
	}
	@Override
	public int hashCode() {
		return driveFileId.hashCode();
	}
}
