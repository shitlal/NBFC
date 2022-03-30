package com.nbfc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "NBFC_OUTSTANDING_AMT")
public class OutStandingAmtModel {

	@Id
	@Column(name = "CGPAN")
	private String CGPAN;
	@Column(name = "DAN_ID")
	private String DAN_ID;
	public String getCGPAN() {
		return CGPAN;
	}
	public void setCGPAN(String cGPAN) {
		CGPAN = cGPAN;
	}
	public String getDAN_ID() {
		return DAN_ID;
	}
	public void setDAN_ID(String dAN_ID) {
		DAN_ID = dAN_ID;
	}
	
	
	
	
	

}
