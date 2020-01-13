package com.startinpoint.ssreport.serviceImpl;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import com.startinpoint.ssreport.dao.SSReportDao;
import com.startinpoint.ssreport.daoImpl.SSReportDaoImpl;
import com.startinpoint.ssreport.entity.SSReport;
import com.startinpoint.ssreport.main.Constants;
import com.startinpoint.ssreport.main.ReadPropertiesFile;
import com.startinpoint.ssreport.service.ExportExcelReportService;

public class ExportExcelReportServiceImpl implements ExportExcelReportService{
	
	private Logger debugLog = Logger.getLogger("debug.logger");
	private static Logger errorLog = Logger.getLogger("error.logger");
	SSReport ssReport=new SSReport();
	SSReportDao ssDAO=new SSReportDaoImpl();
	List<String> nonWorkingDays=ssDAO.nonWorkingDay();
	
	public SSReport CalculateData(SSReport ssReport) {
		
		String filename=ssReport.getReportName();
		//prepare time and date
		String removepdf=filename.split("\\.")[0];
		String time=removepdf.substring(removepdf.lastIndexOf('_') + 1);
		String date=removepdf.substring(removepdf.lastIndexOf('_') -8,removepdf.lastIndexOf('_'));
		System.out.println(removepdf);
		
		date=changeUserdefinedFormatForDateTime(Constants.formatForReportDate,ReadPropertiesFile.REPORTDATEFORMAT,date);
		ssReport.setReportDate(date);//Set user defined format date
		
		time=changeUserdefinedFormatForDateTime(Constants.formatForReportTime,ReadPropertiesFile.REPORTTIMEFORMAT,time);
		ssReport.setReportTime(time);//Set userdefined format time
		
		//prepare duration
		String dateStart = ssReport.getSssStart();
		String dateStop = ssReport.getSssComplete();
		debugLog.debug("start and end : "+dateStart+" "+dateStop);
		
		if(ssReport.getSssStart()!=null && ssReport.getSssComplete()!=null) {
			 
			 int nonWorkDays=ssDAO.getFilteredCount(ssReport,nonWorkingDays);
			 ssReport.setNonWorkingDays(nonWorkDays);
		 }
		
		String calculatedDuration=calculateDuration(dateStart,dateStop,ssReport.getNonWorkingDays());
		DateTimeFormatter simpleCurrentFormat = DateTimeFormatter.ofPattern(Constants.formatForReportDuration);
		DateTimeFormatter simpleUserFormat =  DateTimeFormatter.ofPattern(ReadPropertiesFile.DURATION);
		calculatedDuration = simpleUserFormat.format(simpleCurrentFormat.parse(calculatedDuration));
		ssReport.setDuration(calculatedDuration);
		return ssReport;
	}
	
	//to calculate for start date, end date and non working days
	public String calculateDuration(String dateStart,String dateStop,int nonWorkDays){

		//HH converts hour in 24 hours format (0-23), day calculation
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String diffDateTime = null;
		Date d1 = null;
		Date d2 = null;
		try {
			d1 = format.parse(dateStart);
			d2 = format.parse(dateStop);
			
			//in milliseconds
			long diff = d2.getTime() - d1.getTime();

			//String diffSeconds = Long.toString(diff / 1000 % 60);
			String diffMinutes = Long.toString(diff / (60 * 1000) % 60);
			String diffHours = Long.toString(diff / (60 * 60 * 1000) % 24);
			String diffDays = Long.toString((diff / (24 * 60 * 60 * 1000))-Long.valueOf(nonWorkDays));
			System.out.println("original calculated duration: "+diffDays+" "+diffHours+":"+diffMinutes);
			
			diffDays=appendStringToFormat(diffDays);
			diffHours=appendStringToFormat(diffHours);
			diffMinutes=appendStringToFormat(diffMinutes);
			
			diffDateTime=diffDays+" "+diffHours+":"+diffMinutes;
			System.out.println("diffDateTime including (nonWorkingDay): "+diffDateTime);
			debugLog.debug("diffDateTime including (nonWorkingDay): "+diffDateTime);
		} catch (Exception e) {
			e.printStackTrace();
			errorLog.error("Calculation Error in Duration Column For Excel");
		}
		return diffDateTime;
	}
	
	//to get format of date in user input string (eg. 1/1/2020 to 01/01/2020 in date)
	public String appendStringToFormat(String str){
		
		String newString=new String();
		if(str.length()==1){
			newString="0"+str;
		}
		if(str.length()==2){
			newString=str;
		}
		return newString;
	}
	
	//to get format of date and time as user input 
	public String changeUserdefinedFormatForDateTime(String currentFormat,String definedFormat,String current){
		
		SimpleDateFormat simpleCurrentFormat = new SimpleDateFormat(currentFormat);
		SimpleDateFormat simpleUserFormat = new SimpleDateFormat(definedFormat);
		
		try {
			current = simpleUserFormat.format(simpleCurrentFormat.parse(current));
		} catch (ParseException e) {
			e.printStackTrace();
			errorLog.error("Userfined Date Time Format Error : Parse Exception");
		}
		return current;	
	}
	
	//preparing to get edited data 
	public List<SSReport> PrepareDataToWrite(List<SSReport> ssReportList){
		
		List<SSReport> prepareSSReportList=new ArrayList<SSReport>();
		
		for(SSReport ssList:ssReportList){
			SSReport prepareSSReport = null;
			
			if(ssList.getReportName()!=null){
			prepareSSReport=CalculateData(ssList);
			prepareSSReportList.add(prepareSSReport);
			}else{
			prepareSSReport=ssList;
			prepareSSReportList.add(prepareSSReport);
			}
			
			debugLog.debug("Each EditedSSReport:"+prepareSSReport);
		}
		return prepareSSReportList;
	}
	
	private static String[] columns={Constants.reportName,Constants.reportDate,Constants.reportTime,Constants.sssID,Constants.sssStart,
			Constants.sssComplete,Constants.duration,Constants.category,Constants.remarks};
	
	@Override
	public String exportExcel(String filepath, String filename, List<SSReport> ssReportList) throws IOException {
		// Create a Workbook
        Workbook workbook = new XSSFWorkbook();
        // Create a Sheet
        Sheet sheet = workbook.createSheet(filename);
        // Create a Row
        Row headerRow = sheet.createRow(0);
        // Create cells
        for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }
        int rowNum = 1;
        
        for(SSReport ssReport: ssReportList) {
        	if(!(ssReport==null)){
        		Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(ssReport.getReportName());
                row.createCell(1).setCellValue(ssReport.getReportDate());
                row.createCell(2).setCellValue(ssReport.getReportTime());
                row.createCell(3).setCellValue(ssReport.getSssId());
                row.createCell(4).setCellValue(ssReport.getSssStart());
                row.createCell(5).setCellValue(ssReport.getSssComplete());
                row.createCell(6).setCellValue(ssReport.getDuration());
                row.createCell(7).setCellValue(ssReport.getCategory());
                row.createCell(8).setCellValue(ssReport.getRemarks());
        	}  
        }
        // Resize all columns to fit the content size
        for(int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }
        boolean isRowEmpty = false;
        for(int i = 0; i < sheet.getLastRowNum(); i++){
            if(sheet.getRow(i)==null){
                sheet.shiftRows(i + 1, sheet.getLastRowNum(), -1);
                i--;
            continue;
            }
            for(int j =0; j<sheet.getRow(i).getLastCellNum();j++){
                if(sheet.getRow(i).getCell(j).toString().trim().equals("")){
                    isRowEmpty=true;
                }else {
                    isRowEmpty=false;
                    break;
                }
            }
            if(isRowEmpty==true){
                sheet.shiftRows(i + 1, sheet.getLastRowNum(), -1);
                i--;
            }
        }
        //Delete the old file if exist
        fileDelete(filepath);
        
        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(filepath+"\\"+filename+".xlsx");
        workbook.write(fileOut);
        fileOut.close();
        // Closing the workbook
        //workbook.close();
		return "Sucessfully exported excel";
    }
	
	//to delete files if exist in folder and to check folder is or not in given folder path
	public void fileDelete(String filepath) {
		
		File directory = new File(filepath);
        File[] files = directory.listFiles();
        try {
		for (File file : files) {
			if(file.exists()) {
			file.delete();
			}
		}
		}catch(NullPointerException e ) {
			e.printStackTrace();
			errorLog.error(e.getMessage()+" Pls, Check your folder path.");
		}
	}
}
