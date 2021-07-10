package com.synectiks.security.email;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

	private JavaMailSender javaMailSender;
	
	@Autowired
	public MailService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public void sendEmail(SimpleMailMessage mail) throws MailException {
		javaMailSender.send(mail);
	}

	public void sendEmail(MimeMessage mail) throws MailException {
		javaMailSender.send(mail);
	}
	
	public JavaMailSender getJavaMailSender() {
		return this.javaMailSender;
	}
}
