package st.whineHouse.raincloud.tile.spawn_level;

import st.whineHouse.raincloud.graphics.DomainScreen;
import st.whineHouse.raincloud.graphics.Sprite;
import st.whineHouse.raincloud.tile.Tile;

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
	
	public void render(int x, int y, DomainScreen screen){
		screen.renderTile(x << 4, y << 4, this);
	}

}
