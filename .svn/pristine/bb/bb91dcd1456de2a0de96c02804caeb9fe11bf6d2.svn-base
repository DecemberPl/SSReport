package com.startinpoint.ssreport.daoImpl;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import com.startinpoint.ssreport.dao.SSReportDao;
import com.startinpoint.ssreport.main.Constants;
import com.startinpoint.ssreport.main.ReadPropertiesFile;
import com.startinpoint.ssreport.service.DB2Connection;
import com.startinpoint.ssreport.entity.SSReport;

public class SSReportDaoImpl implements SSReportDao{
	
	private Logger debugLog = Logger.getLogger("debug.logger");
	private static Logger errorLog = Logger.getLogger("error.logger");
	DB2Connection db2Connection;
	List<SSReport> ssReportList=new ArrayList<>();
	SSReport ssReport;
	/**
	 * The purpose of this method is calculate start date of start month 
	 * and end date of end month according to report month from config.properties
	 */
	@Override
	public List<Timestamp> getReportMonthsForCalculation() {
		
		String reportmonthLength=ReadPropertiesFile.REPORTMONTH;
		
		//take start date of start month
		Calendar calendarForStart=Calendar.getInstance();
    	Date dateForStart=new Date();
    	calendarForStart.setTime(dateForStart);
    	calendarForStart.add(Calendar.MONTH, -Integer.parseInt(reportmonthLength));
    	calendarForStart.set(Calendar.DAY_OF_MONTH, 1);
    	calendarForStart.set(Calendar.HOUR_OF_DAY, 0);
    	calendarForStart.set(Calendar.MINUTE, 0);
    	calendarForStart.set(Calendar.SECOND, 0);
    	calendarForStart.set(Calendar.MILLISECOND, 0);
		Date startDate=calendarForStart.getTime();
		
		//take end date of end month
		Calendar calendarForEnd=Calendar.getInstance();
		Date dateForEnd=new Date();
		calendarForEnd.setTime(dateForEnd);
		calendarForEnd.add(Calendar.MONTH, -1);
		calendarForEnd.set(Calendar.DAY_OF_MONTH, calendarForEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendarForEnd.set(Calendar.HOUR_OF_DAY, 23);
		calendarForEnd.set(Calendar.MINUTE, 59);
		calendarForEnd.set(Calendar.SECOND, 59);
		calendarForEnd.set(Calendar.MILLISECOND, 999);
		Date endDate=calendarForEnd.getTime();
		
		//To take currentDate with timestampFormat
		Date currentDate=new Date();
		Timestamp timestampForCurrentDate=new Timestamp(currentDate.getTime());  
        System.out.println("Today Date (Timestamp): "+timestampForCurrentDate);
        debugLog.debug("Today Date (Timestamp): "+timestampForCurrentDate);
        
        //To take start Date with timestampFormat
        Timestamp timestampForStartDate=new Timestamp(startDate.getTime());  
        System.out.println("Start Date (Timestamp): "+timestampForStartDate);
        debugLog.debug("Start Date (Timestamp): "+timestampForStartDate);
        
        //To take end Date with timestampFormat
        Timestamp timestampForEndDate=new Timestamp(endDate.getTime());
        System.out.println("End Date (Timestamp): "+timestampForEndDate);
        debugLog.debug("End Date (Timestamp): "+timestampForEndDate);
        
        //To insert CurrentDate, StartDate of StartMonth, EndDate of EndMonth into List
        List<Timestamp> timestampList=new ArrayList<Timestamp>();
        timestampList.add(timestampForCurrentDate);
        timestampList.add(timestampForStartDate);
        timestampList.add(timestampForEndDate);
        return timestampList;
	}
	

	/**
	 * The purpose of this method is to take required data from 'SSReport' table of database
	 */
	@Override
	public List<SSReport> getData() {
		
		try(Connection con=db2Connection.getInstance().getConnection()){
		
		 List<Timestamp> timestampList=new ArrayList<Timestamp>(3);
		 timestampList=getReportMonthsForCalculation();
		 Timestamp startDateTimestamp=timestampList.get(1);
		 Timestamp endDateTimestamp=timestampList.get(2);
		 
		 String sql="SELECT "+Constants.reportNameDB+", "+Constants.SSUserDB+", "+Constants.dateStartDB+", "+Constants.dateEndDB+", "+Constants.ssCategoryDB
				+" FROM "+ReadPropertiesFile.USERNAME+"."+ReadPropertiesFile.VIEWNAME+" WHERE "+Constants.timestampForSSReportDB+">='"+startDateTimestamp+"' AND "+Constants.timestampForSSReportDB+"<='"+endDateTimestamp+"'";
		 
		 /*for sql db
		 String sql="SELECT "+Constants.reportNameDB+", "+Constants.SSUserDB+", "+Constants.dateStartDB+", "+Constants.dateEndDB+", "+Constants.ssCategoryDB
					+" FROM "+ReadPropertiesFile.VIEWNAME+" WHERE "+Constants.timestampForSSReportDB+">='"+tsStart+"' AND "+Constants.timestampForSSReportDB+"<='"+tsEnd+"'";*/
		 System.out.println(sql);
		 debugLog.debug("sql statement for SSReport : "+sql);
		 
		 Statement statement=con.createStatement();
		 ResultSet rs=statement.executeQuery(sql);
		 while(rs.next()){
			 ssReport=new SSReport();
			 ssReport.setReportName(rs.getString(Constants.reportNameDB));
			 ssReport.setSssId(rs.getString(Constants.SSUserDB));
			 ssReport.setSssStart(rs.getString(Constants.dateStartDB));
			 ssReport.setSssComplete(rs.getString(Constants.dateEndDB));
			 ssReport.setCategory(rs.getString(Constants.ssCategoryDB));
			 System.out.println("Each SSReport"+ssReport.toString());
			 debugLog.debug("Each SSReport"+ssReport.toString());
			 ssReportList.add(ssReport);
		 }
		 System.out.println("ssReportList after sql: "+ssReportList);
		 debugLog.debug("ssReportList after sql: "+ssReportList);
		 
		}catch(SQLException e){
			e.printStackTrace();
			errorLog.error("Failed DB2 Connection or SQLException");
			System.exit(0);
		}catch(Exception e){
			e.printStackTrace();
			errorLog.error("Failed DB2 Connection Exception");
			System.exit(0);
		}
		return ssReportList;
	}
	
	/**
	 * The purpose of this method is to convert String to Timestamp for NonWorkingDay sql
	 */
	public static Timestamp convertStringToTimestamp(String str_date, String pattern) {
        try {

            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            Date date = formatter.parse(str_date);
            java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
            return timeStampDate;

        } catch (ParseException e) {
            System.out.println("Exception :" + e);
            errorLog.error("Timestamp Parse Exception");
            return null;
        }
    }
	
	/**
	 * The purpose of this method is to take all data from 'NonWorkingDay' table of database
	 */
	@Override
	public List<String> nonWorkingDay() {
		String nonWorkingDay =null;
		List<String> nonWorkingDays=new ArrayList<>();
		try(Connection con=db2Connection.getInstance().getConnection()){
			
			//String sql="SELECT COUNT(*) FROM "+ReadPropertiesFile.NONWORKINGTABLENAME+" WHERE "+Constants.date+">='"+sDate+"' AND "+Constants.date+"<='"+eDate+"'";
		 
			String sql="SELECT * FROM "+ReadPropertiesFile.USERNAME+"."+ReadPropertiesFile.NONWORKINGTABLENAME;

		 Statement statement=con.createStatement();
		 ResultSet rs=statement.executeQuery(sql);
		 while ( rs.next())
		 {
			 
			 nonWorkingDay = rs.getString("DATE"); //COUNT(*) for mysqldb
		     nonWorkingDays.add(nonWorkingDay);
		 }
		 System.out.println("List of Non Working Days: "+nonWorkingDays);
		 debugLog.debug("List of Non Working Days: "+nonWorkingDays);
		 
		}catch(SQLException e){
			e.printStackTrace();
			errorLog.error("Failed DB2 Connection or SQLException");
			System.exit(0);
		}catch(Exception e){
			e.printStackTrace();
			errorLog.error("Failed DB2 Connection Exception");
			System.exit(0);
		}
		
		return nonWorkingDays;
	}
	
	/**
	 * The purpose of this method is to filter count of NonWorkingDay between StartDate and EndDate
	 */
	@Override
	public int getFilteredCount(SSReport ssReport, List<String> nonWorkingDays) {

		List<Timestamp> nonWorkingDaysTimestampList=new ArrayList<>();
		
		Timestamp startDate=convertStringToTimestamp(ssReport.getSssStart(),"yyyy-MM-dd HH:mm:ss.SSS");
		Timestamp endDate=convertStringToTimestamp(ssReport.getSssComplete(),"yyyy-MM-dd HH:mm:ss.SSS");
		
		for(String nonWorkingDay: nonWorkingDays) {
			Timestamp nonWorkingDayTimestamp=convertStringToTimestamp(nonWorkingDay,"yyyyMMdd");
			nonWorkingDaysTimestampList.add(nonWorkingDayTimestamp);
		}
		
		long filtered = nonWorkingDaysTimestampList.stream().filter(Timestampfilter -> 
		Timestampfilter.after(startDate) && Timestampfilter.before(endDate)).count();
		int filter=(int) filtered;
		System.out.println("filterd TimestampList"+filter);
		return filter;	
	}

}
