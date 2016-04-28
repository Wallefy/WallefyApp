package com.example.dpene.wallefy.mail;

import android.util.Log;

import java.util.Date;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Mail extends Authenticator {

    private String      _body;
    private String      _host;
    private String      _port;
    private String      _pass;
    private String      _sport;
    private String      _subject;
    private String      _systemMail;
    private String      _generatedPass;
    private String[]    _to;
    private boolean     _auth;
    private boolean     _debuggable;
    private Multipart   _multipart;

    public Mail() {

        _systemMail      = "";                            // set email
        _pass            = "";                               // set password
        _host            = "smtp.abv.bg";                               // default smtp server
        _port            = "465";                                       // default smtp port
        _sport           = "465";                                       // default socketfactory port
        _subject         = "Wallefy Password recovery";                 // email subject
        _body            = "Your new password is : ";                   // email body
        _debuggable      = false;                                       // debug mode on or off - default off
        _auth            = true;                                        // smtp authentication - default on
        _multipart       = new MimeMultipart();

// There is something wrong with MailCap, javamail can not find a handler for the multipart
// /mixed part, so this bit needs to be added.
        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(mc);
    }

    public boolean send() throws MessagingException {

        Properties props = _setProperties();

        if(     !_systemMail.equals("") &&
                !_body.equals("") &&
                !_pass.equals("") &&
                !_subject.equals("") &&
                _to.length > 0
                                ) {

//            Authenticator       authentication    = new SMTPAuthenticatorCOPY(_systemMail,_pass);
            Authenticator       authentication    = new SMTPAuthenticator();
            Session             session           = Session.getInstance(props, authentication);
            Message             msg               = new MimeMessage(Session.getDefaultInstance(props, authentication));
            InternetAddress[]   addressTo         = new InternetAddress[_to.length];

            msg.setFrom(new InternetAddress(_systemMail));

            for (int i = 0; i < _to.length; i++) {
                addressTo[i] = new InternetAddress(_to[i]);
            }

            msg.setRecipients(MimeMessage.RecipientType.TO, addressTo);
            msg.setSubject(_subject);
            msg.setSentDate(new Date());


// setup message body
            BodyPart messageBodyPart    = new MimeBodyPart();
            messageBodyPart.setText(_body + _generatedPass);
            _multipart.addBodyPart(messageBodyPart);

// Put parts in message
            msg.setContent(_multipart);

// send email
            String      protocol           = "smtp";
            Transport   transport          = session.getTransport(protocol);

            props.put("mail." + protocol + ".auth", "true");
            try {
                transport.connect(_host, _systemMail,_pass);
                transport.sendMessage(msg, msg.getAllRecipients());
            } finally {
                transport.close();
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(_systemMail, _pass);
    }

    private Properties _setProperties() {

        Properties props = new Properties();

        props.put("mail.smtp.host", _host);

        if(_debuggable) {
            props.put("mail.debug", "true");
        }

        if(_auth) {
            props.put("mail.smtp.auth", "true");
        }

        props.put("mail.smtp.port", _port);
        props.put("mail.smtp.socketFactory.port", _sport);
        props.put("mail.smtp.host", _host);
        props.put("mail.smtp.ssl.enable",true);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.starttls.enable", "true");

        return props;
    }

    public void setTo(String[] to) {
        this._to = to;
    }

//    Not to use the abstract class Authenticator
    private class SMTPAuthenticator extends Authenticator {}

    protected void setGeneratedPass(String generatedPass){
        this._generatedPass = generatedPass;
    }

}
