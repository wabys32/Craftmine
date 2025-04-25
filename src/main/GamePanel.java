package main;

import entity.Entity;
import entity.Player;
import object.SuperObject;
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
    public final int maxWorldCol = 40; // World width
    public final int maxWorldRow = 40; // World height

    int FPS = 60;

    TileManager tileManager = new TileManager(this);
    KeyHandler keyHandler = new KeyHandler(this);
    Sound sound = new Sound();
    Sound music = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public Player player = new Player(this, keyHandler);
    Thread gameThread;
    public UI ui = new UI(this);

    // Objects
    public SuperObject obj[] = new SuperObject[10]; // prep 10 slots for object (can create up to 10 object at a time)
    public AssetSetter aSetter = new AssetSetter(this); // class for object placement

    // Entities
    public Entity[] npc = new Entity[10];

    // Game state
    public int gameState;
    public final int playState = 1;
    public final int pauseState = 2;



    // Function to create game panel
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); // better for rendering performance
        // Key input handler
        this.addKeyListener(keyHandler);
        this.setFocusable(true); // so that GamePanel can be focused to receive key input
    }

    public void setUpGame(){
        aSetter.setObject(); // spawn all objects
        aSetter.setNPC(); // spawn all NPCs
        //play music here
        //music.setFile(1);
        //music.loop();

        gameState = playState;
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

        // Debug
        //long drawStart = 0;
        //drawStart = System.nanoTime();


        // we first draw the map
        tileManager.draw(g2);

        // then we draw objects
        for(int i = 0; i < obj.length; i++){
            if(obj[i] != null){
                obj[i].draw(g2, this);
            }
        }

        // draw npcs
        for(int i = 0; i < npc.length; i++){
            if(npc[i] != null){
                npc[i].draw(g2);
            }
        }

        // then we draw the player
        player.draw(g2);

        // UI's gotta be drawn above all layers
        ui.draw(g2);

        //Debug
        //long drawEnd = System.nanoTime();
        //long passed = drawEnd - drawStart;
        //System.out.println(passed);

        g2.dispose();
    }

    public void playMusic(int i){
        sound.setFile(i);
        sound.play();
        sound.loop();
    }

    public void stopMusic(){
        sound.stop();
    }

    public void playSoundEffect(int i){
        sound.setFile(i);
        sound.play();
    }

    public void Update(){
        if(gameState == playState){
            // Player
            player.update();
            // NPC
            for(int i = 0; i < npc.length; ++i){
                if(npc[i] != null){
                    npc[i].update();
                }
            }
        }
        if(gameState == pauseState){
            // noting for now
        }
    }
}


