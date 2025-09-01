package main;

public class Hud {
    GamePanel gp;
    public int life = 5;
    public int wave = 1;

   
    private long lastDamageTime = 0; 
    private final long cooldown = 1000; 

    public Hud (GamePanel gp){
        this.gp = gp;
        System.out.println("Wave:"+ wave);
        System.out.println("Life:"+ life);
       
    }
        
    public void update(){
        
        if (gp.monster.x == 0){
            
            long currentTime = System.currentTimeMillis();

            
            if ((currentTime - lastDamageTime) > cooldown && life > 0) {
                life -= 1; 
                lastDamageTime = currentTime; 
                
                System.out.println("Wave:"+ wave);
                System.out.println("Life:"+ life);
                    
                if (life ==0 ){
                    System.out.println("Game Over");
                }
                
            }
        }
    }
}