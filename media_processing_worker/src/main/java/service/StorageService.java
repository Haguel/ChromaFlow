package service;

import java.awt.image.BufferedImage;

public interface StorageService {
    BufferedImage downloadImage(String fileKey);
}
