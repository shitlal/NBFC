package com.nbfc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="NBFC_INTERFACE_UPLOAD")
public class PortfolioBatchApp {


	@Column(name = "PORTFOLIO_NO")
	private String arp_ref_no;
	
	@Column(name = "STATUS")
	private String status;

	@Column(name = "VERIFIEDSTATUS")
	private String verifiedStatus;
	
//	@Column(name = "PORTFOLIO_NAME")
//	private String portfolio_name;
	@Id	
	@Column(name="FILE_ID")
	private String FILE_ID;
	
	@Column(name="MEM_BNK_ID")
	private String mem_BNK_ID;

	


	public String getArp_ref_no() {
		return arp_ref_no;
	}

	public void setArp_ref_no(String arp_ref_no) {
		this.arp_ref_no = arp_ref_no;
	}

	public String getMem_BNK_ID() {
		return mem_BNK_ID;
	}

	public void setMem_BNK_ID(String mem_BNK_ID) {
		this.mem_BNK_ID = mem_BNK_ID;
	}
	public String getFILE_ID() {
		return FILE_ID;
	}

	public void setFILE_ID(String fILE_ID) {
		FILE_ID = fILE_ID;
	}


	public String getVerifiedStatus() {
		return verifiedStatus;
	}

	public void setVerifiedStatus(String verifiedStatus) {
		this.verifiedStatus = verifiedStatus;
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "PortfolioBatchApp [arp_ref_no=" + arp_ref_no + ", status=" + status + ", verifiedStatus="
				+ verifiedStatus + ", FILE_ID=" + FILE_ID + ", mem_BNK_ID=" + mem_BNK_ID + "]";
	}

//	@Override
//	public String toString() {
//		return "PortfolioBatchApp [status=" + status + ", verifiedStatus=" + verifiedStatus + ", FILE_ID=" + FILE_ID
//				+ ", mem_BNK_ID=" + mem_BNK_ID + "]";
//	}


//
//	@Override
//	public String toString() {
//		return "PortfolioBatchApp [arp_ref_no=" + arp_ref_no + ", status=" + status + ", verifiedStatus="
//				+ verifiedStatus + ", portfolio_name=" + portfolio_name + ", FILE_ID=" + FILE_ID + "]";
//	}

	
	
	
	
//	@Override
//	public String toString() {
//		return "PortfolioBatchApp [arp_ref_no=" + arp_ref_no + ", status="
//				+ status + ", verifiedStatus=" + verifiedStatus
//				+ ", portfolio_name=" + portfolio_name + "]";
//	}
//
//	

	

}
