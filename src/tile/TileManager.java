package tile;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;


public class TileManager {
    private GamePanel gp;
    private Tile[] tile;
    private int[][] mapTileNum;
    BufferedImage background;

    public TileManager(GamePanel gp){
        this.gp = gp;

        tile = new Tile[10];
        mapTileNum = new int[gp.getMaxScreenCol()][gp.getMaxScreenRow()];

        getTileImage();
        loadMap();
    }

    public void getTileImage(){
        try{
            background = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/tiles/background.png")));

            tile[0]= new Tile();
            tile[0].setImage(ImageIO.read(getClass().getResourceAsStream("/res/tiles/floor01.png")));

            tile[1]= new Tile();
            tile[1].setImage(ImageIO.read(getClass().getResourceAsStream("/res/tiles/floor02.png")));

            tile[2]= new Tile();
            tile[2].setImage(ImageIO.read(getClass().getResourceAsStream("/res/tiles/floor03.png")));

            tile[3]= new Tile();
            tile[3].setImage(ImageIO.read(getClass().getResourceAsStream("/res/tiles/cloud.png")));
            tile[3].setColision(true);

            tile[4]= new Tile();
            tile[4].setImage(ImageIO.read(getClass().getResourceAsStream("/res/tiles/wall01.png")));
            tile[4].setColision(true);

            tile[5]= new Tile();
            tile[5].setImage(ImageIO.read(getClass().getResourceAsStream("/res/tiles/wall02.png")));
            tile[5].setColision(true);

            tile[6]= new Tile();
            tile[6].setImage(ImageIO.read(getClass().getResourceAsStream("/res/tiles/sky.png")));
            tile[6].setColision(true);

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

            while(col < gp.getMaxScreenCol() && row<gp.getMaxScreenRow()){
                String line = bufferedReader.readLine();

                while(col < gp.getMaxScreenCol()){
                    String[] numbers = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] = num;
                    col++;
                }
                if(col == gp.getMaxScreenCol()){
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
        if (background != null) {
            g2.drawImage(background, 0, 0, gp.getScreenWidth(), gp.getScreenHeight(), null);
        }

        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;

        while(col<gp.getMaxScreenCol() && row < gp.getMaxScreenRow()){
            int tileNum = mapTileNum[col][row];

            // 2. MODIFICAÇÃO: PULAR O DESENHO DO TILE DE CÉU ANTIGO
            // Se o tile for 6 (céu), nós NÃO desenhamos ele, para que o background apareça.
            if (tileNum != 6 && tileNum != 3) {
                g2.drawImage(tile[tileNum].getImage(), x, y, gp.getTileSize(), gp.getTileSize(), null);
            }

            col++;
            x += gp.getTileSize();
            if(col == gp.getMaxScreenCol()) {
                col = 0;
                x = 0;
                row++;
                y += gp.getTileSize();
            }
        }
    }

    public Tile[] getTile() {
        return tile;
    }

    public int[][] getMapTileNum() {
        return mapTileNum;
    }
}