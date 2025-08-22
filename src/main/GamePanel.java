package main;

import entity.Player;
import tile.TileManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D; //habilita o desenho na janela criada, sem os imports estava dando erro

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{
    final int originalSize = 16; //tamanho de um quadrado
    final int scale = 3; //aumenta em 3x a tela pra ficar numa resolução aceitável

    public final int tileSize = originalSize * scale;
    public int maxScreenCol = 16; //16*48 = 768px
    public int maxScreenRow = 9; //9*48 = 432px
    public int screenWidth = tileSize * maxScreenCol;
    public int screenHeight = tileSize * maxScreenRow;


    //FPS
    int FPS = 60;

    TileManager tileManager = new TileManager(this);
    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;
    Player player = new Player(this,keyHandler);

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.cyan);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

public void startThread() {
    
    gameThread = new Thread(this);
    gameThread.start();
}

@Override
public void run () { //método necessário para a Thread rodar

    double drawInterval = 1000000000/FPS;// 0,01666...
    double delta = 0;
    long lastTime = System.nanoTime();
    long currentTime;
    

    while (gameThread != null){ 
        currentTime = System.nanoTime();
        delta += (currentTime - lastTime)/drawInterval;
        
        lastTime = currentTime;

        if (delta >=1){
        update();
        repaint();
        delta--;
        }
    }

    }
public void update(){
    player.update();

}
public void paintComponent(Graphics g){
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D)g;

    tileManager.draw(g2);
    player.draw(g2);

    g2.dispose();//ajuda a salvar memória, é uma boa prática


}
}

