package com.example.dpene.wallefy.controller.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.controller.fragments.interfaces.IToolbar;
import com.example.dpene.wallefy.model.classes.Account;
import com.example.dpene.wallefy.model.classes.Category;
import com.example.dpene.wallefy.model.classes.History;
import com.example.dpene.wallefy.model.classes.User;
import com.example.dpene.wallefy.model.dao.IAccountDao;
import com.example.dpene.wallefy.model.dao.ICategoryDao;
import com.example.dpene.wallefy.model.dao.IHistoryDao;
import com.example.dpene.wallefy.model.dao.IUserDao;
import com.example.dpene.wallefy.model.datasources.AccountDataSource;
import com.example.dpene.wallefy.model.datasources.CategoryDataSource;
import com.example.dpene.wallefy.model.datasources.HistoryDataSource;
import com.example.dpene.wallefy.model.datasources.UserDataSource;
import com.example.dpene.wallefy.model.utils.RegisterHelper;

public class EditProfileFragment extends Fragment {

    private EditText edtUsername;
    private EditText edtEmail;
    private EditText edtOldPassword;
    private EditText edtPassword;
    private EditText edtRetypePassword;

    private User user;
    IUserDao userDataSource;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        this.userDataSource = UserDataSource.getInstance(getContext());

        IToolbar toolbar = (IToolbar) getActivity();
        toolbar.setTitle("Edit profile");

        View v = inflater.inflate(R.layout.fragment_edit_profile, container, false);



        setHasOptionsMenu(true);

        this.edtUsername = (EditText) v.findViewById(R.id.edit_profile_name);
        this.edtEmail = (EditText) v.findViewById(R.id.edit_profile_email);
        this.edtOldPassword = (EditText) v.findViewById(R.id.edit_profile_old_password);
        this.edtPassword = (EditText) v.findViewById(R.id.edit_profile_password);
        this.edtRetypePassword = (EditText) v.findViewById(R.id.edit_profile_retype_password);

        this.user = (User) getArguments().get("user");
        this.edtUsername.setText(user.getUsername());
        this.edtEmail.setText(user.getEmail());

        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items

        String newUsername = this.edtUsername.getText().toString();
        String newEmail = this.edtEmail.getText().toString();
        String oldPass = this.edtOldPassword.getText().toString();
        String newPass = this.edtPassword.getText().toString();
        String retypedPass = this.edtRetypePassword.getText().toString();

        switch (item.getItemId()) {
            case R.id.save_entry:

                boolean isCorrect = true;

                if (newEmail.length() > 0 && !newEmail.equals(user.getEmail())) {
                    if (RegisterHelper.validateEmail(newEmail)) {
                        edtEmail.setError("Invalid email");
                        isCorrect = false;
                    }
                }

                if (newUsername.length() > 0 && !newUsername.equals(user.getUsername())) {
                    if (!RegisterHelper.validateUsername(newUsername)) {
                        edtUsername.setError("Name's length must be bigger than 3");
                        isCorrect = false;
                    }
                }

                // TODO: remove the getPassword() method and check in DB
                if (oldPass.length() > 0 && !RegisterHelper.md5(oldPass).equals(user.getPassword())) {
                    Log.e("tag", RegisterHelper.md5(oldPass) + " " + oldPass + "  " + user.getPassword());
                    this.edtOldPassword.setError("Wrong password!");
                    isCorrect = false;
                }

                if (newPass.length() > 0) {
                    if(!RegisterHelper.strongPassword(newPass)){
                        edtPassword.setError("Password must be between 5-10 characters and contain letters AND numbers");
                        isCorrect = false;
                    } else {
                        if (!newPass.equals(retypedPass)) {
                            edtRetypePassword.setError("The passwords don't match");
                            isCorrect = false;
                        }
                    }

                }

                if (isCorrect) {

                    if(newUsername.equals("")) {
                        newUsername = user.getUsername();
                    }

                    if(newEmail.equals("")) {
                        newEmail = user.getEmail();
                    }

                    if(newPass.equals("")) {
                        newPass = user.getPassword();
                    }

                    new TaskUpdateUserProfile(user.getUserId()).execute(newEmail, newUsername, newPass);
                }
                return true;
            case R.id.clear_values:
                // NO SUCH BUTTON
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class TaskUpdateUserProfile extends AsyncTask<String, Void, Boolean>{

        private long userID;

        public TaskUpdateUserProfile(long userId) {
            this.userID = userId;
        }


        @Override
        protected Boolean doInBackground(String... params) {

            IUserDao userDataSource = UserDataSource.getInstance(getContext());
            ((UserDataSource) userDataSource).open();
            //TODO update user in DB
            Log.e("tag", userID +", " + params[0]+", " + params[1]+", " + params[2]);
//            User user = userDataSource.updateUser(userID, params[0], params[1], params[2]);

            if (user != null) {
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean)
                Toast.makeText(getContext(), "Save success", Toast.LENGTH_SHORT).show();
        }
    }
}
