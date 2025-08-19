package dev.haguel.media_processing_worker.service.impl;

import org.springframework.stereotype.Service;
import dev.haguel.media_processing_worker.service.StorageService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FakeStorageService implements StorageService {
    @Override
    public BufferedImage downloadImage(String fileKey) {
        String imageResourcePath = "images/test-image.jpg";
        try (InputStream imageInputStream = getClass().getClassLoader().getResourceAsStream(imageResourcePath)) {
            if (imageInputStream == null) throw new IOException("Resource not found: " + imageResourcePath);

            return ImageIO.read(imageInputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load test image", e);
        }
    }
}
