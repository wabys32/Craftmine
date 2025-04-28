package entity;

import main.GamePanel;

public class NPC_Van extends Entity{
    public NPC_Van(GamePanel gamePanel) {
        super(gamePanel);

        direction = "idle";
        speed = 1;
        getImage();

        distanceToAttack = 40f;
    }

    // load all animations sprites
    public void getImage(){
        for(int i = 0; i < rightAnimationFrames; ++i){
            rightAnimations[i] = setup("/npc/Van/van_runRight"+(i+1));
        }
        for(int i = 0; i < leftAnimationFrames; ++i){
            leftAnimations[i] = setup("/npc/Van/van_runLeft"+(i+1));
        }
        for(int i = 0; i < downAnimationFrames; ++i){
            downAnimations[i] = setup("/npc/Van/van_runDown"+(i+1));
        }
        for(int i = 0; i < upAnimationFrames; ++i){
            upAnimations[i] = setup("/npc/Van/van_runUp"+(i+1));
        }

        hitAnimations[0] = setup("/npc/Van/van_hitTop");
        hitAnimations[1] = setup("/npc/Van/van_hitBottom");
        hitAnimations[2] = setup("/npc/Van/van_hitLeft");
        hitAnimations[3] = setup("/npc/Van/van_hitRight");

        idle = setup("/npc/Van/van_idle");
        idleUp = setup("/npc/Van/van_idleUp");
        ded = setup("/npc/Van/van_ded");
    }

    public void setAction(){
        combat();

        /*actionLockCounter++;
        if(actionLockCounter >= 120){
            Random random = new Random();
            int i = random.nextInt(125)+1; // random num from 1 to 100

            if(i <= 25){
                direction = "up"; // up
                current_moving_directions[0] = 1;
                current_moving_directions[1] = 0;
                current_moving_directions[2] = 0;
                current_moving_directions[3] = 0;
            }
            if(i > 25 && i <= 50){
                direction = "down"; // down
                current_moving_directions[0] = 0;
                current_moving_directions[1] = 1;
                current_moving_directions[2] = 0;
                current_moving_directions[3] = 0;
            }
            if(i > 50 && i <= 75){
                direction = "left"; // left
                current_moving_directions[0] = 0;
                current_moving_directions[1] = 0;
                current_moving_directions[2] = 1;
                current_moving_directions[3] = 0;
            }
            if(i > 75 && i <= 100){
                direction = "right"; // right
                current_moving_directions[0] = 0;
                current_moving_directions[1] = 0;
                current_moving_directions[2] = 0;
                current_moving_directions[3] = 1;
            }
            if(i > 100 && i <= 125){
                direction = "idle";
                current_moving_directions[0] = 0;
                current_moving_directions[1] = 0;
                current_moving_directions[2] = 0;
                current_moving_directions[3] = 0;
            }

            actionLockCounter = 0;
        }*/
    }
}
