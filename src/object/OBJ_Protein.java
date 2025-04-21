package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Protein extends SuperObject{
    GamePanel gamePanel;

    public OBJ_Protein(GamePanel gamePanel){
        this.gamePanel = gamePanel;

        name = "Protein";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/protein.png"));
            uTool.scaledImage(image, gamePanel.tileSize, gamePanel.tileSize);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
