package com.nbfc.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "NBFC_USER_INFO")
public class UserInfoModel{

	@Id
	@Column(name = "USR_ID")
	private String usr_id;
	@Column(name = "USR_FIRST_NAME")
	private String fName;
	@Column(name = "USR_MIDDLE_NAME")
	private String mName;
	@Column(name = "USR_LAST_NAME")
	private String lName;
	@Column(name = "MOBILE_NO")
	private String mobileNumber;
	@Column(name = "PHONE_NO")
	private String phoneNumber;
	@Column(name = "USR_EMAIL_ID")
	private String email;
	@Column(name = "USR_TYPE")
	private String userType;
	
	public String getUsr_id() {
		return usr_id;
	}

	public void setUsr_id(String usr_id) {
		this.usr_id = usr_id;
	}

	public String getfName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getlName() {
		return lName;
	}

	public void setlName(String lName) {
		this.lName = lName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	
	@Override
	public String toString() {
		return "UserInfoModel [usr_id=" + usr_id + ", fName="
				+ fName + ", lName=" + lName + ", email="
				+ email + ", mobileNumber=" + mobileNumber + ", phoneNumber="
				+ phoneNumber + ",userType="+userType+"]";
	}

	public UserInfoModel() {
		super();
	}

	
	
}
