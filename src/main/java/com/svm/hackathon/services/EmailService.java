package com.svm.hackathon.services;

import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailService {

	public static String HOST = "smtp.sendgrid.net";
	public static String EMAIL_FROM = "fake@gmail.com";
	public static String USERNAME = "secret";
	public static String PASSWORD = "secret";
	//public static String URL = "http://partner-pivotal.cfapps.io/#/registration/";
	public static String URL = "localhost:8080/#/registration/";	
	public static void sendVerificationEmail(String emailTo){
		UUID token = UUID.randomUUID();
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", HOST);
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(USERNAME, PASSWORD);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(EMAIL_FROM));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));
			message.setSubject("Email Verification");
			message.setContent("<a href=\"" + URL + token.toString() + "\">" + URL + token.toString() + "</a>", "text/html");
			Transport.send(message);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

}
