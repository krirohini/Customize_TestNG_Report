package com.rohini.automation;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DemoGroups {

	Utils util;
	
	//String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
	
	// 
	@BeforeClass(alwaysRun=true) // alwaysRun parameter is used for the execution of this method whenever the class will start the execution.
	public void beforeClass() {
		System.out.println(">>>> " + new Throwable().getStackTrace()[0].getMethodName() + "\n"); // Printing the current executing method name
		util = Utils.getInstance();
	}
	

	@BeforeMethod(alwaysRun=true) // alwaysRun parameter is used for the execution of before method, before start the execution of any test case.
	public void beforeMethod() {
		System.out.println(">>>> " + new Throwable().getStackTrace()[0].getMethodName());
	}

	@AfterMethod(alwaysRun=true)
	public void afterMethod() {
		System.out.println(">>>> " + new Throwable().getStackTrace()[0].getMethodName() + "\n");
	}

	@Test(priority = 0, groups = {"sanity_must"})
	public void testCase_1() {
		System.out.println(">>>> " + new Throwable().getStackTrace()[0].getMethodName());
	}
	
	@Test(priority = 0, groups = {"sanity_must"}) 
	//@Test(priority = 0)
	public void testCase_2() {
		System.out.println(">>>> " + new Throwable().getStackTrace()[0].getMethodName());
	}
	
	@Test(priority = 0, groups = {"sanity_if_time"})
	//@Test(priority = 0)
	public void testCase_3() {
		System.out.println(">>>> " + new Throwable().getStackTrace()[0].getMethodName());
	}
	
}
