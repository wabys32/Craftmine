package main;

import entity.Entity;

public class Camera {
    private int x, y; // Координаты верхнего левого угла камеры
    private final int screenWidth, screenHeight;
    private final int worldWidth, worldHeight;

    public Camera(int screenWidth, int screenHeight, int worldWidth, int worldHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.x = 0;
        this.y = 0;
    }

    public void update(Entity target) {
        x = target.x - screenWidth / 2;
        y = target.y - screenHeight / 2;
        x = Math.max(0, Math.min(x, worldWidth - screenWidth));
        y = Math.max(0, Math.min(y, worldHeight - screenHeight));
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}