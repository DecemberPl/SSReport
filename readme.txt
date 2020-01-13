Note:
SSReport Overview

	 1)	The main purpose of this program is to send mail to the user with the generated excel report as attachment.
	
	 2) the user can define the total amount of month desired to report in the config.properties . 
		E.g current month is Jan 2020 and defined total month is 3 , 
			the report will generate for previous 3 months Dec ,Nov and Oct 2019.
			
	 3) the user can also define the format of date time columns for report excel, subject and content with the HTML format for email in the config.properties.
	 
	 4) for duration column, it is only count the working days.
	 
	 5) the name of report excel and temporary location to save the generated excel also can configure in the config.properties.
	 
	 6) the excel in the temporary location will be deleted when the program run next time.
	 
	 7) for the detail of config.properties it can reference the "Documentation for SSReport Properties.docx".
	 
	 8) if the program not working properly it can see the 'ssreport_error.log' file in the 'log' folder at the same directory level of SSReport.exe.
