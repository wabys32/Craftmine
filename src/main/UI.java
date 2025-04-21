package main;

import object.OBJ_Protein;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UI {
    GamePanel gamePanel;
    Font arial_50;
    BufferedImage proteinImage;

    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;

    public UI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        arial_50 = new Font("Arial", Font.BOLD, 50);
        OBJ_Protein protein = new OBJ_Protein(gamePanel);
        proteinImage = protein.image;
    }

    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }

    public void draw(Graphics2D g2){
        g2.setFont(arial_50);
        g2.setColor(Color.white);
        g2.drawString(gamePanel.player.proteinsDrank+"", 90, 70);
        g2.drawImage(proteinImage, 20, 20, gamePanel.tileSize*2, gamePanel.tileSize*2, null);

        if(messageOn){
            g2.setFont(g2.getFont().deriveFont(30f));
            g2.drawString(message, gamePanel.tileSize/2, gamePanel.maxScreenRow*gamePanel.tileSize/2);

            messageCounter++;

            if(messageCounter > 60){ // message disappears after 2 seconds (2 60-frame frames)
                messageCounter = 0;
                messageOn = false;
            }
        }
    }
}
