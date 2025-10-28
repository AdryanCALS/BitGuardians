package entity;

import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Monster extends Entity{
    private GamePanel gp;
    private boolean takingDamage = false;
    private int damageFlashCounter = 0;
    private final int damageFlashDuration = 20; // Duração do "hit stun" em frames (0.5s @ 60FPS)
    private int originalSpeed;

    public Monster(GamePanel gp, int startX, int startY){
        this.gp = gp;
        setDefaultValues(startX, startY);
        getMonsterImage();

        setSolidArea(new Rectangle(8,16,32,32));
        setLife(2);
    }

    public void takeDamage(int damage){
        setLife(getLife()-damage);

        if(getLife()>0){
            takingDamage = true;
            damageFlashCounter=0;
            setSpeed(0);//hit-stun
        }

        //provisorio enquanto não adicionamos moedas
        if(getLife() <=0 ){
            System.out.println("Monstro derrotado");
        }
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

        originalSpeed = getSpeed();
    }

    public void update(){
        if (takingDamage) {
            // Gerencia o estado "tomando dano"
            damageFlashCounter++;
            if (damageFlashCounter > damageFlashDuration) {
                takingDamage = false;
                damageFlashCounter = 0;
                setSpeed(originalSpeed); // Restaura a velocidade
            }
        } else {
            //movimento padrao
            if (getX() > 0) {
                setX(getX() - getSpeed());
            } else{
                setX(0);
                setSpeed(2);
            }
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = getDown1();
        if (image != null) {

            if (takingDamage) {
                // Lógica de piscar:
                // (damageFlashCounter / X) % 2 == 0
                // Isso faz: X frames ON, X frames OFF
                if ((damageFlashCounter / 3) % 2 == 0) {
                    g2.drawImage(image, getX(), getY(), gp.getTileSize(), gp.getTileSize(), null);
                }
                // Se for ímpar, não desenha nada (efeito de piscar)
            } else {
                // Desenho normal
                g2.drawImage(image, getX(), getY(), gp.getTileSize(), gp.getTileSize(), null);
            }
        }
    }

}