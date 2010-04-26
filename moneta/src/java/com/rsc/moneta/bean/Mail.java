/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.bean;

import java.util.Locale;
import java.util.logging.Level;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import org.apache.struts.util.MessageResources;

/**
 *
 * @author sulic
 */
public class Mail extends Thread {

    private String recipient;
    private String message;
    private final Properties properties;
    private final String subject;
    private String contentType = null;


    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public Mail(String recepient, String subject, String message) {
        this.recipient = recepient;
        this.message = message;
        this.subject = subject;
        properties = new Properties();
        ResourceBundle bundle = ResourceBundle.getBundle("mail");
        Enumeration e = bundle.getKeys();
        while (e.hasMoreElements()) {
            String name = e.nextElement().toString();
            String value = bundle.getString(name);
            properties.put(name, value);
            Logger.getLogger(Mail.class.getName()).severe(name + "=" + value);
        }

    }

    @Override
    public void run() {
        try {
            this.send();
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(Mail.class.getName()).log(Level.SEVERE, ex.getMessage());
        } catch (MessagingException ex) {
            Logger.getLogger(Mail.class.getName()).log(Level.SEVERE, ex.getMessage());
        }
    }

    private void send() throws NoSuchProviderException, MessagingException {
        Session mailSession = Session.getDefaultInstance(properties, null);
        mailSession.setDebug(Boolean.valueOf(properties.getProperty("debug")).booleanValue());
        Transport transport = mailSession.getTransport();
        MimeMessage mimeMessage = new MimeMessage(mailSession);
        mimeMessage.setFrom(new InternetAddress(properties.getProperty("from")));
        mimeMessage.setSubject(this.subject, properties.getProperty("encoding"));// windows-1251
        if (contentType == null) {
            if (properties.getProperty("mime_content_type") != null) {
                mimeMessage.setContent(this.message, properties.getProperty("mime_content_type"));
            }
        } else {
            mimeMessage.setContent(this.message, contentType);
        }
        InternetAddress addrTo = new InternetAddress(recipient);
        mimeMessage.addRecipient(Message.RecipientType.TO, addrTo);
        mimeMessage.setHeader("X-Mailer", properties.getProperty("mailer"));
        mimeMessage.setSentDate(new Date());
        mimeMessage.setHeader("Content-Transfer-Encoding", properties.getProperty("content_transfer_encoding"));
        try {
            transport.connect();
            Transport.send(mimeMessage);
        } catch (Exception exception) {
            Logger.getLogger(Mail.class.getName()).severe("Transport connect error: " + exception + "\n" + exception.getMessage());
        } finally {
            transport.close();
        }
    }
}
