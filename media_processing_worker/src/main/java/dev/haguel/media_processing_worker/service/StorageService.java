package dev.haguel.media_processing_worker.service;

import java.awt.image.BufferedImage;

public interface StorageService {
    BufferedImage downloadImage(String fileKey);
}
