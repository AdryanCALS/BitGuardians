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

        solidArea = new Rectangle(8, 16, 32, 32);
    }
    
    public void getPlayerImage() { //inverti o down com o up no pgn pq achei q fazia mais sentido na animação dele
    	try {                       // vê o que tu acha
    	    down1 = ImageIO.read(getClass().getResourceAsStream("/res/player/up1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/res/player/up2.png"));
            up1 = ImageIO.read(getClass().getResourceAsStream("/res/player/down1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/res/player/down2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/res/player/left1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/res/player/left2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/res/player/right1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/res/player/right2.png"));
    	}catch(IOException e) {
    		e.printStackTrace();
    	}
    }
    public void setDefaultValues(){
        x = 100;
        y = 300;
        speed = 4;
        direction = "down";
    }
    public void update(){
        if(keyH.downPressed==true || keyH.leftPressed == true || keyH.upPressed == true || keyH.rightPressed == true){
            if (keyH.upPressed == true){
                direction = "up";
            }
            else if (keyH.downPressed == true){
                direction = "down";          
            }
            else if (keyH.leftPressed == true){
                direction = "left";
            }
            else if (keyH.rightPressed == true){
                direction = "right";
            }
                colisionON = false;
                gp.collisionCheck.checkTile(this);

                if (colisionON == false){
                    switch(direction){
                    case "up":
                         y -= speed;
                        break;
                     case "down":
                         y += speed;
                        break;
                     case "left":
                        x -= speed;
                        break;
                     case "right":
                        x += speed;
                        break;
                        
                    }
                }

            spriteCounter++;
            if(spriteCounter>12){
                if(spriteNum==1){
                    spriteNum=2;
                } else if (spriteNum==2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;

        }

        }
    }
    public void draw(Graphics2D g2){
        BufferedImage image = null;
        //perdão pela identação, a IDE ta bugada
        switch(direction) {
        case "up":
            if(spriteNum == 1){
                image = up1;
            }
            if(spriteNum == 2){
                image = up2;
            }

        	break;
            case "down":
                if(spriteNum == 1){
                    image = down1;
                }
                if(spriteNum == 2){
                    image = down2;
                }
                break;
            case "left":
                if(spriteNum == 1){
                    image = left1;
                }
                if(spriteNum == 2){
                    image = left2;
                }
                break;
            case "right":
                if(spriteNum == 1){
                    image = right1;
                }
                if(spriteNum == 2){
                    image = right2;
                }
                break;
        }
        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
        
    }
}
