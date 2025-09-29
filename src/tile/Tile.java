package tile;

import java.awt.image.BufferedImage;

public class Tile {
    private BufferedImage image;
    private boolean colision = false;

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public boolean isColision() {
        return colision;
    }

    public void setColision(boolean colision) {
        this.colision = colision;
    }
}