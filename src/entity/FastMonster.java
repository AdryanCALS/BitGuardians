package entity;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.Objects;

public class FastMonster extends Monster {

    public FastMonster(GamePanel gp, int startX, int startY) {
        super(gp, startX, startY);
    }

    @Override
    public void setDefaultValues(int startX, int startY) {
        setX(startX);
        setY(startY);

        setSpeed(4);
        setLife(1);

        setOriginalSpeed(getSpeed());
    }
    @Override
    public void getMonsterImage(){
        try{
            //o requireNonNull é para controle de exceções
            setDown1(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/monster/FastMONdown1.png"))));
            setDown2(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/monster/FastMONdown2.png"))));
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
    // Opcional: Adiciona um filtro de cor para diferenciar visualmente
    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);
    }
}
