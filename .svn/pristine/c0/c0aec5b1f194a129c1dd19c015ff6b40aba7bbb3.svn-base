package com.startinpoint.ssreport.main;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.startinpoint.ssreport.daoImpl.SSReportDaoImpl;
import com.startinpoint.ssreport.entity.SSReport;
import com.startinpoint.ssreport.service.SendEmailService;
import com.startinpoint.ssreport.serviceImpl.ExportExcelReportServiceImpl;
import com.startinpoint.ssreport.serviceImpl.SendEmailServiceImpl;

public class SSReportMain {

	public static void main(String[] args) throws SQLException, IOException {
		
		Logger debugLog = Logger.getLogger("debug.logger");
		ReadPropertiesFile.initialAppConfig();
	
		SSReportDaoImpl ssReportDao=new SSReportDaoImpl();//to get DB2 Data
		List<SSReport> prepareList=new ArrayList<SSReport>();//to edit in order to export data
		prepareList=ssReportDao.getData();
		
		ExportExcelReportServiceImpl exportExcelReportService=new ExportExcelReportServiceImpl();
		prepareList=(List<SSReport>) exportExcelReportService.PrepareDataToWrite(prepareList);//to export excel sheet
		
		String exportExcelStatus=exportExcelReportService.exportExcel(ReadPropertiesFile.TEMPFILEPATH, ReadPropertiesFile.FILENAME,prepareList);
		System.out.println("Export Condition: "+exportExcelStatus);
		debugLog.debug("Export Condition"+exportExcelStatus);
		
		SendEmailService emailService=new SendEmailServiceImpl();//to send Email
	    emailService.sendMail(ReadPropertiesFile.TOEMAIL, ReadPropertiesFile.FROM, ReadPropertiesFile.EMAILPASSWORD,ReadPropertiesFile.HOST,ReadPropertiesFile.PORT,ReadPropertiesFile.FILENAME, ReadPropertiesFile.TEMPFILEPATH,ReadPropertiesFile.SUBJECT, ReadPropertiesFile.CONTENT);
	
	}
}
