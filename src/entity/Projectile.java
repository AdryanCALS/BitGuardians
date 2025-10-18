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
        setSpeed(8);//possível modificar depois se a gente quiser
        setSolidArea(new Rectangle(0,0,gamePanel.getTileSize()/2, gamePanel.getTileSize()/2));
    }

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
            setLife(0);//para matar o projetil se ele sair da tela
        }

        int monsterIndex = gamePanel.getCollisionCheck().checkEntity(this, gamePanel.getWaveManager().getActiveMonsters());
        if(monsterIndex != -1){
            gamePanel.getWaveManager().damageMonster(monsterIndex, 1);//causa só 1 de dano
            setLife(0);
        }
    }

    public void draw(Graphics2D graphics2D){
        graphics2D.setColor(new Color(150,0,255));
        graphics2D.fillOval(getX(),getY(),getSolidArea().width, getSolidArea().height);
    }

}
