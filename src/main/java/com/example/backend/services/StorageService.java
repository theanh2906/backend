package com.example.backend.services;

import com.example.backend.dtos.FileDto;
import com.example.backend.dtos.ImageDto;
import com.example.backend.mappers.ImageMapper;
import com.example.backend.models.Images;
import com.example.backend.repositories.ImagesRepository;
import com.example.backend.utils.Utils;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StorageService {
    @Transactional
    public Boolean deleteAllImages() {
        try {
            imagesRepository.deleteAll();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void deleteImage(String id) {
        imagesRepository.deleteById(id);
    }

    public void downloadFile(String encodedPath, HttpServletResponse response) {
        try {
            String decodedPath = Utils.decodeBase64Str(encodedPath);
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", String.format("attachment;filename=%s", encodedPath));
        } catch (Exception ignore) {

        }
    }

    public void downloadImage(int index, HttpServletResponse response) {
        response.setContentType("image/jpeg");
        try {
            Images images = imagesRepository.findAll().get(index);
            response.setHeader("Content-Disposition", String.format("attachment;filename=%s.jpg", images.getName()));
            response.getOutputStream().write(images.getData());
        } catch (Exception e) {
            LOGGER.error("Could not download file {}", e.getLocalizedMessage());
        }
    }

    public List<FileDto> getAllFiles() {
        List<FileDto> result = new ArrayList<>();
        File[] listFile = Objects.requireNonNull(new File(getUploadFolder).listFiles());
        if (listFile.length > 0) {
            Stream.of(listFile).forEach(file -> {
                final FileDto f = new FileDto();
                f.setName(file.getName());
                f.setUploadedDate(file.lastModified());
                String[] fileName = file.getName().split("\\.");
                f.setExtension(fileName[fileName.length - 1].toLowerCase());
                try {
                    f.setSize(Files.size(Paths.get(file.getPath())));
                } catch (Exception ignored) {
                }
                f.setPath(file.getAbsolutePath().replaceAll("\\\\", "/"));
                result.add(f);
            });
        }
        return result;
    }

    public List<ImageDto> getAllImages() {
        return imagesRepository.findAll().stream().map(ImageMapper::toDto).collect(Collectors.toList());
    }

    public String getImageBase64(int index) {
        try {
            Images images = imagesRepository.findAll().get(index);
            return Utils.toBase64(images.getData());
        } catch (Exception e) {
            LOGGER.error("Could not get file {}", e.getLocalizedMessage());
            return null;
        }
    }

    @PostConstruct
    public void init() {
        root = Paths.get(getUploadFolder);
        try {
            if (Files.notExists(root)) {
                Files.createDirectory(root);
            }
        } catch (IOException e) {
            LOGGER.error("Could not initialize storage {}", e.getLocalizedMessage());
        }
    }

    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else throw new RuntimeException("Could not read file: " + filename);
        } catch (IOException e) {
            LOGGER.error("Could not load file {}", e.getLocalizedMessage());
            throw new RuntimeException("Error: " + e.getLocalizedMessage());
        }
    }

    public Boolean save(MultipartFile file) {
        try {
            Path targetPath = root.resolve(Objects.requireNonNull(file.getOriginalFilename()));
            if (Files.exists(targetPath)) {
                Files.delete(targetPath);
            }
            Files.copy(file.getInputStream(), root.resolve(Objects.requireNonNull(file.getOriginalFilename())));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("Could not save file {}", e.getLocalizedMessage());
            return false;
        }
    }

    public void uploadImage(MultipartFile file) {
        List<Images> result = new ArrayList<>();
        try {
            imagesRepository.save(ImageMapper.toEntity(file));
        } catch (Exception e) {
            LOGGER.error("Could not upload file {}", e.getLocalizedMessage());
        }
    }

    public List<ImageDto> uploadImages(List<MultipartFile> files) {
        try {
            List<Images> result = files.stream().map(ImageMapper::toEntity).collect(Collectors.toList());
            return imagesRepository.saveAll(result).stream().map(ImageMapper::toDto).collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error("Could not upload file {}", e.getLocalizedMessage());
        }
        return null;
    }
    public static final Logger LOGGER = LoggerFactory.getLogger(StorageService.class);
    private Path root = null;
    @Autowired
    private ImagesRepository imagesRepository;
    @Autowired
    private String getUploadFolder;
}
