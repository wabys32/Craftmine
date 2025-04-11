package tile;

import java.awt.image.BufferedImage;

public class Tile {

    public BufferedImage image;
    public boolean isSolid; // Можно ли пройти сквозь тайл

    public Tile(BufferedImage image, boolean isSolid) {
        this.image = image;
        this.isSolid = isSolid;
    }


}
