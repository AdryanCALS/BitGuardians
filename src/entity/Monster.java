package entity;

import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Monster extends Entity{ //384 em y e 432 em x, coordenadas do monstro teste
    GamePanel gp;

    public Monster(GamePanel gp){
       this.gp = gp;

       setDefaultValues();
       getMonsterImage();
    }



public void getMonsterImage() {
    try{
        down1 = ImageIO.read(getClass().getResourceAsStream("/res/monster/MONdown1.png"));
    }catch(IOException e) {
    		e.printStackTrace();
    	}

}
public void setDefaultValues(){
        x = 700;
        y = 280;
        speed = 2;
    }
    public void update(){
        x -= speed;
        if (x == 0){ //fiz só para testar, retirar esse if depois!!
            x = 700;
        }
        
        }
    
     public void draw(Graphics2D g2){
        BufferedImage image = down1;
        if (image != null){
        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
        } 

    }
}