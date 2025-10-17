package entity;

import main.GamePanel;
import main.KeyHandler;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;


import javax.imageio.ImageIO;

public class Player extends Entity {
    private int attackCounter = 0;
    private final int attackDuration = 15; //duração do ataque
    private Rectangle attackArea;
    private boolean attacking = false;
    private GamePanel gp;
    private KeyHandler keyH;


    public Player(GamePanel gp, KeyHandler keyH){
        this.gp = gp;
        this.keyH = keyH;

        attackArea = new Rectangle(0,0,0,0);//possivel modificar a área de ataque

        setDefaultValues();
        getPlayerImage();

        setSolidArea(new Rectangle(8, 16, 32, 32));
    }

    public void getPlayerImage() {
        try {
            setDown1(ImageIO.read(getClass().getResourceAsStream("/res/player/up1.png")));
            setDown2(ImageIO.read(getClass().getResourceAsStream("/res/player/up2.png")));
            setUp1(ImageIO.read(getClass().getResourceAsStream("/res/player/down1.png")));
            setUp2(ImageIO.read(getClass().getResourceAsStream("/res/player/down2.png")));
            setLeft1(ImageIO.read(getClass().getResourceAsStream("/res/player/left1.png")));
            setLeft2(ImageIO.read(getClass().getResourceAsStream("/res/player/left2.png")));
            setRight1(ImageIO.read(getClass().getResourceAsStream("/res/player/right1.png")));
            setRight2(ImageIO.read(getClass().getResourceAsStream("/res/player/right2.png")));
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
    public void setDefaultValues(){
        setX(100);
        setY(300);
        setSpeed(4);
        setDirection("down");
        setLife(5);//dependendo da classe de jogador a vida seria diferente
    }

    public void update(){
        if(attacking){
            attackCounter++;

            if(attackCounter >5 && attackCounter<10){
                int monsterIndex = gp.getCollisionCheck().checkEntity(this, gp.getWaveManager().getActiveMonsters());
                if(monsterIndex != -1){
                    gp.getWaveManager().damageMonster(monsterIndex, 1);
                    attacking = false;
                }
            }

            if(attackCounter>attackDuration){
                attackCounter=0;
                attacking=false;
            }

            return;
        }

        Monster collidingMonster = gp.getCollisionCheck().checkPlayerMonsterCollision(this, gp.getWaveManager().getActiveMonsters());
        if (collidingMonster != null) {
            // Se colidiu, aplica o empurrão e impede o movimento normal
            int knockbackSpeed = 5; // A "força" do empurrão
            int dx = getX() - collidingMonster.getX();
            int dy = getY() - collidingMonster.getY();

            // Normaliza o vetor para obter a direção
            double distance = Math.sqrt(dx*dx + dy*dy);
            double knockbackX = (dx / distance) * knockbackSpeed;
            double knockbackY = (dy / distance) * knockbackSpeed;

            setX(getX() + (int)knockbackX);
            setY(getY() + (int)knockbackY);
        }

        if(keyH.isUpPressed() || keyH.isDownPressed() || keyH.isLeftPressed() || keyH.isRightPressed()){
            if (keyH.isUpPressed()){
                setDirection("up");
            }
            else if (keyH.isDownPressed()){
                setDirection("down");
            }
            else if (keyH.isLeftPressed()){
                setDirection("left");
            }
            else if (keyH.isRightPressed()){
                setDirection("right");
            }

            // VERIFICA A COLISÃO COM TILES
            setColisionON(false);
            gp.getCollisionCheck().checkTile(this);

            //VERIFICA A COLISÃO COM MONSTROS
//            gp.getCollisionCheck().checkPlayerMonsterCollision(this, gp.getWaveManager().getActiveMonsters());

            // SE NÃO HOUVER COLISÃO, O JOGADOR PODE SE MOVER
            if (!isColisionON()){
                switch(getDirection()){
                    case "up":
                        // Adicionada verificação do limite superior da tela
                        if (getY() - getSpeed() >= 0) {
                            setY(getY() - getSpeed());
                        }
                        break;
                    case "down":
                        // Adicionada verificação do limite inferior da tela
                        if (getY() - getSpeed() <= gp.getScreenHeight() - gp.getTileSize()) {
                            setY(getY() + getSpeed());
                        }
                        break;
                    case "left":
                        // Adicionada verificação do limite esquerdo da tela
                        if (getX() - getSpeed() >= 0) {
                            setX(getX() - getSpeed());
                        }
                        break;
                    case "right":
                        // Adicionada verificação do limite direito da tela
                        if (getX() + getSpeed() <= gp.getScreenWidth() - gp.getTileSize()) {
                            setX(getX() + getSpeed());
                        }
                        break;
                }
            }

            // LÓGICA DE ANIMAÇÃO DO SPRITE
            setSpriteCounter(getSpriteCounter() + 1);
            if(getSpriteCounter() > 12){
                if(getSpriteNum() == 1){
                    setSpriteNum(2);
                } else if (getSpriteNum() == 2) {
                    setSpriteNum(1);
                }
                setSpriteCounter(0);
            }
        }

        if(keyH.isAttackPressed()){
            attack();
            keyH.setAttackPressed(false);
        }
    }

    public void attack(){
        int currentX = getX();
        int currentY = getY();
        int solidAreaWidth = getSolidArea().width;
        int solidAreaHeight = getSolidArea().height;

        switch (getDirection()){
            case "up":
                attackArea.x = currentX;
                attackArea.y = currentY - solidAreaHeight;
                break;
            case "down":
                attackArea.x = currentX;
                attackArea.y = currentY + solidAreaHeight;
                break;
            case "left":
                attackArea.x = currentX - solidAreaWidth;
                attackArea.y = currentY;
                break;
            case "right":
                attackArea.x = currentX + solidAreaWidth;
                attackArea.y = currentY;
                break;
        }
        attackArea.width = solidAreaWidth;
        attackArea.height = solidAreaHeight;
    }

    public void takeDamage(int damage){
        setLife(getLife()-damage);
        if(getLife() <=0){
            System.out.println("Game over!");
        }
    }

    public void draw(Graphics2D g2){
        BufferedImage image = null;

        switch(getDirection()) {
            case "up":
                if(getSpriteNum() == 1){
                    image = getUp1();
                }
                if(getSpriteNum() == 2){
                    image = getUp2();
                }

                break;
            case "down":
                if(getSpriteNum() == 1){
                    image = getDown1();
                }
                if(getSpriteNum() == 2){
                    image = getDown2();
                }
                break;
            case "left":
                if(getSpriteNum() == 1){
                    image = getLeft1();
                }
                if(getSpriteNum() == 2){
                    image = getLeft2();
                }
                break;
            case "right":
                if(getSpriteNum() == 1){
                    image = getRight1();
                }
                if(getSpriteNum() == 2){
                    image = getRight2();
                }
                break;
        }
        g2.drawImage(image, getX(), getY(), gp.getTileSize(), gp.getTileSize(), null);

    }
}