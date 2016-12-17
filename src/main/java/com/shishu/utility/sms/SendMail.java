package com.shishu.utility.sms;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendMail {

	private String from;
	private String to;
	private String smtpServer;
	private String subject = "账号激活邮件";
	private String content;
	private String username;
	private String password;
	private String attFile;

	public void mail(String smtpServer, String from, String to, String username, String password, String subject,
			String content) {
		this.smtpServer = smtpServer;
		this.from = from;
		this.to = to;
		this.username = username;
		this.password = password;
		this.subject = subject;
		this.content = content;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSmtpServer() {
		return smtpServer;
	}

	public void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAttFile() {
		return attFile;
	}

	public void setAttFile(String attFile) {
		this.attFile = attFile;
	}

	public void send() {
		Properties props = System.getProperties();
		props.put("mail.smtp.host", smtpServer);
		props.put("mail.smtp.auth", "true");
		/*
		 * Session: 邮件会话 使用Session类提供的getDefaultInstance()静态工厂方法获得一个默认的Session对象
		 */
		Session session = Session.getDefaultInstance(props, new PasswordAuthenticator(username, password));
		// 信息对象
		Message msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress(from));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
			msg.setSubject(subject);
			// msg.setText(content);
			// 发送邮件的时间
			msg.setSentDate(new Date());

			// 添加内容
			MimeBodyPart contentPart = new MimeBodyPart();
			contentPart.setContent(content, "text/html;charset=utf-8");
			MimeMultipart contentMultipart = new MimeMultipart();
			contentMultipart.addBodyPart(contentPart);
			// 添加附件
			if (attFile != null && new File(attFile).exists()) {
				MimeBodyPart attachPart = new MimeBodyPart();
				FileDataSource fds = new FileDataSource(attFile);
				attachPart.setDataHandler(new DataHandler(fds));
				attachPart.setFileName(fds.getName());
				contentMultipart.addBodyPart(attachPart);
			}

			msg.setContent(contentMultipart);

			// 邮件的发送是由Transport来完成的
			Transport.send(msg);
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 密码验证类 PasswordAuthenticator
	 * 
	 * @param username:邮件地址
	 * 
	 * @param password:密码
	 */
	class PasswordAuthenticator extends Authenticator {
		private String username;
		private String password;

		public PasswordAuthenticator(String username, String password) {
			this.username = username;
			this.password = password;
		}// 构造方法

		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(username, password);
		}
	}

	public static void send(String toEmail, String content) {
		SendMail sendMail = new SendMail();
		// 设置SMTP服务器地址
		sendMail.setSmtpServer("smtp.jsdatum.com");
		// 发送方的邮件地址
		sendMail.setFrom("postmaster@jsdatum.com");
		// 接收方的邮件地址
		sendMail.setTo(toEmail);
		// 发送方的邮件名称
		sendMail.setUsername("postmaster@jsdatum.com");
		// 发送方的邮箱密码(根据情况自己设定)
		sendMail.setPassword("xjz123678Js");
		// 邮件的主题
		sendMail.setSubject("账号激活邮件");
		// 邮件的内容
		sendMail.setContent(content);

		sendMail.send();
	}

	public static void sendComment(String toEmail, String content) {
		SendMail sendMail = new SendMail();
		// 设置SMTP服务器地址
		sendMail.setSmtpServer("smtp.jsdatum.com");
		// 发送方的邮件地址
		sendMail.setFrom("postmaster@jsdatum.com");
		// 接收方的邮件地址
		sendMail.setTo(toEmail);
		// 发送方的邮件名称
		sendMail.setUsername("postmaster@jsdatum.com");
		// 发送方的邮箱密码(根据情况自己设定)
		sendMail.setPassword("xjz123678Js");
		// 邮件的主题
		sendMail.setSubject("来自掘数科技的新闻评论邮件");
		// 邮件的内容
		sendMail.setContent(content);

		sendMail.send();
	}

	//发送alert定制信息的邮件
	public void sendAlert(String toEmail, String content, String subject) {
		if(subject.isEmpty()){
			subject = "来自掘数科技定制信息邮件";
		}
		SendMail sendMail = new SendMail();
		// 设置SMTP服务器地址
		sendMail.setSmtpServer("smtp.jsdatum.com");
		// 发送方的邮件地址
		sendMail.setFrom("postmaster@jsdatum.com");
		// 接收方的邮件地址
		sendMail.setTo(toEmail);
		// 发送方的邮件名称
		sendMail.setUsername("postmaster@jsdatum.com");
		// 发送方的邮箱密码(根据情况自己设定)
		sendMail.setPassword("xjz123678Js");
		// 邮件的主题
		sendMail.setSubject(subject);
		// 邮件的内容
		sendMail.setContent(content);

		sendMail.send();
	}

	public static void sendpwd(String toEmail, String content) {
		SendMail sendMail = new SendMail();
		// 设置SMTP服务器地址
		sendMail.setSmtpServer("smtp.jsdatum.com");
		// 发送方的邮件地址
		sendMail.setFrom("postmaster@jsdatum.com");
		// 接收方的邮件地址
		sendMail.setTo(toEmail);
		// 发送方的邮件名称
		sendMail.setUsername("postmaster@jsdatum.com");
		// 发送方的邮箱密码(根据情况自己设定)
		sendMail.setPassword("xjz123678Js");
		// 邮件的主题
		sendMail.setSubject("重置密码邮件");
		// 邮件的内容
		sendMail.setContent(content);

		sendMail.send();
	}

	public static void main(String[] argv) {
		send("tony_xjz@126.com", "vvvvvvvv测试用JavaMail发送电子邮件!");

		System.out.println("------------------------");

		send("tony_xjz@126.com", "测试用JavaMail发送电子邮件!");
	}

}
