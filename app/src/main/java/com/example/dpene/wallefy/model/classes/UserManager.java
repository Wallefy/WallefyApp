package com.example.dpene.wallefy.model.classes;

import java.util.Map;

public class UserManager {

    private static User user;
    private static UserManager instance;

    private UserManager() {

    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    //TODO inicialize user using the DAO and prepare user's methods

    public void registerUser(String email, String password, String username){

    }

    public boolean existEmail(String email) {
        return false;
    }

    public boolean rightPassword(String password) {
        return false;
    }

    public boolean loginUser(String email, String password) {
        return false;
    }

    public boolean validateEmail(String email) {
        String pattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        return email != null && email.matches(pattern);
    }

    public boolean strongPasword(String password) {
        String pattern = "(?=.*[0-9])(?=.*[a-z]).{5,10}";
        return (password != null && password.matches(pattern));
    }

    public boolean validateUsername(String username) {
        return username != null && username != "";
    }

    public String getUsername() {
        return user.getUsername();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public String getPassword() {
        return user.getPassword();
    }

    public Map<String, Account> getAccounts() {
        return user.getAccounts();
    }


}
