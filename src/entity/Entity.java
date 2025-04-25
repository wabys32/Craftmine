package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Entity {
    GamePanel gamePanel;
    public int worldX, worldY;
    public float speed;

    // Let's leave this shit as it is
    public int rightAnimationFrames = 2;
    public BufferedImage[] rightAnimations = new BufferedImage[rightAnimationFrames];
    public int leftAnimationFrames = 2;
    public BufferedImage[] leftAnimations = new BufferedImage[leftAnimationFrames];
    public int upAnimationFrames = 2;
    public BufferedImage[] upAnimations = new BufferedImage[upAnimationFrames];
    public int downAnimationFrames = 2;
    public BufferedImage[] downAnimations = new BufferedImage[downAnimationFrames];
    public BufferedImage idle;
    public BufferedImage idleUp;

    // Current direction of an entity
    public String direction = "idle";

    // These two variables are for sprite changing during the game, to make animation
    public int spriteCounter = 0;
    public int[] spriteNumbers = {1,1,1,1}; // right left top down

    public Rectangle solidArea = new Rectangle(0, 0, 32, 32);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;

    // Another direction variable, don't ask why
    public final int[] current_moving_directions = {0, 0, 0, 0}; // checks current moving directions {top, down, left, right}
    // if player moves in the certain direction, for instance up, then the list above would be: {1, 0, 0, 0} etc.

    public int actionLockCounter = 0;


    public Entity(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void setAction(){} // already declared in childrens' classes

    public void update(){
        setAction();

        collisionOn = false;
        gamePanel.cChecker.checkTile(this);
        gamePanel.cChecker.checkPlayer(this);

        if(!collisionOn){
            switch(direction){
                case "up":
                    if(worldY/gamePanel.tileSize >= 1) // extra check for world borders
                        worldY -= speed;
                    break;
                case "down":
                    if(worldY/gamePanel.tileSize < gamePanel.maxWorldRow-2) // extra check for world borders
                        worldY += speed;
                    break;
                case "left":
                    if(worldX/gamePanel.tileSize >= 1) // extra check for world borders
                        worldX -= speed;
                    break;
                case "right":
                    if(worldX/gamePanel.tileSize < gamePanel.maxWorldCol-2) // extra check for world borders
                        worldX += speed;
                    break;
                case "idle":
                    break;
            }
        }
    }

    public void draw(Graphics2D g2){
        BufferedImage image = null;

        int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
        int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;

        // That huge if statement is for occlusion culling
        if(worldX + gamePanel.tileSize > gamePanel.player.worldX - gamePanel.player.screenX &&
                worldX - gamePanel.tileSize < gamePanel.player.worldX + gamePanel.player.screenX &&
                worldY + gamePanel.tileSize > gamePanel.player.worldY - gamePanel.player.screenY &&
                worldY - gamePanel.tileSize < gamePanel.player.worldY + gamePanel.player.screenY){
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

            switch(direction) {
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
                case "idleUp":
                    image = idleUp;
                    break;
                case "idleDown":
                    image = idle;
                    break;
                case "idleLeft":
                    image = leftAnimations[1];
                    break;
                case "idleRight":
                    image = rightAnimations[1];
                    break;
                default:
                    image = idle;
                    break;
            }


            g2.drawImage(image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);
        }

    }

    public BufferedImage setup(String imagePath){
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try{
            image = ImageIO.read(getClass().getResourceAsStream(imagePath+".png"));
            image = uTool.scaledImage(image, gamePanel.tileSize, gamePanel.tileSize);
        }catch (IOException e){
            e.printStackTrace();
        }

        return image;
    }
}
