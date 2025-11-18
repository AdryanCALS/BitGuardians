package main;

import entity.Monster;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import entity.TankMonster;
import entity.FastMonster;

public class WaveManager {

    private GamePanel gp;
    private List<Monster> activeMonsters;
    private Random random;

    private int currentWave = 1;
    private int monstersToSpawnInWave;
    private int spawnedCount = 0;
    private boolean waveInProgress = false;


    private final long SPAWN_INTERVAL = 500;
    private long lastSpawnTime = 0;

    private final long WAVE_INTERVAL = 10000;
    private long waveEndTime = 0;

    private int[] spawnYPositions;
    private int damageTakenThisFrame = 0;


    public WaveManager(GamePanel gp) {
        this.gp = gp;
        this.activeMonsters = new ArrayList<>();
        this.random = new Random();

        int tileSize = gp.getTileSize();
        this.spawnYPositions = new int[]{
                tileSize * 3 + (tileSize / 2),
                tileSize * 4 + (tileSize / 2),
                tileSize * 5 + (tileSize / 2),
                tileSize * 6 + (tileSize / 2),

        };

        startNextWave();
    }

    public void damageMonster(int index, int damage) {
        if (index != -1) {
            try {
                Monster monster = activeMonsters.get(index);
                monster.takeDamage(damage);
                if (monster.getLife() <= 0) {
                    gp.getHud().addGold(1);
                    activeMonsters.remove(index);
                }
            } catch (IndexOutOfBoundsException e) {
                // Ignora o erro, pois o monstro pode já ter sido removido por outro processo
            }
        }
    }

    public List<Monster> getActiveMonsters(){
        return activeMonsters;
    }
    public void update() {
        if (gp.getHud().GameOver()) {
            return;
        }

        long currentTime = System.currentTimeMillis();
        damageTakenThisFrame = 0;

        if (waveInProgress && spawnedCount < monstersToSpawnInWave) {
            if (currentTime - lastSpawnTime >= SPAWN_INTERVAL) {
                spawnMonster();
                lastSpawnTime = currentTime;
            }
        }

        Iterator<Monster> iterator = activeMonsters.iterator();
        while (iterator.hasNext()) {
            Monster monster = iterator.next();
            monster.update();


            if (monster.getX() <= 0) {
                damageTakenThisFrame++;

            }
        }

        if (waveInProgress && spawnedCount >= monstersToSpawnInWave) {
            waveInProgress = false;
            waveEndTime = currentTime;
        }

        if (!waveInProgress && currentTime - waveEndTime >= WAVE_INTERVAL && !gp.getHud().GameOver()) {
            currentWave++;
            startNextWave();
        }
    }

    private void startNextWave() {
        if (gp.getHud().GameOver()) return;

        monstersToSpawnInWave = 1 + (currentWave * 2);
        spawnedCount = 0;
        waveInProgress = true;
        lastSpawnTime = System.currentTimeMillis();
    }

    private void spawnMonster() {
        int spawnYIndex = random.nextInt(spawnYPositions.length);
        int startY = spawnYPositions[spawnYIndex];
        int startX = gp.getScreenWidth() + gp.getTileSize();

        Monster newMonster;
        int currentWave = getCurrentWave();

        // Lógica de Permutação e Sorteio de Tipo de Monstro

        int roll = random.nextInt(100); // Rola um número de 0 a 99

        if (currentWave == 1) {
            // Wave 1: Apenas Monster Base (100% de chance)
            newMonster = new Monster(gp, startX, startY);
        } else if (currentWave == 2) {
            // Wave 2: Monster Base e Monster Rápido (Swift)
            // (Exemplo: 70% Base, 30% Swift)
            if (roll < 70) {
                newMonster = new Monster(gp, startX, startY);
            } else {
                newMonster = new FastMonster(gp, startX, startY);
            }
        } else if (currentWave >= 3) {
            // Wave 3+: Monster Base, Monster Rápido (Swift) e Monster Lento (Tank)
            // (Exemplo: 40% Base, 40% Swift, 20% Tank)
            if (roll < 40) {
                newMonster = new Monster(gp, startX, startY);
            } else if (roll < 80) { // 40 + 40 = 80
                newMonster = new FastMonster(gp, startX, startY);
            } else { // O restante (20%)
                newMonster = new TankMonster(gp, startX, startY);
            }
        } else {
            // Fallback para Monster Base
            newMonster = new Monster(gp, startX, startY);
        }


        activeMonsters.add(newMonster);

        spawnedCount++;
    }

    public void draw(Graphics2D g2) {
        for (Monster monster : activeMonsters) {
            monster.draw(g2);
        }
    }

    public int getCurrentWave() {
        return currentWave;
    }

    public int getDamageTakenThisFrame() {
        return damageTakenThisFrame;
    }
}