# BitGuardians 🛡️⚔️

![Java](https://img.shields.io/badge/Java-17+-orange?style=for-the-badge&logo=java)
![Swing](https://img.shields.io/badge/Swing-GUI-blue?style=for-the-badge&logo=oracle)

**BitGuardians** é um jogo 2D com elementos de Tower Defense, desenvolvido em Java. Este projeto foi criado como parte da disciplina de Linguagem de Programação Orientada a Objetos da Universidade de Pernambuco.

O objetivo do jogo é proteger a base no lado esquerdo da tela, impedindo que hordas de monstros que avançam em ondas consigam atravessar.

## ✨ Funcionalidades

* **Sistema de Ondas de Inimigos:** Os monstros surgem em ondas progressivamente mais difíceis (`WaveManager`).
* **Seleção de Personagens:** O jogador pode escolher entre duas classes distintas antes de iniciar o jogo (`GamePanel`).
* **Mecânicas de Combate:** Cada classe possui um tipo de ataque único para derrotar os inimigos.
* **Colisão Física:** Implementação de um sistema de colisão com os limites do mapa (`CollisionCheck`) e um efeito de *knockback* ao tocar nos monstros (`Player`).
* **Interface Gráfica Simples:** Menus e HUD construídos com Java Swing.

## ⚔️ Classes Jogáveis

O jogo apresenta duas classes com estilos de combate diferentes:

1.  **Espadachim:** Um guerreiro focado no combate corpo a corpo. Seu ataque é um golpe de espada que atinge inimigos em uma área próxima.
2.  **Mago:** Um mestre das artes arcanas que ataca à distância. Ele dispara projéteis mágicos que viajam pela tela para atingir os inimigos.

## 🚀 Como Executar

Para compilar e rodar o projeto, você precisará ter o **JDK 17 ou superior** instalado.

### Via IDE (Recomendado)

1.  Clone ou baixe este repositório.
2.  Abra o projeto na sua IDE Java preferida (IntelliJ, Eclipse, etc.).
3.  Localize o arquivo `src/main/Main.java`.
4.  Execute o método `main` para iniciar o jogo.

### Via Linha de Comando

1.  Navegue até a pasta raiz do projeto.
2.  Compile todos os arquivos `.java`:
    ```bash
    javac -d out src/main/*.java src/entity/*.java src/tile/*.java
    ```
3.  Execute o jogo:
    ```bash
    java -cp out main.Main
    ```

## 🎮 Controles

| Ação              | Tecla          |
| ----------------- | -------------- |
| Mover para Cima   | `W`            |
| Mover para Baixo  | `S`            |
| Mover para Esquerda| `A`            |
| Mover para Direita| `D`            |
| Atacar            | `Barra de Espaço` |
| Confirmar (Menus) | `Enter`        |


## 📂 Estrutura do Projeto

O código-fonte está organizado nos seguintes pacotes:

* `main`: Contém as classes principais que gerenciam o jogo, como `GamePanel`, `Main`, e os gerenciadores de eventos (`KeyHandler`, `CollisionCheck`).
* `entity`: Define as entidades do jogo, como `Player`, `Monster` e `Projectile`. Todas herdam da classe base `Entity`.
* `tile`: Responsável pelo gerenciamento e renderização dos `Tiles` (blocos) que compõem o mapa do jogo.

## Justificativas de Design

Desde o início do projeto queriamos construir algo mais dinâmico que mesmo assim mantesse a essência de um ``tower defense``. Para isso, fizemos com que o jogador pudesse controlar a sua "torre" (personagem jogável) para poder atacar os inimigos e impedi-los de chegar ao canto esquerdo da tela, de certa forma se assemelhando à [Plants vs Zombies](https://pt.wikipedia.org/wiki/Plants_vs._Zombies) porém com controle __dinâmico__ do player.

O jogo não tem um tema em específico porém tem forte inspiração em elementos clássicos de `RPG` como as classes dos players e os monstros .


## UML - Diagrama de Classes

Abaixo está um diagrama que representa a arquitetura atual do projeto.

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
```
