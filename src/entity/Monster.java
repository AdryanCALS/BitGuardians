package entity;

import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Monster extends Entity{
    private GamePanel gp;

    public Monster(GamePanel gp, int startX, int startY){
        this.gp = gp;
        setDefaultValues(startX, startY);
        getMonsterImage();
    }



    public void getMonsterImage() {
        try{
            setDown1(ImageIO.read(getClass().getResourceAsStream("/res/monster/MONdown1.png")));
        }catch(IOException e) {
            e.printStackTrace();
        }

    }
    public void setDefaultValues(int startX, int startY){
        setX(startX);
        setY(startY);
        setSpeed(2);
    }
    public void update(){
        if (getX() > 0) {
            setX(getX() - getSpeed());
        } else{
            setX(0);
            setSpeed(0);
        }

    }

    public void draw(Graphics2D g2){
        BufferedImage image = getDown1();
        if (image != null){
            g2.drawImage(image, getX(), getY(), gp.getTileSize(), gp.getTileSize(), null);
        }

    }
}