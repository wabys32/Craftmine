package main;

import entity.Entity;
import entity.NPC_Van;
import object.*;

// THIS CLASS IS FOR OBJECT PLACEMENT
public class AssetSetter {
    GamePanel gamePanel;

    public AssetSetter(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    private int currentObject = 0;

    public void setObject(){
        placeObject(14, 11, new OBJ_Protein(gamePanel));
        placeObject(18, 11, new OBJ_Protein(gamePanel));
        placeObject(18, 15, new OBJ_Protein(gamePanel));
        placeObject(18, 6, new OBJ_BarbellStand(gamePanel));
        placeObject(20, 6, new OBJ_BarbellStand(gamePanel));
        placeObject(22, 6, new OBJ_BarbellStand(gamePanel));
    }


    private void placeObject(int x, int y, SuperObject object){
        if(currentObject < gamePanel.obj.length){
            gamePanel.obj[currentObject] = object;
            gamePanel.obj[currentObject].worldX = x * gamePanel.tileSize;
            gamePanel.obj[currentObject].worldY = y * gamePanel.tileSize;
            currentObject++;
        }else{
            System.out.println("Cannot create any more objects");
        }
    }

    // method that spawns a bunch of NPCs (at the start of the game ig)
    public void spawnNPCS(){
        spawnNPC(20, 20, new NPC_Van(gamePanel));
        spawnNPC(30, 20, new NPC_Van(gamePanel));
    }

    // method to spawn single NPCs
    public void spawnNPC(int posX, int posY, Entity npc){
        for(int i = 0; i < gamePanel.npc.length; ++i){
            if(gamePanel.npc[i] == null){
                gamePanel.npc[i] = new NPC_Van(gamePanel);
                // start location
                gamePanel.npc[i].worldX = gamePanel.tileSize * posX;
                gamePanel.npc[i].worldY = gamePanel.tileSize * posY;
                System.out.println("Spawned NPC " + npc + " at " + posX + " " + posY);
                break;
            }

        }
    }
}
