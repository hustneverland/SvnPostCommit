package mail;

import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {

	// Recipient's email ID needs to be mentioned.
	private static final String[] to = PropUtil.getString("send_to").split(",");

	// Sender's email ID needs to be mentioned
	private static final String from = PropUtil.getString("send_from");

	// Assuming you are sending email from localhost
	private static final String host = PropUtil.getString("send_host");

	// Get system properties
	private static final Properties properties = System.getProperties();

	// Get the default Session object.
	private static final Session session = Session.getDefaultInstance(
			properties, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(from, PropUtil.getString("mail_password"));
				}
			});

	// Properties init
	private static Boolean isProInit = false;
	
	/*
	 * thread timeout controll
	 */
	private static final ExecutorService executorService = Executors.newCachedThreadPool();
	private static int job_timeout_second = PropUtil.getInt("job_timeout_second");

	public static boolean submitJob(Callable<String> task) {
		Future<String> future = executorService.submit(task);
		boolean flag = true;
		try {
			future.get(job_timeout_second, TimeUnit.SECONDS);
		} catch (Exception e) {
			future.cancel(true);
			flag = false;
		}
		return flag;
	}

	public static void sendMail(final String subject, final String text, String sendTo) {

		if (!isProInit) {
			// Setup mail server
			properties.put("mail.smtp.auth", "true");
			properties.setProperty("mail.smtp.host", host);
			isProInit = true;
		}
		
		if(sendTo == null) {
			for(String toWho : to) {
				Callable<String> task = new Callable<String>() {
					@Override
					public String call() throws Exception {
						try {
							// Create a default MimeMessage object.
							MimeMessage message = new MimeMessage(session);

							// Set From: header field of the header.
							message.setFrom(new InternetAddress(from));

							// Set To: header field of the header.
							message.addRecipient(Message.RecipientType.TO,
									new InternetAddress(toWho));

							// Set Subject: header field
							message.setSubject(subject);
							
							// UTF-8
							message.setContent(text, "text/plain;charset=utf-8");

							/*// Now set the actual message
							message.setText(text);*/

							// Send message
							Transport.send(message);
						} catch (MessagingException mex) {
							mex.printStackTrace();
						}
						return "done";
					}
				};
				submitJob(task);
			}
		} else {
			Callable<String> task = new Callable<String>() {
				@Override
				public String call() throws Exception {
					try {
						// Create a default MimeMessage object.
						MimeMessage message = new MimeMessage(session);

						// Set From: header field of the header.
						message.setFrom(new InternetAddress(from));

						// Set To: header field of the header.
						message.addRecipient(Message.RecipientType.TO,
								new InternetAddress(sendTo));

						// Set Subject: header field
						message.setSubject(subject);
						
						// UTF-8
						message.setContent(text, "text/plain;charset=utf-8");

						/*// Now set the actual message
						message.setText(text);*/

						// Send message
						Transport.send(message);
					} catch (MessagingException mex) {
						mex.printStackTrace();
					}
					return "done";
				}
			};
			submitJob(task);
		}
	}

}
