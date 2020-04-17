package com.kpmg.test;
//import com.kpmg.util.*;

import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.kpmg.xls.read.Xls_Reader;
public class DriverScript {

	public static Logger APP_LOGS;
	//suite.xlsx
	public Xls_Reader suiteXLS;
	public int currentSuiteID;
	public String currentTestSuite;
	public static String currentExcellColumnName;
	
	// current test suite
	public static Xls_Reader currentTestSuiteXLS;
	public static int currentTestCaseID;
	public static String currentTestCaseName;
	public static int currentTestStepID;
	public static String currentKeyword;
	public static String resultColumnName;
	public static int currentTestDataSetID=2;
	public static Method method[];
	
	public static Method capturescreenShot_method;
	public static Keywords keywords;
	public static String keyword_execution_result;
	public static ArrayList<String> resultSet;
	
	
	public static String data;
	
	public static String object;
	public static int data_count;
	public static int object_count;
	public static int Dynamic_object_count;
	public static String dataArray[];
	public static String objectArray[];
	public static String orderno=null;
	public static String currentBrowser;
	
	// properties
	public static Properties CONFIG;
	public static Properties CONFIG1;
	public static Properties OR;

	public DriverScript() throws Exception{
		keywords = new Keywords();
		method = keywords.getClass().getMethods();
		capturescreenShot_method =keywords.getClass().getMethod("captureScreenshot",String.class,String.class);
	}
	
	public static void main(String[] args) throws Exception {
		FileInputStream fs = new FileInputStream(System.getProperty("user.dir")+"//src//com//kpmg//config//config.properties");
		CONFIG= new Properties();
		CONFIG.load(fs);

		
		fs = new FileInputStream(System.getProperty("user.dir")+"//src//com//kpmg//config//or.properties");
		OR= new Properties();
		OR.load(fs);
		
		//System.out.println(CONFIG.getProperty("testsiteURL"));
		//System.out.println(OR.getProperty("name"));
		
		
		DriverScript test = new DriverScript();
		//if multiple browsers
        if(CONFIG.getProperty("browserType").contains(","))
        {
          String[] browsers = CONFIG.getProperty("browserType").split(",");                         
            for(int x=0;x<browsers.length;x++)
            {
        currentBrowser= browsers[x];
          test.start();
        }}
        else{
        	currentBrowser=CONFIG.getProperty("browserType");
            test.start();
        }
        
// ReportUtil.Report();
		
		//ReportUtil.Report();
	//SendMail.sendMail();
		
	}
	
	
	public void start() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		// initialize the app logs
		APP_LOGS = Logger.getLogger("DriverScript");
		//Logger logger=Logger.getLogger("DriverScript");
		PropertyConfigurator.configure("log4j.properties");
		APP_LOGS.info("Hello");
			
		APP_LOGS.info("Properties loaded. Starting testing");
		
		// 1) check the runmode of test Suite
		
		// 2) Runmode of the test case in test suite
	    // 3) Execute keywords of the test case serially
		// 4) Execute Keywords as many times as
		// number of data sets - set to Y
		APP_LOGS.debug("Intialize Suite xlsx");
		suiteXLS = new Xls_Reader(System.getProperty("user.dir")+"//src//com//kpmg//xls//Suite.xlsx");
		
		
		for(currentSuiteID=2;currentSuiteID<=suiteXLS.getRowCount(Constants.TEST_SUITE_SHEET);currentSuiteID++){
		
			APP_LOGS.debug(suiteXLS.getCellData(Constants.TEST_SUITE_SHEET, Constants.Test_Suite_ID, currentSuiteID)+" -- "+  suiteXLS.getCellData("Test Suite", "Runmode", currentSuiteID));
			// test suite name = test suite xls file having tes cases
			currentTestSuite=suiteXLS.getCellData(Constants.TEST_SUITE_SHEET, Constants.Test_Suite_ID, currentSuiteID);
			if(suiteXLS.getCellData(Constants.TEST_SUITE_SHEET, Constants.RUNMODE, currentSuiteID).equals(Constants.RUNMODE_YES)){
				// execute the test cases in the suite
				APP_LOGS.debug("******Executing the Suite******"+suiteXLS.getCellData(Constants.TEST_SUITE_SHEET, Constants.Test_Suite_ID, currentSuiteID));
				currentTestSuiteXLS=new Xls_Reader(System.getProperty("user.dir")+"//src//com//kpmg//xls//"+currentTestSuite+".xlsx");
				// iterate through all the test cases in the suite
				for(currentTestCaseID=2;currentTestCaseID<=currentTestSuiteXLS.getRowCount("Test Cases");currentTestCaseID++){				
					APP_LOGS.debug(currentTestSuiteXLS.getCellData(Constants.TEST_CASES_SHEET, Constants.TCID, currentTestCaseID)+" -- "+currentTestSuiteXLS.getCellData("Test Cases", "Runmode", currentTestCaseID));
					currentTestCaseName=currentTestSuiteXLS.getCellData(Constants.TEST_CASES_SHEET, Constants.TCID, currentTestCaseID);
									
					if(currentTestSuiteXLS.getCellData(Constants.TEST_CASES_SHEET, Constants.RUNMODE, currentTestCaseID).equals(Constants.RUNMODE_YES)){
						APP_LOGS.debug("Executing the test case -> "+currentTestCaseName);
					 if(currentTestSuiteXLS.isSheetExist(currentTestCaseName)){
					  	// RUN as many times as number of test data sets with runmode Y
					  for(currentTestDataSetID=2;currentTestDataSetID<=currentTestSuiteXLS.getRowCount(currentTestCaseName);currentTestDataSetID++)	
					  {
						resultSet = new ArrayList<String>();
						APP_LOGS.debug("Iteration number "+(currentTestDataSetID-1));
						// checking the runmode for the current data set
					   if(currentTestSuiteXLS.getCellData(currentTestCaseName, Constants.RUNMODE, currentTestDataSetID).equals(Constants.RUNMODE_YES)){
						
					    // iterating through all keywords	
						   executeKeywords(); // multiple sets of data
					   }
					   createXLSReport();
					  }
					 }else{
						// iterating through all keywords	
						 resultSet= new ArrayList<String>();
						 executeKeywords();// no data with the test
						 
					 } createXLSReport();
					}
				}
			}

		}	
	}
	
	
	public void executeKeywords() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		// iterating through all keywords	
		for(currentTestStepID=2;currentTestStepID<=currentTestSuiteXLS.getRowCount(Constants.TEST_STEPS_SHEET);currentTestStepID++){
			// checking TCID
		  if(currentTestCaseName.equals(currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.TCID, currentTestStepID))){
						
			data=currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.DATA,currentTestStepID  );
		
			// if data given in sheet
			if (data.contains(",")) {
				
				String[] variables = data.split(",");
				data_count = variables.length;
				dataArray = new String[data_count];
				for (int x = 0; x < data_count; x++) {

					data = variables[x];
					if (data.startsWith(Constants.DATA_START_COL)) {
						currentExcellColumnName = data.split(Constants.DATA_SPLIT)[1];
						// read actual data value from the corresponding column
						data = currentTestSuiteXLS.getCellData(currentTestCaseName,data.split(Constants.DATA_SPLIT)[1],currentTestDataSetID);
					} else if (data.startsWith(Constants.CONFIG)) {
						// read actual data value from config.properties
						data = CONFIG.getProperty(data.split(Constants.DATA_SPLIT)[1]);
					} else {
						// by default read actual data value from
						// or.properties
						data = OR.getProperty(data);
					}
					
					
					dataArray[x] = data;
				}

			} else{
					
			if(data.startsWith(Constants.DATA_START_COL)){
				
				currentExcellColumnName = data.split(Constants.DATA_SPLIT)[1];
				// read actual data value from the corresponding column				
				data=currentTestSuiteXLS.getCellData(currentTestCaseName, data.split(Constants.DATA_SPLIT)[1] ,currentTestDataSetID );
			}
			else if(data.startsWith(Constants.CONFIG)){
				//read actual data value from config.properties		
				data=CONFIG.getProperty(data.split(Constants.DATA_SPLIT)[1]);
						
			}else{
				//by default read actual data value from or.properties
				data=OR.getProperty(data);
			}
			dataArray = new String[1];
			dataArray[0] = data;
			}
			// read object from sheet
			object=currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.OBJECT,currentTestStepID);
				currentKeyword=currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.KEYWORD, currentTestStepID);
				
				if (object.contains(",")) {
					String[] objectvariable = object.split(",");
					object_count = objectvariable.length;
					objectArray = new String[object_count];
					for (int x = 0; x < object_count; x++) {
						objectArray[x] = objectvariable[x];
					}
				} else {
					objectArray = new String[1];
					objectArray[0] = object;
				}

				// check dynamic objects
				int z = 0;
				for (int x = 0; x < objectArray.length; x++) {
					String OrObject = OR.getProperty(objectArray[x]);
					if (OrObject != null) {
						if (OrObject.contains(Constants.DYNAMICOBJECTTEXT)) {
							Dynamic_object_count = StringUtils.countMatches(
									OrObject, Constants.DYNAMICOBJECTTEXT);
							for (int y = 0; y < Dynamic_object_count; y++) {
								OrObject = OrObject.replaceFirst("\\"
										+ Constants.DYNAMICOBJECTTEXT,
										dataArray[z]);
								z++;
							}
						} else {
							Dynamic_object_count = 0;
						}
						objectArray[x] = OrObject;
						object = objectArray[0];
					}
				}

				// Read keyword from sheet
				currentKeyword = currentTestSuiteXLS.getCellData(
						Constants.TEST_STEPS_SHEET, Constants.KEYWORD,
						currentTestStepID);
				APP_LOGS.debug(currentKeyword);
				System.out.println(currentKeyword);

				APP_LOGS.info(currentKeyword);
				// code to execute the keywords as well
			    // reflection API
				
				for (int i = 0; i < method.length; i++) {
					if (method[i].getName().equals(currentKeyword)) {
						if ((objectArray.length <= 1)
								&& (dataArray.length - Dynamic_object_count <= 1)) {
							APP_LOGS.debug("currentKeyword...: "
									+ currentKeyword);
							APP_LOGS.debug("object...: " + object);
							APP_LOGS.debug("data...: " + data);
							System.out.println("Array keyword "+currentKeyword+"----"+data);
							//System.out.println("Array keyword "+currentKeyword);
							keyword_execution_result = (String) method[i]
									.invoke(keywords, object, data);
						} else {
							// System.out.println("Array keyword "+currentKeyword);
							APP_LOGS.debug("currentKeyword...: "
									+ currentKeyword);
							APP_LOGS.debug("objectArray...: " + objectArray[0]);
							APP_LOGS.debug("dataArray...: " + dataArray[0]);

							keyword_execution_result = (String) method[i]
									.invoke(keywords, objectArray, dataArray);
						}
						APP_LOGS.debug(keyword_execution_result);
						resultSet.add(keyword_execution_result);
						// Write result for every keyword after keyword
						// execution completed
						createIndidualReport(currentTestStepID,
								keyword_execution_result);
						// capture screenshot
						capturescreenShot_method.invoke(keywords,
								currentTestSuite + "_" + currentTestCaseName
										+ "_TS" + currentTestStepID + "_"
										+ (currentTestDataSetID - 1),
								keyword_execution_result);
						// how do we call
						// what will be the file name
					}
				}

			}
		}

	}
	public void createXLSReport(){
		
   
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
	    Date date = new Date();
	    //System.out.println("Time Stamp : "+dateFormat.format(date));
	    String colName=Constants.RESULT +dateFormat.format(date);
		  
		boolean isColExist=false;
		
		for(int c=0;c<currentTestSuiteXLS.getColumnCount(Constants.TEST_STEPS_SHEET);c++){
			if(currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET,c , 1).equals(colName)){
				System.out.println("column data:"+ currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET,c , 1));
				isColExist=true;
				break;
			}
		}
		System.out.println("Results not updated2");
		if(!isColExist)
			currentTestSuiteXLS.addColumn(Constants.TEST_STEPS_SHEET, colName);
		int index=0;
		System.out.println("Results not updated3");
		for(int i=2;i<=currentTestSuiteXLS.getRowCount(Constants.TEST_STEPS_SHEET);i++){
			
			if(currentTestCaseName.equals(currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.TCID, i))){
				if(resultSet.size()==0)
					currentTestSuiteXLS.setCellData(Constants.TEST_STEPS_SHEET, colName, i, Constants.KEYWORD_SKIP);
				else	
					currentTestSuiteXLS.setCellData(Constants.TEST_STEPS_SHEET, colName, i, resultSet.get(index));
				index++;
			}
			
					}
		System.out.println("Results not updated4");
		if(resultSet.size()==0){
			// skip
			currentTestSuiteXLS.setCellData(currentTestCaseName, Constants.RESULT, currentTestDataSetID, Constants.KEYWORD_SKIP);
			return;
		}else{
			for(int i=0;i<resultSet.size();i++){
				if(!resultSet.get(i).equals(Constants.KEYWORD_PASS)){
					currentTestSuiteXLS.setCellData(currentTestCaseName, Constants.RESULT, currentTestDataSetID, resultSet.get(i));
					return;
				}
			}
		}
		System.out.println("Results not updated5");
		currentTestSuiteXLS.setCellData(currentTestCaseName, Constants.RESULT, currentTestDataSetID, Constants.KEYWORD_PASS);
	//	if(!currentTestSuiteXLS.getCellData(currentTestCaseName, "Runmode",currentTestDataSetID).equals("Y")){}
		
	}
	public void createIndidualReport(int currentTestStepID,
			String keyword_execution_result) 
	{
		// Write PASS (or) FAIL
		currentTestSuiteXLS.setCellData(Constants.TEST_STEPS_SHEET,
				resultColumnName, currentTestStepID, keyword_execution_result);
	}
	

}