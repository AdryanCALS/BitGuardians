package main;

import entity.Player;
import entity.Monster;
import entity.Projectile;
import tile.TileManager;

import java.awt.*;
import java.util.List;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{
    private final int originalSize = 16;
    private final int scale = 3 ;

    private final int tileSize = originalSize * scale;
    private final int maxScreenCol = 16;
    private final int maxScreenRow = 9;
    private final int screenWidth = tileSize * maxScreenCol;
    private final int screenHeight = tileSize * maxScreenRow;

    private java.util.List<entity.Projectile> projectiles = new java.util.ArrayList<>();

    private final int FPS = 60;

    public final String classEspadachim = "Espadachim";
    public final String classMago = "Mago";
    public final int menuState = 0;
    public final int characterMenuState = 1;
    public final int playState = 3;
    public final int gameOverState = 4;
    public int gameState;
    public int menuNum = 0;
    public int characterMenuNum = 0;
    public int gameOverNum = 0;

    private TileManager tileManager;
    private KeyHandler keyHandler = new KeyHandler();
    private MouseHandler mouseHandler = new MouseHandler();
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
        this.addMouseListener(mouseHandler);

        this.setFocusable(true);
        this.tileManager = new TileManager(this);
        this.collisionCheck = new CollisionCheck(this);
        this.player = new Player(this, keyHandler, mouseHandler);
        this.hud = new Hud(this);
        this.waveManager = new WaveManager(this);

        gameState = menuState;

    }

    public void startThread() {

        gameThread = new Thread(this);
        gameThread.start();
    }

    public int getScale(){
        return scale;
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

            hud.update();

            if (hud.GameOver()) {
                gameState = gameOverState;
                return;
            }

            player.update();
            waveManager.update();
            java.util.Iterator<entity.Projectile> iterator = projectiles.iterator();
            while (iterator.hasNext()) {
                entity.Projectile p = iterator.next();
                if (p.getLife() <= 0) {
                    iterator.remove(); // Maneira segura de remover itens durante a iteração
                } else {
                    p.update();
                }
            }

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
        else if (gameState == gameOverState) {

            if (keyHandler.isUpPressed()) {
                gameOverNum--;
                if (gameOverNum < 0) {
                    gameOverNum = 1;
                }
                keyHandler.setUpPressed(false);
            }

            if (keyHandler.isDownPressed()) {
                gameOverNum++;
                if (gameOverNum > 1) {
                    gameOverNum = 0;
                }
                keyHandler.setDownPressed(false);
            }

            if (keyHandler.isEnterPressed()) {
                if (gameOverNum == 0) {
                    gameState = menuState;
                    retry(); // reseta o jogo
                } else if (gameOverNum == 1) {
                    System.exit(0);
                }
                keyHandler.setEnterPressed(false);
            }
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        if (gameState == menuState) {
            drawMenu(g2);
        }
        else {

            tileManager.draw(g2);
            player.draw(g2);
            waveManager.draw(g2);

            for (entity.Projectile p : projectiles) {
                p.draw(g2);
            }

            hud.draw(g2);

            // desenha o menu de seleção
            if (gameState == characterMenuState) {
                drawCharacterMenu(g2);
            }

            // desenha a tela de Game Over
            if (gameState == gameOverState) {
                drawGameOverScreen(g2);
            }
        }

        g2.dispose();
    }

    public void drawMenu(Graphics2D g2) {
        // fundo
        g2.setColor(Color.black);
        g2.fillRect(0, 0, screenWidth, screenHeight);

        // título do jogo
        g2.setFont(new Font("Arial", Font.BOLD, 70));
        String title = "BitGuardians";
        g2.setColor(Color.white);
        int x = getXforCenteredText(g2, title);
        int y = tileSize * 2;
        g2.drawString(title, x, y);

        // opções do menu
        g2.setFont(new Font("Arial", Font.PLAIN, 40));

        // opção 1
        String textJogar = "JOGAR";
        x = getXforCenteredText(g2, textJogar);
        y += tileSize * 3;
        g2.drawString(textJogar, x, y);
        if (menuNum == 0) {
            g2.drawString(">", x - tileSize, y);
        }

        // opção 2
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

        // posições Y fixas para o texto
        int textYSwordsman = tileSize * 2 + tileSize * 3;
        int textYMage = textYSwordsman + (int)(tileSize * 1.5);

        // largura do texto
        FontMetrics fm = g2.getFontMetrics();
        int xCentered;
        int textLength;
        int imageOffsetX = tileSize / 2; // margem entre o texto e a imagem


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

        // impede que se mova no menu
        String currentSelectedClass = (characterMenuNum == 0) ? classEspadachim : classMago;
        player.setCharacterClass(currentSelectedClass);
        player.getPlayerImage();

    }

    public void retry() {
        // reseta posições e vida do jogador
        player.setDefaultValues();

        // primeiro recria o HUD para que o estado de GameOver volte a ser 'false'
        hud = new Hud(this);

        // depois recria o WaveManager. agora ele verá que não é mais GameOver e iniciará a Wave 1 corretamente
        waveManager = new WaveManager(this);

        // limpa projéteis
        projectiles.clear();

        // reseta variáveis
        gameOverNum = 0;
        menuNum = 0;
    }

    public void drawGameOverScreen(Graphics2D g2) {

        // fundo
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, screenWidth, screenHeight);

        int x;
        int y;
        String text;

        g2.setFont(new Font("Arial", Font.BOLD, 90));
        text = "GAME OVER";

        // efeito no texto
        g2.setColor(Color.black);
        x = getXforCenteredText(g2, text);
        y = tileSize * 4;
        g2.drawString(text, x+5, y+5); // sombra

        // texto principal vermelho
        g2.setColor(Color.red);
        g2.drawString(text, x, y);

        // opções do menu
        g2.setFont(new Font("Arial", Font.BOLD, 40));


        text = "Menu Principal";
        x = getXforCenteredText(g2, text);
        y += tileSize * 3;

        if (gameOverNum == 0) {
            g2.setColor(Color.yellow);
            g2.drawString(">", x - 40, y);
        } else {
            g2.setColor(Color.white);
        }
        g2.drawString(text, x, y);

        text = "Sair do Jogo";
        x = getXforCenteredText(g2, text);
        y += 55;

        if (gameOverNum == 1) {
            g2.setColor(Color.yellow);
            g2.drawString(">", x - 40, y);
        } else {
            g2.setColor(Color.white);
        }
        g2.drawString(text, x, y);
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

    public List<Projectile> getProjectiles() {
        return projectiles;
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

    public Player getPlayer() { return player; }

}