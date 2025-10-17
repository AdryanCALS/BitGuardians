package main;

import entity.Entity;
import entity.Monster;
import entity.Player;

import java.awt.*;

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

    public int checkEntity(Entity entity, java.util.List<Monster> targets){
        int index = -1;

        for (int i=0;i<targets.size();i++){
            Monster target = targets.get(i);

            entity.getSolidArea().x = entity.getX() + entity.getSolidArea().x;
            entity.getSolidArea().y = entity.getY() + entity.getSolidArea().y;

            target.getSolidArea().x = target.getX() + target.getSolidArea().x;
            target.getSolidArea().y = target.getY() + target.getSolidArea().y;

            if(entity.getSolidArea().intersects(target.getSolidArea())){
                entity.setColisionON(true);
                index = i;
                break;
            }

            entity.getSolidArea().x = 8;
            entity.getSolidArea().y = 16;
            target.getSolidArea().x = 0;
            target.getSolidArea().y = 0;
        }
        return  index;
    }

    public Monster checkPlayerMonsterCollision(Player player, java.util.List<Monster> monsters) {
        for (Monster monster : monsters) {
            Rectangle playerArea = new Rectangle(
                    player.getX() + player.getSolidArea().x,
                    player.getY() + player.getSolidArea().y,
                    player.getSolidArea().width,
                    player.getSolidArea().height
            );
            Rectangle monsterArea = new Rectangle(
                    monster.getX() + monster.getSolidArea().x,
                    monster.getY() + monster.getSolidArea().y,
                    monster.getSolidArea().width,
                    monster.getSolidArea().height
            );

            if (playerArea.intersects(monsterArea)) {
                player.setColisionON(true);
                return monster; // Retorna o monstro com o qual colidiu
            }
        }
        return null; // Retorna null se não houver colisão
    }
}