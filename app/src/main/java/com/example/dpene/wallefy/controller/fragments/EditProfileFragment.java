package com.example.dpene.wallefy.controller.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.controller.MainActivity;
import com.example.dpene.wallefy.controller.fragments.interfaces.IEditPassUser;
import com.example.dpene.wallefy.controller.fragments.interfaces.IToolbar;
import com.example.dpene.wallefy.model.classes.User;
import com.example.dpene.wallefy.model.dao.IUserDao;
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
    IEditPassUser parent;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parent = (IEditPassUser) context;
    }

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

//        this.user = (User) getArguments().get("user");
        this.edtUsername.setText(MainActivity.user.getUsername());
        this.edtEmail.setText(MainActivity.user.getEmail());

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

                if (newEmail.length() > 0 && !newEmail.equals(MainActivity.user.getEmail())) {
                    if (!RegisterHelper.validateEmail(newEmail)) {
                        edtEmail.setError("Invalid email");
                        isCorrect = false;
                    }
                }

                if (newUsername.length() > 0 && !newUsername.equals(MainActivity.user.getUsername())) {
                    if (!RegisterHelper.validateUsername(newUsername)) {
                        edtUsername.setError("Name's length must be bigger than 3");
                        isCorrect = false;
                    }
                }

                // TODO: remove the getPassword() method and check in DB
                if (oldPass.length() > 0 && !RegisterHelper.md5(oldPass).equals(MainActivity.user.getPassword())) {
                    Log.e("tag", RegisterHelper.md5(oldPass) + " " + oldPass + "  " + MainActivity.user.getPassword());
                    this.edtOldPassword.setError("Wrong password!");
                    isCorrect = false;
                }

                if (newPass.length() > 0) {
                    if (!RegisterHelper.strongPassword(newPass)) {
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

                    if (newUsername.equals("")) {
                        newUsername = MainActivity.user.getUsername();
                    }

                    if (newEmail.equals("")) {
                        newEmail = MainActivity.user.getEmail();
                    }

                    if (newPass.equals("")) {
                        newPass = MainActivity.user.getPassword();
                    }

                    new TaskUpdateUserProfile(MainActivity.user.getUserId()).execute(newEmail, newUsername, newPass);

                }
                return true;
            case R.id.clear_values:
                // NO SUCH BUTTON
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class TaskUpdateUserProfile extends AsyncTask<String, Void, Boolean> {

        private long userID;

        public TaskUpdateUserProfile(long userId) {
            this.userID = userId;
        }


        @Override
        protected Boolean doInBackground(String... params) {

            IUserDao userDataSource = UserDataSource.getInstance(getContext());
            ((UserDataSource) userDataSource).open();
            User updateUser = userDataSource.updateUser(userID, params[0], params[1], params[2]);
            if (updateUser != null) {
                MainActivity.user.setEmail(updateUser.getEmail());
                MainActivity.user.setUsername(updateUser.getUsername());
                if (updateUser.getPassword().length()>0)
                    MainActivity.user.setPassword(updateUser.getPassword());
                parent.getUser(MainActivity.user);
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                Toast.makeText(getContext(), "Save success", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getContext(), MainActivity.class);
                i.putExtra("user",parent.sendUser());
                startActivity(i);
                getActivity().finish();
            }

        }
    }
}
