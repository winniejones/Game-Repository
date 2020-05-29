package whineHouse.rain.utilities;
import st.whineHouse.rain.gx.Screen;
import st.whineHouse.rain.level.Level;

/**
 * DEBUG 
 * Används för tillfället för att debugga screen och att allt ritas upp ordentligt.
 * @author winston jones
 *
 */
public class Debug {
	private Debug(){
	}
	
	public static void drawRect(Screen screen, int x, int y, int width, int height, boolean fixed){
		drawRect(screen, x, y, width, height, 0xff0000, fixed);
	}
	public static void drawRect(Screen screen, int x, int y, int width, int height,int color, boolean fixed){
		screen.drawRect(x, y, width, height, color, fixed);
	}
	
	public static void drawLine(Screen screen, int x0, int y0, int x1, int y1, int colour, boolean fixed){
		   screen.drawVectors(Level.BresenhamLine(x0, y0, x1, y1), colour, fixed);
	}
}
