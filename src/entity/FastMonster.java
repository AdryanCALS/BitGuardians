package entity;

import main.GamePanel;
import java.awt.Color;
import java.awt.Graphics2D;

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

    // Opcional: Adiciona um filtro de cor para diferenciar visualmente
    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2); // Desenha o sprite base
        if (!isTakingDamage()) {
            g2.setColor(new Color(0, 0, 255, 60)); // Filtro azul claro
            g2.fillRect(getX(), getY(), gp.getTileSize(), gp.getTileSize());
        }
    }
}
