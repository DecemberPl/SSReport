package com.startinpoint.ssreport.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class ReadPropertiesFile {
	public static String LOG4J_FILEPATH;
	public static String URL;
	public static String DRIVER;
	public static String USERNAME;
	public static String PASSWORD;
	public static String DATEBASENAME;
	public static String VIEWNAME;
	public static String TOEMAIL;
	public static String FROM;
	public static String EMAILUSERNAME;
	public static String EMAILPASSWORD;
	public static String HOST;
	public static String PORT;
	public static String SUBJECT;
	public static String CONTENT;
	public static String NONWORKINGTABLENAME;
	public static String TEMPFILEPATH;
	public static String FILENAME;
	public static String DURATION;
	public static String REPORTDATEFORMAT;
	public static String REPORTTIMEFORMAT;
	public static String REPORTMONTH;
	public static boolean isInitialized = false;
	private static Logger debugLog = Logger.getLogger("debug.logger");

	public ReadPropertiesFile() {
		super();
	}
	
	public static void initialAppConfig(){
		if(!isInitialized){
			isInitialized=true;
		}else{
			return;
		}
		
		if(readConfig()){
			System.out.println("Read Configuration from config.properties successfully");
			debugLog.debug("Read Configuration from config.properties successfully");

		}else{
			System.out.println("Read configuration failed.");
			debugLog.debug("Read configuration failed.");

		}
		
	}
	
	//to read Properties\\config.properties
	public static boolean readConfig(){
		boolean flag=true;
		String workingDir = System.getProperty("user.dir");		
		Properties prop = new Properties();
		InputStream input=null;	
		
		try {
			input = new FileInputStream(workingDir + "\\Properties\\config.properties");			
			prop.load(input);
			
			URL=prop.getProperty(Constants.urlForProperties)!=null? prop.getProperty(Constants.urlForProperties).trim():"";
			DRIVER=prop.getProperty(Constants.driverForProperties)!=null? prop.getProperty(Constants.driverForProperties).trim():"";
			USERNAME=prop.getProperty(Constants.usernameForProperties)!=null? prop.getProperty(Constants.usernameForProperties).trim():"";	
			PASSWORD=prop.getProperty(Constants.passwordForProperties)!=null? prop.getProperty(Constants.passwordForProperties).trim():"";
			DATEBASENAME=prop.getProperty(Constants.databasenameForProperties)!=null? prop.getProperty(Constants.databasenameForProperties).trim():"";
			VIEWNAME=prop.getProperty(Constants.viewnameForProperties)!=null? prop.getProperty(Constants.viewnameForProperties).trim():"";
			NONWORKINGTABLENAME=prop.getProperty(Constants.nonworkingTableForProperties)!=null? prop.getProperty(Constants.nonworkingTableForProperties).trim():"";
			
			TOEMAIL=prop.getProperty(Constants.toMailForProperties)!=null? prop.getProperty(Constants.toMailForProperties).trim():"";
			FROM=prop.getProperty(Constants.fromEmailForProperties)!=null? prop.getProperty(Constants.fromEmailForProperties).trim():"";
			EMAILUSERNAME=prop.getProperty(Constants.usernameEmailForProperties)!=null? prop.getProperty(Constants.usernameEmailForProperties).trim():"";
			EMAILPASSWORD=prop.getProperty(Constants.passwordEmailForProperties)!=null? prop.getProperty(Constants.passwordEmailForProperties).trim():"";	
			HOST=prop.getProperty(Constants.hostEmailForProperties)!=null? prop.getProperty(Constants.hostEmailForProperties).trim():"";
			PORT=prop.getProperty(Constants.portEmailForProperties)!=null? prop.getProperty(Constants.portEmailForProperties).trim():"";
			SUBJECT=prop.getProperty(Constants.subjectEmailForProperties)!=null? prop.getProperty(Constants.subjectEmailForProperties).trim():"";
			CONTENT=prop.getProperty(Constants.contentEmailForProperties)!=null? prop.getProperty(Constants.contentEmailForProperties).trim():"";
			
			TEMPFILEPATH=prop.getProperty(Constants.tempfilepathForProperties)!=null? prop.getProperty(Constants.tempfilepathForProperties).trim():"";
			FILENAME=prop.getProperty(Constants.filenameForProperties)!=null? prop.getProperty(Constants.filenameForProperties).trim():"";
			
			DURATION=prop.getProperty(Constants.durationForProperties)!=null? prop.getProperty(Constants.durationForProperties).trim():"";			
			REPORTDATEFORMAT=prop.getProperty(Constants.reportdateformatForProperties)!=null? prop.getProperty(Constants.reportdateformatForProperties).trim():"";
			REPORTTIMEFORMAT=prop.getProperty(Constants.reporttimeformatForProperties)!=null? prop.getProperty(Constants.reporttimeformatForProperties).trim():"";
			REPORTMONTH=prop.getProperty(Constants.reportmonthForProperties)!=null? prop.getProperty(Constants.reportmonthForProperties).trim():"";

			LOG4J_FILEPATH=prop.getProperty("scan_log4jFilePath")!=null? prop.getProperty("scan_log4jFilePath").trim():"";
			
			InputStream log4jFile = new FileInputStream(workingDir + LOG4J_FILEPATH);//create log folder if not exist
			prop.load(log4jFile);
			String filePath = workingDir +"/"+prop.getProperty("log4j.appender.debug.File");
			String[] arr = filePath.split("/");
			String replaceStr = filePath.replace(arr[arr.length-1], "");
			File logDir = new File(replaceStr);
			createDirectoryFolder(logDir);
			PropertyConfigurator.configure(workingDir + LOG4J_FILEPATH);
			
		} catch (FileNotFoundException e) {
			flag=false;
			e.printStackTrace();
		} catch (IOException e) {
			flag=false;
			e.printStackTrace();
		}finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return flag;
		
	}
	
	//to create new folder if folder of log4j does not exist
	public static void createDirectoryFolder(File filePath) {
		if(!filePath.exists()) {
			boolean isMakedir = filePath.mkdir();
			String message = isMakedir ? "Successfully created. "+filePath : "Filed to create. "+filePath;
			System.out.println(message);
		}else{
			System.out.println(filePath+" already exits.");
		}
	}
	
}
