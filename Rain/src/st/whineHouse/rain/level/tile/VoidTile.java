package st.whineHouse.rain.level.tile;

import st.whineHouse.rain.gx.Screen;
import st.whineHouse.rain.gx.Sprite;

/**
 * VoidTile används som en tom ruta. 
 * @author Winston Jones
 *
 */
public class VoidTile extends Tile {

	public VoidTile(Sprite sprite) {
		super(sprite);
	}
	
	public void render(int x, int y, Screen screen){
		screen.renderTile(x <<4, y <<4, this);
	}

}
