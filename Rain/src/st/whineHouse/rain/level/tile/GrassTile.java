package st.whineHouse.rain.level.tile;

import st.whineHouse.rain.gx.Screen;
import st.whineHouse.rain.gx.Sprite;

/**
 * GrassTile är en Tile-klass.
 * Används för att rita upp specifik gräs-sprite.
 * @author winston_8
 *
 */
public class GrassTile extends Tile {

	public GrassTile(Sprite sprite) {
		super(sprite);
	}
	
	public void render(int x, int y, Screen screen){
		screen.renderTile(x << 4, y << 4, this);
	}

}
