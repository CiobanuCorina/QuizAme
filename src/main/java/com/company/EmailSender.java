package com.company;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public class EmailSender {
    private String to;
    private String from;
    private String senderPassword;
    private String host;
    private String port;
    private String subject;
    private String content;

    public EmailSender(String to, String subject, String content) throws IOException {
        Properties props = new Properties();
        props.load(ClassLoader.getSystemResourceAsStream("config/email.properties"));
        this.host = props.getProperty("host");
        this.port = props.getProperty("port");
        this.from = props.getProperty("sender");
        this.senderPassword = props.getProperty("password");
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
                        return new PasswordAuthentication(EmailSender.this.from, EmailSender.this.senderPassword);
                    }
                });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(this.from));
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
