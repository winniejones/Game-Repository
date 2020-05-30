package st.whineHouse.rainserver.world;

/**
 * Spawn level klassen
 * En test level för att testa funktionerna
 * @author Winston Jones
 */

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ServerSpawnLevel extends ServerLevel {

	public static int w;
	public static int h;

	//private Quadtree collisionTree;

	public ServerSpawnLevel(String path) {
			super(path);
	}
	
	protected void loadLevel(String path) {
		try{
			BufferedImage image = ImageIO.read(ServerSpawnLevel.class.getResource(path));
			w = width=image.getWidth();
			h = height=image.getHeight();
			tiles = new int[w * h];
			image.getRGB(0, 0, w, h , tiles, 0 , w);
			
		} catch (IOException e){
			e.printStackTrace();
			System.out.println("Exception! Could not load level file!");
		}
		
			//add(new DeidaraMob(15,60));
			//add(new HirukoMob(17,35));
			//add(new Shooter(10,37));
			//add(new ItachiMob(20,48));
			//add(new Shooter(20,55));
			//add(new OrochimaruMob(15,53));
			//for (int i = 0; i< 5; i++){
				//add(new Dummy(20,55));
			//}
		
		
	}
	
	public static Rectangle getVisibleRect(){
		return new Rectangle(0,0,w,h);
	}
	
	// Grass = 0xff00ff00
	// Flower = 0xffffff00
	// Rock = 0xff7f7f00
	protected void generateLevel(){
	}

}
