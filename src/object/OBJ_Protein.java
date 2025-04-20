package object;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Protein extends SuperObject{
    public OBJ_Protein(){
        name = "Dumbbell";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/protein.png"));
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
