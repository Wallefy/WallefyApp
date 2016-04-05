package com.example.dpene.wallefy.model.queries;

import com.example.dpene.wallefy.model.utils.Constants;

public class CreateTableQueries {

    //        ----- CREATE TABLE USERS ------
    public static final String CREATE_TABLE_USERS = "create table if not exists " + Constants.TABLE_USERS + "(" +
            Constants.USER_ID + " integer primary key autoincrement not null," +
            Constants.USER_NAME + " varchar(50) not null," +
            Constants.USER_EMAIL + " varchar(50) not null unique," +
            Constants.USER_PASSWORD + " varchar(50) not null)";

    //   ----- CREATE TABLE ACCOUNT TYPES -----
    public static final String CREATE_TABLE_ACCOUNT_TYPES = "create table if not exists " + Constants.TABLE_ACCOUNT_TYPES + " (" +
            Constants.ACCOUNT_TYPE_ID + " integer primary key autoincrement not null, " +
            Constants.ACCOUNT_NAME + " varchar(20) not null unique," +
            Constants.ACCOUNT_USER_FK + " integer not null references " + Constants.TABLE_USERS + "(" + Constants.USER_ID + "))";

    //   ----- CREATE TABLE CATEGORIES -----
    public static final String CREATE_TABLE_CATEGORIES = "create table if not exists " + Constants.TABLE_CATEGORIES + " (" +
            Constants.CATEGORY_ID + " integer primary key autoincrement not null," +
            Constants.CATEGORY_NAME + " varchar(20) not null," +
            Constants.CATEGORY_ICON_RESOURCE + " long not null," +
            Constants.CATEGORY_IS_EXPENCE + " integer not null," +
            Constants.CATEGORY_USER_FK + " integer not null references " + Constants.TABLE_USERS + "(" + Constants.USER_ID + "))";
    //   ----- CREATE TABLE HISTORY -----
    public static final String CREATE_TABLE_HISTORY = "create table if not exists " + Constants.TABLE_HISTORY + "(" +
            Constants.HISTORY_ID + " integer primary key autoincrement not null, " +
            Constants.HISTORY_USER_FK + " integer not null references " + Constants.TABLE_USERS + "(" + Constants.USER_ID + "), " +
            Constants.HISTORY_ACCOUNT_TYPE_FK + " integer not null references " + Constants.TABLE_ACCOUNT_TYPES + "(" + Constants.ACCOUNT_TYPE_ID + ")," +
            Constants.HISTORY_CATEGORY_FK + " integer not null references " + Constants.TABLE_CATEGORIES + "(" + Constants.CATEGORY_ID + "),history_description" +
            Constants.HISTORY_DESCRIPTION + " varchar(200), " +
            Constants.TRANSACTION_DATE + " DATETIME not null," +
            Constants.TRANSACTION_AMOUNT + " numeric not null," +
            Constants.IMG_PATH + " text," +
            Constants.TRANSACTION_LOCATION + " text)";
}
