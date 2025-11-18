package entity;

import main.GamePanel;
import java.awt.Color;
import java.awt.Graphics2D;

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

    // Opcional: Adiciona um filtro de cor para diferenciar visualmente (ex: Amarelo/Laranja)
    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2); // Desenha o sprite base
        if (!isTakingDamage()) {
            g2.setColor(new Color(255, 165, 0, 60)); // Filtro laranja claro
            g2.fillRect(getX(), getY(), gp.getTileSize(), gp.getTileSize());
        }
    }
}