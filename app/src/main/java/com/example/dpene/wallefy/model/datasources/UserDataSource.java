package com.example.dpene.wallefy.model.datasources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.dpene.wallefy.model.classes.User;
import com.example.dpene.wallefy.model.dao.IUserDao;
import com.example.dpene.wallefy.model.utils.Constants;

public class UserDataSource extends DataSource implements IUserDao {

    private static UserDataSource instance;

    private UserDataSource(Context context) {
        super(context);
    }

    public static UserDataSource getInstance(Context context){
        if(instance == null){
            instance = new UserDataSource(context);
        }
        return instance;
    }

    @Override
    public User selectUserById(long id) {
        String[] selArgs = {String.valueOf(id)};
        Cursor cursor = database.rawQuery("select user_id,user_email,user_name,user_password from users where user_id = ? ",selArgs);
        if (cursor.moveToFirst()) {
            long userId = cursor.getLong(0);
            String userMail = cursor.getString(1);
            String userName = cursor.getString(2);
            String userPassword = cursor.getString(3);
            cursor.close();
            return new User(userId,userMail,userName,userPassword);
        }
        cursor.close();
        return null;
    }

    @Override
    public User loginUser(String mail, String password) {
        String[] selArgs = {mail,password};
        Cursor cursor = database.rawQuery("select user_id,user_email,user_name,user_password from users where user_email = ? and user_password = ?",selArgs);
        if (cursor.moveToFirst()) {
            long userId = cursor.getLong(0);
            String userMail = cursor.getString(1);
            String userName = cursor.getString(2);
            String userPassword = cursor.getString(3);
            cursor.close();
            return new User(userId,userMail,userName,userPassword);
        }
        cursor.close();
        return null;
    }

    @Override
    public User registerUser(String userEmail, String userName, String password) {

        if (checkForExisting(Constants.TABLE_USERS, Constants.USER_EMAIL, userEmail)) {
            Log.e("tag", "email existing");
            return null;
        }

        ContentValues values = new ContentValues();
        values.put(Constants.USER_NAME, userName);
        values.put(Constants.USER_PASSWORD, password);
        values.put(Constants.USER_EMAIL, userEmail);
        long insertId = database.insert(Constants.TABLE_USERS, null, values);
        if (insertId < 0) {
            Log.e("tag", "db error");
            return null;
        }

        return loginUser(userEmail,password);

    }

    public boolean checkForExisting(String table, String column, String selectionArg) {
        String[] columns = {column};
        String[] selectionArgs = {selectionArg};
        Cursor cursor = database.query(table, columns, column + " =? ", selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            return true;
        }
        return false;
    }

    @Override
    public User updateUser(long id, String userEmail, String userName, String password) {

        String[] upArgs = {String.valueOf(id)};
        ContentValues values = new ContentValues();
        values.put(Constants.USER_NAME, userName);
        values.put(Constants.USER_PASSWORD, password);
        values.put(Constants.USER_EMAIL, userEmail);

        long updateId = database.update(Constants.TABLE_USERS, values, Constants.USER_ID + " =? ", upArgs);

        if (updateId < 0) {
            Log.e("tag", "db error");
            return null;
        }

        return loginUser(userEmail,password);
    }

    @Override
    public boolean editUsername(long userId, String newUsername) {
        return false;
    }

    @Override
    public boolean editEmail(long userId, String newEmail) {
        return false;
    }

    @Override
    public boolean editPassword(long userId, String newPassword) {
        return false;
    }

    @Override
    public boolean deleteUser(String email) {
        return false;
    }
}
