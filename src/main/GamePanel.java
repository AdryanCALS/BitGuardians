package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D; //habilita o desenho na janela criada, sem os imports estava dando erro

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{
    final int originalSize = 16; //tamanho de um quadrado
    final int scale = 3; //aumenta em 3x a tela pra ficar numa resolução aceitável

    final int tileSize = originalSize * scale;
    final int maxScreenCol = 16; //16*48 = 768px
    final int maxScreenRow = 9; //9*48 = 432px
    final int screenWidth = tileSize * maxScreenCol;
    final int screenHeight = tileSize * maxScreenRow;

    Thread gameThread;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.PINK);
        this.setDoubleBuffered(true);
    }

public void startThread() {
    
    gameThread = new Thread(this);
    gameThread.start();
}

@Override
public void run () { //método necessário para a Thread rodar

    while (gameThread != null){ 
        //1 - dar update nas informações de posição;
        // 2 - desenhar a tela com essas informações;
    

    }
//public void update(){ //está dando erro, não entendi o motivo

}
public void paintComponent(Graphics g){
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D)g;
    g2.setColor(Color.CYAN);
    g2.fillRect(100, 200, tileSize, tileSize); //pode ser mudado, apenas o tamanho do desenho
    g2.dispose();//ajuda a salvar memória, é uma boa prática

}
}

