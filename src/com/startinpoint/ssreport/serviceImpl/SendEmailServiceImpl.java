package com.startinpoint.ssreport.serviceImpl;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

import com.startinpoint.ssreport.service.SendEmailService;
import com.startinpoint.utils.DesEncrypter;

public class SendEmailServiceImpl implements SendEmailService {
	
	private Logger debugLog = Logger.getLogger("debug.logger");
	private static Logger errorLog = Logger.getLogger("error.logger");
	
	@Override
	public void sendMail(String toMail,String from,String password,String host,String port,String filename,String folderpath,String subject,String content) {
	    
		if(password!=""){
			try{
				//System.out.println("pwd....."+password);
				DesEncrypter desEn = new DesEncrypter();
				password = desEn.decrypt(password);
			}catch (Exception e) {
				e.printStackTrace();
				errorLog.error("Email Password Decryption Exception");
			}
		}
		
	    final String pw=password;
	    Properties props = new Properties();
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.smtp.host", host);
	    props.put("mail.smtp.port", port);
	    props.put("mail.smtp.ssl.trust", host);

	    // Get the Session object.
	    Session session = Session.getInstance(props,
	       new javax.mail.Authenticator() {
	          protected PasswordAuthentication getPasswordAuthentication() {
	             return new PasswordAuthentication(from, pw);
	          }
	       });

	    try {
	       Message message = new MimeMessage(session);
	       message.setFrom(new InternetAddress(from));
	       message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(toMail));
	       message.setSubject(subject);
	       
	       BodyPart messageBodyPart = new MimeBodyPart();
	       messageBodyPart.setContent(content,"text/html");
	       Multipart multipart = new MimeMultipart();
	       multipart.addBodyPart(messageBodyPart);
	       messageBodyPart = new MimeBodyPart();
	       
	       DataSource source = new FileDataSource(folderpath+"\\"+filename+".xlsx");
	       messageBodyPart.setDataHandler(new DataHandler(source));
	       messageBodyPart.setFileName(filename+".xlsx");
	       multipart.addBodyPart(messageBodyPart);
	       message.setContent(multipart);
	       
	       Transport.send(message);
	       debugLog.debug("Sent message successfully....");

	    } catch (MessagingException e) {
	    	e.printStackTrace();
	    	errorLog.error(e.getMessage()+"MessageException in Email Service");
	    }

	}

	
}
