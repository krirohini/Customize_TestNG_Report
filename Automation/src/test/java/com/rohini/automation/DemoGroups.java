package com.rohini.automation;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.awt.AWTException;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
 
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;


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
	
	@Test(priority = 0, groups = {"sanity_if_time"})
	//@Test(priority = 0)
	public void testCase_4() {
		Assert.assertEquals("rk", "blk");
		System.out.println(">>>> " + new Throwable().getStackTrace()[0].getMethodName());
	}
	
	@Test(groups = {"sanity_if_time"}, enabled=false)
	//@Test(priority = 0)
	public void testCase_5() throws InterruptedException {
		Thread.sleep(2000);
		Assert.assertEquals("rk", "blk");
		System.out.println(">>>> " + new Throwable().getStackTrace()[0].getMethodName());
	}
	
	public void takeScreenshot() {
		
	      try {
	        /*
	         * Let the program wait for 5 seconds to allow you to open the
	         * window whose screenshot has to be captured
	         */
	         Thread.sleep(5000);
	         Robot robot = new Robot();
	         String fileName = "./FullScreenshot.jpg";
	 
	         Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit()
	                                     .getScreenSize());
	         BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
	         ImageIO.write(screenFullImage, "jpg", new File(fileName));
	 
	         /*f.setLocation(500, 500);
	         JLabel text = new JLabel("A full screenshot saved!");
	         f.add(text);
	         f.setSize(200, 100);
	         f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	         f.getContentPane().setLayout(new FlowLayout());
	         f.setVisible(true);*/
	      } catch (AWTException | IOException | InterruptedException ex) {
	               System.err.println(ex);
	      }
	}
	
}
