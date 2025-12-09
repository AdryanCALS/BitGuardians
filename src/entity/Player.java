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
    private final int attackDuration = 15;
    private Rectangle attackArea;
    private boolean attacking = false;
    private GamePanel gp;
    private KeyHandler keyH;
    private MouseHandler mouseHandler;
    private String characterClass;
    private long lastAttackTime = 0;
    private long attackCooldown;

    // variaveis de upgrade
    private int attackDamage = 1;
    private boolean hasSlowEffect = false;
    private boolean moving = false;

    // variaveis de custo
    private int damageCost = 5;
    private int speedCost = 5;
    private int specialCost = 5;
    private int speedLevel = 0;
    private int upgradeNum = 0;

    public Player(GamePanel gp, KeyHandler keyH, MouseHandler mouseHandler) {
        this.gp = gp;
        this.keyH = keyH;
        this.characterClass = gp.getClassEspadachim();
        this.mouseHandler = mouseHandler;

        setDefaultValues();
        getPlayerImage();

        setSolidArea(new Rectangle(8, 16, 32, 32));
    }

    public boolean isAttacking() { return attacking; }
    public void setCharacterClass(String characterClass) { this.characterClass = characterClass; }
    public String getCharacterClass() { return characterClass; }
    public Rectangle getAttackArea() { return attackArea; }

    // getter para o hud
    public int getDamageCost() { return damageCost; }
    public int getSpeedCost() { return speedCost; }
    public int getSpecialCost() { return specialCost; }

    public boolean isSpeedMaxed() {
        return speedLevel >= 3;
    }

    public boolean isSpecialMaxed() {
        if (characterClass.equals(gp.getClassEspadachim())) {
            return upgradeNum >= 2;
        } else if (characterClass.equals(gp.getClassMago())) {
            return hasSlowEffect;
        }
        return false;
    }

    public void setDefaultValues() {
        setX(100);
        setY(300);
        setSpeed(4);
        setDirection("down");
        setLife(5);

        attackDamage = 1;
        hasSlowEffect = false;

        upgradeNum = 0;
        speedLevel = 0;

        damageCost = 5;
        speedCost = 5;
        specialCost = 5;

        if (characterClass.equals(gp.getClassEspadachim())) {
            setAttackArea(64, 64);
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

    public void upgradeDamage() {
        if (gp.getHud().spendGold(damageCost)) {
            attackDamage++;
            damageCost += 2;
        }
    }

    public void upgradeSpeed() {
        if (gp.getHud().spendGold(speedCost) && speedLevel < 3) {
            setSpeed(getSpeed() + 1);
            speedCost += 2;
            speedLevel++;
        }
    }

    public void upgradeSpecial() {
        if (characterClass.equals(gp.getClassEspadachim())) {
            if (upgradeNum < 2 && gp.getHud().spendGold(specialCost)) {
                setAttackArea(attackArea.width + 16, attackArea.height + 16);
                specialCost += 2;
                upgradeNum++;
            }
        } else if (characterClass.equals(gp.getClassMago())) {
            if (!hasSlowEffect && gp.getHud().spendGold(specialCost)) {
                hasSlowEffect = true;
                specialCost += 2;
            }
        }
    }

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setAttackArea(int width, int height) {
        attackArea = new Rectangle(0, 0, width, height);
    }

    public BufferedImage getDisplayImage() { return getIdle1(); }

    @Override
    public void update() {
        Monster collidingMonster = gp.getCollisionCheck().checkPlayerMonsterCollision(this, gp.getWaveManager().getActiveMonsters());
        if (collidingMonster != null) {
            int knockbackSpeed = 5;
            int dx = getX() - collidingMonster.getX();
            int dy = getY() - collidingMonster.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);
            if (distance == 0) distance = 1;
            double knockbackX = (dx / distance) * knockbackSpeed;
            double knockbackY = (dy / distance) * knockbackSpeed;
            setX(getX() + (int) knockbackX);
            setY(getY() + (int) knockbackY);
        }

        if (keyH.isUpPressed() || keyH.isDownPressed() || keyH.isLeftPressed() || keyH.isRightPressed()) {
            moving = true;
            if (keyH.isUpPressed()) setDirection("up");
            else if (keyH.isDownPressed()) setDirection("down");
            else if (keyH.isLeftPressed()) setDirection("left");
            else if (keyH.isRightPressed()) setDirection("right");

            setColisionON(false);
            gp.getCollisionCheck().checkTile(this);

            if (!isColisionON()) {
                switch (getDirection()) {
                    case "up": if (getY() - getSpeed() >= 0) setY(getY() - getSpeed()); break;
                    case "down": if (getY() + getSpeed() <= gp.getScreenHeight() - gp.getTileSize()) setY(getY() + getSpeed()); break;
                    case "left": if (getX() - getSpeed() >= 0) setX(getX() - getSpeed()); break;
                    case "right": if (getX() + getSpeed() <= gp.getScreenWidth() - gp.getTileSize()) setX(getX() + getSpeed()); break;
                }
            }
        } else {
            moving = false;
        }

        setSpriteCounter(getSpriteCounter() + 1);
        if (getSpriteCounter() > 12) {
            if (getSpriteNum() == 1) setSpriteNum(2);
            else if (getSpriteNum() == 2) setSpriteNum(1);
            setSpriteCounter(0);
        }

        if (keyH.isKey1Pressed()) { upgradeDamage(); keyH.setKey1Pressed(false); }
        if (keyH.isKey2Pressed()) { upgradeSpeed(); keyH.setKey2Pressed(false); }
        if (keyH.isKey3Pressed()) { upgradeSpecial(); keyH.setKey3Pressed(false); }

        if (characterClass.equals(gp.getClassMago())) {
            if (mouseHandler.isMousePressed()) { attack(); mouseHandler.setMousePressed(false); }
        } else {
            if (keyH.isAttackPressed() || mouseHandler.isMousePressed()) { attack(); keyH.setAttackPressed(false); }
        }

        if (attacking) {
            attackCounter++;
            setSpriteCounter(getSpriteCounter() + 1);
            if (characterClass.equals(gp.getClassEspadachim())) {
                int playerSolidX = getX() + getSolidArea().x;
                int playerSolidY = getY() + getSolidArea().y;
                int playerSolidW = getSolidArea().width;
                int playerSolidH = getSolidArea().height;

                switch (getDirection()) {
                    case "up":
                        attackArea.x = playerSolidX + (playerSolidW - attackArea.width) / 2;
                        attackArea.y = playerSolidY - attackArea.height;
                        break;
                    case "down":
                        attackArea.x = playerSolidX + (playerSolidW - attackArea.width) / 2;
                        attackArea.y = playerSolidY + playerSolidH;
                        break;
                    case "left":
                        attackArea.x = playerSolidX - attackArea.width;
                        attackArea.y = playerSolidY + (playerSolidH - attackArea.height) / 2;
                        break;
                    case "right":
                        attackArea.x = playerSolidX + playerSolidW;
                        attackArea.y = playerSolidY + (playerSolidH - attackArea.height) / 2;
                        break;
                }
                if (attackCounter > 5 && attackCounter < 10) {
                    int monsterIndex = gp.getCollisionCheck().checkPlayerAttack(this, gp.getWaveManager().getActiveMonsters());
                    if (monsterIndex != -1) {
                        gp.getWaveManager().damageMonster(monsterIndex, attackDamage);
                        attackCounter = 0;
                        attacking = false;
                    }
                }
            }
            if (attackCounter > attackDuration) {
                attackCounter = 0;
                attacking = false;
                setSpriteCounter(0);
            }
        }
    }

    public void attack() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAttackTime < attackCooldown || attacking) {
            return;
        }
        attacking = true;
        attackCounter = 0;
        setSpriteCounter(0);
        lastAttackTime = currentTime;

        if (characterClass.equals(gp.getClassMago())) {
            Projectile projectile = new Projectile(gp);
            projectile.setStats(attackDamage, hasSlowEffect);
            int startX = getX() + (gp.getTileSize() / 4);
            int startY = getY() + (gp.getTileSize() / 4);
            projectile.setTarget(startX, startY, mouseHandler.getMouseX(), mouseHandler.getMouseY());
            gp.getProjectiles().add(projectile);
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        int drawX = getX();
        int drawY = getY();
        int width = gp.getTileSize();
        int height = gp.getTileSize();

        if (attacking && characterClass.equals(gp.getClassEspadachim())) {
            int currentAttackSprite = (attackCounter < attackDuration / 2) ? 1 : 2;
            switch (getDirection()) {
                case "up": image = (currentAttackSprite == 1) ? getAttackUp1() : getAttackUp2(); break;
                case "down": image = (currentAttackSprite == 1) ? getAttackDown1() : getAttackDown2(); break;
                case "left": image = (currentAttackSprite == 1) ? getAttackLeft1() : getAttackLeft2(); break;
                case "right": image = (currentAttackSprite == 1) ? getAttackRight1() : getAttackRight2(); break;
            }
            if (image != null) { g2.drawImage(image, drawX, drawY, width, height, null); }

            Stroke oldStroke = g2.getStroke();
            g2.setColor(new Color(200, 255, 255, 100));
            g2.setStroke(new BasicStroke(3));
            int visualX = attackArea.x;
            int visualY = attackArea.y;
            int overlap = 25;
            int arcStart = 0;
            switch (getDirection()) {
                case "up": arcStart = 0; visualY += overlap; break;
                case "down": arcStart = 180; visualY = visualY - 10 - overlap; break;
                case "left": arcStart = 90; visualX += overlap; break;
                case "right": arcStart = 270; visualX = visualX - 5 - overlap; break;
            }
            g2.fillArc(visualX, visualY, attackArea.width, attackArea.height, arcStart, 180);
            g2.setColor(Color.WHITE);
            g2.drawArc(visualX, visualY, attackArea.width, attackArea.height, arcStart, 180);
            g2.setStroke(oldStroke);

        } else if (characterClass.equals(gp.getClassMago())) {
            image = (getSpriteNum() == 1) ? getIdle1() : getIdle2();
            if (image != null) { g2.drawImage(image, drawX, drawY, width, height, null); }
        } else {
            if (moving) {
                switch (getDirection()) {
                    case "up": image = (getSpriteNum() == 1) ? getUp1() : getUp2(); break;
                    case "down": image = (getSpriteNum() == 1) ? getDown1() : getDown2(); break;
                    case "left": image = (getSpriteNum() == 1) ? getLeft1() : getLeft2(); break;
                    case "right": image = (getSpriteNum() == 1) ? getRight1() : getRight2(); break;
                }
            } else {
                image = (getSpriteNum() == 1) ? getIdle1() : getIdle2();
            }
            if (image != null) { g2.drawImage(image, drawX, drawY, width, height, null); }
        }
    }
}