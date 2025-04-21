package main;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UtilityTool {
    /*
    * Whenever you figure out a new convenient function/optimized methods, write that in this class
    * */

    // Optimized scaling functions, that prevents re-scaling every frame
    public BufferedImage scaledImage(BufferedImage original, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(original, 0, 0, width, height, null);
        g2.dispose();

        return scaledImage;
    }


}
