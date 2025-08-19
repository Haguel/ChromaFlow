package dev.haguel.media_processing_worker.media_processing_strategy.impl;

import dev.haguel.media_processing_worker.media_processing_strategy.MediaProcessingStrategy;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

@Component
public class GrayscaleMediaProcessingStrategyImpl implements MediaProcessingStrategy {
    @Override
    public BufferedImage apply(BufferedImage sourceImage) {
        BufferedImage grayscaleImage = new BufferedImage(
                sourceImage.getWidth(),
                sourceImage.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY
        );

        grayscaleImage.getGraphics().drawImage(sourceImage, 0, 0, null);

        return grayscaleImage;
    }
}