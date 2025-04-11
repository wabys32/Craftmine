package entity;

import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy extends Entity {
    GamePanel gamePanel;

    public Enemy(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        setDefaultValues();
        getEnemyImage();
    }

    public void setDefaultValues() {
        x = 200;
        y = 200;
        speed = 2;
        direction = "idle";
    }

    public void getEnemyImage() {
        // Загрузка заглушки для теста
        try {
            idle = gamePanel.tileTypes[1].image; // Используем траву как заглушку
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        // Пример ИИ: случайное движение
        if (Math.random() < 0.05) {
            direction = switch ((int) (Math.random() * 4)) {
                case 0 -> "up";
                case 1 -> "down";
                case 2 -> "left";
                case 3 -> "right";
                default -> "idle";
            };
        }

        int dx = 0, dy = 0;
        if (direction.equals("up")) dy -= speed;
        if (direction.equals("down")) dy += speed;
        if (direction.equals("left")) dx -= speed;
        if (direction.equals("right")) dx += speed;

        // Проверка коллизий
        int nextX = x + dx;
        int nextY = y + dy;
        int tileX = nextX / gamePanel.tileSize;
        int tileY = nextY / gamePanel.tileSize;
        if (tileX >= 0 && tileX < gamePanel.worldCols && tileY >= 0 && tileY < gamePanel.worldRows) {
            if (!gamePanel.tileTypes[gamePanel.worldMap[tileX][tileY]].isSolid) {
                x = nextX;
                y = nextY;
            }
        }
    }

    public void draw(Graphics2D g2, int screenX, int screenY) {
        g2.drawImage(idle, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);
    }

    public void draw(Graphics2D g2) {
        draw(g2, x, y);
    }
}