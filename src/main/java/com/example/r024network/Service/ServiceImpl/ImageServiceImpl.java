package com.example.r024network.Service.ServiceImpl;

import com.example.r024network.Exception.APIException;
import com.example.r024network.SHA256.Sha256Utils;
import com.example.r024network.Service.ImageService;
import com.example.r024network.entity.Images;
import com.example.r024network.mapper.ImagesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private Path fileStorageLocation;
    private final ImagesMapper imagesMapper;
    private final WrapperHelper wrapperHelper;

    public void updateHeadPortraitDefault(String fileName, Integer userId) {
        Images images = imagesMapper.selectOne(wrapperHelper.convert("file_name", fileName));
        // 设置为头像，非null(1)代表这张图是头像
        images.setIsAvator(1);
        images.setUserId(userId);
        // 更新图片信息
        imagesMapper.insertOrUpdate(images);
    }

    public String storeFile(MultipartFile file) {
        Sha256Utils sha256Utils = new Sha256Utils();
        // MultipartFile   spring的缓存文件类
        // 默认subDirectory = /localStorage/data/image
        String subDirectory = "localStorage/data/image";
        if (this.fileStorageLocation == null) {
            this.fileStorageLocation = Paths.get("./localStorage/data/image").toAbsolutePath().normalize();
            // 改变存储路径没做了就
        }
        if (file == null) {
            throw new APIException(411, "上传的文件不能为null");
        }
        if (file.isEmpty()) {
            throw new APIException(411, "上传的文件不能为空");
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
                throw new APIException(412, "包含非法路径: " + fileName);
            }

            // 创建存储路径，复制文件
            Path targetPath = this.fileStorageLocation.resolve(this.fileStorageLocation);
            Files.createDirectories(targetPath);
            Path targetFile = targetPath.resolve(fileName);

            // 获取传入文件字节码
            byte[] buffer = file.getInputStream().readAllBytes();
            // 获取SHA256密文
            String SHAKey = sha256Utils.getSHA256(buffer);
            Images images = imagesMapper.selectOne(wrapperHelper.convert("file_type", SHAKey));
            // 如果查询不到对应图片，就在数据库中存储；如果查到了，就和评论一样，插入对应帖子id
            if (images == null) {
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
                return images.getFileName();
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
