package entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity{
    // inherit properties from the following classes, to use their variables
    GamePanel gamePanel;
    KeyHandler keyHandler;

    // Player constructor
    public Player(GamePanel gamePanel, KeyHandler keyHandler) {
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues(){
        x = 100;
        y = 100;
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
    public void update() {
        int dx = 0;
        int dy = 0;

        if (keyHandler.upPressed) {
            direction = "up";
            dy -= 1;
        }
        if (keyHandler.downPressed) {
            direction = "down";
            dy += 1;
        }
        if (keyHandler.leftPressed) {
            direction = "left";
            dx -= 1;
        }
        if (keyHandler.rightPressed) {
            direction = "right";
            dx += 1;
        }
        if (dx == 0 && dy == 0) {
            direction = "idle";
            return;
        }

        // Нормализация
        double length = Math.sqrt(dx * dx + dy * dy);
        int nextX = x + (int) ((dx / length) * speed);
        int nextY = y + (int) ((dy / length) * speed);

        // Проверка коллизий
        int tileX = nextX / gamePanel.tileSize;
        int tileY = nextY / gamePanel.tileSize;
        if (tileX >= 0 && tileX < gamePanel.worldCols && tileY >= 0 && tileY < gamePanel.worldRows) {
            if (!gamePanel.tileTypes[gamePanel.worldMap[tileX][tileY]].isSolid) {
                x = nextX;
                y = nextY;
            }
        }
        // Обновление анимации
        spriteCounter++;
        if (spriteCounter > 12) {
            spriteNumbers[0] = (spriteNumbers[0] + 1) % rightAnimationFrames;
            spriteNumbers[1] = (spriteNumbers[1] + 1) % leftAnimationFrames;
            spriteNumbers[2] = (spriteNumbers[2] + 1) % upAnimationFrames;
            spriteNumbers[3] = (spriteNumbers[3] + 1) % downAnimationFrames;
            spriteCounter = 0;
        }
    }

    // Render function
    public void draw(Graphics2D g2, int screenX, int screenY) {
        BufferedImage image = null;
        switch (direction) {
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
        g2.drawImage(image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);
    }

    public void draw(Graphics2D g2) {
        draw(g2, x, y); // Для обратной совместимости
    }



}
