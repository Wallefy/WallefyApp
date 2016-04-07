package com.example.dpene.wallefy.model.datasources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.dpene.wallefy.model.classes.Account;
import com.example.dpene.wallefy.model.dao.IAccountDao;
import com.example.dpene.wallefy.model.utils.Constants;

import java.util.ArrayList;

public class AccountDataSource extends DataSource implements IAccountDao {

    private static AccountDataSource instance;

    private AccountDataSource(Context context) {
        super(context);
    }

    public static AccountDataSource getInstance(Context context){
        if(instance == null){
            instance = new AccountDataSource(context);
        }
        return instance;
    }

    @Override
    public Account showAccount(String accountName) {
        return null;
    }

    @Override
    public Account createAccount(long userId, String accountName) {
        ContentValues values = new ContentValues();
        values.put(Constants.ACCOUNT_USER_FK, userId);
        values.put(Constants.ACCOUNT_NAME, accountName);
        long insertId = database.insert(Constants.TABLE_ACCOUNT_TYPES, null, values);
        if (insertId < 0) {
            return null;
        }
        String[] selArgs = {String.valueOf(insertId)};
        Cursor cursor = database.rawQuery("select account_type_id, account_name,account_user_fk from Account_Types where account_type_id = ? ", selArgs);
        if (cursor.moveToFirst()) {
            long accTypeId = cursor.getLong(0);
            String accName = cursor.getString(1);
            long accUserFk = cursor.getLong(2);
            cursor.close();
            return new Account(accTypeId, accUserFk, accName);
        }
        cursor.close();
        return null;
    }

    @Override
    public ArrayList<Account> showAllAccounts(long userId) {
        ArrayList<Account> accounts = new ArrayList<>();
        String[] selArgs = {String.valueOf(userId)};
        Cursor cursor = database.rawQuery("select account_type_id, account_name,account_user_fk from Account_Types where account_user_fk = ? ", selArgs);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                long accTypeId = cursor.getLong(0);
                String accName = cursor.getString(1);
                long accUserFk = cursor.getLong(2);
                accounts.add(new Account(accTypeId, accUserFk, accName));
                cursor.moveToNext();
            }
            cursor.close();
            return accounts;
        }
        cursor.close();
        return null;
    }
}
