package com.example.dpene.wallefy.controller.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.controller.fragments.interfaces.ISendMail;
import com.example.dpene.wallefy.mail.SendEmailService;
import com.example.dpene.wallefy.model.dao.IUserDao;
import com.example.dpene.wallefy.model.datasources.UserDataSource;
import com.example.dpene.wallefy.model.utils.RegisterHelper;

public class PassRecoveryDialog extends DialogFragment{

    private EditText recMail;
    private Button sendEmail;
    private Button cancelREcovery;
    private String email;
    private ISendMail loginActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        loginActivity = (ISendMail) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_pass_recovery,null);
        getDialog().setTitle("Send us your email");

        recMail = (EditText) view.findViewById(R.id.edt_recovery_email);
        recMail.setText(loginActivity.getSenderEmail());
        cancelREcovery = (Button) view.findViewById(R.id.recovery_cancel);
        cancelREcovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        sendEmail = (Button) view.findViewById(R.id.recovery_send);
        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = recMail.getText().toString();
                if (RegisterHelper.validateEmail(email)){
                    new SendMailTask().execute(email);
                }
                else {
                    recMail.setError("Invalid email");
                }

            }
        });

        return view;
    }

    private class SendMailTask extends AsyncTask<String,Void,Boolean>{
        @Override
        protected Boolean doInBackground(String... params) {
            String userMail = params[0];
            IUserDao userDataSource = UserDataSource.getInstance(getContext());
            ((UserDataSource)userDataSource).open();
            return userDataSource.checkForRegisteredEmail(userMail);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean){
                Intent i = new Intent(getActivity(),SendEmailService.class);
                i.putExtra(SendEmailService.USER_EMAIL,email);
                getActivity().startService(i);
                Toast.makeText(getContext(), "Sending email...", Toast.LENGTH_LONG).show();
                dismiss();
            }
            else {
                Toast.makeText(getContext(), "There is no such email in our database", Toast.LENGTH_LONG).show();
            }
        }
    }
}
