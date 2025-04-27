package main;

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
        placeObject(9, 5, new OBJ_Dumbbell(gamePanel));
        placeObject(11, 11, new OBJ_Kettlebell(gamePanel));
        placeObject(14, 11, new OBJ_Protein(gamePanel));
        placeObject(18, 11, new OBJ_Protein(gamePanel));
        placeObject(18, 15, new OBJ_Protein(gamePanel));
        placeObject(18, 6, new OBJ_BarbellStand(gamePanel));
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

    public void setNPC(){
        gamePanel.npc[0] = new NPC_Van(gamePanel);
        // start location
        gamePanel.npc[0].worldX = gamePanel.tileSize * 10;
        gamePanel.npc[0].worldY = gamePanel.tileSize * 15;
    }

}
