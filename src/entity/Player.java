package entity;

import main.GamePanel;
import main.KeyHandler;
import main.MouseHandler;

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
    private MouseHandler mouseHandler;
    private String characterClass;
    private long lastAttackTime = 0;
    private long attackCooldown;
    private int attackDamage = 1;
    private boolean hasSlowEffect = false;
    private int upgradeCost = 5;
    private boolean moving = false;
    int upgradeNum = 0;

    public Player(GamePanel gp, KeyHandler keyH, MouseHandler mouseHandler){

        this.gp = gp;
        this.keyH = keyH;
        this.characterClass = gp.getClassEspadachim();
        this.mouseHandler = mouseHandler;

        setDefaultValues();
        getPlayerImage();

        setSolidArea(new Rectangle(8, 16, 32, 32));
    }


    public boolean isAttacking(){ return attacking; }
    public void setCharacterClass(String characterClass) { this.characterClass = characterClass; }
    public String getCharacterClass(){ return characterClass; }
    public Rectangle getAttackArea(){ return attackArea; }


    public void getPlayerImage() {
        try {
            if (characterClass.equals(gp.getClassEspadachim())) {
                setDown1(ImageIO.read(getClass().getResourceAsStream("/res/player/down1.png")));
                setDown2(ImageIO.read(getClass().getResourceAsStream("/res/player/down2.png")));
                setUp1(ImageIO.read(getClass().getResourceAsStream("/res/player/down1.png")));
                setUp2(ImageIO.read(getClass().getResourceAsStream("/res/player/down2.png")));
                setLeft1(ImageIO.read(getClass().getResourceAsStream("/res/player/left1.png")));
                setLeft2(ImageIO.read(getClass().getResourceAsStream("/res/player/left2.png")));
                setRight1(ImageIO.read(getClass().getResourceAsStream("/res/player/right1.png")));
                setRight2(ImageIO.read(getClass().getResourceAsStream("/res/player/right2.png")));
                setAttackRight1(ImageIO.read(getClass().getResourceAsStream("/res/player/attackright1.png")));
                setAttackRight2(ImageIO.read(getClass().getResourceAsStream("/res/player/attackright2.png")));
                setAttackLeft1(ImageIO.read(getClass().getResourceAsStream("/res/player/attackleft1.png")));
                setAttackLeft2(ImageIO.read(getClass().getResourceAsStream("/res/player/attackleft2.png")));
                setAttackUp1(ImageIO.read(getClass().getResourceAsStream("/res/player/attackup1.png")));
                setAttackUp2(ImageIO.read(getClass().getResourceAsStream("/res/player/attackup2.png")));
                setAttackDown1(ImageIO.read(getClass().getResourceAsStream("/res/player/attackdown1.png")));
                setAttackDown2(ImageIO.read(getClass().getResourceAsStream("/res/player/attackdown2.png")));
                setIdle1(ImageIO.read(getClass().getResourceAsStream("/res/player/idle1.png")));
                setIdle2(ImageIO.read(getClass().getResourceAsStream("/res/player/idle2.png")));

            } else if (characterClass.equals(gp.getClassMago())) {
                setIdle1(ImageIO.read(getClass().getResourceAsStream("/res/player/mageIdle1.png")));
                setIdle2(ImageIO.read(getClass().getResourceAsStream("/res/player/mageIdle2.png")));

                setUp1(getIdle1());
            }
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void setAttackArea(int width, int height){
        attackArea = new Rectangle(0,0,width,height);//possivel modificar a área de ataque
    }

    public void setDefaultValues(){
        setX(100);
        setY(300);
        setSpeed(4);
        setDirection("down");
        setLife(5);

        if (characterClass.equals(gp.getClassEspadachim())) {
            setAttackArea(64,64);
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

    public void upgradeDamage(){
        if(gp.getHud().spendGold(upgradeCost)){
            attackDamage++;
            upgradeCost+=2;//podemos mudar posteriormente
            System.out.println("Upgrade dano: novo dano = "+attackDamage);
        }
    }

    public void upgradeSpeed(){
        if(gp.getHud().spendGold(upgradeCost)&& upgradeNum<3){
            setSpeed(getSpeed()+1);
            upgradeCost +=2;
            System.out.println("Upgrade Velocidade de movimento! a velocidade de movimento agora é: " + getSpeed());
        }else{
            System.out.println("Upgrade maximo de veolicade atingido! sua velocidade agora é: "+ getSpeed());
        }
    }

    public void upgradeSpecial(){
        if (characterClass.equals(gp.getClassEspadachim())) {
            if (upgradeNum>=2) {
                System.out.println("Upgrade maximo de area de ataque atingido! sua area de ataque é: " + attackArea.width + "x" + attackArea.height);
            }else if (gp.getHud().spendGold(upgradeCost)) {
                setAttackArea(attackArea.width + 16, attackArea.height + 16);
                upgradeCost += 2;
                System.out.println("Upgrade Área: Nova Área = "+ attackArea.width + "x" + attackArea.height );
                upgradeNum++;
            }else{
                System.out.println("Gold insuficiente!");
            }

        } else if (characterClass.equals(gp.getClassMago())) {
            if (!hasSlowEffect) { // Compra única para desbloquear o efeito
                if (gp.getHud().spendGold(upgradeCost)) {
                    hasSlowEffect = true;
                    upgradeCost += 2;
                    System.out.println("Upgrade Slow: Efeito de Gelo Ativado!");
                }
            } else {
                System.out.println("Você já possui o efeito de Slow!");
            }
        }
    }

    public BufferedImage getDisplayImage() { return getIdle1(); }

    @Override
    public void update(){
        Monster collidingMonster = gp.getCollisionCheck().checkPlayerMonsterCollision(this, gp.getWaveManager().getActiveMonsters());
        if (collidingMonster != null) {
            int knockbackSpeed = 5;
            int dx = getX() - collidingMonster.getX();
            int dy = getY() - collidingMonster.getY();
            double distance = Math.sqrt(dx*dx + dy*dy);
            if (distance == 0) distance = 1;
            double knockbackX = (dx / distance) * knockbackSpeed;
            double knockbackY = (dy / distance) * knockbackSpeed;
            setX(getX() + (int)knockbackX);
            setY(getY() + (int)knockbackY);
        }

        if(keyH.isUpPressed() || keyH.isDownPressed() || keyH.isLeftPressed() || keyH.isRightPressed()){
            moving = true; // Está se movendo

            if (keyH.isUpPressed()) setDirection("up");
            else if (keyH.isDownPressed()) setDirection("down");
            else if (keyH.isLeftPressed()) setDirection("left");
            else if (keyH.isRightPressed()) setDirection("right");

            setColisionON(false);
            gp.getCollisionCheck().checkTile(this);

            if (!isColisionON()){
                switch(getDirection()){
                    case "up": if (getY() - getSpeed() >= 0) setY(getY() - getSpeed()); break;
                    case "down": if (getY() + getSpeed() <= gp.getScreenHeight() - gp.getTileSize()) setY(getY() + getSpeed()); break;
                    case "left": if (getX() - getSpeed() >= 0) setX(getX() - getSpeed()); break;
                    case "right": if (getX() + getSpeed() <= gp.getScreenWidth() - gp.getTileSize()) setX(getX() + getSpeed()); break;
                }
            }
        } else {
            moving = false; // Parou
        }

        setSpriteCounter(getSpriteCounter() + 1);
        if(getSpriteCounter() > 12) { // Ajuste esse 12 para mudar a velocidade da animação
            if(getSpriteNum() == 1) setSpriteNum(2);
            else if (getSpriteNum() == 2) setSpriteNum(1);
            setSpriteCounter(0);
        }

        if(keyH.isKey1Pressed()){
            upgradeDamage();
            keyH.setKey1Pressed(false);
        }
        if(keyH.isKey2Pressed()){
            upgradeSpeed();
            keyH.setKey2Pressed(false);
        }
        if(keyH.isKey3Pressed()){
            upgradeSpecial();
            keyH.setKey3Pressed(false);
        }

        if(characterClass.equals(gp.getClassMago())){
            if(mouseHandler.isMousePressed()){
                attack();
                mouseHandler.setMousePressed(false);
            }
        }else{
            if(keyH.isAttackPressed() || mouseHandler.isMousePressed()){
                attack();
                keyH.setAttackPressed(false);
            }
        }

        if(attacking){
            attackCounter++;
            setSpriteCounter(getSpriteCounter()+1);
            if(characterClass.equals(gp.getClassEspadachim())){
                // Pega as coordenadas e dimensões da área sólida (física) do player
                int playerSolidX = getX() + getSolidArea().x;
                int playerSolidY = getY() + getSolidArea().y;
                int playerSolidW = getSolidArea().width;
                int playerSolidH = getSolidArea().height;

                switch (getDirection()){
                    case "up":
                        // Centraliza X: Posição X + (Metade do Player) - (Metade do Ataque)
                        attackArea.x = playerSolidX + (playerSolidW - attackArea.width)/2;
                        attackArea.y = playerSolidY - attackArea.height;
                        break;
                    case "down":
                        // Centraliza X
                        attackArea.x = playerSolidX + (playerSolidW - attackArea.width)/2;
                        attackArea.y = playerSolidY + playerSolidH;
                        break;
                    case "left":
                        attackArea.x = playerSolidX - attackArea.width;
                        // Centraliza Y: Posição Y + (Metade do Player) - (Metade do Ataque)
                        attackArea.y = playerSolidY + (playerSolidH - attackArea.height)/2;
                        break;
                    case "right":
                        attackArea.x = playerSolidX + playerSolidW;
                        // Centraliza Y
                        attackArea.y = playerSolidY + (playerSolidH - attackArea.height)/2;
                        break;
                }
                if(attackCounter > 5 && attackCounter < 10){
                    int monsterIndex = gp.getCollisionCheck().checkPlayerAttack(this, gp.getWaveManager().getActiveMonsters());
                    if(monsterIndex != -1){
                        gp.getWaveManager().damageMonster(monsterIndex, attackDamage);
                        attackCounter = 0;
                        attacking = false;
                    }
                }
            }
            if(attackCounter > attackDuration){
                attackCounter = 0;
                attacking = false;
                setSpriteCounter(0);
            }
        }
    }

    public void attack(){
        long currentTime = System.currentTimeMillis();
        if(currentTime-lastAttackTime < attackCooldown || attacking){
            return;
        }
        attacking = true;
        attackCounter = 0;
        setSpriteCounter(0);
        lastAttackTime = currentTime;
        if (characterClass.equals(gp.getClassEspadachim())) {
        } else if (characterClass.equals(gp.getClassMago())) {
            Projectile projectile = new Projectile(gp);
            projectile.setStats(attackDamage, hasSlowEffect);
            int startX = getX() + (gp.getTileSize() / 4);
            int startY = getY() + (gp.getTileSize() / 4);

            projectile.setTarget(startX, startY, mouseHandler.getMouseX(), mouseHandler.getMouseY());
            gp.getProjectiles().add(projectile);
        }
    }

    public void takeDamage(int damage){
        setLife(getLife()-damage); // setLife() e getLife() são herdados de Entity
        if(getLife() <=0){
            System.out.println("Game over!");
        }
    }

    @Override
    public void draw(Graphics2D g2){
        BufferedImage image = null;
        int drawX = getX();
        int drawY = getY();
        int width = gp.getTileSize();
        int height = gp.getTileSize();

        // --- LÓGICA DE ATAQUE DO ESPADACHIM ---
        if (attacking && characterClass.equals(gp.getClassEspadachim())) {
            int currentAttackSprite = (attackCounter < attackDuration / 2) ? 1 : 2;

            switch(getDirection()) {
                case "up":
                    image = (currentAttackSprite == 1) ? getAttackUp1() : getAttackUp2();
                    break;
                case "down":
                    image = (currentAttackSprite == 1) ? getAttackDown1() : getAttackDown2();
                    break;
                case "left":
                    image = (currentAttackSprite == 1) ? getAttackLeft1() : getAttackLeft2();
                    break;
                case "right":
                    image = (currentAttackSprite == 1) ? getAttackRight1() : getAttackRight2();
                    break;
            }

            if(image != null) {
                g2.drawImage(image, drawX, drawY, width, height, null);
            }

            // --- EFEITO VISUAL DE VENTO (SLASH) ---
            Stroke oldStroke = g2.getStroke();
            g2.setColor(new Color(200, 255, 255, 100));
            g2.setStroke(new BasicStroke(3));

            // Define o ângulo do arco baseado na direção
            int visualX = attackArea.x;
            int visualY = attackArea.y;
            int overlap = 25;

            int arcStart = 0;
            switch(getDirection()) {
                case "up":
                    arcStart = 0; // Começa na direita (0) e gira anti-horário 180° = Semicírculo superior
                    visualY += overlap; // Puxa para baixo (para dentro do player)
                    break;
                case "down":
                    arcStart = 180; // Começa na esquerda (180) e gira 180° = Semicírculo inferior
                    visualY = visualY-10-overlap; // Puxa para cima
                    break;
                case "left":
                    arcStart = 90; // Começa em cima (90) e gira 180° = Semicírculo esquerdo
                    visualX += overlap; // Puxa para a direita
                    break;
                case "right":
                    arcStart = 270; // Começa em baixo (270) e gira 180° = Semicírculo direito
                    visualX = visualX-5-overlap; // Puxa para a esquerda
                    break;
            }

            g2.fillArc(visualX, visualY, attackArea.width, attackArea.height, arcStart, 180);

            g2.setColor(Color.WHITE);
            g2.drawArc(visualX, visualY, attackArea.width, attackArea.height, arcStart, 180);

            g2.setStroke(oldStroke);

            g2.setStroke(oldStroke);
        }else if (characterClass.equals(gp.getClassMago())) {

            // O Mago usa idle1 e idle2 para TUDO (andar, atacar, parado)
            // Apenas alterna baseado no contador global de sprite
            image = (getSpriteNum() == 1) ? getIdle1() : getIdle2();

            // Desenha
            if (image != null) {
                g2.drawImage(image, drawX, drawY, width, height, null);
            }

            // ------------------------------------------
            // 3. LÓGICA GENÉRICA / ESPADACHIM NORMAL
            // ------------------------------------------
        } else {
            // Se estiver se movendo, usa sprites de direção
            if (moving) {
                switch (getDirection()) {
                    case "up":
                        image = (getSpriteNum() == 1) ? getUp1() : getUp2();
                        break;
                    case "down":
                        image = (getSpriteNum() == 1) ? getDown1() : getDown2();
                        break;
                    case "left":
                        image = (getSpriteNum() == 1) ? getLeft1() : getLeft2();
                        break;
                    case "right":
                        image = (getSpriteNum() == 1) ? getRight1() : getRight2();
                        break;
                }
            } else {
                // Se parado, usa Idle
                image = (getSpriteNum() == 1) ? getIdle1() : getIdle2();
            }

            if (image != null) {
                g2.drawImage(image, drawX, drawY, width, height, null);
            }
        }
    }
}