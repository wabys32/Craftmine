package object;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Kettlebell extends SuperObject{
    public OBJ_Kettlebell(){
        name = "Kettlebell";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/kettlebell.png"));
        }catch(IOException e){
            e.printStackTrace();
        }

        collision = true;
    }
}
