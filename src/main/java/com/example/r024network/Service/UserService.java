package com.example.r024network.Service;

public interface UserService {
    void register(String userAccount, String userName, String password, Integer userType);
    String login(String userAccount, String password);
    void updateUserInformation(String previousAccount, String newAccount, String newUserName, String newPassword);
    void pullBlack(String userAccount, String targetAccount);
    void pullWhite(String userAccount, String targetAccount);
    int[] getBlack(String userAccount);
    //void loginByToken(String token);
}
