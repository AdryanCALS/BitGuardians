package main;

import entity.Player;
import entity.Monster;
import tile.TileManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;

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

    public final String classEspadachim = "Espadachim";
    public final String classMago = "Mago";
    public final int menuState = 0;
    public final int characterMenuState = 1;
    public final int playState = 3;
    public int gameState;
    public int menuNum = 0;
    public int characterMenuNum = 0;

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

        gameState = menuState;

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
        if ( gameState == playState){
            player.update();
            waveManager.update();
            hud.update();
        } else if (gameState==menuState) {

            if (keyHandler.isDownPressed()|| keyHandler.isUpPressed()){
                if (keyHandler.isUpPressed()){
                    menuNum--;
                    if (menuNum < 0){
                        menuNum= 1;
                    }
                    keyHandler.setUpPressed(false);
                }
                if(keyHandler.isDownPressed()){
                    menuNum++;
                    if(menuNum > 1){
                        menuNum= 0;
                    }
                    keyHandler.setDownPressed(false);
                }
            }
            if (keyHandler.isEnterPressed()) {
                if (menuNum == 0) {
                    gameState = characterMenuState;
                } else if (menuNum == 1) {
                    System.exit(0);
                }
                keyHandler.setEnterPressed(false);
            }
        } else if (gameState == characterMenuState) {
            if (keyHandler.isDownPressed() || keyHandler.isUpPressed()) {
                if (keyHandler.isUpPressed()) {
                    characterMenuNum--;
                    if (characterMenuNum < 0) {
                        characterMenuNum = 1; // 0: Espadachim, 1: Mago
                    }
                    keyHandler.setUpPressed(false);
                }
                if (keyHandler.isDownPressed()) {
                    characterMenuNum++;
                    if (characterMenuNum > 1) {
                        characterMenuNum = 0; // 0: Espadachim, 1: Mago
                    }
                    keyHandler.setDownPressed(false);
                }
            }

            if (keyHandler.isEnterPressed()) {
                String selectedClass = (characterMenuNum == 0) ? classEspadachim : classMago;

                player.setCharacterClass(selectedClass);
                player.getPlayerImage();
                player.setDefaultValues();

                gameState = playState;
                keyHandler.setEnterPressed(false);
            }
        }
    }


    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        if (gameState==menuState) {
            drawMenu(g2);
        } else if (gameState == playState || gameState == characterMenuState) {
            tileManager.draw(g2);
            player.draw(g2);
            waveManager.draw(g2);

            // DESENHA O MENU DE SELEÇÃO POR CIMA DO JOGO
            if (gameState == characterMenuState) {
                drawCharacterMenu(g2);
            }
        }

        g2.dispose();

    }

    public void drawMenu(Graphics2D g2) {
        // Fundo
        g2.setColor(Color.black);
        g2.fillRect(0, 0, screenWidth, screenHeight);

        // Título do Jogo
        g2.setFont(new Font("Arial", Font.BOLD, 70));
        String title = "BitGuardians";
        g2.setColor(Color.white);
        int x = getXforCenteredText(g2, title);
        int y = tileSize * 2;
        g2.drawString(title, x, y);

        // Opções do Menu
        g2.setFont(new Font("Arial", Font.PLAIN, 40));

        // JOGAR
        String textJogar = "JOGAR";
        x = getXforCenteredText(g2, textJogar);
        y += tileSize * 3;
        g2.drawString(textJogar, x, y);
        if (menuNum == 0) {
            g2.drawString(">", x - tileSize, y);
        }

        // SAIR
        String textSair = "SAIR";
        x = getXforCenteredText(g2, textSair);
        y += tileSize * 1.5;
        g2.drawString(textSair, x, y);
        if (menuNum == 1) {
            g2.drawString(">", x - tileSize, y);
        }
    }


    public void drawCharacterMenu(Graphics2D g2) {
        g2.setColor(Color.black);
        g2.fillRect(0, 0, screenWidth, screenHeight);

        g2.setColor(Color.white);
        g2.setFont(new Font("Arial", Font.BOLD, 40));
        String title = "SELECIONE SEU PERSONAGEM";
        int x = getXforCenteredText(g2, title);
        int y = tileSize * 2;
        g2.drawString(title, x, y);

        g2.setFont(new Font("Arial", Font.PLAIN, 30));

        // Posições Y fixas para o texto
        int textYSwordsman = tileSize * 2 + tileSize * 3;
        int textYMage = textYSwordsman + (int)(tileSize * 1.5);

        // Largura do texto (será usado para posicionar a imagem)
        FontMetrics fm = g2.getFontMetrics();
        int xCentered;
        int textLength;
        int imageOffsetX = tileSize / 2; // Offset (margem) entre o texto e a imagem


        String textSwordsman = classEspadachim;
        textLength = fm.stringWidth(textSwordsman);
        xCentered = getXforCenteredText(g2, textSwordsman);


        g2.drawString(textSwordsman, xCentered, textYSwordsman);
        if (characterMenuNum == 0) {
            g2.drawString(">", xCentered - tileSize, textYSwordsman);
        }

        player.setCharacterClass(classEspadachim);
        player.getPlayerImage();


        int imageXSwordsman = xCentered + textLength + imageOffsetX;

        g2.drawImage(player.getDisplayImage(), imageXSwordsman, textYSwordsman - tileSize / 2, tileSize, tileSize, null);



        String textMage = classMago;
        textLength = fm.stringWidth(textMage);
        xCentered = getXforCenteredText(g2, textMage);


        g2.drawString(textMage, xCentered, textYMage);
        if (characterMenuNum == 1) {
            g2.drawString(">", xCentered - tileSize, textYMage);
        }

        player.setCharacterClass(classMago);
        player.getPlayerImage();

        int imageXMage = xCentered + textLength + imageOffsetX;

        g2.drawImage(player.getDisplayImage(), imageXMage, textYMage - tileSize / 2, tileSize, tileSize, null);

        // Impede que se mova no menu
        String currentSelectedClass = (characterMenuNum == 0) ? classEspadachim : classMago;
        player.setCharacterClass(currentSelectedClass);
        player.getPlayerImage();

    }

    public String getClassEspadachim() {
        return classEspadachim;
    }

    public String getClassMago() {
        return classMago;
    }

    public int getXforCenteredText(Graphics2D g2, String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return (screenWidth - length) / 2;
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