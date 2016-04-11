package com.example.dpene.wallefy.model.dao;

import com.example.dpene.wallefy.model.classes.Category;

import java.util.ArrayList;

public interface ICategoryDao {

    Category showCategory(long userId,String categoryName);

    Category createCategory(String categoryName, boolean isExpense, long iconResource,long userFk);

    Category updateCategory(String categoryName,long iconResource,long userFk,String oldCategoryName);

    ArrayList<Category> showAllCategoriesForUser(long userId);

    ArrayList<Category> showCategoriesForAccount(long userId,long accountTypeId);

    ArrayList<Category> showCategoriesByType(long userId,boolean isExpense);



}
