package entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D; // Importação necessária

// 1. Tornada uma classe abstrata
public abstract class Entity {
    private int life;
    private int x,y;
    private int speed;
    private BufferedImage up1,up2,down1,down2,left1,left2,right1,right2;
    private BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;
    private String direction;

    private int spriteCounter = 0;
    private int spriteNum = 1;

    private Rectangle solidArea;
    private boolean colisionON = false;

    public abstract void update();

    public abstract void draw(Graphics2D g2);

    public int getLife(){
        return life;
    }
    public void setLife(int life){
        this.life = life;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public BufferedImage getAttackUp1() { return attackUp1; }

    public void setAttackUp1(BufferedImage attackUp1) { this.attackUp1 = attackUp1; }

    public BufferedImage getAttackUp2() { return attackUp2; }

    public void setAttackUp2(BufferedImage attackUp2) { this.attackUp2 = attackUp2; }

    public BufferedImage getAttackDown1() { return attackDown1; }

    public void setAttackDown1(BufferedImage attackDown1) { this.attackDown1 = attackDown1; }

    public BufferedImage getAttackDown2() { return attackDown2; }
    public void setAttackDown2(BufferedImage attackDown2) { this.attackDown2 = attackDown2; }

    public BufferedImage getAttackLeft1() { return attackLeft1; }

    public void setAttackLeft1(BufferedImage attackLeft1) { this.attackLeft1 = attackLeft1; }

    public BufferedImage getAttackLeft2() { return attackLeft2; }

    public void setAttackLeft2(BufferedImage attackLeft2) { this.attackLeft2 = attackLeft2; }

    public BufferedImage getAttackRight1() { return attackRight1; }

    public void setAttackRight1(BufferedImage attackRight1) { this.attackRight1 = attackRight1; }

    public BufferedImage getAttackRight2() { return attackRight2; }

    public void setAttackRight2(BufferedImage attackRight2) { this.attackRight2 = attackRight2; }

    public BufferedImage getUp1() {
        return up1;
    }

    public void setUp1(BufferedImage up1) {
        this.up1 = up1;
    }

    public BufferedImage getUp2() {
        return up2;
    }

    public void setUp2(BufferedImage up2) {
        this.up2 = up2;
    }

    public BufferedImage getDown1() {
        return down1;
    }

    public void setDown1(BufferedImage down1) {
        this.down1 = down1;
    }

    public BufferedImage getDown2() {
        return down2;
    }

    public void setDown2(BufferedImage down2) {
        this.down2 = down2;
    }

    public BufferedImage getLeft1() {
        return left1;
    }

    public void setLeft1(BufferedImage left1) {
        this.left1 = left1;
    }

    public BufferedImage getLeft2() {
        return left2;
    }

    public void setLeft2(BufferedImage left2) {
        this.left2 = left2;
    }

    public BufferedImage getRight1() {
        return right1;
    }

    public void setRight1(BufferedImage right1) {
        this.right1 = right1;
    }

    public BufferedImage getRight2() {
        return right2;
    }

    public void setRight2(BufferedImage right2) {
        this.right2 = right2;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getSpriteCounter() {
        return spriteCounter;
    }

    public void setSpriteCounter(int spriteCounter) {
        this.spriteCounter = spriteCounter;
    }

    public int getSpriteNum() {
        return spriteNum;
    }

    public void setSpriteNum(int spriteNum) {
        this.spriteNum = spriteNum;
    }

    public Rectangle getSolidArea() {
        return solidArea;
    }

    public void setSolidArea(Rectangle solidArea) {
        this.solidArea = solidArea;
    }

    public boolean isColisionON() {
        return colisionON;
    }

    public void setColisionON(boolean colisionON) {
        this.colisionON = colisionON;
    }
}