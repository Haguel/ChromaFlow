package dev.haguel.media_processing_worker.media_processing_strategy.impl;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

class GrayscaleMediaProcessingStrategyImplTest {
    @Test
    void givenColorImage_whenApply_thenPixelIsGrayscale() {
        // Arrange
        BufferedImage color1x1Image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        color1x1Image.setRGB(0, 0, Color.RED.getRGB());
        GrayscaleMediaProcessingStrategyImpl strategy = new GrayscaleMediaProcessingStrategyImpl();

        // Act
        BufferedImage grayscaleImage = strategy.apply(color1x1Image);

        // Assert
        int pixelRGB = grayscaleImage.getRGB(0, 0);
        Color pixelColor = new Color(pixelRGB);
        assertEquals(pixelColor.getRed(), pixelColor.getGreen());
        assertEquals(pixelColor.getGreen(), pixelColor.getBlue());
    }
}