package whineHouse.rain.level;

/**
 * Spawn level klassen
 * En test level för att testa funktionerna
 * @author Winston Jones
 */
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import st.whineHouse.rain.entity.mob.npc.DeidaraMob;
import st.whineHouse.rain.entity.mob.npc.HirukoMob;
import st.whineHouse.rain.entity.mob.npc.ItachiMob;
import st.whineHouse.rain.entity.mob.npc.OrochimaruMob;

public class SpawnLevel extends Level {
	
	public static int w;
	public static int h;
	
	//private Quadtree collisionTree;
	
	public SpawnLevel(String path) {
			super(path);
	}
	
	protected void loadLevel(String path) {
		try{
			BufferedImage image = ImageIO.read(SpawnLevel.class.getResource(path));
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
