package com.svm.hackathon.services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import javax.imageio.ImageIO;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.twilio.sdk.*; 
import com.twilio.sdk.resource.factory.*; 
import com.twilio.sdk.resource.instance.*; 
import com.twilio.sdk.resource.list.*; 
import java.io.ByteArrayInputStream;
import sun.misc.BASE64Decoder;
import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
public class TwilioSMS {
	private static final String ACCOUNT_SID = "secret"; 
	private static final String AUTH_TOKEN = "secret"; 
	
	private static final String CLOUD_NAME = "secret";
	private static final String API_KEY = "secret";
	private static final String API_SECRET = "secret";
	public static String verifyNumber(String phonenum) throws TwilioRestException{
		TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);
		Map<String, String> params = new HashMap<String, String>();
		params.put("FriendlyName", phonenum);
		params.put("PhoneNumber", phonenum);
		OutgoingCallerIdFactory callerIdFactory = client.getAccount().getOutgoingCallerIdFactory();
		CallerIdValidation validationAttempt = callerIdFactory.create(params);
		return validationAttempt.getValidationCode();
	}
	
	public static Boolean isVerified(String phonenum) throws TwilioRestException{
		phonenum = "+"+phonenum;
		TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);
		OutgoingCallerIdList callerIds = client.getAccount().getOutgoingCallerIds();
		for (OutgoingCallerId callerId : callerIds) {
			System.out.println(callerId.getPhoneNumber());
			if(phonenum.equals(callerId.getPhoneNumber())){
				return true;
			}
		} 
		return false;
	}
	
	public static void sendSMS(String toNum, String message, String source) throws TwilioRestException, IOException { 
		String url = "";
		if(source !=null){
			File outputfile = convertToImage(source);
			url = uploadImageToCloud(outputfile);
		}

		TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
	    params.add(new BasicNameValuePair("To", toNum)); // Replace with a valid phone number for your account.
	    params.add(new BasicNameValuePair("From", "+16266429968")); // Replace with a valid phone number for your account.
	    params.add(new BasicNameValuePair("Body", message));
	    
	    if(source !=null){
	    	 params.add(new BasicNameValuePair("MediaUrl", url)); 
	    }

	    Account account = client.getAccount();
        MessageFactory messageFactory = account.getMessageFactory();
        Message mms = messageFactory.create(params);
	} 
	
	public static File convertToImage(String source) throws IOException{
		String imageDataBytes = source.substring(source.indexOf(",")+1);
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] imageByte = decoder.decodeBuffer(imageDataBytes);
		ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
		BufferedImage image = ImageIO.read(bis);
		bis.close();
		File outputfile = new File("image.png");
		

		ImageIO.write(image, "png", outputfile);
		return outputfile;
	}
	
	public static String uploadImageToCloud(File outputfile) throws IOException{
		Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
				  "cloud_name", CLOUD_NAME,
				  "api_key", API_KEY,
				  "api_secret", API_SECRET));
		
		Map uploadResult = cloudinary.uploader().upload(outputfile, ObjectUtils.asMap());
		return (String) uploadResult.get("url");
	}
}
