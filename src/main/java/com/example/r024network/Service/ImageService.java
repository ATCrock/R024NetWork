package com.example.r024network.Service;

import com.example.r024network.Exception.APIException;
import com.example.r024network.Service.ServiceImpl.ImageServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    void updateHeadPortraitDefault(String headAddress, Integer userId);
    String storeFile(MultipartFile file) throws APIException;
}
