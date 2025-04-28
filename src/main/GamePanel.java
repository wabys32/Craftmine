package main;

import entity.Entity;
import entity.Player;
import object.SuperObject;
import tile.TileManager;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

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
    public final boolean setUpGame = false;
    public int gameState;
    public final int menuState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int introState = 3;


    // Function to create game panel
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); // better for rendering performance
        // Key input handler
        this.addKeyListener(keyHandler);
        this.setFocusable(true); // so that GamePanel can be focused to receive key input

        // Mouse pressed event listener
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Handle mouse down event
                if (gameState == menuState) {
                    int x = e.getX();
                    int y = e.getY();
                    // Check for button intersections on mouse down
                    checkIntersection(x, y, 335, 200, ui.startButton, ui::runGame); // Play button
                    checkIntersection(x, y, 30, 420, ui.exitButton, ui::exitButtonPressed); // Exit button
                }
                if(gameState == playState) {
                    if(player.direction != "barbell"){
                        player.Hit();
                    }
                }
            }
        });

        // Mouse move event listener (to check ui elements hover)
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (gameState == menuState) {
                    int x = e.getX();
                    int y = e.getY();
                    exitButtonHovered = checkHover(x, y, 30, 420, ui.exitButton, exitButtonHovered, ui::exitButtonDown, ui::exitButtonUp);
                    playButtonHovered = checkHover(x, y, 335, 200, ui.startButton, playButtonHovered, ui::playButtonDown, ui::playButtonUp);
                }
            }
        });
    }

    // Function that checks intersection with buttons with mouse click
    public void checkIntersection(int mouseX, int mouseY, int positionX, int positionY, BufferedImage button, Runnable action){
        if(mouseX >= positionX && mouseY >= positionY && mouseX <= positionX + button.getWidth() && mouseY <= positionY + button.getHeight()) {
            action.run();
        }
    }

    private boolean exitButtonHovered = false;
    private boolean playButtonHovered = false;

    // Function that checks if some ui element is hovered or not
    public boolean checkHover(int mouseX, int mouseY, int positionX, int positionY, BufferedImage button, boolean buttonHoverState, Runnable actionHover, Runnable actionOutOfHover){
        if(mouseX >= positionX && mouseY >= positionY && mouseX <= positionX + button.getWidth() && mouseY <= positionY + button.getHeight()) {
            if(!buttonHoverState) {
                //System.out.println("Hover");
                actionHover.run();
                buttonHoverState = true;
            }
        }else{
            if(buttonHoverState) {
                //System.out.println("Not hover");
                actionOutOfHover.run();
                buttonHoverState = false;
            }
        }
        return buttonHoverState;
    }

    public void setUpGame(){
        gameState = playState;

        aSetter.setObject(); // spawn all objects
        aSetter.setNPC(); // spawn all NPCs

        //play music here
        //music.setFile(1);
        //music.loop();
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
                try {
                    Update();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
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
        // Title screen
        if(gameState == introState || gameState == menuState){
            ui.draw(g2);
        }else{
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
        }

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

    public void Update() throws InterruptedException {
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
        if(gameState == introState){
            TimeUnit.SECONDS.sleep((long) 1f);
            gameState = menuState;

        }
    }
}


