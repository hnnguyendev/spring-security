package com.nhnghia.service;

public interface EmailService {
	
	void sendNotificationEmail();
	
	void sendVerifyEmail(String email);
	
	void sendResetPasswordEmail(String email);

}
