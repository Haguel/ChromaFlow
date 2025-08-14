package dev.haguel.media_processing_worker.media_processing_strategy;

import java.awt.image.BufferedImage;

public interface MediaProcessingStrategy {
    BufferedImage apply(BufferedImage sourceImage);
}
