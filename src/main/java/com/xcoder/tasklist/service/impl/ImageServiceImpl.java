package com.xcoder.tasklist.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import com.xcoder.tasklist.domain.task.TaskImage;
import com.xcoder.tasklist.exception.ImageUpLoadException;
import com.xcoder.tasklist.properties.MinioProperties;
import com.xcoder.tasklist.service.ImageService;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    @Override
    public String upload(TaskImage image) {
        try {
            createBucket();
        } catch (Exception e) {
            throw new ImageUpLoadException("Image upload failed " + e.getMessage());
        }

        MultipartFile file = image.getFile();

        if (file.isEmpty() || file.getOriginalFilename() == null) {
            throw new ImageUpLoadException("Image must have name.");
        }

        String filename = generateFilename(file);
        InputStream inputStream;

        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            throw new ImageUpLoadException("Image upload failed " + e.getMessage());
        }

        saveImage(inputStream, filename);

        return filename;
    }

    private void saveImage(InputStream inputStream, String filename) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                                      .stream(inputStream, inputStream.available(), -1)
                                      .bucket(minioProperties.getBucket())
                                      .object(filename)
                                      .build());
        } catch (Exception e) {
            throw new ImageUpLoadException("Image save failed " + e.getMessage());
        }
    }

    private String generateFilename(MultipartFile file) {
        String extension = getExtension(file);
        return UUID.randomUUID() + "." + extension;
    }

    private String getExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null) {
            return null;
        }

        return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
    }

    private void createBucket() {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                                                         .bucket(minioProperties.getBucket())
                                                         .build());

            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                                           .bucket(minioProperties.getBucket())
                                           .build());
            }
        } catch (Exception e) {
            throw new ImageUpLoadException("Creating bucket failed: " + e.getMessage());
        }
    }
}
