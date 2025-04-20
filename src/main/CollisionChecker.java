package main;

import entity.Entity;

public class CollisionChecker {
    GamePanel gamePanel;

    public CollisionChecker(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    // TILE COLLISION
    public void checkTile(Entity entity){
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX / gamePanel.tileSize;
        int entityRightCol = entityRightWorldX / gamePanel.tileSize;
        int entityTopRow = entityTopWorldY / gamePanel.tileSize;
        int entityBottomRow = entityBottomWorldY / gamePanel.tileSize;

        int tileNum1, tileNum2;

        if(entity.current_moving_directions[0] == 1){ // entity moving up
            entityTopRow = (int) ((entityTopWorldY - entity.speed) / gamePanel.tileSize);
            tileNum1 = gamePanel.tileManager.mapTileNum[entityLeftCol][entityTopRow];
            tileNum2 = gamePanel.tileManager.mapTileNum[entityRightCol][entityTopRow];
            if(gamePanel.tileManager.tiles[tileNum1].collision == true || gamePanel.tileManager.tiles[tileNum2].collision == true){
                entity.collisionOn = true;
            }
        }
        if(entity.current_moving_directions[1] == 1){ // entity moving down
            entityBottomRow = (int) ((entityBottomWorldY + entity.speed) / gamePanel.tileSize);
            tileNum1 = gamePanel.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
            tileNum2 = gamePanel.tileManager.mapTileNum[entityRightCol][entityBottomRow];
            if(gamePanel.tileManager.tiles[tileNum1].collision == true || gamePanel.tileManager.tiles[tileNum2].collision == true){
                entity.collisionOn = true;
            }
        }
        if(entity.current_moving_directions[2] == 1){ // entity moving left
            entityLeftCol = (int) ((entityLeftWorldX - entity.speed) / gamePanel.tileSize);
            tileNum1 = gamePanel.tileManager.mapTileNum[entityLeftCol][entityTopRow];
            tileNum2 = gamePanel.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
            if(gamePanel.tileManager.tiles[tileNum1].collision == true || gamePanel.tileManager.tiles[tileNum2].collision == true){
                entity.collisionOn = true;
            }
        }
        if(entity.current_moving_directions[3] == 1){ // entity moving right
            entityRightCol = (int) ((entityRightWorldX + entity.speed) / gamePanel.tileSize);
            tileNum1 = gamePanel.tileManager.mapTileNum[entityRightCol][entityTopRow];
            tileNum2 = gamePanel.tileManager.mapTileNum[entityRightCol][entityBottomRow];
            if(gamePanel.tileManager.tiles[tileNum1].collision == true || gamePanel.tileManager.tiles[tileNum2].collision == true){
                entity.collisionOn = true;
            }
        }
    }

    // OBJECT COLLISION
    public int checkObject(Entity entity, boolean player){
        int index = 999;

        for(int i = 0; i < gamePanel.obj.length; i++){
            if(gamePanel.obj[i] != null){
                // Get entity's solid area position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                gamePanel.obj[i].solidArea.x = gamePanel.obj[i].worldX + gamePanel.obj[i].solidArea.x;
                gamePanel.obj[i].solidArea.y = gamePanel.obj[i].worldY + gamePanel.obj[i].solidArea.y;

                if(entity.current_moving_directions[0] == 1){ // up
                    entity.solidArea.y -= entity.speed;
                }
                if(entity.current_moving_directions[1] == 1){ // down
                    entity.solidArea.y += entity.speed;
                }
                if(entity.current_moving_directions[2] == 1){ // left
                    entity.solidArea.x -= entity.speed;
                }
                if(entity.current_moving_directions[3] == 1){ // right
                    entity.solidArea.x += entity.speed;
                }

                if(entity.solidArea.intersects(gamePanel.obj[i].solidArea)){
                    if(gamePanel.obj[i].collision){
                        entity.collisionOn = true;
                    }
                    if(player){
                        index = i;
                    }
                }

                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gamePanel.obj[i].solidArea.x = gamePanel.obj[i].solidAreaDefaultX;
                gamePanel.obj[i].solidArea.y = gamePanel.obj[i].solidAreaDefaultY;
            }
        }
        return index;
    }

}
