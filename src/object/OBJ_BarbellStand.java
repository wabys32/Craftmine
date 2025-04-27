package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_BarbellStand extends SuperObject{
    GamePanel gamePanel;

    public OBJ_BarbellStand(GamePanel gamePanel){
        this.gamePanel = gamePanel;

        name = "Barbell Stand";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/barbell_stand.png"));
            uTool.scaledImage(image, gamePanel.tileSize, gamePanel.tileSize);
        }catch(IOException e){
            e.printStackTrace();
        }

        collision = false;
    }
}
