package entity;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;
import object.SuperObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity{
    // create variables of the following classes, to use their properties
    KeyHandler keyHandler;

    // I have absolutely no damn idea what that is
    public final int screenX;
    public final int screenY;

    // Player spawn point
    public final int[] spawnPoint = {10,10};

    // Player stats
    public int proteinsDrank = 0;
    public int strength = 10;
    public float speedMultiplier = 1;
    public int health = 100;

    // Barbell settings
    private boolean pumpingBarbell = false;
    private int currentBarbellFrame;
    private int barbellAnimationFrames = 3;
    private BufferedImage[] barbellAnimations = new BufferedImage[barbellAnimationFrames];

    // This will be a reference to the last barbell stand that has been used (to put player in the middle of it, when pumping)
    private SuperObject lastObject;

    // Combat
    public float distanceToAttack = 45f;
    public boolean hit = false;
    public float hit_timer = 0;
    public float hit_time = 12f;
    public int damage = 25;


    // Player constructor
    public Player(GamePanel gamePanel, KeyHandler keyHandler) {
        super(gamePanel);
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
        // Basic Animations
        for(int i = 0; i < rightAnimationFrames; ++i){
            rightAnimations[i] = setup("/player/runRight"+(i+1));
        }
        for(int i = 0; i < leftAnimationFrames; ++i){
            leftAnimations[i] = setup("/player/runLeft"+(i+1));
        }
        for(int i = 0; i < downAnimationFrames; ++i){
            downAnimations[i] = setup("/player/runDown"+(i+1));
        }
        for(int i = 0; i < upAnimationFrames; ++i){
            upAnimations[i] = setup("/player/runUp"+(i+1));
        }

        hitAnimations[0] = setup("/player/hitTop");
        hitAnimations[1] = setup("/player/hitBottom");
        hitAnimations[2] = setup("/player/hitLeft");
        hitAnimations[3] = setup("/player/hitRight");

        idle = setup("/player/idle");
        idleUp = setup("/player/idleUp");

        // Special Animations
        for(int i = 0; i < barbellAnimationFrames; ++i){
            barbellAnimations[i] = setup("/player/barbell"+(i+1));
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

        // Check tiles collision
        collisionOn = false;
        gamePanel.cChecker.checkTile(this); //pass this(entity) class to the collision checker class

        // Check object collision
        int objIndex = gamePanel.cChecker.checkObject(this, true);
        pickUpObject(objIndex);

        // Check NPC collision
        int npcIndex = gamePanel.cChecker.checkEntity(this, gamePanel.npc);
        interactWithNPC(npcIndex);


        // Move Player
        if(!collisionOn){
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
            if(!keyHandler.upPressed && !keyHandler.downPressed && !keyHandler.leftPressed && !keyHandler.rightPressed){
                if(direction == "up")
                    direction = "idleUp";
                if(direction == "down")
                    direction = "idleDown";
                if(direction == "left")
                    direction = "idleLeft";
                if(direction == "right")
                    direction = "idleRight";
            }
        }

        // Barbell
        if(keyHandler.ePressed && pumpingBarbell){
            collisionOn = true;
            direction = "barbell";
            worldX = lastObject.worldX;
            worldY = lastObject.worldY;
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
            currentBarbellFrame = (currentBarbellFrame + 1) % barbellAnimationFrames;
            spriteCounter = 0;
        }
    }

    // OBJECT COLLISION HANDLER
    public void pickUpObject(int i){
        if(i != 999){
            String objectName = gamePanel.obj[i].name;
            switch(objectName){
                case "Dumbbell":
                    pumpingBarbell = true;
                    lastObject = gamePanel.obj[i];
                    break;
                case "Kettlebell":
                    break;
                case "Protein":
                    gamePanel.playSoundEffect(0); // play pick up sound
                    proteinsDrank++;
                    gamePanel.obj[i] = null; // delete object, after picking it up
                    gamePanel.ui.showMessage("+Power");
                    System.out.println("Drank protein, current power: " + proteinsDrank);
                    break;
                case "Barbell Stand":
                    pumpingBarbell = true;
                    lastObject = gamePanel.obj[i];
                    break;
            }
        }else{
            pumpingBarbell = false;
        }
    }

    // Interact with NPC
    public void interactWithNPC(int i){
        if(i != 999){
            // Interacting with NPC
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
            case "barbell":
                image = barbellAnimations[currentBarbellFrame];
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

        g2.drawImage(image, screenX, screenY, null); // drawing that nigga
    }

    public void Hit(){
        for(int i = 0; i < gamePanel.npc.length; i++) {
            if(gamePanel.npc[i] != null){
                float distanceToNPC = (float) Math.sqrt( Math.pow(Math.abs(gamePanel.npc[i].worldX-worldX),2) + Math.pow(Math.abs(gamePanel.npc[i].worldY-worldY),2));
                if(distanceToNPC <= distanceToAttack){
                    gamePanel.npc[i].health -= damage;
                    System.out.println("Hit enemy, health: " + gamePanel.npc[i].health);
                    if(gamePanel.npc[i].health <= 0){
                        System.out.println("Enemy ded");
                        gamePanel.npc[i].enemy_index = i;
                        //gamePanel.npc[i] = null;
                    }
                }
            }
        }
        hit = true;
    }
}
