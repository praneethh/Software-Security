package boss.utilities;

import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class SendEmail
{
   public static void main(String [] args)
   {
      

   }
   
   public String sendSignUpMail(String toEmailId,long otp,String fileName, String keyFile){
	      // Recipient's email ID needs to be mentioned.
	      String to = toEmailId;
			final String username = "boss.sbs0210@gmail.com";
			final String password = "boss0210";
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.port", "587");

			Session session = Session.getInstance(props,
					new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			}
   );
	      // Sender's email ID needs to be mentioned
	    //  String from = "boss@gmail.com";
String from = "boss.sbs0210@gmail.com";
	      // Assuming you are sending email from localhost
	  //    String host = "localhost";

	      // Get system properties
	//      Properties properties = System.getProperties();

	      // Setup mail server
	    //  properties.setProperty("mail.smtp.host", host);

	      // Get the default Session object.
	      //Session session = Session.getDefaultInstance(properties);

	      try{
	         // Create a default MimeMessage object.
	         MimeMessage message = new MimeMessage(session);

	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress(from));

	         // Set To: header field of the header.
	         message.addRecipient(Message.RecipientType.TO,
	                                  new InternetAddress(to));

	         // Set Subject: header field
	         message.setSubject("Welcome to BOSS");

	         // Create the message part 
	         BodyPart messageBodyPart = new MimeBodyPart();

	         // Fill the message
	        // long otp = UUID.randomUUID().getMostSignificantBits();
	         messageBodyPart.setText( "Your One time password for login is below. Please reset your password after you login."
	        +"Your One time Password: "+otp+" Go to https://boss-rs.vlab.asu.edu/boss-rs/resetPassword"
	       +" Attached are your key and certificate files which will be used for authentication during transfer transactions "
	        + " Please download the certificate file using secure download manager (.sdx is mandatory)."
	         +" Remove your appended username from the files while downloading. (certificate.sdx and DSAPrivateKey.key) "
	         +" Place them together with the JAR file we have provided and execute the JAR file(just double click) to generate your encryptedKeyFile"
	         +" You would have to upload this encrypted key file while doing tranfer transactions." 
	         +" Thank You "
	         +" BoSS Team ");
	         // Create a multipar message
	         Multipart multipart = new MimeMultipart();

	         // Set text message part
	         multipart.addBodyPart(messageBodyPart);

	         // Part two is attachment
	         messageBodyPart = new MimeBodyPart();
	         //String filename = "file.txt";
	         DataSource source = new FileDataSource(fileName);
	         messageBodyPart.setDataHandler(new DataHandler(source));
	         messageBodyPart.setFileName(source.getName());
	         multipart.addBodyPart(messageBodyPart);

	         BodyPart messageBodyPart2 = new MimeBodyPart();
	         //String filename = "file.txt";
	         DataSource source2 = new FileDataSource(keyFile);
	         messageBodyPart2.setDataHandler(new DataHandler(source2));
	         messageBodyPart2.setFileName(source2.getName());
	         multipart.addBodyPart(messageBodyPart2);
	         
	         // Send the complete message parts
	         message.setContent(multipart);

	         // Send message
	         Transport.send(message);
	         System.out.println("Sent message successfully....");
	         return "sent";
	      }catch (MessagingException mex) {
	         mex.printStackTrace();
	         return "not sent";
	      }   
   }
   
   
   public String sendForgotPasswordMail(String toEmailId,long otp){
	      // Recipient's email ID needs to be mentioned.
	      String to = toEmailId;
			final String username = "boss.sbs0210@gmail.com";
			final String password = "boss0210";
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.port", "587");

			Session session = Session.getInstance(props,
					new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			}
);
	      // Sender's email ID needs to be mentioned
	    //  String from = "boss@gmail.com";
String from = "boss.sbs0210@gmail.com";
	      // Assuming you are sending email from localhost
	  //    String host = "localhost";

	      // Get system properties
	//      Properties properties = System.getProperties();

	      // Setup mail server
	    //  properties.setProperty("mail.smtp.host", host);

	      // Get the default Session object.
	      //Session session = Session.getDefaultInstance(properties);

	      try{
	         // Create a default MimeMessage object.
	         MimeMessage message = new MimeMessage(session);

	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress(from));

	         // Set To: header field of the header.
	         message.addRecipient(Message.RecipientType.TO,
	                                  new InternetAddress(to));

	         // Set Subject: header field
	         message.setSubject("Forgot Password");

	         // Create the message part 
	         BodyPart messageBodyPart = new MimeBodyPart();

	         // Fill the message
	        // long otp = UUID.randomUUID().getMostSignificantBits();
	         messageBodyPart.setText("Your One time password is given below. Please reset your password to login."
	         +" Your One time Password: "+otp+" Go to https://boss-rs.vlab.asu.edu/boss-rs/resetPassword");
	         // Create a multipar message
	         Multipart multipart = new MimeMultipart();

	         // Set text message part
	         multipart.addBodyPart(messageBodyPart);

	         // Part two is attachment
//	         messageBodyPart = new MimeBodyPart();
//	         String filename = "file.txt";
//	         DataSource source = new FileDataSource(filename);
//	         messageBodyPart.setDataHandler(new DataHandler(source));
//	         messageBodyPart.setFileName(filename);
//	         multipart.addBodyPart(messageBodyPart);

	         // Send the complete message parts
	         message.setContent(multipart );

	         // Send message
	         Transport.send(message);
	         System.out.println("Sent message successfully....");
	         return "sent";
	      }catch (MessagingException mex) {
	         mex.printStackTrace();
	         return "not sent";
	      }   
}

public String sendResetPasswordMail(String eMailId, long otp) {
	  String to = eMailId;
		final String username = "boss.sbs0210@gmail.com";
		final String password = "boss0210";
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		}
);
    // Sender's email ID needs to be mentioned
  //  String from = "boss@gmail.com";
String from = "boss.sbs0210@gmail.com";
    // Assuming you are sending email from localhost
//    String host = "localhost";

    // Get system properties
//      Properties properties = System.getProperties();

    // Setup mail server
  //  properties.setProperty("mail.smtp.host", host);

    // Get the default Session object.
    //Session session = Session.getDefaultInstance(properties);

    try{
       // Create a default MimeMessage object.
       MimeMessage message = new MimeMessage(session);

       // Set From: header field of the header.
       message.setFrom(new InternetAddress(from));

       // Set To: header field of the header.
       message.addRecipient(Message.RecipientType.TO,
                                new InternetAddress(to));

       // Set Subject: header field
       message.setSubject("Reset Password");

       // Create the message part 
       BodyPart messageBodyPart = new MimeBodyPart();

       // Fill the message
      // long otp = UUID.randomUUID().getMostSignificantBits();
       messageBodyPart.setText("Your One time password is given below. Please reset your password to login."
     +"Your One time Password: "+otp+" Go To https://boss-rs.vlab.asu.edu/boss-rs/resetPassword");
       // Create a multipar message
       Multipart multipart = new MimeMultipart();

       // Set text message part
       multipart.addBodyPart(messageBodyPart);

       // Part two is attachment
//       messageBodyPart = new MimeBodyPart();
//       String filename = "file.txt";
//       DataSource source = new FileDataSource(filename);
//       messageBodyPart.setDataHandler(new DataHandler(source));
//       messageBodyPart.setFileName(filename);
//       multipart.addBodyPart(messageBodyPart);

       // Send the complete message parts
       message.setContent(multipart );

       // Send message
       Transport.send(message);
       System.out.println("Sent message successfully....");
       return "sent";
    }catch (MessagingException mex) {
       mex.printStackTrace();
       return "not sent";
    } 
}


   
   
}
