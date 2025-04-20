package entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity{
    // create variables of the following classes, to use their properties
    GamePanel gamePanel;
    KeyHandler keyHandler;

    // I have absolutely no damn idea what that is
    public final int screenX;
    public final int screenY;

    // Player spawn point
    public final int[] spawnPoint = {2,2};

    // Player constructor
    public Player(GamePanel gamePanel, KeyHandler keyHandler) {
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;

        screenX = gamePanel.screenWidth/2 - (gamePanel.tileSize/2);
        screenY = gamePanel.screenHeight/2 - (gamePanel.tileSize/2);

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues(){ // Here's a spawn point of the player
        worldX = gamePanel.tileSize * spawnPoint[0];
        worldY = gamePanel.tileSize * spawnPoint[1];
        speed = 3;
        direction = "idle";
    }

    // load all animations sprites
    public void getPlayerImage(){
        try{
            for(int i = 0; i < rightAnimationFrames; ++i){
                rightAnimations[i] = ImageIO.read(getClass().getResourceAsStream("/player/runRight"+(i+1)+".png"));
            }
            for(int i = 0; i < leftAnimationFrames; ++i){
                leftAnimations[i] = ImageIO.read(getClass().getResourceAsStream("/player/runLeft"+(i+1)+".png"));
            }
            for(int i = 0; i < downAnimationFrames; ++i){
                downAnimations[i] = ImageIO.read(getClass().getResourceAsStream("/player/runDown"+(i+1)+".png"));
            }
            for(int i = 0; i < upAnimationFrames; ++i){
                upAnimations[i] = ImageIO.read(getClass().getResourceAsStream("/player/runUp"+(i+1)+".png"));
            }

            idle = ImageIO.read(getClass().getResourceAsStream("/player/idle.png"));

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    // update method for player only
    public void update(){
        int dx = 0;
        int dy = 0;

        // change direction according to the key presses
        if (keyHandler.upPressed) {
            direction = "up";
            worldY -= speed;
        }
        else if (keyHandler.downPressed) {
            direction = "down";
            worldY += speed;
        }
        else if (keyHandler.leftPressed) {
            direction = "left";
            worldX -= speed;
        }
        else if (keyHandler.rightPressed) {
            direction = "right";
            worldX += speed;
        }else {
            direction = "idle";
        }


        // change sprite over time (every 10 iterations (which is 1/6 second (10/60)))
        spriteCounter++;
        // 12 here is how fast the sprite changes. The higher the value, the slower it changes, and vice versa
        if(spriteCounter > 12){
            spriteNumbers[0] = (spriteNumbers[0] + 1) % rightAnimationFrames;
            spriteNumbers[1] = (spriteNumbers[1] + 1) % leftAnimationFrames;
            spriteNumbers[2] = (spriteNumbers[2] + 1) % upAnimationFrames;
            spriteNumbers[3] = (spriteNumbers[3] + 1) % downAnimationFrames;
            spriteCounter = 0;
        }
    }

    // Render function
    public void draw(Graphics2D g2){
        //g2.setColor(Color.white);
        //g2.fillRect(x, y, gamePanel.tileSize, gamePanel.tileSize);

        BufferedImage image = null;

        // render sprite, according to player's direction
        switch(direction){
            case "left":
                image = leftAnimations[spriteNumbers[1]];
                break;
            case "right":
                image = rightAnimations[spriteNumbers[0]];
                break;
            case "up":
                image = upAnimations[spriteNumbers[2]];
                break;
            case "down":
                image = downAnimations[spriteNumbers[3]];
                break;
            case "idle":
                image = idle;
                break;
        }
        g2.drawImage(image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null); // drawing that nigga
    }
}
