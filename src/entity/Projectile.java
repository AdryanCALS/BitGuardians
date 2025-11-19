package entity;

import main.GamePanel;

import java.awt.*;


public class Projectile extends Entity{
    GamePanel gamePanel;
    private int damage = 1;
    private boolean hasSlowEffect = false;

    public Projectile(GamePanel gamePanel){
        this.gamePanel = gamePanel;
        setLife(1);
    }

    public void setStats(int damage, boolean hasSlowEffect){
        this.damage = damage;
        this.hasSlowEffect = hasSlowEffect;
    }

    public void set(int x, int y, String direction){
        setX(x);
        setY(y);
        setDirection(direction);
        setSpeed(5);
        setSolidArea(new Rectangle(0,0,gamePanel.getTileSize()/2, gamePanel.getTileSize()/2));
    }

    @Override
    public void update(){
        switch (getDirection()){
            case "up": setY(getY()-getSpeed()); break;
            case "down": setY(getY()+getSpeed()); break;
            case "left": setX(getX()-getSpeed()); break;
            case "right": setX(getX()+getSpeed()); break;
        }

        if(getX()<0 || getX()>gamePanel.getScreenWidth()||getY()<0||getY()> gamePanel.getScreenHeight()){
            setLife(0);
        }

        // Verifica colisão
        int monsterIndex = gamePanel.getCollisionCheck().checkEntity(this, gamePanel.getWaveManager().getActiveMonsters());

        if(monsterIndex != -1){
            try {
                // Tenta pegar o monstro para aplicar o efeito
                // Se o monstro tiver sido removido por outro motivo milissegundos antes, o 'get' falha
                // e caímos no 'catch', evitando o crash do jogo.
                Monster target = gamePanel.getWaveManager().getActiveMonsters().get(monsterIndex);

                if (hasSlowEffect) {
                    target.applySlow();
                }

                // Aplica o dano (o método damageMonster já tem proteção interna, mas estamos protegendo o acesso acima)
                gamePanel.getWaveManager().damageMonster(monsterIndex, damage);

            } catch (IndexOutOfBoundsException e) {
                // Se o monstro não existe mais (índice inválido), apenas ignoramos.
                System.out.println("Monstro removido antes do impacto do projétil.");
            }

            // Independente de acertar ou dar erro, o projétil deve ser destruído ao bater em algo
            setLife(0);
        }
    }


    @Override
    public void draw(Graphics2D graphics2D){
        if(hasSlowEffect)graphics2D.setColor(Color.cyan);
        else graphics2D.setColor(new Color(150,0,255));

        graphics2D.fillOval(getX(),getY(),getSolidArea().width, getSolidArea().height);
    }

}