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
    private String characterClass;

    //esses 2 aqui são long pq vão pegar hora em milisegundo
    private long lastAttackTime = 0;
    private long attackCooldown;


    public Player(GamePanel gp, KeyHandler keyH){
        this.gp = gp;
        this.keyH = keyH;

        attackArea = new Rectangle(0,0,64,32);//possivel modificar a área de ataque

        //Define uma classe inicial e chama a inicialização para evitar NullPointerException.
        this.characterClass = gp.getClassEspadachim(); // Define Espadachim como padrão
        setDefaultValues();
        getPlayerImage();

        setSolidArea(new Rectangle(8, 16, 32, 32));
    }

    public boolean isAttacking(){
        return attacking;
    }

    public void setCharacterClass(String characterClass) {
        this.characterClass = characterClass;
    }

    public String getCharacterClass(){
        return characterClass;
    }

    public Rectangle getAttackArea(){
        return attackArea;
    }

    public void getPlayerImage() {
        try {
            if (characterClass.equals(gp.getClassEspadachim())) {
                // Configuração atual do Espadachim (mantendo o aparente swap de up/down da implementação original)
                setDown1(ImageIO.read(getClass().getResourceAsStream("/res/player/up1.png")));
                setDown2(ImageIO.read(getClass().getResourceAsStream("/res/player/up2.png")));
                setUp1(ImageIO.read(getClass().getResourceAsStream("/res/player/down1.png")));
                setUp2(ImageIO.read(getClass().getResourceAsStream("/res/player/down2.png")));
                setLeft1(ImageIO.read(getClass().getResourceAsStream("/res/player/left1.png")));
                setLeft2(ImageIO.read(getClass().getResourceAsStream("/res/player/left2.png")));
                setRight1(ImageIO.read(getClass().getResourceAsStream("/res/player/right1.png")));
                setRight2(ImageIO.read(getClass().getResourceAsStream("/res/player/right2.png")));
            } else if (characterClass.equals(gp.getClassMago())) {
                //adicionar as imagens do mago depois
                setUp1(ImageIO.read(getClass().getResourceAsStream("/res/player/mageDown1.png")));
                setUp2(ImageIO.read(getClass().getResourceAsStream("/res/player/mageDown1.png")));
                setDown1(ImageIO.read(getClass().getResourceAsStream("/res/player/mageDown1.png")));
                setDown2(ImageIO.read(getClass().getResourceAsStream("/res/player/mageDown1.png")));
                setLeft1(ImageIO.read(getClass().getResourceAsStream("/res/player/mageDown1.png")));
                setLeft2(ImageIO.read(getClass().getResourceAsStream("/res/player/mageDown1.png")));
                setRight1(ImageIO.read(getClass().getResourceAsStream("/res/player/mageDown1.png")));
                setRight2(ImageIO.read(getClass().getResourceAsStream("/res/player/mageDown1.png")));
            }
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

        //  DEIXANDO AQUI PARA TALVEZ ADICIONAR DEPOIS
        // Adryan: boa ideia, vou usar e incluir cooldown diefrentes para cada classe ;)!
        if (characterClass.equals(gp.getClassEspadachim())) {
            setSpeed(4);
            setLife(5);
            attackCooldown = 100;
        } else if (characterClass.equals(gp.getClassMago())) {
            setSpeed(3);
            setLife(3);
            attackCooldown = 249;
        }
        lastAttackTime = 0;
    }

    public BufferedImage getDisplayImage() {
        return getDown1();
    }

    public void update(){
        Monster collidingMonster = gp.getCollisionCheck().checkPlayerMonsterCollision(this, gp.getWaveManager().getActiveMonsters());
        if (collidingMonster != null) {
            //colisão com os montros
            int knockbackSpeed = 5;
            int dx = getX() - collidingMonster.getX();
            int dy = getY() - collidingMonster.getY();

            double distance = Math.sqrt(dx*dx + dy*dy);
            if (distance == 0) distance = 1; // Evita divisão por zero

            double knockbackX = (dx / distance) * knockbackSpeed;
            double knockbackY = (dy / distance) * knockbackSpeed;

            setX(getX() + (int)knockbackX);
            setY(getY() + (int)knockbackY);
        }

        //usando if/else em uma linha(chique)
        if(keyH.isUpPressed() || keyH.isDownPressed() || keyH.isLeftPressed() || keyH.isRightPressed()){
            if (keyH.isUpPressed()) setDirection("up");
            else if (keyH.isDownPressed()) setDirection("down");
            else if (keyH.isLeftPressed()) setDirection("left");
            else if (keyH.isRightPressed()) setDirection("right");

            // VERIFICA A COLISÃO COM TILES
            setColisionON(false);
            gp.getCollisionCheck().checkTile(this);

            //verificação de colisão para deixar o jogador se mover
            if (!isColisionON()){
                switch(getDirection()){
                    case "up": if (getY() - getSpeed() >= 0) setY(getY() - getSpeed()); break;
                    case "down": if (getY() + getSpeed() <= gp.getScreenHeight() - gp.getTileSize()) setY(getY() + getSpeed()); break;
                    case "left": if (getX() - getSpeed() >= 0) setX(getX() - getSpeed()); break;
                    case "right": if (getX() + getSpeed() <= gp.getScreenWidth() - gp.getTileSize()) setX(getX() + getSpeed()); break;
                }
            }

            setSpriteCounter(getSpriteCounter() + 1);
            if(getSpriteCounter() > 12){
                if(getSpriteNum() == 1) setSpriteNum(2);
                else if (getSpriteNum() == 2) setSpriteNum(1);
                setSpriteCounter(0);
            }
        }

        //Parte do update para o atque
        if(keyH.isAttackPressed()){
            attack();
            keyH.setAttackPressed(false);
        }

        // 4. ATUALIZA O ATAQUE (SE O ESPADACHIM ESTIVER ATACANDO)
        if(attacking && characterClass.equals(gp.getClassEspadachim())){
            attackCounter++;

            int playerBodyX = getX() + getSolidArea().x;
            int playerBodyY = getY() + getSolidArea().y;
            switch (getDirection()){
                case "up":
                    attackArea.x = playerBodyX -16;
                    attackArea.y = playerBodyY - attackArea.height;
                    break;
                case "down":
                    attackArea.x = playerBodyX -16;
                    attackArea.y = playerBodyY + getSolidArea().height;
                    break;
                case "left":
                    attackArea.x = playerBodyX - attackArea.width;
                    attackArea.y = playerBodyY -16;
                    break;
                case "right":
                    attackArea.x = playerBodyX + getSolidArea().width;
                    attackArea.y = playerBodyY -16;
                    break;
            }

            // Verifica o acerto (no meio da animação)
            if(attackCounter > 5 && attackCounter < 10){
                int monsterIndex = gp.getCollisionCheck().checkPlayerAttack(this, gp.getWaveManager().getActiveMonsters());
                if(monsterIndex != -1){
                    gp.getWaveManager().damageMonster(monsterIndex, 1); // Causa 1 de dano
                    // Para o ataque assim que acertar (evita multi-hit)
                    attackCounter = 0;
                    attacking = false;
                }
            }


            if(attackCounter > attackDuration){
                attackCounter = 0;
                attacking = false;
            }
        }
    }

    public void attack(){
        long currentTime = System.currentTimeMillis();

        if(currentTime-lastAttackTime<attackCooldown){
            return;
        }

        if (characterClass.equals(gp.getClassEspadachim())) {
            // Só permite um novo ataque se não estiver atacando
            if (!attacking) {
                attacking = true;
                attackCounter = 0;
                lastAttackTime = currentTime;
            }
        } else if (characterClass.equals(gp.getClassMago())) {
            Projectile projectile = new Projectile(gp);

            // Configura a posição inicial e direção do projétil
            int startX = getX() + (gp.getTileSize() / 4);
            int startY = getY() + (gp.getTileSize() / 4);
            projectile.set(startX, startY, getDirection());

            // Adiciona o projétil à lista de projéteis do jogo
            gp.getProjectiles().add(projectile);
            //deixando o mago com cooldown pra balancear o jogo
            lastAttackTime = currentTime;
        }
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