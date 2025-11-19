package entity;

import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

// Herda de Entity (que agora é abstrata)
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

    public boolean isTakingDamage() {
        return takingDamage;
    }
    public int getDamageFlashCounter() {
        return damageFlashCounter;
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
        setLife(2);
        setOriginalSpeed(getSpeed());
    }

    @Override
    public void update(){
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
                setSpeed(originalSpeed / 2); // Reduz velocidade pela metade
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
                // Se chegou na base, reinicia
                setX(gp.getScreenWidth()); // Apenas para evitar bugs visuais se não for destruído
            }
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        BufferedImage image = getDown1();
        if (image != null) {
            if (takingDamage) {
                if ((damageFlashCounter / 3) % 2 == 0) {
                    g2.drawImage(image, getX(), getY(), gp.getTileSize(), gp.getTileSize(), null);
                }
            } else {
                if(isSlowed){
                    g2.setColor(new Color(0,100,255,50));
                }
                g2.drawImage(image, getX(), getY(), gp.getTileSize(), gp.getTileSize(), null);
                if(isSlowed){
                    g2.fillOval(getX(),getY()+20,32,10);
                }
            }
        }
    }

}