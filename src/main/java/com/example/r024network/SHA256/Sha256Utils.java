package com.example.r024network.SHA256;
import org.apache.commons.codec.digest.DigestUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

public class Sha256Utils {
    // multipart file 是暂时文件数据流，传完就没有了
    public String getSHA256(File file) {
        String sha256Hex;
        try (FileInputStream inputStream = new FileInputStream(file)) {
            sha256Hex = DigestUtils.sha256Hex(inputStream);
            return sha256Hex;
        } catch (IOException e) {
            return null;
        }
    }

    // 从url打开文件并生成SHA256
    public String getSHA256(Path path) {
        String sha256Hex;
        File file = new File(path.toUri());
        try (FileInputStream inputStream = new FileInputStream(file)) {
            sha256Hex = DigestUtils.sha256Hex(inputStream);
            return sha256Hex;
        } catch (IOException e) {
            return null;
        }
    }

    // 从字节码流生成SHA256
    public String getSHA256(byte[] buffer) {
        if (buffer == null || buffer.length == 0) {
            return null;
        }
        return DigestUtils.sha256Hex(buffer);
    }
}
