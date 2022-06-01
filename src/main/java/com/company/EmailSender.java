package com.company;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class EmailSender {
    private String to;
    private String host = "smtp.gmail.com";
    private String port = "587";
    private String subject;
    private String content;

    public EmailSender(String to, String subject, String content) {
        this.to = to;
        this.subject = subject;
        this.content = content;
    }

    public void sendEmail() {
        Properties properties = System.getProperties();

        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("", "");
                    }
                });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(""));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(content);
            message.setSentDate(new Date());
            System.out.println("sending...");
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch(MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
