package com.nbfc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * @author Saurav Tyagi 2017
 * 
 */
@Entity
@Table(name = "NBFC_STATE_MASTER")
public class StateName {
	@Id
	@Column(name="STE_CODE")
	private String ste_code;
	
	@Column(name="STE_NAME")
	private String ste_name;
	
	
	public String getSte_code() {
		return ste_code;
	}
	public void setSte_code(String ste_code) {
		this.ste_code = ste_code;
	}
	public String getSte_name() {
		return ste_name;
	}
	public void setSte_name(String ste_name) {
		this.ste_name = ste_name;
	}
	
}
