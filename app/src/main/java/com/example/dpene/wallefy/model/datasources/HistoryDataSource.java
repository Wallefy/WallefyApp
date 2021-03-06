package com.example.dpene.wallefy.model.datasources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.dpene.wallefy.model.classes.Category;
import com.example.dpene.wallefy.model.classes.History;
import com.example.dpene.wallefy.model.dao.IHistoryDao;
import com.example.dpene.wallefy.model.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class HistoryDataSource extends DataSource implements IHistoryDao {

    private static HistoryDataSource instance;

    private HistoryDataSource(Context context) {
        super(context);
    }

    public static HistoryDataSource getInstance(Context context){
        if(instance == null){
            instance = new HistoryDataSource(context);
        }
        return instance;
    }

    @Override
    public History createHistory(long userId, long accountTypeId, long categoryId, double amount, String description,
                                 String dateOfTransaction, String imgPath, String location) {
        ContentValues values = new ContentValues();

        if (dateOfTransaction == null || dateOfTransaction.length() <= 0) {
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
        long insertId = database.insert(Constants.TABLE_HISTORY, null, values);
        if (insertId < 0) {
            return null;
        }

        String[] selArgs = {String.valueOf(insertId)};
        Cursor cursor = database.rawQuery("select history_id,history_user_fk,history_account_type_fk," +
                "history_category_fk,history_description,transaction_date,transaction_amount,img_path,"+Constants.TRANSACTION_LOCATION_LAT+"," +
                Constants.TRANSACTION_LOCATION_LONG +
                ",category_name,category_icon_resource" +
                " from history  join categories on history.history_category_fk = categories.category_id  where history_id = ? ", selArgs);
        if (cursor.moveToFirst()) {
            long historyId = cursor.getLong(0);
            long historyUserFk = cursor.getLong(1);
            long historyAccType = cursor.getLong(2);
            long historyCatFk = cursor.getLong(3);
            String historyDescr = cursor.getString(4);
            String historyDate = cursor.getString(5);
            double historyAmount = cursor.getDouble(6);
            String historyImgPath = cursor.getString(7);
            String historyLocationLat = cursor.getString(8);
            String historyLocationLong = cursor.getString(9);
            String historyCategoryName = cursor.getString(10);
            int historyCategoryIconResource = cursor.getInt(11);
            cursor.close();
            return new History(historyId, historyUserFk, historyAccType, historyCatFk, historyCategoryName, historyCategoryIconResource,
                    historyAmount, historyDescr, historyDate, historyImgPath, historyLocationLat,historyLocationLong);
        }
        cursor.close();
        return null;
    }

    @Override
    public double calcAmountForAccount(long userId, String accountTypeName) {
        double amount = 0;
        String[] args = {String.valueOf(userId), accountTypeName};
        Cursor cursor = database.rawQuery("select sum(transaction_amount) " +
                "from history h " +
                " join Account_Types on h.history_account_type_fk = Account_Types.account_type_id " +
                "where history_user_fk = ? and account_name = ? ", args);
        if (cursor.moveToNext()) {
            amount = cursor.getDouble(0);
            return amount;
        }
        cursor.close();
        return 0;
    }

    @Override
    public ArrayList<History> listAllHistory(long userID) {
        ArrayList<History> historyArrayList = new ArrayList<>();
        String[] selArgs = {String.valueOf(userID)};
        Cursor cursor = database.rawQuery("select history_id,history_user_fk,history_account_type_fk," +
                "history_category_fk,history_description,transaction_date,transaction_amount,img_path,"+
                Constants.TRANSACTION_LOCATION_LAT+"," +
                 Constants.TRANSACTION_LOCATION_LONG +
                ",category_name,category_icon_resource " +
                " from history join categories on history.history_category_fk = categories.category_id where history_user_fk = ? order by transaction_date desc ", selArgs);
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
                String historyLocationLat = cursor.getString(8);
                String historyLocationLong = cursor.getString(9);
                String historyCategoryName = cursor.getString(10);
                int historyCategoryIconResource = cursor.getInt(11);
                historyArrayList.add(new History(historyId, historyUserFk, historyAccType, historyCatFk, historyCategoryName, historyCategoryIconResource,
                        historyAmount, historyDescr, historyDate, historyImgPath, historyLocationLat,historyLocationLong));
                cursor.moveToNext();
            }

            cursor.close();
            return historyArrayList;
        }
        cursor.close();
        return null;
    }

    @Override
    public ArrayList<History> listHistoryByAccount(long userID, long accountType) {
        return null;
    }

    @Override
    public ArrayList<History> listHistoryByAccountName(long userID, String accountType) {
        ArrayList<History> historyArrayList = new ArrayList<>();
        String[] selArgs = {String.valueOf(userID), accountType};
        String whereCaluse = "where history_user_fk = ? and account_name = ? order by transaction_date desc";

        historyArrayList = searchEntriesByCriteria(whereCaluse,selArgs);
        return historyArrayList;
    }

    @Override
    public ArrayList<History> listHistoryByCategoryName(long userID, String categoryName) {
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

    @Override
    public ArrayList<History> filterEntries(String userId, String accName, String typeOfEntry, String catName, String dateAfter) {

        ArrayList<History> historyArrayList = listAllHistory(Long.parseLong(userId));
        if (historyArrayList == null)
            historyArrayList = new ArrayList<>();

        if (accName != null && !accName.equalsIgnoreCase("all")){
            historyArrayList.retainAll(listHistoryByAccountName(Long.parseLong(userId),accName));
        }

        if (typeOfEntry != null && !typeOfEntry.equalsIgnoreCase("all")){
            String[] args = {userId,typeOfEntry};
            String whereCaluse = "where history_user_fk = ? and category_is_expense = ? order by transaction_date desc";
            historyArrayList.retainAll(searchEntriesByCriteria(whereCaluse, args));
        }
        if (typeOfEntry != null && !typeOfEntry.equalsIgnoreCase("1")) {

        }

        if (catName != null){
            String[] args = {userId,catName};
            String whereCaluse = "where history_user_fk = ?  and category_name = ? order by transaction_date desc";
            historyArrayList.retainAll(searchEntriesByCriteria(whereCaluse, args));
        }
        if (dateAfter != null && dateAfter.length() > 1){
            String[] args = {userId,dateAfter};
            String whereCaluse = "where history_user_fk = ?  and transaction_date > ? order by transaction_date desc";
            historyArrayList.retainAll(searchEntriesByCriteria(whereCaluse, args));
        }

        return historyArrayList;
    }

    private ArrayList<History> searchEntriesByCriteria(String whereClause,String[] selArgs){
        ArrayList<History> historyArrayList = new ArrayList<>();
       Cursor cursor = database.rawQuery("select history_id,history_user_fk,history_account_type_fk," +
                "history_category_fk,history_description,transaction_date,transaction_amount,img_path, " +
               Constants.TRANSACTION_LOCATION_LAT+"," +
               Constants.TRANSACTION_LOCATION_LONG +
                ",category_name,category_icon_resource " +
                " from history " +
                "join categories on history.history_category_fk = categories.category_id " +
                " join Account_Types on history.history_account_type_fk = Account_Types.account_type_id " +
                whereClause, selArgs);
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
                String historyLocationLat = cursor.getString(8);
                String historyLocationLong = cursor.getString(9);
                String historyCategoryName = cursor.getString(10);
                int historyCategoryIconResource = cursor.getInt(11);
                historyArrayList.add(new History(historyId, historyUserFk, historyAccType, historyCatFk, historyCategoryName, historyCategoryIconResource,
                        historyAmount, historyDescr, historyDate, historyImgPath, historyLocationLat,historyLocationLong));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return historyArrayList;
    }
}
