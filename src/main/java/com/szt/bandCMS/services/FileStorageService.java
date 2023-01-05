package com.szt.bandCMS.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {

    public String save(MultipartFile file, String filename) throws IllegalArgumentException, IOException {
        if (!file.getContentType().contains("image/") || file.getSize() > 1048576) {
            throw new IllegalArgumentException();
        }
        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        String shortPath = "/images/" + filename + "." + extension;
        Path path = Paths.get("c:/temp/semestr9/SZT/" + shortPath);
        try {
            Files.copy(file.getInputStream(), path);
            return shortPath;
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                Files.delete(path);
                return save(file, filename);
            } else {
                throw new IOException();
            }
        }
    }
}
