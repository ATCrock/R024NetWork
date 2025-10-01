package com.example.r024network.Service.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.r024network.Exception.APIException;
import com.example.r024network.SHA256.Sha256Utils;
import com.example.r024network.Service.ImageService;
import com.example.r024network.entity.Images;
import com.example.r024network.entity.Userdata;
import com.example.r024network.mapper.ImagesMapper;
import com.example.r024network.mapper.UserdataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private Path fileStorageLocation;
    private final ImagesMapper imagesMapper;
    private final UserdataMapper userdataMapper;
    private final WrapperHelper wrapperHelper;

    public void updateHeadPortraitDefault(@Value("${file.upload-dir}") String headAddress, Integer account) {
        this.fileStorageLocation = Paths.get(headAddress).toAbsolutePath().normalize();
        Images images = imagesMapper.selectOne(wrapperHelper.convert("file_path", headAddress));
        if (images == null) {
            throw new APIException(410, "没有对应图片");
        }
        Userdata userdata = userdataMapper.selectOne(wrapperHelper.convert("user_account", account));
        userdata.setUserHeadPortraitAddress(headAddress); // 设置用户头像地址
        // 设置为头像，非null(1)代表这张图是头像
        images.setIsAvator(1);
        images.setUserId(userdata.getUserId());
        // 更新用户信息
        userdataMapper.insertOrUpdate(userdata);
        imagesMapper.insertOrUpdate(images);
    }

    private void setFileStorageLocation(String path){
        // 绝对化路径
        this.fileStorageLocation = Paths.get(path).toAbsolutePath().normalize();
    }

    public String storeFile(MultipartFile file) {
        Sha256Utils sha256Utils = new Sha256Utils();
        // MultipartFile   spring的缓存文件类
        // 默认subDirectory = /localStorage/data/image
        String subDirectory = "localStorage/data/image";
        if (this.fileStorageLocation == null) {
            setFileStorageLocation("./localStorage/data/image");
        }
        if (file == null) {
            throw new APIException(11, "上传的文件不能为null");
        }
        if (file.isEmpty()) {
            throw new APIException(11, "上传的文件不能为空");
        }
        String originalFilename = file.getOriginalFilename();
        // 获取文件原地址，包含盘符
        String fileName;

        if (originalFilename == null || originalFilename.isBlank()) {
            String fileExtension = getFileExtensionFromContentType(file.getContentType());
            fileName = "file_" + System.currentTimeMillis() + fileExtension;
        } else {
            fileName = UUID.randomUUID() + getFileExtensionFromContentType(file.getContentType());
            // 统一文件路径，把\斜杠统一换成/
        }
        try {
            if (fileName.contains("..")) {
                throw new APIException(12, "包含非法路径: " + fileName);
            }

            // 创建存储路径，复制文件
            Path targetPath = this.fileStorageLocation.resolve(this.fileStorageLocation);
            Files.createDirectories(targetPath);
            Path targetFile = targetPath.resolve(fileName);

            // 获取传入文件字节码
            byte[] buffer = file.getInputStream().readAllBytes();
            // 获取SHA256密文
            String SHAKey = sha256Utils.getSHA256(buffer);

            if (imagesMapper.selectOne(wrapperHelper.convert("file_SHA256", SHAKey)) == null) {
                // 复制文件
                Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
                // 数据库插入images
                Images image = new Images();
                image.setFileName(fileName);
                image.setFilePath(subDirectory);
                image.setFileSize(String.valueOf(file.getSize()));
                image.setFileType(SHAKey);
                imagesMapper.insert(image);
                return fileName;
            }else {
                return null;
            }
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
