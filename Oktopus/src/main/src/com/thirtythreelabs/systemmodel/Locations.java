package com.thirtythreelabs.systemmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Locations implements Serializable {

	private int locationId;
	private ArrayList<String> locations;
	private String loginError;
	private String loginErrorMessage;
	private String currentLocation;


	public Locations(){
		this.locationId = 4;
		this.locations = new ArrayList<String>();
	}

	public int getLocationId() {
		return locationId;
	}

	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	public ArrayList<String> getLocations() {
		return locations;
	}
	
	public void setLocations(ArrayList<String> locations) {
		this.locations = locations;
	}

	public void setCurrentLocation(String tempLocation) {
		this.currentLocation = tempLocation;
	}

	public String getCurrentLocation() {
		return this.currentLocation;
	}

	public String getLoginError() {
		return loginError;
	}

	public void setLoginError(String loginError) {
		this.loginError = loginError;
	}

	public String getLoginErrorMessage() {
		return loginErrorMessage;
	}

	public void setLoginErrorMessage(String loginErrorMessage) {
		this.loginErrorMessage = loginErrorMessage;
	}


}
