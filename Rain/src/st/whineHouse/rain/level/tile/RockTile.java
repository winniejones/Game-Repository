package st.whineHouse.rain.level.tile;

import st.whineHouse.rain.gx.Screen;
import st.whineHouse.rain.gx.Sprite;

/**
 * RockTile som är en Tile-klass
 * Används för att en specifik sten ska ritas upp och om den är solid(för kollision).
 * 
 * @author Winston Jones
 *
 */
public class RockTile extends Tile {

	public RockTile(Sprite sprite) {
		super(sprite);
	}
	
	public void render(int x, int y, Screen screen){
		screen.renderTile(x << 4, y << 4, this);
	}
	
	public boolean solid(){
		return true;
	}

}
