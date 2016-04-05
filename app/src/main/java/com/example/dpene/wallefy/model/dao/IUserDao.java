package com.example.dpene.wallefy.model.dao;


public interface IUserDao {

    long loginUser(String userEmail, String password);

    long registerUser(String userEmail,String userName, String password);

    boolean editUsername(long userId,String newUsername);

    boolean editEmail(long userId,String newEmail);

    boolean editPassword(long userId,String newPassword);

    boolean deleteUser(String email);

}

