package main;

import object.OBJ_Protein;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class UI {
    Graphics2D g2;
    GamePanel gamePanel;
    Font arial_50;
    BufferedImage proteinImage;
    BufferedImage bicepsImage;
    BufferedImage speedImage;

    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;

    public UI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        arial_50 = new Font("Arial", Font.BOLD, 45);
        OBJ_Protein protein = new OBJ_Protein(gamePanel);
        proteinImage = protein.image;

        try{
            bicepsImage = ImageIO.read(getClass().getResourceAsStream("/objects/biceps.png"));
        }catch(Exception e){
            e.printStackTrace();
        }

        try{
            speedImage = ImageIO.read(getClass().getResourceAsStream("/objects/energy.png"));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }

    public void draw(Graphics2D g2){
        this.g2 = g2;
        g2.setFont(arial_50);
        g2.setColor(Color.BLACK);

        g2.drawString(gamePanel.player.proteinsDrank+"", 90, 63); // strength text
        g2.drawString("0", 90, 128); // speed text

        g2.drawImage(bicepsImage, 20, 20, 48, 48, null);
        g2.drawImage(speedImage, 20, 90, 48, 48, null);

        if(messageOn){
            g2.setFont(g2.getFont().deriveFont(30f));
            g2.drawString(message, gamePanel.tileSize/2, gamePanel.maxScreenRow*gamePanel.tileSize/2);

            // Message time
            messageCounter++;
            if(messageCounter > 60){ // message disappears after 2 seconds (2 60-frame frames)
                messageCounter = 0;
                messageOn = false;
            }
        }

        g2.setFont(arial_50);
        g2.setColor(Color.yellow);
        if(gamePanel.gameState == gamePanel.playState){
            // do playState stuff here
        }
        if(gamePanel.gameState == gamePanel.pauseState){
            drawPauseScreen();
        }
    }

    public void drawPauseScreen() {
        String text = "PAUSED";
        int x = getXforCenteredText(text);
        int y = gamePanel.screenHeight/2;

        g2.drawString(text, x, y);
    }

    public int getXforCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text,g2).getWidth();
        int x = gamePanel.screenWidth/2 - length/2;
        return x;
    }
}
