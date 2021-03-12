//package com.example.schedulemanager.email;
//import android.util.Log;
//
//import com.sun.mail.smtp.SMTPTransport;
//import com.sun.mail.util.BASE64EncoderStream;
//
//import java.util.Properties;
//
//import javax.activation.DataHandler;
//import javax.mail.Message;
//import javax.mail.Session;
//import javax.mail.URLName;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//import javax.mail.util.ByteArrayDataSource;
//
//public class SendmailTest {
//    private Session session;
//
//
//    public SMTPTransport connectToSmtp(String host, int port, String userEmail,
//                                       String oauthToken, boolean debug) throws Exception {
//        Log.v("ranjapp", "came to connecttosmtp");
//
//        Properties props = new Properties();
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.starttls.required", "true");
//        props.put("mail.smtp.sasl.enable", "false");
//        //props.put("mail.imaps.sasl.mechanisms.oauth2.oauthToken", oauthToken);
//        session = Session.getInstance(props);
//        session.setDebug(debug);
//
//        final URLName unusedUrlName = null;
//        SMTPTransport transport = new SMTPTransport(session, unusedUrlName);
//        // If the password is non-null, SMTP tries to do AUTH LOGIN.
//        final String emptyPassword = null;
//        transport.connect(host, port, userEmail, emptyPassword);
//        Log.v("ranjapp", "came before gen response");
//        byte[] response = String.format("user=%s\1auth=Bearer %s\1\1", userEmail, oauthToken).getBytes();
//        response = BASE64EncoderStream.encode(response);
//
//        Log.v("ranjapp", "came to call issuecommand " + transport.isConnected());
//        Log.v("ranjapp", new String(response));
//
//        transport.issueCommand("AUTH XOAUTH2 " + new String(response), 235);
//
//        Log.v("ranjapp", "came after issue command");
//        return transport;
//    }
//
//
//    public synchronized void sendMail(String subject, String body, String user,
//                                      String oauthToken, String recipients) {
//        try {
//            Log.e("SMTP", subject);
//            Log.e("SMTP", body);
//            Log.e("SMTP", user);
//            Log.e("SMTP", oauthToken);
//            Log.e("SMTP", recipients);
//            SMTPTransport smtpTransport = connectToSmtp("smtp.gmail.com", 587, user, oauthToken, true);
//
//            MimeMessage message = new MimeMessage(session);
//            DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
//            message.setSender(new InternetAddress(user));
//            message.setSubject(subject);
//            message.setDataHandler(handler);
//
//            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
//            Log.e("SMTP", "sending");
//            smtpTransport.sendMessage(message, message.getAllRecipients());
//            Log.e("SMTP", "sent");
//
//        } catch (Exception e) {
//            Log.v("ranjith", e.toString());
//        }
//
//    }
//}