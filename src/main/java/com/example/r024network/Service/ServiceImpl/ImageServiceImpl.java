package com.example.r024network.Service.ServiceImpl;

import ch.qos.logback.core.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.r024network.Exception.APIException;
import com.example.r024network.Service.ImageService;
import com.example.r024network.entity.Images;
import com.example.r024network.entity.Userdata;
import com.example.r024network.mapper.ImagesMapper;
import com.example.r024network.mapper.UserdataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private Path fileStorageLocation;
    private final ImagesMapper imagesMapper;
    private final UserdataMapper userdataMapper;
    private final WrapperHelper warpperHelper;

    public void updateHeadPortraitDefault(@Value("${file.upload-dir}") String headAddress, Integer account) {
        this.fileStorageLocation = Paths.get(headAddress).toAbsolutePath().normalize();
        Images images = imagesMapper.selectOne(warpperHelper.convert("file_path", headAddress));
        if (images == null) {
            throw new APIException(410, "没有对应图片");
        }
        Userdata userdata = userdataMapper.selectOne(warpperHelper.convert("user_account", account));
        userdata.setUserHeadPortraitAddress(headAddress);
        images.setIsAvator(1);
        images.setUserId(userdata.getUserId());
        userdataMapper.insertOrUpdate(userdata);
        imagesMapper.insertOrUpdate(images);
    }

    private void setFileStorageLocation(String path){
        this.fileStorageLocation = Paths.get(path).toAbsolutePath().normalize();
    }

    public String storeFile(MultipartFile file) {
        // MultipartFile spring的缓存文件类
        // 默认subDirectory = data
        if (this.fileStorageLocation == null) {
            setFileStorageLocation("./localStorage/data/image");
        }
        String subDirectory = "data/image";
        if (file == null) {
            throw new APIException(11, "上传的文件不能为null");
        }
        if (file.isEmpty()) {
            throw new APIException(11, "上传的文件不能为空");
        }
        String originalFilename = file.getOriginalFilename();
        // 获取文件原地址，包含盘符了
        String fileName;

        if (originalFilename == null || originalFilename.isBlank()) {
            String fileExtension = getFileExtensionFromContentType(file.getContentType());
            fileName = "file_" + System.currentTimeMillis() + fileExtension;
        } else {
            fileName = StringUtils.cleanPath(originalFilename);
            // 统一文件路径，把\斜杠统一换成/
        }
        try {
            if (fileName.contains("..")) {
                throw new APIException(12, "包含非法路径: " + fileName);
            }
            Path targetPath = this.fileStorageLocation.resolve(subDirectory);
            Files.createDirectories(targetPath);
            Path targetFile = targetPath.resolve(fileName);
            Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
            Images image = new Images();
            image.setFileName(fileName);
            Images imageStatic = imagesMapper.selectOne(warpperHelper.convert("file_name", fileName));
            Path path = Paths.get(subDirectory, fileName);
            if (imageStatic == null && !Objects.equals(imageStatic.getFileName(), fileName)) { // 先判断是否为null，如果不是null再获取文件名，与给定文件名对比，不同就保存新图片，相同就返回已有文件路径
                image.setFilePath(path.toString());
                image.setFileSize(String.valueOf(file.getSize()));
                imagesMapper.insert(image);
            }
            return path.toString();
        }catch (IOException e){
            throw new APIException(13, e.getMessage());
        }
    }

    private String getFileExtensionFromContentType(String contentType) {
        if (contentType == null) {
            return "";
        }
        return switch (contentType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            default -> ".bin";
        };
    }


}
