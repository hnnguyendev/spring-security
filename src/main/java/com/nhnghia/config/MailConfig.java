package com.nhnghia.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {
	
	@Value("${app.email.address}")
	private String emailAddress;
	
	@Value("${app.email.password}")
	private String emailPassword;

	@Bean
	public JavaMailSender getJavaMailSender() {

		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setDefaultEncoding("UTF-8");
//		mailSender.setHost("smtp.gmail.com");
//		mailSender.setPort(587);
		mailSender.setHost("smtp.mailtrap.io");
		mailSender.setPort(25);
		mailSender.setUsername(this.emailAddress);
		mailSender.setPassword(this.emailPassword);

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");

		return mailSender;
	}

}
