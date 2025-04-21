package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Dumbbell extends SuperObject{
    GamePanel gamePanel;

    public OBJ_Dumbbell(GamePanel gamePanel){
        this.gamePanel = gamePanel;

        name = "Dumbbell";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/dumbbell.png"));
            uTool.scaledImage(image, gamePanel.tileSize, gamePanel.tileSize);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
