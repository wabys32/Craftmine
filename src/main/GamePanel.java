package main;

import java.awt.*;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {
    // Screen settings
    final int originalTileSize = 16; // 16x16 tile (size for all characters, blocks etc.)
    final int scale = 3; // 16x16 pixels for a character is small as fuck, so we gotta multiply that by 3 to make it's size more reasonable

    final int tileSize = originalTileSize * scale; // final tile size
    final int maxScreenCol = 16;
    final int maxScreenRow = 12; // so it makes the ratio of 4 by 3
    // by multiplying max amount of cols/rows to tile size, we get our window resolution
    final int screenWidth = tileSize * maxScreenCol; // 768px
    final int screenHeight = tileSize * maxScreenRow; // 576px

    int FPS = 60;
    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;

    // Set Player's default position
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;


    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); // better for rendering performance
        // Key input handler
        this.addKeyListener(keyHandler);
        this.setFocusable(true); // so that GamePanel can be focused to receive key input
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }
    @Override
    public void run() { // this creates game thread
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
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            //timer += (currentTime - lastTime); to display fps
            lastTime = currentTime;

            if(delta >= 1){
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

    public void Update(){
        if(keyHandler.upPressed == true){
            playerY -= playerSpeed;
        }else if(keyHandler.downPressed == true){
            playerY += playerSpeed;
        }else if(keyHandler.leftPressed == true){
            playerX -= playerSpeed;
        }else if(keyHandler.rightPressed == true){
            playerX += playerSpeed;
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g); // super means JPanel, because this GamePanel class is a subclass of JPanel
        Graphics2D g2 = (Graphics2D) g; // we change graphics g to graphics 2d graph, which has more functions
        g2.setColor(Color.white);
        g2.fillRect(playerX, playerY, tileSize, tileSize);
        g2.dispose();
    }
}


