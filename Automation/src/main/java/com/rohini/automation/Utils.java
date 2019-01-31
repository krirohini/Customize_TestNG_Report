package com.rohini.automation;

// Singleton Class
public class Utils {
	
	public static Utils util;
	
	private Utils() {
		
	}
	
	public static Utils getInstance() {
		if(util == null) {
			util = new Utils();
		}			
		return util;
		
	}
	
	
	
}
