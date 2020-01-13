package com.startinpoint.ssreport.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.startinpoint.ssreport.main.ReadPropertiesFile;
import com.startinpoint.utils.DesEncrypter;

/**
 * 
 * @author phyueizaw
 * @since 23/12/2019
 * To get db connection with singleton pattern
 */
public class DB2Connection {

	private static DB2Connection instance;
	private Connection connection;
	
	private String url = ReadPropertiesFile.URL;
	private String user = ReadPropertiesFile.USERNAME;
	private String password =ReadPropertiesFile.PASSWORD;
	private String driver =ReadPropertiesFile.DRIVER;
	
	/*
	 *for mysql connection
	private String url = "jdbc:mysql://192.168.4.201:3306/ssreport?characterEncoding=UTF-8";
	private String user = ReadPropertiesFile.USERNAME;
	private String password = ReadPropertiesFile.PASSWORD;
	private String driver = "com.mysql.jdbc.Driver";*/
	
	private DB2Connection() {
		
		Logger errorLog = Logger.getLogger("error.logger");
		try {
			  // Load the IBM Data Server Driver for JDBC and SQLJ with DriverManager
			  Class.forName(driver);
			  DesEncrypter des = new DesEncrypter();
			  password = des.decrypt(password);
			  this.connection = DriverManager.getConnection(url, user, password);
			  //for testing to lose connection
			  //this.connection.close();
			} catch (ClassNotFoundException e) {
			     e.printStackTrace();
			     errorLog.error("ClassNotFound Exception in DB2");
			} catch (SQLException e) {
				e.printStackTrace();
				 errorLog.error("SQLException Exception in DB2");
			} catch (Exception e) {
				e.printStackTrace();
				 errorLog.error("General Exception in DB2");
			}
	}
	
	public Connection getConnection() {
			return connection;
	}
	
	public static DB2Connection getInstance() throws SQLException {
		
		if(instance ==  null) {
			instance = new DB2Connection();
		}else if(instance.getConnection().isClosed()){
			instance = new DB2Connection();
		}
		return instance;
	}
	
}
