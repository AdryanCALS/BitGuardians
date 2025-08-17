package main;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

public class GamePanel extends JPanel {
    final int originalSize = 16; //tamanho de um quadrado
    final int scale = 3; //aumenta em 3x a tela pra ficar numa resolução aceitável

    final int tileSize = originalSize * scale;
    final int maxScreenCol = 16; //16*48 = 768px
    final int maxScreenRow = 9; //9*48 = 432px
    final int screenWidth = tileSize * maxScreenCol;
    final int screenHeight = tileSize * maxScreenRow;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.PINK);
        this.setDoubleBuffered(true);
    }
}
