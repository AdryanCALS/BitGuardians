package main;

import entity.Entity;

public class CollisionCheck {
    GamePanel gp;

    public CollisionCheck(GamePanel gp){
        this.gp = gp;
    }
    
    public void checkTile (Entity entity){

    int entityLeftX = entity.x + entity.solidArea.x;
    int entityRightX = entity.x + entity.solidArea.x + entity.solidArea.width;
    int entityTopY = entity.y + entity.solidArea.y;
    int entityBottomY = entity.y + entity.solidArea.y + entity.solidArea.height;

    int entityLeftCol = entityLeftX/gp.tileSize;
    int entityRightCol = entityRightX/gp.tileSize;
    int entityTopRow = entityTopY/gp.tileSize;
    int entityBottomRow = entityBottomY/gp.tileSize;

    int tileNum1, tileNum2;

    switch(entity.direction) {
    case "up":
        entityTopRow = (entityTopY - entity.speed)/gp.tileSize;
        tileNum1 = gp.tileManager.mapTileNum[entityLeftCol][entityTopRow];
        tileNum2 = gp.tileManager.mapTileNum[entityRightCol][entityTopRow]; 
            if(gp.tileManager.tile[tileNum1].colision == true || gp.tileManager.tile[tileNum2].colision == true) {
                entity.colisionON= true;
            }
            break;

    case "down":
        entityBottomRow = (entityBottomY + entity.speed)/gp.tileSize;
         tileNum1 = gp.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
            tileNum2 = gp.tileManager.mapTileNum[entityRightCol][entityBottomRow];
            if (gp.tileManager.tile[tileNum1].colision == true || gp.tileManager.tile[tileNum2].colision == true) {
                entity.colisionON = true;
            }
            break;
    case "left":
        entityLeftCol = (entityLeftX - entity.speed)/gp.tileSize;
        tileNum1 = gp.tileManager.mapTileNum[entityLeftCol][entityTopRow];
            tileNum2 = gp.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
            if (gp.tileManager.tile[tileNum1].colision == true || gp.tileManager.tile[tileNum2].colision == true) {
                entity.colisionON = true;
            }
            break;
    case "right":
        entityRightCol = (entityRightX + entity.speed)/gp.tileSize;
        tileNum1 = gp.tileManager.mapTileNum[entityRightCol][entityTopRow];
            tileNum2 = gp.tileManager.mapTileNum[entityRightCol][entityBottomRow];
            if (gp.tileManager.tile[tileNum1].colision == true || gp.tileManager.tile[tileNum2].colision == true) {
                entity.colisionON = true;
    }
            break;
}
    }
}

