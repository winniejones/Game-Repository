package st.whineHouse.raincloud.tile;

import st.whineHouse.raincloud.graphics.DomainScreen;
import st.whineHouse.raincloud.graphics.Sprite;

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
	
	public void render(int x, int y, DomainScreen screen){
		screen.renderTile(x << 4, y << 4, this);
	}

}
