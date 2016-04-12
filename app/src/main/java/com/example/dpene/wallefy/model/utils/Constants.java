package com.example.dpene.wallefy.model.utils;

public class Constants {

    public static final String EXISTING_EMAIL = "email exists";
    public static final String EXISTING_USERNAME = "username exists";

    public static final String DB_NAME = "WallefyDB.db";
    public static final String ALLOW_FOREIGN_KEYS = "pragma foreign_keys = on";
    public static final int DB_VERSION = 1;

    public static final String TABLE_ACCOUNT_TYPES = "Account_Types";
    public static final String TABLE_CATEGORIES = "Categories";
    public static final String TABLE_USERS = "Users";
    public static final String TABLE_HISTORY = "History";

    //        ----- USERS -----
    public static final String USER_ID = "user_id";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_PASSWORD = "user_password";
    public static final String USER_NAME = "user_name";
    public static final String[] USERS_ALL_COLUMNS = {USER_ID, USER_NAME, USER_EMAIL, USER_PASSWORD};

    //    ----- Account_Types -----
    public static final String ACCOUNT_TYPE_ID = "account_type_id";
    public static final String ACCOUNT_NAME = "account_name";
    public static final String ACCOUNT_USER_FK = "account_user_fk";

    //    ----- Categories -----
    public static final String CATEGORY_ID = "category_id";
    public static final String CATEGORY_NAME = "category_name";
    public static final String CATEGORY_ICON_RESOURCE = "category_icon_resource";
    public static final String CATEGORY_IS_EXPENCE = "category_is_expense";
    public static final String CATEGORY_USER_FK = "category_user_fk";
    public static final String CATEGORY_IS_SYSTEM = "category_is_system";

    //    ----- HISTORY -----
    public static final String HISTORY_ID = "history_id";
    public static final String HISTORY_USER_FK = "history_user_fk";
    public static final String HISTORY_ACCOUNT_TYPE_FK = "history_account_type_fk";
    public static final String HISTORY_CATEGORY_FK = "history_category_fk";
    public static final String HISTORY_DESCRIPTION = "history_description";
    public static final String TRANSACTION_DATE = "transaction_date";
    public static final String TRANSACTION_AMOUNT = "transaction_amount";
    public static final String IMG_PATH = "img_path";
    public static final String TRANSACTION_LOCATION = "transaction_location";
}
