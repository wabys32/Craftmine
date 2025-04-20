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
    public final int[] spawnPoint = {10,10};

    int proteinsDrank = 0;


    // Player constructor
    public Player(GamePanel gamePanel, KeyHandler keyHandler) {
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;

        screenX = gamePanel.screenWidth/2 - (gamePanel.tileSize/2);
        screenY = gamePanel.screenHeight/2 - (gamePanel.tileSize/2);

        //solidArea = new Rectangle(0, 0, gamePanel.tileSize, gamePanel.tileSize); - would make collider's size the same as tile size
        //creating player's collider
        solidArea = new Rectangle(12, 12, 8, 14);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

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
        // change direction according to the key presses
        current_moving_directions[0] = keyHandler.upPressed ? 1 : 0;
        current_moving_directions[1] = keyHandler.downPressed ? 1 : 0;
        current_moving_directions[2] = keyHandler.leftPressed ? 1 : 0;
        current_moving_directions[3] = keyHandler.rightPressed ? 1 : 0;

        /*
        * 0 - top
        * 1 - down
        * 2 - left
        * 3 - right
        * */

        // Check collision
        collisionOn = false;
        gamePanel.cChecker.checkTile(this); //pass this(entity) class to the collision checker class

        // Check object collision
        int objIndex = gamePanel.cChecker.checkObject(this, true);
        pickUpObject(objIndex);

        if(collisionOn == false){
            if(keyHandler.upPressed){ // player moving up
                if(worldY/gamePanel.tileSize >= 1) // extra check for world borders
                    worldY -= speed;
                direction = "up";
            }
            if(keyHandler.downPressed){ // player moving down
                if(worldY/gamePanel.tileSize < gamePanel.maxWorldRow-2) // extra check for world borders
                    worldY += speed;
                direction = "down";
            }
            if(keyHandler.leftPressed){ // player moving left
                if(worldX/gamePanel.tileSize >= 1) // extra check for world borders
                    worldX -= speed;
                direction = "left";
            }
            if(keyHandler.rightPressed){ // player moving right
                if(worldX/gamePanel.tileSize < gamePanel.maxWorldCol-2) // extra check for world borders
                    worldX += speed;
                direction = "right";
            }
        }



        // Animate
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

    // Object collision Function
    public void pickUpObject(int i){
        if(i != 999){
            String objectName = gamePanel.obj[i].name;
            switch(objectName){
                case "Dumbbell":
                    break;
                case "Kettlebell":
                    break;
                case "Protein":
                    proteinsDrank++;
                    gamePanel.obj[i] = null; // delete object, after picking it up
                    System.out.println("Drank protein, current power: " + proteinsDrank);
                    break;
            }
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
