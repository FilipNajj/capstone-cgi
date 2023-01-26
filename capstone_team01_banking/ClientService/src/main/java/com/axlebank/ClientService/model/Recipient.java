package com.axlebank.ClientService.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

public class Recipient {
	
	
	@Id
	private int recipientId;
	private String recipientName;
	private long mobilePhone;
	private String emailAddress;
	
	public Recipient() {}

	public Recipient(int recipientId, String recipientName, long mobilePhone, String emailAddress) {
		this.recipientId = recipientId;
		this.recipientName = recipientName;
		this.mobilePhone = mobilePhone;
		this.emailAddress = emailAddress;
	}

	public int getRecipientId() {
		return recipientId;
	}

	public void setRecipientId(int recipientId) {
		this.recipientId = recipientId;
	}

	public String getRecipientName() {
		return recipientName;
	}

	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}

	public long getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(long mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	@Override
	public String toString() {
		return "Recipient [recipientId=" + recipientId + ", recipientName=" + recipientName + ", mobilePhone="
				+ mobilePhone + ", emailAddress=" + emailAddress + "]";
	}

}
