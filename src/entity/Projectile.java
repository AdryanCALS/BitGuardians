package entity;

import main.GamePanel;

import java.awt.*;


public class Projectile extends Entity{
    GamePanel gamePanel;

    public Projectile(GamePanel gamePanel){
        this.gamePanel = gamePanel;
        setLife(1);
    }

    public void set(int x, int y, String direction){
        setX(x);
        setY(y);
        setDirection(direction);
        setSpeed(5);
        setSolidArea(new Rectangle(0,0,gamePanel.getTileSize()/2, gamePanel.getTileSize()/2));
    }

    @Override
    public void update(){
        switch (getDirection()){
            case "up":
                setY(getY()-getSpeed());
                break;
            case "down":
                setY(getY()+getSpeed());
                break;
            case "left":
                setX(getX()-getSpeed());
                break;
            case "right":
                setX(getX()+getSpeed());
                break;
        }

        if(getX()<0 || getX()>gamePanel.getScreenWidth()||getY()<0||getY()> gamePanel.getScreenHeight()){
            setLife(0);
        }

        int monsterIndex = gamePanel.getCollisionCheck().checkEntity(this, gamePanel.getWaveManager().getActiveMonsters());
        if(monsterIndex != -1){
            gamePanel.getWaveManager().damageMonster(monsterIndex, 1);//causa só 1 de dano
            setLife(0);
        }
    }


    @Override
    public void draw(Graphics2D graphics2D){
        graphics2D.setColor(new Color(150,0,255));
        graphics2D.fillOval(getX(),getY(),getSolidArea().width, getSolidArea().height);
    }

}