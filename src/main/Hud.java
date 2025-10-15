package main;

public class Hud {
    private GamePanel gp;
    private int life = 5;
    private int wave = 1;
    private boolean gameOverState = false;


    private long lastDamageTime = 0;
    private final long cooldown = 1000;

    public Hud (GamePanel gp){
        this.gp = gp;
        System.out.println("Wave:"+ wave);
        System.out.println("Life:"+ life);

    }

    public void update(){
        if (gameOverState) return;

        int danoRecebido = gp.getWaveManager().getDamageTakenThisFrame();

        if (danoRecebido > 0){
            long currentTime = System.currentTimeMillis();


            if ((currentTime - lastDamageTime) > cooldown && life > 0) {
                life -= danoRecebido;
                lastDamageTime = currentTime;

                System.out.println("Wave:"+ wave);
                System.out.println("Life:"+ life);

                if (life < 0 ){
                    life = 0;
                    System.out.println("Game Over");

                        if (life <= 0 ){
                        life = 0;
                        gameOverState = false; // (MUDAR PARA TRUE, ESTÁ FALSE PARA TESTAR
                        System.out.println("====================");
                        System.out.println("     GAME OVER      ");
                        System.out.println("====================");
                    }
                }

            }
        }
        int currentWaveManagerWave = gp.getWaveManager().getCurrentWave();
        if (wave != currentWaveManagerWave) {
            wave = currentWaveManagerWave;
            System.out.println("Wave:"+ wave);
        }
    }

    public int getLife() {
        return life;
    }

    public int getWave() {
        return wave;
    }

    public boolean GameOver() { return gameOverState; }

}