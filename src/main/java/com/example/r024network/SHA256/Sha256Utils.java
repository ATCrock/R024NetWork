package com.example.r024network.SHA256;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

public class Sha256Utils {
    private static final Logger logger = LoggerFactory.getLogger(Sha256Utils.class);

    // multipart file 是暂时文件数据流，传完就没有了
    public String getSHA256(File file) {
        String sha256Hex;
        try (FileInputStream inputStream = new FileInputStream(file)) {
            sha256Hex = DigestUtils.sha256Hex(inputStream);
            return sha256Hex;
        } catch (IOException e) {
            logger.error("获取SHA256失败", e);
        }
        return null;
    }

    public String getSHA256(Path path) {
        String sha256Hex;
        File file = new File(path.toUri());
        try (FileInputStream inputStream = new FileInputStream(file)) {
            sha256Hex = DigestUtils.sha256Hex(inputStream);
            return sha256Hex;
        } catch (IOException e) {
            logger.error("获取SHA256失败", e);
        }
        return null;
    }

    public String getSHA256(byte[] buffer) {
        if (buffer == null || buffer.length == 0) {
            return null;
        }
        return DigestUtils.sha256Hex(buffer);
    }

    public boolean checkSHA256(File file, String SHA256) {
        String sha256Hex;
        try (FileInputStream inputStream = new FileInputStream(file)) {
            sha256Hex = DigestUtils.sha256Hex(inputStream);
            if (sha256Hex.equals(SHA256)) {
                return true;
            }
        } catch (IOException e) {
            logger.error("文件不同", e);
        }
        return false;
    }


}
