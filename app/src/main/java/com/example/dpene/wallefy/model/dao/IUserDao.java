package com.example.dpene.wallefy.model.dao;


import com.example.dpene.wallefy.model.classes.User;

import java.util.ArrayList;

public interface IUserDao {

    User selectUserById(long id);

    User loginUser(String userEmail, String password);

    User registerUser(String userEmail,String userName, String password);

    boolean editUsername(long userId,String newUsername);

    boolean editEmail(long userId,String newEmail);

    boolean editPassword(long userId,String newPassword);

    boolean deleteUser(String email);

    User updateUser(long id, String userEmail, String userName, String password);

}

