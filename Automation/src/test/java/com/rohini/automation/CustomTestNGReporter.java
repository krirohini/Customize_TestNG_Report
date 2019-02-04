package com.rohini.automation;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.xml.XmlSuite;

public class CustomTestNGReporter implements IReporter {

	// This is the customize emailable report template file path.
	private static final String emailableReportTemplateFile = "./src/test/java/com/rohini/automation/customize_testng_report.html";

	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
		
		try
		{
			// Get content data in TestNG report template file.
			String customReportTemplateStr = this.readEmailabelReportTemplate();
			
			// Create custom report title.
			String customReportTitle = this.getCustomReportTitle("Automation Report: Execution Date - ");
			
			// Create test suite summary data.
			String customSuiteSummary = this.getTestSuiteSummary(suites);
			
			// Create test methods summary data.
			String customTestMethodSummary = this.getTestMehodSummary(suites);
			
			// Replace report title place holder with custom title.
			customReportTemplateStr = customReportTemplateStr.replaceAll("\\$TestNG_Custom_Report_Title\\$", customReportTitle);
			
			// Replace test suite place holder with custom test suite summary.
			customReportTemplateStr = customReportTemplateStr.replaceAll("\\$Test_Case_Summary\\$", customSuiteSummary);
			
			// Replace test methods place holder with custom test method summary.
			customReportTemplateStr = customReportTemplateStr.replaceAll("\\$Test_Case_Detail\\$", customTestMethodSummary);
			
			// Write replaced test report content to custom-emailable-report.html.
			File targetFile = new File(outputDirectory + "/custom-emailable-report.html");
			FileWriter fw = new FileWriter(targetFile);
			fw.write(customReportTemplateStr);
			fw.flush();
			fw.close();
			
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/* Read template content. */
	private String readEmailabelReportTemplate()
	{
		StringBuffer retBuf = new StringBuffer();
		
		try {
		
			File file = new File(this.emailableReportTemplateFile);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			
			String line = br.readLine();
			while(line!=null)
			{
				retBuf.append(line);
				line = br.readLine();
			}
			
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}finally
		{
			return retBuf.toString();
		}
	}
	
	/* Build custom report title. */
	private String getCustomReportTitle(String title)
	{
		StringBuffer retBuf = new StringBuffer();
		retBuf.append(title + " " + this.getDateInStringFormat(new Date()));
		return retBuf.toString();
	}
	
	/* Build test suite summary data. */
	private String getTestSuiteSummary(List<ISuite> suites)
	{
		StringBuffer retBuf = new StringBuffer();
		
		try
		{
			int totalTestCount = 0;
			int totalTestPassed = 0;
			int totalTestFailed = 0;
			int totalTestSkipped = 0;
			
			for(ISuite tempSuite: suites)
			{
				retBuf.append("<tr><td colspan=11><center><b>" + tempSuite.getName() + "</b></center></td></tr>");
				
				Map<String, ISuiteResult> testResults = tempSuite.getResults();
				
				for (ISuiteResult result : testResults.values()) {
					
					retBuf.append("<tr>");
					
					ITestContext testObj = result.getTestContext();
					
					totalTestPassed = testObj.getPassedTests().getAllMethods().size();
					totalTestSkipped = testObj.getSkippedTests().getAllMethods().size();
					totalTestFailed = testObj.getFailedTests().getAllMethods().size();
					
					totalTestCount = totalTestPassed + totalTestSkipped + totalTestFailed;
					
					/* Test name. */
					retBuf.append("<td style=\"text-align:center\">");
					retBuf.append(testObj.getName());
					retBuf.append("</td>");
					
					/* Total method count. */
					retBuf.append("<td style=\"text-align:center\">");
					retBuf.append(totalTestCount);
					retBuf.append("</td>");
					
					/* Passed method count. */
					retBuf.append("<td style=\"text-align:center\" bgcolor=green>");
					retBuf.append(totalTestPassed);
					retBuf.append("</td>");
					
					/* Skipped method count. */
					retBuf.append("<td style=\"text-align:center\" bgcolor=yellow>");
					retBuf.append(totalTestSkipped);
					retBuf.append("</td>");
					
					/* Failed method count. */
					retBuf.append("<td style=\"text-align:center\" bgcolor=red>");
					retBuf.append(totalTestFailed);
					retBuf.append("</td>");
					
					/* Get browser type. 
					String browserType = tempSuite.getParameter("browserType");
					if(browserType==null || browserType.trim().length()==0)
					{
						browserType = "Chrome";
					}*/
					
					/* Append browser type. 
					retBuf.append("<td>");
					retBuf.append(browserType);
					retBuf.append("</td>");*/
					
					/* Start Date*/
					Date startDate = testObj.getStartDate();
					retBuf.append("<td style=\"text-align:center\">");
					retBuf.append(this.getTimeInStringFormat(startDate));
					retBuf.append("</td>");
					
					/* End Date*/
					Date endDate = testObj.getEndDate();
					retBuf.append("<td style=\"text-align:center\">");
					retBuf.append(this.getTimeInStringFormat(endDate));
					retBuf.append("</td>");
					
					/* Execute Time */
					long deltaTime = endDate.getTime() - startDate.getTime();
					String deltaTimeStr = this.convertDeltaTimeToString(deltaTime);
					retBuf.append("<td style=\"text-align:center\">");
					retBuf.append(deltaTimeStr);
					retBuf.append("</td>");
					
					/* Include groups. */
					retBuf.append("<td style=\"text-align:center\">");
					retBuf.append(this.stringArrayToString(testObj.getIncludedGroups()));
					retBuf.append("</td>");
					
					/* Exclude groups. */
					retBuf.append("<td style=\"text-align:center\">");
					retBuf.append(this.stringArrayToString(testObj.getExcludedGroups()));
					retBuf.append("</td>");
					
					retBuf.append("</tr>");
				}
			}
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}finally
		{
			return retBuf.toString();
		}
	}

	/* Get date string format value. */
	private String getDateInStringFormat(Date date)
	{
		StringBuffer retBuf = new StringBuffer();
		if(date==null)
		{
			date = new Date();
		}
		//DateFormat df = new SimpleDateFormat("HH:mm:ss");
		//DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		retBuf.append(df.format(date));
		return retBuf.toString();
	}
	
	/* Get current time in string format value. */
	private String getTimeInStringFormat(Date date)
	{
		StringBuffer retBuf = new StringBuffer();
		if(date==null)
		{
			date = new Date();
		}
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		retBuf.append(df.format(date));
		return retBuf.toString();
	}
	
	/* Convert long type deltaTime to format hh:mm:ss:mi. */
	private String convertDeltaTimeToString(long deltaTime)
	{
		StringBuffer retBuf = new StringBuffer();
		
		long milli = deltaTime;
		
		long seconds = deltaTime / 1000;
		
		long minutes = seconds / 60;
		
		long hours = minutes / 60;
		
		retBuf.append(hours + ":" + minutes + ":" + seconds + ":" + milli);
		
		return retBuf.toString();
	}
	
	/* Get test method summary info. */
	private String getTestMehodSummary(List<ISuite> suites)
	{
		StringBuffer retBuf = new StringBuffer();
		
		try
		{
			for(ISuite tempSuite: suites)
			{
				retBuf.append("<tr><td colspan=7><center><b>" + tempSuite.getName() + "</b></center></td></tr>");
				
				Map<String, ISuiteResult> testResults = tempSuite.getResults();
				
				for (ISuiteResult result : testResults.values()) {
					
					ITestContext testObj = result.getTestContext();

					String testName = testObj.getName();
					
					/* Get failed test method related data. */
					IResultMap testFailedResult = testObj.getFailedTests();
					String failedTestMethodInfo = this.getTestMethodReport(testName, testFailedResult, false, false);
					retBuf.append(failedTestMethodInfo);
					
					/* Get skipped test method related data. */
					IResultMap testSkippedResult = testObj.getSkippedTests();
					String skippedTestMethodInfo = this.getTestMethodReport(testName, testSkippedResult, false, true);
					retBuf.append(skippedTestMethodInfo);
					
					/* Get passed test method related data. */
					IResultMap testPassedResult = testObj.getPassedTests();
					String passedTestMethodInfo = this.getTestMethodReport(testName, testPassedResult, true, false);
					retBuf.append(passedTestMethodInfo);
				}
			}
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}finally
		{
			return retBuf.toString();
		}
	}
	
	/* Get failed, passed or skipped test methods report. */
	private String getTestMethodReport(String testName, IResultMap testResultMap, boolean passedReault, boolean skippedResult)
	{
		StringBuffer retStrBuf = new StringBuffer();
		
		String resultTitle = testName;
		
		String color = "green";
		
		if(skippedResult)
		{
			resultTitle += " - Skipped ";
			color = "yellow";
		}else
		{
			if(!passedReault)
			{
				resultTitle += " - Failed ";
				color = "red";
			}else
			{
				resultTitle += " - Passed ";
				color = "green";
			}
		}
		
		retStrBuf.append("<tr bgcolor=" + color + "><td colspan=7><center><b>" + resultTitle + "</b></center></td></tr>");
			
		Set<ITestResult> testResultSet = testResultMap.getAllResults();
			
		for(ITestResult testResult : testResultSet)
		{
			String testClassName = "";
			String testMethodName = "";
			String startDateStr = "";
			String executeTimeStr = "";
			String paramStr = "";
			String reporterMessage = "";
			String exceptionMessage = "";
			String screenShotFile = "";
			
			//Get testClassName
			testClassName = testResult.getTestClass().getName();
				
			//Get testMethodName
			testMethodName = testResult.getMethod().getMethodName();
				
			//Get startDateStr
			long startTimeMillis = testResult.getStartMillis();
			//startDateStr = this.getDateInStringFormat(new Date(startTimeMillis));
			startDateStr = this.getTimeInStringFormat(new Date(startTimeMillis));
				
			//Get Execute time.
			long deltaMillis = testResult.getEndMillis() - testResult.getStartMillis();
			executeTimeStr = this.convertDeltaTimeToString(deltaMillis);
				
			//Get parameter list.
			Object paramObjArr[] = testResult.getParameters();
			for(Object paramObj : paramObjArr)
			{
				paramStr += (String)paramObj;
				paramStr += " ";
			}
				
			//Get reporter message list.
			List<String> repoterMessageList = Reporter.getOutput(testResult);
			for(String tmpMsg : repoterMessageList)				
			{
				reporterMessage += tmpMsg;
				reporterMessage += " ";
			}
				
			//Get exception message.
			Throwable exception = testResult.getThrowable();
			if(exception!=null)
			{
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				exception.printStackTrace(pw);
				
				exceptionMessage = sw.toString();
			}
			
			//Get screenshot file.
			try {
		        /*
		         * Let the program wait for 5 seconds to allow you to open the
		         * window whose screenshot has to be captured
		         */
		         Thread.sleep(5000);
		         Robot robot = new Robot();
		         //screenShotFile = testMethodName + ".jpg";
		         //screenShotFile = testMethodName ;
		 
		         Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit()
		                                     .getScreenSize());
		         BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
		         ImageIO.write(screenFullImage, "jpg", new File(screenShotFile));
		         
		      } catch (AWTException | IOException | InterruptedException ex) {
		               System.err.println(ex);
		      }
			
			if(exception!=null)
			{
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				exception.printStackTrace(pw);
				
				exceptionMessage = sw.toString();
			}
			
			retStrBuf.append("<tr bgcolor=" + color + ">");
			
			/* Add test class name. */
			retStrBuf.append("<td>");
			retStrBuf.append(testClassName);
			retStrBuf.append("</td>");
			
			/* Add test method name. */
			retStrBuf.append("<td>");
			retStrBuf.append(testMethodName);
			retStrBuf.append("</td>");
			
			/* Add start time. */
			retStrBuf.append("<td style=\"text-align:center\">");
			retStrBuf.append(startDateStr);
			retStrBuf.append("</td>");
			
			/* Add execution time. */
			retStrBuf.append("<td style=\"text-align:center\">");
			retStrBuf.append(executeTimeStr);
			retStrBuf.append("</td>");
			
			/* Add parameter. 
			retStrBuf.append("<td>");
			retStrBuf.append(paramStr);
			retStrBuf.append("</td>");
			
			 Add reporter message. 
			retStrBuf.append("<td>");
			retStrBuf.append(reporterMessage);
			retStrBuf.append("</td>");*/
			
			/* Add exception message. */
			retStrBuf.append("<td>");
			retStrBuf.append(exceptionMessage);
			retStrBuf.append("</td>");
			
			/* Add Screenshot. */
			//retStrBuf.append("<td> <img src='"screenShotFile"' height='100' width='100' />");
			retStrBuf.append("<td> <a href='" + testMethodName + ".jpg'><img src='" + testMethodName + ".jpg' height='100' width='100'/></a>");
/*			retStrBuf.append("<td> <a href='\" + testMethodName\n" + 
					"                    + \".jpg'><img src='\" + testMethodName\n" + 
					"                    + \".jpg' height='100' width='100' /></a>");
*/			//retStrBuf.append(screenShotFile);
			retStrBuf.append("</td>");
			
			retStrBuf.append("</tr>");

		}
		
		return retStrBuf.toString();
	}
	
	/* Convert a string array elements to a string. */
	private String stringArrayToString(String strArr[])
	{
		StringBuffer retStrBuf = new StringBuffer();
		if(strArr!=null)
		{
			for(String str : strArr)
			{
				retStrBuf.append(str);
				retStrBuf.append(" ");
			}
		}
		return retStrBuf.toString();
	}

	
}
