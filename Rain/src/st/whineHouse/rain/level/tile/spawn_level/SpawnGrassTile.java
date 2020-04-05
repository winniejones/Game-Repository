package st.whineHouse.rain.level.tile.spawn_level;

import st.whineHouse.rain.gx.Screen;
import st.whineHouse.rain.gx.Sprite;
import st.whineHouse.rain.level.tile.Tile;

/**
 * SpawnGrassTile är en Tile-klass.
 * Denna klass är används för att översätta en 'Tile' till en specifik sprite tillhörande Spawnlevel.
 * Denna Tile representerar gräs.
 * 
 * @author Winston Jones
 *
 */
public class SpawnGrassTile extends Tile {

	public SpawnGrassTile(Sprite sprite) {
		super(sprite);
	}
	
	public void render(int x, int y, Screen screen){
		screen.renderTile(x << 4, y << 4, this);
	}

}