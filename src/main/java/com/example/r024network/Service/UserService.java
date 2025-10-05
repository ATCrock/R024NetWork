package com.example.r024network.Service;

public interface UserService {
    boolean register(String userAccount, String userName, String password, Integer userType);
    String login(String userAccount, String password);
    void updateUserInformation(String previousAccount, String newAccount, String newUserName, String newPassword);
    void pullBlack(Integer userAccount, Integer targetAccount);
    void pullWhite(Integer userAccount, Integer targetAccount);
    int[] getBlack(Integer userAccount);
    String loginRefresh(String userAccount, int userId);
    //void loginByToken(String token);
}
