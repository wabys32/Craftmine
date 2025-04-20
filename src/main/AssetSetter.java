package main;

import object.OBJ_Dumbbell;
import object.OBJ_Kettlebell;
import object.OBJ_Protein;
import object.SuperObject;

// THIS CLASS IS FOR OBJECT PLACEMENT
public class AssetSetter {
    GamePanel gamePanel;

    public AssetSetter(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    private int currentObject = 0;

    public void setObject(){
        placeObject(9, 5, new OBJ_Kettlebell());
        placeObject(11, 11, new OBJ_Kettlebell());
        placeObject(14, 11, new OBJ_Protein());
        placeObject(18, 11, new OBJ_Protein());
        placeObject(18, 15, new OBJ_Protein());
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
}
