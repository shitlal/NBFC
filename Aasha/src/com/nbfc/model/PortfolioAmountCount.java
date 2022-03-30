package com.nbfc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "NBFC_INTERFACE_UPLOAD")
public class PortfolioAmountCount {

	
	@Column(name = "PORTFOLIO_NO")
	private String portfolioNUmber;
	@Column(name = "SANCTIONED_AMOUNT")
	private String sanctionAmount;
	@Column(name = "OUTSTANDING_AMOUNT")
	private long outStandingAmount;
	@Column(name="STATUS")
	private String status;
	@Column(name="VERIFIEDSTATUS")
	private String verifactionStatus;
	@Id
	@Column(name="FILE_ID")
	private String file_ID;
	
	public String getFile_ID() {
		return file_ID;
	}

	public void setFile_ID(String file_ID) {
		this.file_ID = file_ID;
	}


	

	public long getOutStandingAmount() {
		return outStandingAmount;
	}

	public void setOutStandingAmount(long outStandingAmount) {
		this.outStandingAmount = outStandingAmount;
	}

	public String getPortfolioNUmber() {
		return portfolioNUmber;
	}

	public void setPortfolioNUmber(String portfolioNUmber) {
		this.portfolioNUmber = portfolioNUmber;
	}

	public String getSanctionAmount() {
		return sanctionAmount;
	}

	public void setSanctionAmount(String sanctionAmount) {
		this.sanctionAmount = sanctionAmount;
	}

}
