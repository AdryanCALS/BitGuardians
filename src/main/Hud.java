package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class Hud {
    private GamePanel gp;
    private int life = 5;
    private int wave = 1;
    private int gold = 0;
    private boolean gameOverState = false;

    // Fonte salva em memória para evitar recriação constante (Eficiência)
    private Font hudFont;

    private long lastDamageTime = 0;
    private final long cooldown = 1000;

    public Hud (GamePanel gp){
        this.gp = gp;
        // Instancia a fonte apenas uma vez
        hudFont = new Font("Arial", Font.BOLD, 20);
    }

    public void addGold(int amount) {
        gold += amount;
    }

    public boolean spendGold(int amount){
        if(gold >= amount){
            gold -= amount;
            return true;
        } else {
            return false;
        }
    }

    public void update(){
        if (gameOverState) return;

        int danoRecebido = gp.getWaveManager().getDamageTakenThisFrame();

        if (danoRecebido > 0){
            long currentTime = System.currentTimeMillis();

            if ((currentTime - lastDamageTime) > cooldown && life > 0) {
                life -= danoRecebido;
                lastDamageTime = currentTime;

                if (life <= 0 ){
                    life = 0;
                    gameOverState = true;
                }
            }
        }

        // Atualiza a wave visual
        int currentWaveManagerWave = gp.getWaveManager().getCurrentWave();
        if (wave != currentWaveManagerWave) {
            wave = currentWaveManagerWave;
        }
    }

    public void draw(Graphics2D g2) {
        // se estivermos no menu principal ou seleção, talvez não queira desenhar,
        // mas como esses menus desenham um fundo preto por cima, não há problema em desenhar aqui.

        g2.setFont(hudFont);
        g2.setColor(Color.WHITE);

        // posicionamento no canto superior esquerdo
        int x = 20;
        int y = 40;
        int lineHeight = 25;

        g2.drawString("Wave: " + wave, x, y);
        g2.drawString("Life: " + life, x, y + lineHeight);
        g2.drawString("Gold: " + gold, x, y + lineHeight * 2);
    }

    public int getLife() { return life; }
    public int getWave() { return wave; }
    public int getGold() { return gold; }
    public boolean GameOver() { return gameOverState; }
}