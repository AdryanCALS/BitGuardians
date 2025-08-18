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


    //FPS
    int FPS = 60;

    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;

    //posição do player
    int playerX = 100; 
    int playerY = 100;
    int playerSpeed = 4;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.PINK);
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
        
        try {
            Thread.sleep(16); // ~60 FPS
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        }
    }

    }
public void update(){
if (keyHandler.upPressed == true){
    playerY -= playerSpeed;
}
else if (keyHandler.downPressed == true){
    playerY += playerSpeed;
}
else if (keyHandler.leftPressed == true){
    playerX -= playerSpeed;
}
else if (keyHandler.rightPressed == true){
    playerX += playerSpeed;
}


}
public void paintComponent(Graphics g){
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D)g;
    g2.setColor(Color.CYAN);
    g2.fillRect(playerX, playerY, tileSize, tileSize); //pode ser mudado, apenas o tamanho do desenho
    g2.dispose();//ajuda a salvar memória, é uma boa prática


}
}

