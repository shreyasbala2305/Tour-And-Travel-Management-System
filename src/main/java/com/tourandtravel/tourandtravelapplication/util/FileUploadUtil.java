package com.tourandtravel.tourandtravelapplication.util;
//src/main/java/com/tourandtravel/util/FileUploadUtil.java

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class FileUploadUtil {
 
 public static String saveFile(String uploadDir, MultipartFile file) throws IOException {
     String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
     
     Path uploadPath = Paths.get(uploadDir);
     if (!Files.exists(uploadPath)) {
         Files.createDirectories(uploadPath);
     }
     
     try (InputStream inputStream = file.getInputStream()) {
         Path filePath = uploadPath.resolve(fileName);
         Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
         return fileName;
     } catch (IOException e) {
         throw new IOException("Could not save file: " + fileName, e);
     }
 }
 
 public static void deleteFile(String uploadDir, String fileName) throws IOException {
     Path filePath = Paths.get(uploadDir).resolve(fileName);
     Files.deleteIfExists(filePath);
 }
}