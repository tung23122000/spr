package dts.com.vn.service;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class SendFileEmail {

  public static void main(String[] args) {

    // Recipient's email ID needs to be mentioned.
    String to = "tinhbdt@dts.com.vn";

    // Sender's email ID needs to be mentioned
    // String from = "linhnd@dts.com.vn";
    String from = "linhnd@dts.com.vn";

    String host = "mail.dts.com.vn";
    String port = "587";

    // Get system properties
    Properties properties = System.getProperties();

    // Setup mail server
    properties.setProperty("mail.smtp.host", host);
    properties.setProperty("mail.smtp.port", port);
    properties.setProperty("mail.smtp.auth", "true");

    try {

      SmtpAuthenticator authentication = new SmtpAuthenticator ();
      MimeMessage message = new MimeMessage(Session.getDefaultInstance(properties, authentication));

      // Set From: header field of the header.
      message.setFrom(new InternetAddress(from));

      // Set To: header field of the header.
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

      // Set Subject: header field
      message.setSubject("------------------------Report FO------------------------");

      // Create the message part
      BodyPart messageBodyPart = new MimeBodyPart();

      // Fill the message
      StringBuffer sbBodyText = new StringBuffer();
      sbBodyText.append("Many thanks!\n");
      sbBodyText.append("--\n");
      sbBodyText.append("Best regards, good luck and have fun!");
      messageBodyPart.setText(sbBodyText.toString());

      // Create a multipar message
      Multipart multipart = new MimeMultipart();

      // Set text message part
      multipart.addBodyPart(messageBodyPart);

      // Part two is attachment
      messageBodyPart = new MimeBodyPart();
      String filename = "D:/today/today21042022.txt";
      DataSource source = new FileDataSource(filename);
      messageBodyPart.setDataHandler(new DataHandler(source));
      messageBodyPart.setFileName(filename);
      multipart.addBodyPart(messageBodyPart);

      // Send the complete message parts
      message.setContent(multipart);

      // Send message
      Transport.send(message);
      System.out.println("Sent message successfully....");
    } catch (MessagingException mex) {
      mex.printStackTrace();
    }
  }
}
