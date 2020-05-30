package st.whineHouse.raincloud.graphics;

import st.whineHouse.raincloud.tile.Tile;

public class DomainScreen {
    public int width, height;
    public int[] pixels;
    public final int MAP_SIZE = 16;
    public final int MAP_SIZE_MASK = MAP_SIZE-1;
    protected final int ALPHA_COL = 0xffff00ff;
    public int xOffset, yOffset;

    public void renderTile(int xp, int yp, Tile tile){
        xp -= xOffset;
        yp -= yOffset;
        for(int y =0; y< tile.sprite.SIZE; y++){
            int ya = y + yp;
            for(int x =0; x < tile.sprite.SIZE; x++){
                int xa = x + xp;
                if(xa < - tile.sprite.SIZE || xa >= width || ya < 0 || ya >= height) break;
                if (xa < 0) xa = 0;
                pixels[xa + ya*width]= tile.sprite.pixels[x + y * tile.sprite.SIZE];
            }
        }
    }
}
