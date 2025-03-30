package main;

import javax.swing.JPanel;
import java.awt.*;

public class GamePanel extends JPanel {
    // Screen settings
    final int originalTileSize = 16; // 16x16 tile (size for all characters, blocks etc.)
    final int scale = 3; // 16x16 pixels for a character is small as fuck, so we gotta multiply that by 3 to make it's size more reasonable

    final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 16;
    final int maxScreenRow = 12; // so it makes the ratio of 4 by 3
    final int screenWidth = tileSize * maxScreenCol; // 768px
    final int screenHeight = tileSize * maxScreenRow; // 576 px

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); // better for rendering performance
    }

}
