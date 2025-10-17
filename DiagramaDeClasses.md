```mermaid

classDiagram
    class Main {
        +main(String[] args)
    }

    class GamePanel {
        &lt;&lt;JPanel, Runnable&gt;&gt;
        -int tileSize
        -int maxScreenCol
        -int maxScreenRow
        -int FPS
        -Thread gameThread
        -Player player
        -Monster monster
        -KeyHandler keyHandler
        -TileManager tileManager
        -CollisionCheck collisionCheck
        -Hud hud
        +GamePanel()
        +startThread()
        +run()
        +update()
        +paintComponent(Graphics g)
    }

    class KeyHandler {
        &lt;&lt;KeyListener&gt;&gt;
        +boolean upPressed
        +boolean downPressed
        +boolean leftPressed
        +boolean rightPressed
        +keyPressed(KeyEvent e)
        +keyReleased(KeyEvent e)
    }

    class Entity {
        +int x
        +int y
        +int speed
        +String direction
        +BufferedImage up1, up2, down1, down2, left1, left2, right1, right2
        +int spriteCounter
        +int spriteNum
        +Rectangle solidArea
        +boolean colisionON
    }

    class Player {
        -GamePanel gp
        -KeyHandler keyH
        +Player(GamePanel gp, KeyHandler keyH)
        +getPlayerImage()
        +setDefaultValues()
        +update()
        +draw(Graphics2D g2)
    }

    class Monster {
        -GamePanel gp
        +Monster(GamePanel gp)
        +getMonsterImage()
        +setDefaultValues()
        +update()
        +draw(Graphics2D g2)
    }

    class TileManager {
        -GamePanel gp
        +Tile[] tile
        +int[][] mapTileNum
        +TileManager(GamePanel gp)
        +getTileImage()
        +loadMap()
        +draw(Graphics2D g2)
    }

    class Tile {
        +BufferedImage image
        +boolean colision
    }

    class CollisionCheck {
        -GamePanel gp
        +CollisionCheck(GamePanel gp)
        +checkTile(Entity entity)
    }

    class Hud {
        -GamePanel gp
        -int life
        -int wave
        -long lastDamageTime
        -long cooldown
        +Hud(GamePanel gp)
        +update()
        +getLife()
        +getWave()
    }

    Main --&gt; GamePanel
    GamePanel --o Player
    GamePanel --o Monster
    GamePanel --o KeyHandler
    GamePanel --o TileManager
    GamePanel --o CollisionCheck
    GamePanel --o Hud
    Player --|&gt; Entity
    Monster --|&gt; Entity
    Player ..&gt; KeyHandler
    Player ..&gt; GamePanel
    Monster ..&gt; GamePanel
    TileManager ..&gt; GamePanel
    CollisionCheck ..&gt; GamePanel
    CollisionCheck ..&gt; Entity
    Hud ..&gt; GamePanel
    TileManager "1" --o "10" Tile