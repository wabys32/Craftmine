package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class Entity {
    GamePanel gamePanel;
    public int worldX, worldY;
    public int speed;
    public int health = 100;

    // Let's leave this shit as it is
    public int rightAnimationFrames = 2;
    public BufferedImage[] rightAnimations = new BufferedImage[rightAnimationFrames];
    public int leftAnimationFrames = 2;
    public BufferedImage[] leftAnimations = new BufferedImage[leftAnimationFrames];
    public int upAnimationFrames = 2;
    public BufferedImage[] upAnimations = new BufferedImage[upAnimationFrames];
    public int downAnimationFrames = 2;
    public BufferedImage[] downAnimations = new BufferedImage[downAnimationFrames];
    public BufferedImage[] hitAnimations = new BufferedImage[4];
    public BufferedImage idle;
    public BufferedImage idleUp;
    public BufferedImage ded;

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

    // Combat
    public int damage = 10;
    public float distanceToPlayer;
    public float distanceToAttack = 40f;

    private float currentAttackTime = 0f; // first timer
    private float attack_timer = 0f; // second timer
    private float timeToAttack = 2f; // time to attack (in seconds)
    Random rand = new Random();

    public boolean hit = false;
    public float hit_timer = 0;
    public float hit_time = 12f;


    public Entity(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        timeToAttack = rand.nextFloat(0.2f, 1.5f);
        System.out.println(timeToAttack);
    }

    public void setAction(){} // already declared in children's classes

    public void update(){
        setAction();

        collisionOn = false;
        gamePanel.cChecker.checkTile(this);
        if(!collisionOn && distanceToPlayer > distanceToAttack){

            int xDiff = worldX - gamePanel.player.worldX;
            int yDiff = worldY - gamePanel.player.worldY;

            if (Math.abs(xDiff) >= Math.abs(yDiff)) {
                // Move horizontally ONLY
                if (xDiff > 0) {
                    worldX -= speed;
                } else if (xDiff < 0) {
                    worldX += speed;
                }
            } else {
                // Move vertically ONLY
                if (yDiff > 0) {
                    worldY -= speed;
                } else if (yDiff < 0) {
                    worldY += speed;
                }
            }

            if(Math.abs(xDiff)+1 >= Math.abs(yDiff)){
                if (xDiff > 0) {
                    direction = "left";
                } else{
                    direction = "right";
                }
            }else{
                if (yDiff > 0) {
                    direction = "up";
                } else {
                    direction = "down";
                }
            }



            /*switch(direction){
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
            }*/
        }
    }

    private int ded_timer = 0;
    private int ded_time = 60;
    public int enemy_index = 999;

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

            if(hit){
                if(hit_timer < hit_time){
                    speed = 2;
                    switch(direction){
                        case "left", "idleLeft":
                            image = hitAnimations[2];
                            break;
                        case "right", "idleRight":
                            image = hitAnimations[3];
                            break;
                        case "up", "idleUp":
                            image = hitAnimations[0];
                            break;
                        case "down", "idleDown":
                            image = hitAnimations[1];
                            break;
                    }
                    hit_timer++;
                }
                else{
                    hit = false;
                    hit_timer = 0;
                    speed = 3;
                }
            }

            if(health <= 0){
                ded_timer++;
                image = ded;
                speed = 0;
                if(ded_timer >= ded_time){
                    gamePanel.npc[enemy_index] = null;
                }
            }

            g2.drawImage(image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);
        }

    }

    // function that scales images (optimized)
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


    public void combat(){ // implement in enemies classes only
        distanceToPlayer = (float) Math.sqrt( Math.pow(Math.abs(worldX-gamePanel.player.worldX),2) + Math.pow(Math.abs(worldY-gamePanel.player.worldY),2) );
        if(distanceToPlayer <= distanceToAttack){
            currentAttackTime += 1;
            attack_timer = currentAttackTime / 60;

            if(attack_timer >= timeToAttack){
                // So we basically hit the player right here
                EnemyHit();

                timeToAttack = rand.nextFloat(0.2f, 1.5f);
                currentAttackTime = 0;
                attack_timer = 0;
            }
        }

    }

    public void EnemyHit(){
        gamePanel.player.health -= damage;
        this.hit = true;
        System.out.println("Hit player");
    }
}
