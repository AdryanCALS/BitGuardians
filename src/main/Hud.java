package main;

import entity.Player;
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

    public Hud(GamePanel gp) {
        this.gp = gp;
        // Instancia a fonte apenas uma vez no construtor
        hudFont = new Font("Arial", Font.BOLD, 20);
    }

    public void addGold(int amount) {
        gold += amount;
    }

    public boolean spendGold(int amount) {
        if (gold >= amount) {
            gold -= amount;
            return true;
        } else {
            return false;
        }
    }

    public void update() {
        if (gameOverState) return;

        // Verifica dano recebido (monstros que passaram)
        int danoRecebido = gp.getWaveManager().getDamageTakenThisFrame();

        if (danoRecebido > 0) {
            long currentTime = System.currentTimeMillis();

            if ((currentTime - lastDamageTime) > cooldown && life > 0) {
                life -= danoRecebido;
                lastDamageTime = currentTime;

                if (life <= 0) {
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
        g2.setFont(hudFont);
        g2.setColor(Color.WHITE);

        // --- Lado Esquerdo (Status do Jogo) ---
        int xLeft = 20;
        int y = 40;
        int lineHeight = 25;

        g2.drawString("Wave: " + wave, xLeft, y);
        g2.drawString("Life: " + life, xLeft, y + lineHeight);
        g2.drawString("Gold: " + gold, xLeft, y + lineHeight * 2);

        // --- Lado Direito (Upgrades) ---
        Player player = gp.getPlayer();
        if (player != null) {
            // Configuração do alinhamento à direita
            // Ajuste o 220 se precisar empurrar mais para a esquerda ou direita
            int xRight = gp.getScreenWidth() - 220;
            int yRight = 40;

            // Título
            g2.setColor(Color.YELLOW);
            g2.drawString("UPGRADES", xRight + 30, yRight);
            g2.setColor(Color.WHITE);

            // [1] Dano
            // Agora pegamos o custo específico de Dano
            String damageCost = String.valueOf(player.getDamageCost());
            g2.drawString("[1] Dano: " + damageCost + "G", xRight, yRight + lineHeight);

            // [2] Velocidade
            String speedCost;
            if (player.isSpeedMaxed()) {
                speedCost = "MAX";
                g2.setColor(Color.GRAY); // Indica visualmente que não pode comprar
            } else {
                speedCost = player.getSpeedCost() + "G";
                g2.setColor(Color.WHITE);
            }
            g2.drawString("[2] Velocidade: " + speedCost, xRight, yRight + lineHeight * 2);

            // [3] Especial (Muda o nome dependendo da classe)
            String specialName;
            if (player.getCharacterClass().equals(gp.getClassEspadachim())) {
                specialName = "Área";
            } else {
                specialName = "Gelo";
            }

            String specialCost;
            if (player.isSpecialMaxed()) {
                specialCost = "MAX";
                g2.setColor(Color.GRAY);
            } else {
                specialCost = player.getSpecialCost() + "G";
                g2.setColor(Color.WHITE);
            }
            g2.drawString("[3] " + specialName + ": " + specialCost, xRight, yRight + lineHeight * 3);
        }
    }

    public int getLife() { return life; }
    public int getWave() { return wave; }
    public int getGold() { return gold; }
    public boolean GameOver() { return gameOverState; }
}