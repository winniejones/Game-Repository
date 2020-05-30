package st.whineHouse.raincloud.tile;

import st.whineHouse.raincloud.graphics.DomainScreen;
import st.whineHouse.raincloud.graphics.Sprite;

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
	
	public void render(int x, int y, DomainScreen screen){
		screen.renderTile(x << 4, y << 4, this);
	}

}
