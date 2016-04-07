package com.example.dpene.wallefy.model.datasources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.dpene.wallefy.model.classes.Account;
import com.example.dpene.wallefy.model.classes.Category;
import com.example.dpene.wallefy.model.dao.ICategoryDao;
import com.example.dpene.wallefy.model.utils.Constants;

import java.util.ArrayList;


public class CategoryDataSource extends DataSource implements ICategoryDao{

    private static CategoryDataSource instance;

    private CategoryDataSource(Context context) {
        super(context);
    }

    public static CategoryDataSource getInstance(Context context){
        if(instance == null){
            instance = new CategoryDataSource(context);
        }
        return instance;
    }

    @Override
    public Category showCategory(String categoryName) {
        return null;
    }

    @Override
    public Category createCategory(String categoryName, boolean isExpense, long iconResource, long userFk) {
        ContentValues values = new ContentValues();
        values.put(Constants.CATEGORY_NAME, categoryName);
        values.put(Constants.CATEGORY_IS_EXPENCE, isExpense);
        values.put(Constants.CATEGORY_ICON_RESOURCE, iconResource);
        values.put(Constants.CATEGORY_USER_FK, userFk);
        long insertId = database.insert(Constants.TABLE_CATEGORIES, null, values);
        if (insertId < 0) {
            return null;
        }
        String[] selArgs = {String.valueOf(insertId)};
        Cursor cursor = database.rawQuery("select category_id,category_icon_resource,category_name,category_is_expense,category_user_fk" +
                " from categories where category_id = ? ", selArgs);
        if (cursor.moveToFirst()) {
            long catId = cursor.getLong(0);
            long catResIcon = cursor.getLong(1);
            String catName = cursor.getString(2);
            boolean catIsExpense = (cursor.getInt(3) == 1);
            long catUserFk = cursor.getLong(4);
            cursor.close();
            return new Category(catId,catName,catIsExpense,catResIcon,catUserFk);
        }
        cursor.close();
        return null;
    }

    @Override
    public ArrayList<Category> showAllCategoriesForUser(long userId) {
        ArrayList<Category> categories = new ArrayList<>();
        String[] selArgs = {String.valueOf(userId)};
        Cursor cursor = database.rawQuery("select category_id,category_icon_resource,category_name,category_is_expense,category_user_fk" +
                " from categories where category_user_fk = ? ", selArgs);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                long catId = cursor.getLong(0);
                long catResIcon = cursor.getLong(1);
                String catName = cursor.getString(2);
                boolean catIsExpense = (cursor.getInt(3) == 1);
                long catUserFk = cursor.getLong(4);
                categories.add(new Category(catId,catName,catIsExpense,catResIcon,catUserFk));
                cursor.moveToNext();
            }
            cursor.close();
            return categories;
        }
        cursor.close();
        return null;
    }

    @Override
    public ArrayList<Category> showCategoriesForAccount(long userId, long accountTypeId) {
        return null;
    }

    @Override
    public ArrayList<Category> showCategoriesByType(long userId, boolean isExpense) {
        return null;
    }
}
