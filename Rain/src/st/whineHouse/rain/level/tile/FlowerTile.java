package st.whineHouse.rain.level.tile;

import st.whineHouse.rain.gx.Screen;
import st.whineHouse.rain.gx.Sprite;

/**
 * FlowerTile som är en Tile-klass.
 * Används som speifik blom-sprite. Används för estetik.
 * @author Winston Jones
 *
 */
public class FlowerTile extends Tile {

	public FlowerTile(Sprite sprite) {
		super(sprite);
	}
	
	public void render(int x, int y, Screen screen){
		screen.renderTile(x << 4, y << 4, this);
	}

}
