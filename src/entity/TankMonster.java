package entity;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;

public class TankMonster extends Monster {

    public TankMonster(GamePanel gp, int startX, int startY) {
        super(gp, startX, startY);
    }

    @Override
    public void setDefaultValues(int startX, int startY) {
        setX(startX);
        setY(startY);
        setSpeed(1);
        setLife(5);

        setOriginalSpeed(getSpeed());
    }
    @Override
    public void getMonsterImage(){
        try{
            setDown1(ImageIO.read(getClass().getResourceAsStream("/res/monster/TankMONdown1.png")));
            setDown2(ImageIO.read(getClass().getResourceAsStream("/res/monster/TankMONdown2.png")));
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
    // Opcional: Adiciona um filtro de cor para diferenciar visualmente (ex: Amarelo/Laranja)
    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);
    }
}