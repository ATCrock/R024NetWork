package com.example.r024network.Service;

public interface UserService {
    void register(String userAccount, String userName, String password, Integer userType);
    String login(String userAccount, String password);
    void updateUserInformation(String previousAccount, String newAccount, String newUserName, String newPassword);
    void pullBlack(Integer userAccount, Integer targetAccount);
    void pullWhite(Integer userAccount, Integer targetAccount);
    int[] getBlack(Integer userAccount);
    //void loginByToken(String token);
}
