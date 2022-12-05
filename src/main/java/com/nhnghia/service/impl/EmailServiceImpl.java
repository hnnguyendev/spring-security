package com.nhnghia.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.nhnghia.service.EmailService;
import com.nhnghia.util.TokenUtil;

@Service
@EnableScheduling
public class EmailServiceImpl implements EmailService {

	private final Logger LOGGER = LogManager.getLogger(EmailServiceImpl.class);

	@Value("${security.jwt.secret-key}")
	private String secretKey;

	@Value("${app.url.frontend}")
	private String url;
	
	@Value("${app.email.address}")
	private String emailAddress;

	@Autowired
	private JavaMailSender javaMailSender;

	@Override
	@Scheduled(cron = "0 59 15 * * ?")
	public void sendNotificationEmail() {
		
		List<String> emailList = new ArrayList<>();
		emailList.add("nghianguyenhuu848@gmail.com");
//		emailList.add("nh.nghia@outlook.com");
		
		String[] emailArr  = emailList.toArray(new String[0]);
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(emailArr);
		message.setSubject("Notification email");
		message.setText("Test notification email");
		this.javaMailSender.send(message);

	}

	@Override
	@Async
	public void sendVerifyEmail(String email) {

		LOGGER.info("Begin Service SendMailServiceImpl Function sendVerifyEmail");
		LOGGER.info("Service SendMailServiceImpl Function sendVerifyEmail PARAMS email: " + email);
		try {
			File file = new File(getClass().getClassLoader().getResource("emailVerify.html").getFile());
			String fileStr = file.toString();
			String fileDecode = URLDecoder.decode(fileStr, "UTF-8");
			File fileFinal = new File(fileDecode);

			BufferedReader in = new BufferedReader(new FileReader(fileFinal));
			String str;
			StringBuilder contentBuilder = new StringBuilder();
			while ((str = in.readLine()) != null) {
				contentBuilder.append(str);
			}
			in.close();

			MimeMessage message = javaMailSender.createMimeMessage();
			// true = multipart message
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
			messageHelper.setFrom(this.emailAddress, "My App");
			messageHelper.setTo(email);
			messageHelper.setSubject("Complete Registration!");
			
			long createDate = System.currentTimeMillis();

			String token = "createDate=" + createDate + "&email=" + email;
			String emailContent = contentBuilder.toString();
			token = TokenUtil.encrypt(token, secretKey);
			
			String fullEmailContent = emailContent.replace("verifyLink", this.url + "verify?token=" + token);
			messageHelper.setText(fullEmailContent, true);
			
			javaMailSender.send(message);

		} catch (Exception e) {
			LOGGER.error("Service SendMailServiceImpl Function sendVerifyEmail ERROR MESSAGE: "
					+ ExceptionUtils.getStackTrace(e));
		}

		LOGGER.info("End Service SendMailServiceImpl Function sendVerifyEmail");

	}

	@Override
	@Async
	public void sendResetPasswordEmail(String email) {

		LOGGER.info("Begin Service SendMailServiceImpl Function sendResetPasswordEmail");
		LOGGER.info("Service SendMailServiceImpl Function sendResetPasswordEmail PARAMS email: " + email);
		try {
			File file = new File(getClass().getClassLoader().getResource("resetPassword.html").getFile());
			String fileStr = file.toString();
			String fileDecode = URLDecoder.decode(fileStr, "UTF-8");
			File fileFinal = new File(fileDecode);

			BufferedReader in = new BufferedReader(new FileReader(fileFinal));
			String str;
			StringBuilder contentBuilder = new StringBuilder();
			while ((str = in.readLine()) != null) {
				contentBuilder.append(str);
			}
			in.close();

			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
			messageHelper.setFrom(this.emailAddress, "My App");
			messageHelper.setTo(email);
			messageHelper.setSubject("Reset your account!");
			
			long createDate = System.currentTimeMillis();

			String token = "createDate=" + createDate + "&email=" + email + "&code=" + UUID.randomUUID().toString();
			String emailContent = contentBuilder.toString();
			token = TokenUtil.encrypt(token, secretKey);
			
			String fullEmailContent = emailContent.replace("resetPasswordLink", this.url + "resetpassword?token=" + token);
			messageHelper.setText(fullEmailContent, true);

			javaMailSender.send(message);

		} catch (Exception e) {
			LOGGER.error("Service SendMailServiceImpl Function sendResetPasswordEmail ERROR MESSAGE: "
					+ ExceptionUtils.getStackTrace(e));
		}

		LOGGER.info("End Service SendMailServiceImpl Function sendResetPasswordEmail");

	}

}
