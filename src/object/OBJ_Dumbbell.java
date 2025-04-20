package object;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Dumbbell extends SuperObject{
    public OBJ_Dumbbell(){
        name = "Dumbbell";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/dumbbell.png"));
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
