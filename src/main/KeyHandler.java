package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    private boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, attackPressed, key1Pressed, key2Pressed, key3Pressed;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();
        if(code == KeyEvent.VK_SPACE){
            attackPressed = true;
        }
        if (code == KeyEvent.VK_W) {
            upPressed = true;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = true;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = true;
        }
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        if(code == KeyEvent.VK_1){
            key1Pressed = true;
        }
        if(code == KeyEvent.VK_2){
            key2Pressed = true;
        }
        if(code == KeyEvent.VK_3){
            key3Pressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_SPACE){
            attackPressed = false;
        }
        if (code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = false;
        }
        if(code == KeyEvent.VK_1){
            key1Pressed = false;
        }
        if(code == KeyEvent.VK_2){
            key2Pressed = false;
        }
        if(code == KeyEvent.VK_3){
            key3Pressed = false;
        }
    }

    public boolean isKey1Pressed(){
        return key1Pressed;
    }

    public boolean isKey2Pressed(){
        return key2Pressed;
    }

    public boolean isKey3Pressed(){
        return key3Pressed;
    }

    public boolean isAttackPressed() {
        return attackPressed;
    }

    public void setAttackPressed(boolean attackPressed) {
        this.attackPressed = attackPressed;
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }
    public boolean isEnterPressed() {
        return enterPressed;
    }

    public void setUpPressed(boolean upPressed) {
        this.upPressed = upPressed;
    }

    public void setDownPressed(boolean downPressed) {
        this.downPressed = downPressed;
    }

    public void setEnterPressed(boolean enterPressed) {
        this.enterPressed = enterPressed;
    }

    public void setKey1Pressed(boolean key1Pressed) {
        this.key1Pressed = key1Pressed;
    }

    public void setKey2Pressed(boolean key2Pressed) {
        this.key2Pressed = key2Pressed;
    }

    public void setKey3Pressed(boolean key3Pressed) {
        this.key3Pressed = key3Pressed;
    }
}