package main;

import java.awt.image.BufferedImage;

public class ImageData {
    /// This class is for storing image data, button image data in particular.
    /// This is later accessed in the GamePanel.java class to click on the buttons, by tracking button locations

    public BufferedImage image;
    public int posX;
    public int posY;
    public int width;
    public int height;

    public ImageData(BufferedImage image, int posX, int posY, int width, int height) {
        this.image = image;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
    }
}