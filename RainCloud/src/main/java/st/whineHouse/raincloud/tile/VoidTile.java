package st.whineHouse.raincloud.tile;

import st.whineHouse.raincloud.graphics.DomainScreen;
import st.whineHouse.raincloud.graphics.Sprite;

/**
 * VoidTile används som en tom ruta. 
 * @author Winston Jones
 *
 */
public class VoidTile extends Tile {

	public VoidTile(Sprite sprite) {
		super(sprite);
	}
	
	public void render(int x, int y, DomainScreen screen){
		screen.renderTile(x <<4, y <<4, this);
	}

}
