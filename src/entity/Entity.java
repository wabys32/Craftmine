package entity;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {
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

    // Current direction of an entity
    public String direction;

    // These two variables are for sprite changing during the game, to make animation
    public int spriteCounter = 0;
    public int[] spriteNumbers = {1,1,1,1}; // right left top down

    public Rectangle solidArea;
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;

    // Another direction variable, don't ask why
    public final int[] current_moving_directions = {0, 0, 0, 0}; // checks current moving directions {top, down, left, right}
    // if player moves in the certain direction, for instance up, then the list above would be: {1, 0, 0, 0} etc.
}
