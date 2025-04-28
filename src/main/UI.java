package main;

import object.OBJ_Protein;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;


public class UI {
    Graphics2D g2;
    GamePanel gamePanel;
    Font arial_50;
    Font blackNorthFont;

    // Game
    BufferedImage proteinImage;
    BufferedImage bicepsImage;
    BufferedImage speedImage;
    BufferedImage heartImage;

    // Intro
    BufferedImage logoImage;

    // Menu
    BufferedImage menuText;
    BufferedImage startButton;
    BufferedImage exitButton;
    BufferedImage menuBackground;


    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;

    public UI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        arial_50 = new Font("Arial", Font.BOLD, 45);
        OBJ_Protein protein = new OBJ_Protein(gamePanel);
        proteinImage = protein.image;

        // Game
        bicepsImage = getImage("/objects/biceps.png");
        speedImage = getImage("/objects/energy.png");
        heartImage = getImage("/objects/heart.png");

        // Intro
        logoImage = getImage("/UI/logo.png");

        // Menu
        menuText = getImage("/UI/menu.png");
        startButton = getImage("/UI/play.png");
        exitButton = getImage("/UI/exit.png");
        menuBackground = getImage("/UI/Lockerroom.png");

    }

    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }

    public void draw(Graphics2D g2){
        this.g2 = g2;

        if(gamePanel.gameState == gamePanel.introState){
            drawIntroScreen();
        }

        if(gamePanel.gameState == gamePanel.menuState){
            drawMenuScreen();
        }

        if(gamePanel.gameState == gamePanel.playState){
            drawGameScreen();
        }
    }

    public void drawGameScreen(){
        g2.setFont(arial_50);
        g2.setColor(Color.BLACK);

        g2.drawString(gamePanel.player.strength*10+"", 90, 63); // strength text
        g2.drawString((int)(gamePanel.player.speedMultiplier*100)+"", 90, 128); // speed text
        g2.drawString(gamePanel.player.health+"", 740, 59); // speed text

        g2.drawImage(bicepsImage, 20, 20, 48, 48, null);
        g2.drawImage(speedImage, 20, 90, 48, 48, null);
        g2.drawImage(heartImage, 830, 20, 52, 48, null);

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

    public void drawIntroScreen(){
        g2.drawImage(logoImage, 50, 0, 197*4, 120*4, null);
        g2.setColor(Color.BLACK);
    }

    public void drawMenuScreen(){
        g2.drawImage(menuBackground, 0, 0, null);
        g2.drawImage(menuText, 287, 100, null);
        g2.drawImage(startButton, 335, 200, null);
        g2.drawImage(exitButton, 30, 420, null);


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

    public BufferedImage getImage(String path){
        BufferedImage image = null;
        try{
            image = ImageIO.read(getClass().getResourceAsStream(path));
        }catch(Exception e){
            e.printStackTrace();
        }
        return image;
    }


    /// Button actions methods ====================

    public void runGame(){
        gamePanel.gameState = gamePanel.playState;
        gamePanel.aSetter.setObject(); // spawn all objects
        gamePanel.aSetter.spawnNPCS(); // spawn all NPCs
    }

    public void exitButtonDown(){
        exitButton = getImage("/UI/exitPressed.png");
    }

    public void exitButtonUp(){
        exitButton = getImage("/UI/exit.png");
    }


    public void playButtonDown(){
        startButton = getImage("/UI/playHighlighted.png");
    }

    public void playButtonUp(){
        startButton = getImage("/UI/play.png");
    }

    public void exitButtonPressed(){
        System.exit(0);
    }
}
