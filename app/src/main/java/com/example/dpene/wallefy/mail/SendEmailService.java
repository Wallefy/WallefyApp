package com.example.dpene.wallefy.mail;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.dpene.wallefy.model.dao.IUserDao;
import com.example.dpene.wallefy.model.datasources.UserDataSource;
import com.example.dpene.wallefy.model.utils.RegisterHelper;

import javax.mail.MessagingException;

public class SendEmailService extends IntentService {

    public static final String  USER_EMAIL   = "user_email";
    public static final String  MAIL_SENT    = "mail_sent";

    public SendEmailService() {
        super("SendEmailService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            String      userMail    = intent.getStringExtra(USER_EMAIL);
            Mail        mail        = new Mail();
            String[]    toArr       = {userMail};

            mail.setTo(toArr);

            NotificationManager             manager     = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder      builder     = new NotificationCompat.Builder(getBaseContext());
            builder.setSmallIcon(android.R.drawable.ic_dialog_email);
            try {
                IUserDao userDataSource = UserDataSource.getInstance(getApplicationContext());
                ((UserDataSource) userDataSource).open();
                //            TODO GENERATE random pass
                String randomPass = generateRandomPassword();
                mail.setGeneratedPass(randomPass);
                if (userDataSource.changeForgottenPassword(userMail, RegisterHelper.md5(randomPass)) && mail.send()) {
                    builder.setContentTitle("Wallefy sent you an email.");
                    builder.setStyle(new NotificationCompat.BigTextStyle().bigText("Check your email(" + userMail + "). " +
                            "In the email you will find information about your request"));
//                    TODO SendBroadcast
//                    TODO add buttons to ask to open mail client or to return to wallefy
                } else {
                    builder.setContentTitle("Wallefy could not send you an email.");
                    builder.setContentText("There was a problem sending request for new password");
                }
            } catch (MessagingException e) {
                builder.setContentTitle("Wallefy request not sent.");
                builder.setStyle(new NotificationCompat.BigTextStyle().bigText("There was a problem sending request for new password"));
            }
            Notification notification = builder.build();
//            The integer is a notification id
            manager.notify(1,notification);
        }
    }

    private String generateRandomPassword(){
        return String.valueOf(Long.toHexString(Double.doubleToLongBits(Math.random())));
    }
}
