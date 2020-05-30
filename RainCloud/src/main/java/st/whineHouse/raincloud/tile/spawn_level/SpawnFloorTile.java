package st.whineHouse.raincloud.tile.spawn_level;

import st.whineHouse.raincloud.graphics.DomainScreen;
import st.whineHouse.raincloud.graphics.Sprite;
import st.whineHouse.raincloud.tile.Tile;

/**
 * SpawnFloorTile är en Tile-klass.
 * Denna klass är används för att översätta en 'Tile' till en specifik sprite tillhörande Spawnlevel.
 * Denna Tile representerar ett golv.
 * 
 * @author Winston Jones
 *
 */
public class SpawnFloorTile extends Tile {

	public SpawnFloorTile(Sprite sprite) {
		super(sprite);
	}
	
	public void render(int x, int y, DomainScreen screen){
		screen.renderTile(x << 4, y << 4, this);
	}

}
