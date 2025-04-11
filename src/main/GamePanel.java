package main;

import entity.Enemy;
import entity.Entity;
import entity.Player;
import tile.Tile;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GamePanel extends JPanel implements Runnable {
    //================== Screen settings
    final int originalTileSize = 16; // 16x16 tile (size for all characters, blocks etc.)
    final int scale = 2; // 16x16 pixels for a character is small as fuck, so we gotta multiply that by 3 to make it's size more reasonable

    public final int tileSize = originalTileSize * scale; // final tile size
    final int maxScreenCol = 28;
    final int maxScreenRow = 16; // so it makes the ratio of 4 by 3
    // by multiplying max amount of cols/rows to tile size, we get our window resolution
    final int screenWidth = tileSize * maxScreenCol; // 768px
    final int screenHeight = tileSize * maxScreenRow; // 576px

    int FPS = 60;

    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;
    Player player = new Player(this, keyHandler);

    public Tile[] tileTypes; // Массив типов тайлов
    public int[][] worldMap; // Карта мира (индексы типов тайлов)
    public final int worldCols = 50; // Ширина мира
    public final int worldRows = 50; // Высота мира

    public Camera camera;
    public List<Entity> entities = new CopyOnWriteArrayList<>(); // Потокобезопасный список
    private ExecutorService aiExecutor = Executors.newFixedThreadPool(2); // Пул для ИИ


    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
        setupTiles();
        setupWorldMap();

        entities.add(player); // Игрок первая сущность
        entities.add(new Enemy(this));
        camera = new Camera(screenWidth, screenHeight, worldCols * tileSize, worldRows * tileSize);
    }

    private void setupTiles() {
        tileTypes = new Tile[2]; // Например, 2 типа: трава и стена
        try {
            tileTypes[0] = new Tile(ImageIO.read(getClass().getResourceAsStream("/tiles/grass.png")), false);
            tileTypes[1] = new Tile(ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png")), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupWorldMap() {
        worldMap = new int[worldCols][worldRows];

        // Пока что: заполняем травой, добавляем стены по краям
        for (int col = 0; col < worldCols; col++) {
            for (int row = 0; row < worldRows; row++) {
                if (col == 0 || col == worldCols - 1 || row == 0 || row == worldRows - 1) {
                    worldMap[col][row] = 1; // Стена
                } else {
                    worldMap[col][row] = 0; // Трава
                }
            }
        }
    }

    // use threads tu run game
    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() { // this creates a game thread // DO NOT TOUCH THIS FUNCTION
        double drawInterval = 1000000000.0 / FPS;
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

            if(delta >= 1){ // DO NOT WRITE ANYTHING ELSE HERE, update functions is below, so write there, not here
                update();
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
        stopGameThread(); // Гарантируем завершение при остановке
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int startCol = camera.getX() / tileSize;
        int endCol = Math.min(startCol + maxScreenCol + 1, worldCols);
        int startRow = camera.getY() / tileSize;
        int endRow = Math.min(startRow + maxScreenRow + 1, worldRows);

        for (int col = 0; col < worldCols; col++) {
            for (int row = 0; row < worldRows; row++) {
                int x = col * tileSize - camera.getX();
                int y = row * tileSize - camera.getY();
                g2.drawImage(tileTypes[worldMap[col][row]].image, x, y, tileSize, tileSize, null);
            }
        }

        for (Entity e : entities) {
            int screenX = e.x - camera.getX();
            int screenY = e.y - camera.getY();
            if (screenX > -tileSize && screenX < screenWidth && screenY > -tileSize && screenY < screenHeight) {
                e.draw(g2, screenX, screenY);
            }
        }

        g2.dispose();
    }

    // Everything that's gonna happen every damn frame, write here
    public void update() {
        // Обновляем игрока в основном потоке
        player.update();
        // Обновляем ИИ врагов в пуле потоков
        for (Entity e : entities) {
            if (e != player) { // Пропускаем игрока
                aiExecutor.submit(e::update);
            }
        }
        // Обновляем камеру
        camera.update(player);
    }


    public void stopGameThread() {
        gameThread = null;
        try {
            aiExecutor.shutdown();
            aiExecutor.awaitTermination(1, java.util.concurrent.TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



}


