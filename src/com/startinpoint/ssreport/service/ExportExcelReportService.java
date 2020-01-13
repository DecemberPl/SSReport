package com.startinpoint.ssreport.service;

import java.io.IOException;
import java.util.List;
import com.startinpoint.ssreport.entity.SSReport;

public interface ExportExcelReportService {
	
	public String exportExcel(String filepath, String filename,List<SSReport> ssReportList) throws IOException;
	
	public List<SSReport> PrepareDataToWrite(List<SSReport> ssReportList);
}
