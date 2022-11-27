package com.example.mealer_project.utils;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.util.Log;

public class Gmail {

    // Variable Declaration: constants
    final String emailPort = "587";// gmail's smtp port
    final String smtpAuth = "true"; //simple mail transfer protocol!!
    final String starttls = "true"; //transport layer security
    final String emailHost = "smtp.gmail.com";

    // Variable Declaration: email Strings
    /**
     * email address from which the email will be sent
     */
    String fromEmail;

    /**
     * the app password for the sender email address
     */
    String fromAppPassword;

    /**
     * email address to which the email will be sent
     */
    String toEmail;

    /**
     * the subject line of the email
     */
    String emailSubject;

    /**
     * the contents of the email
     */
    String emailBody;

    Properties emailProperties;
    Session mailSession;
    MimeMessage emailMessage;

    /**
     * empty constructor
     */
    public Gmail() {}

    /**
     * constructor method
     * @param fromEmail the sender email address
     * @param fromAppPassword the app password for the send email address
     * @param toEmail the receiver email address
     * @param emailSubject the subject line of the email
     * @param emailBody the contents of the email
     */
    public Gmail(String fromEmail, String fromAppPassword, String toEmail, String emailSubject,
                 String emailBody) {

        // Initialization
        this.fromEmail = fromEmail;
        this.fromAppPassword = fromAppPassword;
        this.toEmail = toEmail;
        this.emailSubject = emailSubject;
        this.emailBody = emailBody;

        this.emailProperties = System.getProperties();

        // Process: setting up the properties of the mail server
        this.emailProperties.put("mail.smtp.port", emailPort);
        this.emailProperties.put("mail.smtp.auth", smtpAuth);
        this.emailProperties.put("mail.smtp.starttls.enable", starttls);

        // LOG MESSAGE
        Log.i("Gmail", "Mail server properties have been set!");
    }

    /**
     * this method creates the email message
     * @return the emailMessage
     * @throws AddressException
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public MimeMessage createEmailMessage() throws AddressException, MessagingException,
            UnsupportedEncodingException {

        // Initialization: creating new mail session
        this.mailSession = Session.getDefaultInstance(emailProperties, null);
        this.emailMessage = new MimeMessage(mailSession); //new msg

        // Process: setting contents of email
        emailMessage.setFrom(new InternetAddress(fromEmail, fromEmail)); //sender
        // LOG MESSAGE
        Log.d("Gmail","fromEmail: " + fromEmail);

        emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail)); //receiver
        // LOG MESSAGE
        Log.d("Gmail","toEmail: " + toEmail);

        emailMessage.setSubject(emailSubject); //subject line
        // LOG MESSAGE
        Log.d("Gmail","emailSubject: " + emailSubject);

        emailMessage.setContent(emailBody, "text/html"); //for HTML email
        // emailMessage.setText(emailBody); //for text-only email
        // LOG MESSAGE
        Log.d("Gmail","emailBody: " + emailBody);

        Log.i("Gmail", "Email message successfully created!");

        // Output
        return emailMessage;

    }

    /**
     * this method attempts to send the email
     * @throws AddressException
     * @throws MessagingException
     */
    public void sendEmail() throws AddressException, MessagingException {

        // Variable Declaration
        Transport transport = mailSession.getTransport("smtp"); //getting transport for current session

        // LOG MESSAGES - check these if email cannot be sent (error)
        Log.d("Gmail", "emailHost: " + emailHost);

        Log.d("Gmail", "fromEmail: " + fromEmail);
        Log.d("Gmail", "sender:" + emailMessage.getFrom());

        Log.d("Gmail", "toEmail: " + toEmail);
        Log.d("Gmail", "recipient:" + emailMessage.getAllRecipients());

        Log.d("Gmail", "fromAppPassword: " + fromAppPassword);

        // Process: connecting to email
        transport.connect(emailHost, fromEmail, fromAppPassword);

        // Process: sending email
        transport.sendMessage(emailMessage, emailMessage.getAllRecipients());

        transport.close(); //closing transport

        // LOG MESSAGE
        Log.i("Gmail", "Email has been sent successfully!");

    }

}