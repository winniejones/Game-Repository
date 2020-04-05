package st.whineHouse.rain.level;
import java.util.Random;

/**
 * Denna klass används inte. Den var till för debugging i början av kodningen.
 * 
 * Random level spottar ut olika tiles på olika positioner av javas randomgenerator.
 * 
 * @author Winston Jones
 */
public class RandomLevel extends Level {
	public static final Random random = new Random();

	public RandomLevel(int width, int height) {
		super(width, height);
	}
	protected void generateLevel(){
		for(int y =0; y< height; y++){
			for(int x =0;x<width;x++){
				tilesInt[x+y*width]= random.nextInt(4);
			}
		}
	}

}
