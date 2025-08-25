package tile;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int[][] mapTileNum;

    public TileManager(GamePanel gp){
        this.gp = gp;

        tile = new Tile[10];//10 possiveis assets de background, podemos aumentar dps
        mapTileNum = new int[gp.maxScreenCol][gp.maxScreenRow];

        getTileImage();
        loadMap();
    }

    public void getTileImage(){
        try{

            tile[0]= new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/floor01.png"));

            tile[1]= new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/floor02.png"));

            tile[2]= new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/floor03.png"));

            tile[3]= new Tile();
            tile[3].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/cloud.png"));
            tile[3].colision = true;

            tile[4]= new Tile();
            tile[4].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/wall01.png"));
            tile[4].colision = true;

            tile[5]= new Tile();
            tile[5].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/wall02.png"));
            tile[5].colision = true;

            tile[6]= new Tile();
            tile[6].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/sky.png"));
            tile[6].colision = true;

        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void loadMap(){
        try{
            InputStream inputStream = getClass().getResourceAsStream("/res/maps/mapa_base.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            int col = 0;
            int row = 0;

            while(col < gp.maxScreenCol && row<gp.maxScreenRow){
                String line = bufferedReader.readLine();

                while(col < gp.maxScreenCol){
                    String[] numbers = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);//transformando de uma STring para um int

                    mapTileNum[col][row] = num;
                    col++;
                }
                if(col == gp.maxScreenCol){
                    col = 0;
                    row++;
                }
            }
            bufferedReader.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void draw(Graphics2D g2){
        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;

        while(col<gp.maxScreenCol && row < gp.maxScreenRow){

            int tileNum = mapTileNum[col][row];//guarda os valores

            g2.drawImage(tile[tileNum].image, x, y, gp.tileSize, gp.tileSize,null);
            col++;
            x+=gp.tileSize;
            if(col == gp.maxScreenCol){
                col = 0;
                x = 0;
                row++;
                y += gp.tileSize;
            }
        }
    }
}
