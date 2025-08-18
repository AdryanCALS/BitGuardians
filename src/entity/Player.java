package entity;

import main.GamePanel;
import main.KeyHandler;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyH;

    public Player(GamePanel gp, KeyHandler keyH){
        this.gp = gp;
        this.keyH = keyH;

        setDefaultValues();
        getPlayerImage();
    }
    
    public void getPlayerImage() {
    	try {
    		standing = ImageIO.read(getClass().getResourceAsStream("/player/standing.png"));
    	}catch(IOException e) {
    		e.printStackTrace();
    	}
    }
    public void setDefaultValues(){
        x = 100;
        y = 100;
        speed = 4;
        direction = "standing";
    }
    public void update(){
        if (keyH.upPressed == true){
        	direction = "standing";//provisorio so pra ver a funcionalidade dos sprites
            y -= speed;
        }
        else if (keyH.downPressed == true){
        	direction = "standing";
            y += speed;
        }
        else if (keyH.leftPressed == true){
        	direction = "standing";
            x -= speed;
        }
        else if (keyH.rightPressed == true){
        	direction = "standing";
            x += speed;
        }

    }
    public void draw(Graphics2D g2){
        BufferedImage image = null;
        
        switch(direction) {
        case "standing":
        	image = standing;
        	break;
        }
        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
        
    }
}
