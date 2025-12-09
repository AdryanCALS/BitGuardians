package entity;

import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Monster extends Entity{
    protected GamePanel gp;
    private boolean takingDamage = false;
    private int damageFlashCounter = 0;
    private final int damageFlashDuration = 20;
    private int originalSpeed;
    private boolean isSlowed = false;
    private int slowCounter = 0;
    private final int slowDuration = 120; //podemos alterar depois.


    public Monster(GamePanel gp, int startX, int startY){
        this.gp = gp;
        setDefaultValues(startX, startY);
        getMonsterImage();

        setSolidArea(new Rectangle(8,16,32,32));
    }

    public void takeDamage(int damage) {
        setLife(getLife() - damage);

        if (getLife() > 0) {
            takingDamage = true;
            damageFlashCounter = 0;
            setSpeed(0);//hit-stun
        }
    }

    public void applySlow(){
        if(!isSlowed){
            isSlowed = true;
            slowCounter=0;
        }
    }

    protected void setOriginalSpeed(int originalSpeed) {
        this.originalSpeed = originalSpeed;
    }

    public void getMonsterImage() {
        try{
            setDown1(ImageIO.read(getClass().getResourceAsStream("/res/monster/MONdown1.png")));
            setDown2(ImageIO.read(getClass().getResourceAsStream("/res/monster/MONdown2.png")));
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void setDefaultValues(int startX, int startY){
        setX(startX);
        setY(startY);
        setSpeed(2);
        setLife(2);
        setOriginalSpeed(getSpeed());
    }

    @Override
    public void update(){

        setSpriteCounter(getSpriteCounter() + 1);
        if(getSpriteCounter() > 12) { // ajustar para mudar a velocidade da animação
            if(getSpriteNum() == 1) setSpriteNum(2);
            else if (getSpriteNum() == 2) setSpriteNum(1);
            setSpriteCounter(0);
        }

        if (takingDamage) {
            damageFlashCounter++;
            if (damageFlashCounter > damageFlashDuration) {
                takingDamage = false;
                damageFlashCounter = 0;
                if(isSlowed){
                    setSpeed(originalSpeed/2);
                }else{
                    setSpeed(originalSpeed);
                }
            }
        } else {
            if (isSlowed) {
                setSpeed(originalSpeed / 2); // reduz velocidade pela metade
                slowCounter++;
                if (slowCounter > slowDuration) {
                    isSlowed = false;
                    slowCounter = 0;
                    setSpeed(originalSpeed);
                }
            } else {
                setSpeed(originalSpeed);
            }

            if (getX() > 0) {
                setX(getX() - getSpeed());
            } else {
                setX(0);
            }
        }
    }

    @Override
    public void draw(Graphics2D g2) {

        BufferedImage image = null;
        if(getSpriteNum() ==1) image = getDown1();
        if(getSpriteNum() ==2) image = getDown2();

        if (image != null) {
            if (takingDamage) {
                if ((damageFlashCounter / 3) % 2 == 0) {
                    g2.drawImage(image, getX(), getY(), gp.getTileSize(), gp.getTileSize(), null);
                }
            } else {
                if(isSlowed){
                    g2.setColor(new Color(0,100,255,100));
                }
                g2.drawImage(image, getX(), getY(), gp.getTileSize(), gp.getTileSize(), null);
                if(isSlowed){
                    g2.fillOval(getX(),getY(),48,48);
                }
            }
        }
    }

}