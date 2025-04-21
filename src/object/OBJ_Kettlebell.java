package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Kettlebell extends SuperObject{
    GamePanel gamePanel;

    public OBJ_Kettlebell(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        name = "Kettlebell";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/kettlebell.png"));
            uTool.scaledImage(image, gamePanel.tileSize, gamePanel.tileSize);
        }catch(IOException e){
            e.printStackTrace();
        }

        collision = true;
    }
}
