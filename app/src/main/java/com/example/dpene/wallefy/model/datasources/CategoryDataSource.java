package com.example.dpene.wallefy.model.datasources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

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
    public Category showCategory(long userId,String categoryName) {
        String[] selArgs = {String.valueOf(userId),categoryName};
        Cursor cursor = database.rawQuery("select category_id,category_icon_resource,category_name,category_is_expense,category_user_fk" +
                " from categories where category_user_fk = ? and category_name = ? ", selArgs);
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
    public Category createCategory(String categoryName, boolean isExpense, long iconResource, long userFk) {
        Category cat = showCategory(userFk,categoryName);
        if (cat != null)
            return null;
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
    public Category createSystemCategory(String categoryName, boolean isExpense, long iconResource, long userFk, boolean isSystem) {
        ContentValues values = new ContentValues();
        Log.e("ISSISTEM", "DATASOURCE: " + isSystem );
        values.put(Constants.CATEGORY_NAME, categoryName);
        values.put(Constants.CATEGORY_IS_EXPENCE, isExpense);
        values.put(Constants.CATEGORY_ICON_RESOURCE, iconResource);
        values.put(Constants.CATEGORY_USER_FK, userFk);
        values.put(Constants.CATEGORY_IS_SYSTEM,isSystem? "1":"0");
        long insertId = database.insert(Constants.TABLE_CATEGORIES, null, values);
        if (insertId < 0) {
            return null;
        }
        String[] selArgs = {String.valueOf(insertId)};
        Cursor cursor = database.rawQuery("select category_id,category_icon_resource,category_name," +
                "category_is_expense,category_user_fk,category_is_system" +
                " from categories where category_id = ? ", selArgs);
        if (cursor.moveToFirst()) {
            long catId = cursor.getLong(0);
            long catResIcon = cursor.getLong(1);
            String catName = cursor.getString(2);
            boolean catIsExpense = (cursor.getInt(3) == 1);
            long catUserFk = cursor.getLong(4);
            boolean catIsSystem = (cursor.getInt(5) == 1);
            Log.e("ISSISTEM", "DATASOURCE: in cursor " + catIsSystem );
            cursor.close();
            return new Category(catId,catName,catIsExpense,catResIcon,catUserFk,catIsSystem);
        }
        cursor.close();
        return null;
    }

    @Override
    public Category updateCategory(String newCategoryName,long newIconResource,long userFk,String oldCategoryName, long oldIconRes,long categId) {

        Category cat = showCategory(userFk,newCategoryName);
        Log.e("UPDATE", ": cates " + cat);
        if (cat != null && cat.getCategoryId() != categId)
            return null;
        ContentValues values = new ContentValues();
        values.put(Constants.CATEGORY_NAME, newCategoryName);
        values.put(Constants.CATEGORY_ICON_RESOURCE, String.valueOf(newIconResource));
        String whereCaluse = " category_name = ? and category_user_fk = ? ";
        String[] whereArgs = {oldCategoryName,String.valueOf(userFk)};
        long insertId = database.update(Constants.TABLE_CATEGORIES, values, whereCaluse,whereArgs);
        Log.e("UPDATE", "updateCAtegory: new res " + newIconResource);
        Log.e("UPDATE", "updateCAtegory: old res " + oldCategoryName);
        Log.e("UPDATE", "updateCAtegory: user fk " + userFk);
        Log.e("UPDATE", "updateCAtegory: cat name " + newCategoryName);
        Log.e("UPDATE", "updateCAtegory: old name " + oldCategoryName);
        Log.e("UPDATE", "updateCAtegory: insertID " + insertId);
        if (insertId < 0) {
            return null;
        }
        String[] selArgs = {String.valueOf(insertId)};
        Cursor cursor = database.rawQuery("select category_id,category_icon_resource,category_name,category_is_expense,category_user_fk" +
                " from categories where category_id = ? ", selArgs);

        if (cursor.moveToFirst()) {
            long catId = cursor.getLong(0);
            Log.e("UpdateCAtegory", "updateCategory: " + catId );
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
        Cursor cursor = database.rawQuery("select category_id,category_icon_resource,category_name,category_is_expense," +
                "category_user_fk,category_is_system" +
                " from categories where category_user_fk = ? ", selArgs);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                long catId = cursor.getLong(0);
                long catResIcon = cursor.getLong(1);
                String catName = cursor.getString(2);
                boolean catIsExpense = (cursor.getInt(3) == 1);
                long catUserFk = cursor.getLong(4);
                boolean catIsSystem = (cursor.getInt(5) == 1);
                categories.add(new Category(catId,catName,catIsExpense,catResIcon,catUserFk,catIsSystem));
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
        ArrayList<Category> categories = new ArrayList<>();
        String[] selArgs = {String.valueOf(userId),String.valueOf(isExpense? 1:0)};
        Cursor cursor = database.rawQuery("select category_id,category_icon_resource,category_name," +
                "category_is_expense,category_user_fk,category_is_system" +
                " from categories where category_user_fk = ? and category_is_expense = ? ", selArgs);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                long catId = cursor.getLong(0);
                long catResIcon = cursor.getLong(1);
                String catName = cursor.getString(2);
                boolean catIsExpense = (cursor.getInt(3) == 1);
                long catUserFk = cursor.getLong(4);
                boolean catIsSystem = (cursor.getInt(5) == 1);
                categories.add(new Category(catId,catName,catIsExpense,catResIcon,catUserFk,catIsSystem));
                cursor.moveToNext();
            }
            cursor.close();
            return categories;
        }
        cursor.close();
        return null;
    }

    @Override
    public boolean deleteCategory(long userId, String categoryName) {
        String whereClause =Constants.CATEGORY_USER_FK + " = ? and " + Constants.CATEGORY_NAME + " = ? ";
        String[] args = {String.valueOf(userId),categoryName};
        return database.delete(Constants.TABLE_CATEGORIES, whereClause, args) > 0;
    }
}
