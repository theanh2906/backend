package com.example.backend.utils;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
    private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);

    public static File convertFile(MultipartFile multipartFile) {
        String filename = multipartFile.getOriginalFilename();
        String name = FilenameUtils.getBaseName(filename);
        String extension = FilenameUtils.getExtension(filename);
        try {
            File convertedFile = File.createTempFile(name, "." + extension);
            FileOutputStream fileOutputStream = new FileOutputStream(convertedFile);
            fileOutputStream.write(multipartFile.getBytes());
            return convertedFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static FileOutputStream prepareFile(String filename) {
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        String fileLocation = path.substring(0, path.length() - 1) + filename;
        try {
            return new FileOutputStream(fileLocation);
        } catch (FileNotFoundException e) {
            LOG.error("prepareFile {}", e.getLocalizedMessage());
            return null;
        }
    }
}
