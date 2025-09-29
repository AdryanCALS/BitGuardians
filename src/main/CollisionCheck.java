package main;

import entity.Entity;

public class CollisionCheck {
    private GamePanel gp;

    public CollisionCheck(GamePanel gp){
        this.gp = gp;
    }

    public void checkTile (Entity entity){

        int entityLeftX = entity.getX() + entity.getSolidArea().x;
        int entityRightX = entity.getX() + entity.getSolidArea().x + entity.getSolidArea().width;
        int entityTopY = entity.getY() + entity.getSolidArea().y;
        int entityBottomY = entity.getY() + entity.getSolidArea().y + entity.getSolidArea().height;

        int entityLeftCol = entityLeftX/gp.getTileSize();
        int entityRightCol = entityRightX/gp.getTileSize();
        int entityTopRow = entityTopY/gp.getTileSize();
        int entityBottomRow = entityBottomY/gp.getTileSize();

        int tileNum1, tileNum2;

        switch(entity.getDirection()) {
            case "up":
                entityTopRow = (entityTopY - entity.getSpeed())/gp.getTileSize();
                tileNum1 = gp.getTileManager().getMapTileNum()[entityLeftCol][entityTopRow];
                tileNum2 = gp.getTileManager().getMapTileNum()[entityRightCol][entityTopRow];
                if(gp.getTileManager().getTile()[tileNum1].isColision() || gp.getTileManager().getTile()[tileNum2].isColision()) {
                    entity.setColisionON(true);
                }
                break;

            case "down":
                entityBottomRow = (entityBottomY + entity.getSpeed())/gp.getTileSize();
                tileNum1 = gp.getTileManager().getMapTileNum()[entityLeftCol][entityBottomRow];
                tileNum2 = gp.getTileManager().getMapTileNum()[entityRightCol][entityBottomRow];
                if (gp.getTileManager().getTile()[tileNum1].isColision() || gp.getTileManager().getTile()[tileNum2].isColision()) {
                    entity.setColisionON(true);
                }
                break;
            case "left":
                entityLeftCol = (entityLeftX - entity.getSpeed())/gp.getTileSize();
                tileNum1 = gp.getTileManager().getMapTileNum()[entityLeftCol][entityTopRow];
                tileNum2 = gp.getTileManager().getMapTileNum()[entityLeftCol][entityBottomRow];
                if (gp.getTileManager().getTile()[tileNum1].isColision() || gp.getTileManager().getTile()[tileNum2].isColision()) {
                    entity.setColisionON(true);
                }
                break;
            case "right":
                entityRightCol = (entityRightX + entity.getSpeed())/gp.getTileSize();
                tileNum1 = gp.getTileManager().getMapTileNum()[entityRightCol][entityTopRow];
                tileNum2 = gp.getTileManager().getMapTileNum()[entityRightCol][entityBottomRow];
                if (gp.getTileManager().getTile()[tileNum1].isColision() || gp.getTileManager().getTile()[tileNum2].isColision()) {
                    entity.setColisionON(true);
                }
                break;
        }
    }
}