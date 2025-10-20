```mermaid

classDiagram
    class Main {
        +main(String[] args)
    }

    class GamePanel {
        <<JPanel, Runnable>>
        -int tileSize
        -Player player
        -WaveManager waveManager
        -TileManager tileManager
        -CollisionCheck collisionCheck
        -KeyHandler keyHandler
        -Hud hud
        -List~Projectile~ projectiles
        +GamePanel()
        +startThread()
        +run()
        +update()
        +paintComponent(Graphics g)
    }

    class KeyHandler {
        <<KeyListener>>
        +boolean upPressed
        +boolean downPressed
        +boolean leftPressed
        +boolean rightPressed
        +boolean attackPressed
        +keyPressed(KeyEvent e)
        +keyReleased(KeyEvent e)
    }

    class Entity {
        -int life
        -int x, y
        -int speed
        -String direction
        -Rectangle solidArea
        +getLife()
        +setLife(int)
        +getX()
        +getY()
    }

    class Player {
        -String characterClass
        -Rectangle attackArea
        -boolean attacking
        +Player(GamePanel, KeyHandler)
        +setCharacterClass(String)
        +update()
        +attack()
        +draw(Graphics2D g2)
    }
    
    class Monster {
        +Monster(GamePanel, int, int)
        +takeDamage(int)
        +update()
        +draw(Graphics2D g2)
    }

    class Projectile {
        +Projectile(GamePanel)
        +set(int, int, String)
        +update()
        +draw(Graphics2D g2)
    }

    class TileManager {
        -Tile[] tile
        -int[][] mapTileNum
        +TileManager(GamePanel)
        +loadMap()
        +draw(Graphics2D g2)
    }

    class Tile {
        -BufferedImage image
        -boolean colision
    }

    class CollisionCheck {
        +checkTile(Entity)
        +checkPlayerAttack(Player, List~Monster~)
        +checkEntity(Entity, List~Monster~)
        +checkPlayerMonsterCollision(Player, List~Monster~)
    }

    class WaveManager {
        -List~Monster~ activeMonsters
        -int currentWave
        +WaveManager(GamePanel)
        +update()
        +damageMonster(int, int)
        +draw(Graphics2D g2)
    }
    
    class Hud {
      -int life
      -int wave
      +Hud(GamePanel)
      +update()
    }

    Main --> GamePanel : inicia
    GamePanel "1" o-- "1" Player
    GamePanel "1" o-- "1" WaveManager
    GamePanel "1" o-- "1" TileManager
    GamePanel "1" o-- "1" CollisionCheck
    GamePanel "1" o-- "1" KeyHandler
    GamePanel "1" o-- "1" Hud
    GamePanel "1" *-- "0..*" Projectile

    WaveManager "1" *-- "0..*" Monster 
    TileManager "1" *-- "0..*" Tile

    Player --|> Entity
    Monster --|> Entity
    Projectile --|> Entity

    Player ..> KeyHandler
    Player ..> CollisionCheck
    Projectile ..> CollisionCheck
    
    GamePanel ..> Entity : gerencia entidades
    Entity ..> GamePanel : interage com o painel
