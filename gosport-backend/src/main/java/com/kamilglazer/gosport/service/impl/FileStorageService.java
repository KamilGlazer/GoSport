package com.kamilglazer.gosport.service.impl;


import com.kamilglazer.gosport.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService(@Value("${upload.dir}") String uploadDir){
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try{
            Files.createDirectories(this.fileStorageLocation);
        }catch (IOException e){
            throw new FileStorageException("Could not initialize the file storage service");
        }
    }

    public String storeFile(MultipartFile file,Long userId){
        try{
            String fileName = userId+"_"+file.getOriginalFilename();
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        }catch (IOException e){
            throw new FileStorageException("Could not store file");
        }
    }

    public Resource loadFile(String fileName) {
        try {
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileStorageException("Could not find file '" + fileName + "'");
            }
        } catch (MalformedURLException e) {
            throw new FileStorageException("Could not find file '" + fileName + "'");
        }
    }

}
