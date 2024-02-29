package com.thirtythreelabs.util;

public class Config {

	//public static String URL = "http://192.168.1.92/smpm/rest/";
	private static final String defaultURL = "http://192.168.73.11/smpm/rest/";
	//public static final String URL = "http://192.168.1.92/smpmT/rest/";
	//public static final String URL = "http://192.168.1.8:8899/Oktopus/";
//	public static String URL = "https://unadopted-spots.000webhostapp.com/";
	public static String URL = "http://192.168.73.11/smpm/rest/";
	//public static final String URL = "http://33labs.com/oktopus/";
	
	public static final String ORDER_STATUS_PICKED = "K";
	public static final String ORDER_STATUS_READY = "R";
	
	public static final String ITEM_STATUS_PAUSED = "P";
	public static final String ITEM_STATUS_READY = "R";
	public static final String ITEM_STATUS_IN_PROGRESS = "Z";
	public static final String ITEM_STATUS_PICKED = "K";

	public static void changeURL(String newURL) {
		if (newURL.equals(null) || newURL.equals("")) {
			URL = defaultURL;
		}
		else {
			if (newURL.substring(newURL.length() - 1).equals("/"))  {
				URL = newURL;
			}
			else {
				URL = newURL + "/";
			}
		}
	}
}
