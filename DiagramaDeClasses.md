```mermaid

classDiagram
    class Main {
        +main(String[] args)
    }

    class GamePanel {
        <<JPanel, Runnable>>
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

    Main --> GamePanel : cria
    GamePanel o-- Player : compõe 1
    GamePanel o-- Monster : compõe 1
    GamePanel o-- KeyHandler : compõe 1
    GamePanel o-- TileManager : compõe 1
    GamePanel o-- CollisionCheck : compõe 1
    
    Player --|> Entity : herda de
    Monster --|> Entity : herda de

    Player ..> KeyHandler : depende de
    Player ..> GamePanel : depende de
    Monster ..> GamePanel : depende de
    TileManager ..> GamePanel : depende de
    CollisionCheck ..> GamePanel : depende de
    CollisionCheck ..> Entity : depende de
    
    TileManager o-- "10" Tile : compõe