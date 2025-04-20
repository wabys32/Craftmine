package main;

import entity.Player;
import tile.TileManager;

import java.awt.*;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {
    //================== Screen settings
    final int originalTileSize = 16; // 16x16 tile (size for all characters, blocks etc.)
    final int scale = 2; // 16x16 pixels for a character is small as fuck, so we gotta multiply that by 3 to make it's size more reasonable

    public final int tileSize = originalTileSize * scale; // final tile size
    public final int maxScreenCol = 28;
    public final int maxScreenRow = 16; // so it makes the ratio of 4 by 3
    // by multiplying max amount of cols/rows to tile size, we get our window resolution
    public final int screenWidth = tileSize * maxScreenCol; // 768px
    public final int screenHeight = tileSize * maxScreenRow; // 576px

    // WORLD SETTINGS
    public final int maxWorldCol = 30; // World width
    public final int maxWorldRow = 30; // World height
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;


    int FPS = 60;

    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;
    public Player player = new Player(this, keyHandler);
    TileManager tileManager = new TileManager(this);
    public CollisionChecker cChecker = new CollisionChecker(this);


    // Function to create game panel
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); // better for rendering performance
        // Key input handler
        this.addKeyListener(keyHandler);
        this.setFocusable(true); // so that GamePanel can be focused to receive key input
    }

    // use threads to run the game
    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() { // this creates a game thread // DO NOT TOUCH THIS FUNCTION
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        // to display fps
        //long timer = 0;
        //int frames = 0;

        while(gameThread != null){
            //System.out.println("The game loop is running"); //to indicate that game is running
            /*
            Summary(how the game will work):
            It will constantly be running through the game loop, which consists of update and repaint functions.
            To draw smth we use repaint function, like it's our paint brush.
            Then we update, using Update() method.
            */
            // I REPEAT DO NOT TOUCH THIS GOD DAMN FUNCTION
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            //timer += (currentTime - lastTime); to display fps
            lastTime = currentTime;

            if(delta >= 1){ // DO NOT WRITE ANYTHING ELSE HERE, update functions is below, so write there, not here
                Update();
                repaint();
                delta--;
                //frames++; to display fps
            }

            /*if(timer >= 1000000000){ // to display fps
                System.out.println("FPS: " + frames);
                frames = 0;
                timer = 0;
            }*/
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g); // super means JPanel, because this GamePanel class is a subclass of JPanel
        Graphics2D g2 = (Graphics2D) g; // we change graphics g to graphics 2d graph, which has more functions

        // we first draw the map
        tileManager.draw(g2);
        // then we draw the player
        player.draw(g2);

        g2.dispose();
    }


    public void Update(){
        player.update();
    }
}


