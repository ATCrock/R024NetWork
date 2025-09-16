package com.example.r024network.service;

public interface UserService {
    void register(Integer userAccount, String userName, String password, Integer userType);
    void login(Integer userAccount, String password);
    void updateUserInformation(Integer previousAccount, Integer newAccount, String newUserName, String newPassword);

}
