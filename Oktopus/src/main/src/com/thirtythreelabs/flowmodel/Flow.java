package com.thirtythreelabs.flowmodel;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class Flow {
	
	@Attribute 
	public String companyId;
	@Attribute
	public String companyName;
//	@Attribute
//	public String companyLocation;
	
	@ElementList
	public List<Module> flowModules = new ArrayList<Module>();
	
	public Flow(){
		this.companyId = "";
		this.companyName = "";
//		this.companyLocation = "";

	}

	public Flow(String companyId, String companyName){
		this.companyId = companyId;
		this.companyName = companyName;
	}
	
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

//	public void setCompanyLocation(String companyLocation) { this.companyLocation = companyLocation; }
//
//	public String getCompanyLocation() { return this.companyLocation; }
}
