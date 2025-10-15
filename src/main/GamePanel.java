package main;

import entity.Player;
import entity.Monster;
import tile.TileManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{
    private final int originalSize = 16;
    private final int scale = 3 ;

    private final int tileSize = originalSize * scale;
    private int maxScreenCol = 16;
    private int maxScreenRow = 9;
    private int screenWidth = tileSize * maxScreenCol;
    private int screenHeight = tileSize * maxScreenRow;


    //FPS
    private int FPS = 60;

    private TileManager tileManager;
    private KeyHandler keyHandler = new KeyHandler();
    private Thread gameThread;
    private Player player;
    private WaveManager waveManager;
    private CollisionCheck collisionCheck;
    private Hud hud;



    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.cyan);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
        this.tileManager = new TileManager(this);
        this.collisionCheck = new CollisionCheck(this);
        this.player = new Player(this, keyHandler);
        this.hud = new Hud(this);
        this.waveManager = new WaveManager(this);

    }




    public void startThread() {

        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run () {

        double drawInterval = 1000000000/FPS;
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
        waveManager.update();
        hud.update();


    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        tileManager.draw(g2);
        player.draw(g2);
        waveManager.draw(g2);

        g2.dispose();


    }

    public int getTileSize() {
        return tileSize;
    }

    public int getMaxScreenCol() {
        return maxScreenCol;
    }

    public int getMaxScreenRow() {
        return maxScreenRow;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public TileManager getTileManager() {
        return tileManager;
    }

    public CollisionCheck getCollisionCheck() {
        return collisionCheck;
    }

    public Hud getHud(){ return hud;}

    public WaveManager getWaveManager() {
        return waveManager;
    }


}