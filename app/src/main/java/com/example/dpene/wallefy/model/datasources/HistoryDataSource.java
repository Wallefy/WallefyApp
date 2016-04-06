package com.example.dpene.wallefy.model.datasources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.dpene.wallefy.model.classes.Category;
import com.example.dpene.wallefy.model.classes.History;
import com.example.dpene.wallefy.model.dao.IHistoryDao;
import com.example.dpene.wallefy.model.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class HistoryDataSource extends DataSource implements IHistoryDao {

    public HistoryDataSource(Context context) {
        super(context);
    }

    @Override
    public History createHistory(long userId, long accountTypeId, long categoryId, double amount, String description,
                                 String dateOfTransaction, String imgPath, String location) {
        ContentValues values = new ContentValues();

        if (dateOfTransaction==null){
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateOfTransaction = df.format(c.getTime());
        }

        values.put(Constants.HISTORY_USER_FK, userId);
        values.put(Constants.HISTORY_ACCOUNT_TYPE_FK, accountTypeId);
        values.put(Constants.HISTORY_CATEGORY_FK, categoryId);
        values.put(Constants.TRANSACTION_AMOUNT, amount);
        values.put(Constants.HISTORY_DESCRIPTION, description);
        values.put(Constants.TRANSACTION_DATE, dateOfTransaction);
        values.put(Constants.IMG_PATH, imgPath);
        values.put(Constants.TRANSACTION_LOCATION, location);
        long insertId = database.insert(Constants.TABLE_HISTORY, null, values);
        if (insertId < 0) {
            return null;
        }
        String[] selArgs = {String.valueOf(insertId)};
        Cursor cursor = database.rawQuery("select history_id,history_user_fk,history_account_type_fk," +
                "history_category_fk,history_description,transaction_date,transaction_amount,img_path,transaction_location" +
                " from history where history_id = ? ", selArgs);
        if (cursor.moveToFirst()) {
            long historyId = cursor.getLong(0);
            long historyUserFk = cursor.getLong(1);
            long historyAccType = cursor.getLong(2);
            long historyCatFk = cursor.getLong(3);
            String historyDescr = cursor.getString(4);
            String historyDate = cursor.getString(5);
            double historyAmount = cursor.getDouble(6);
            String historyImgPath = cursor.getString(7);
            String historyLocation = cursor.getString(8);
            cursor.close();
            return new History(historyId,historyUserFk,historyAccType,historyCatFk,historyAmount,historyDescr,historyDate,
            historyImgPath,historyLocation);
        }
        cursor.close();
        return null;
    }

    @Override
    public ArrayList<History> listAllHistory(long userID) {
        ArrayList<History> historyArrayList = new ArrayList<>();
        String[] selArgs = {String.valueOf(userID)};
        Cursor cursor = database.rawQuery("select history_id,history_user_fk,history_account_type_fk," +
                "history_category_fk,history_description,transaction_date,transaction_amount,img_path,transaction_location" +
                " from history where history_user_fk = ? ", selArgs);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                long historyId = cursor.getLong(0);
                long historyUserFk = cursor.getLong(1);
                long historyAccType = cursor.getLong(2);
                long historyCatFk = cursor.getLong(3);
                String historyDescr = cursor.getString(4);
                String historyDate = cursor.getString(5);
                double historyAmount = cursor.getDouble(6);
                String historyImgPath = cursor.getString(7);
                String historyLocation = cursor.getString(8);
                historyArrayList.add(new History(historyId, historyUserFk, historyAccType, historyCatFk, historyAmount, historyDescr, historyDate,
                        historyImgPath, historyLocation));
                cursor.moveToNext();
            }

            cursor.close();
            return historyArrayList;
        }
        cursor.close();
        return null;
    }

    @Override
    public ArrayList<History> listHistoryByCategory(long userID, long accountType) {
        return null;
    }

    @Override
    public ArrayList<History> listHistoryAfterDate(long userID, String afterDate) {
        return null;
    }

    @Override
    public ArrayList<History> listHistoryBeforeDate(long userID, String beforeDate) {
        return null;
    }

    @Override
    public ArrayList<History> listHistoryBetweenDate(long userID, String afterDate, String beforeDate) {
        return null;
    }
}
