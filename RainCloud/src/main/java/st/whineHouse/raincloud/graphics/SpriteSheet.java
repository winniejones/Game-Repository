package st.whineHouse.raincloud.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * SpriteSheet-klassen
 * Används för att ta emot en "path" för en bild från res-mappen och renderera på spelet.
 * Används även för att hålla koll på specifika entitets sprites(bilder) som ska ritas upp.
 * @author Winston Jones
 *
 */
public class SpriteSheet {
	
	private String path;
	public final int SIZE;
	public final int SPRITE_WIDTH, SPRITE_HEIGHT;
	private int width, height;
	public int[] pixels;
	public static int TRANSPARENT = 0xffff00ff;
	
	public static SpriteSheet tiles = new SpriteSheet("/textures/sheets/spritesheet1.png", 256);
	public static SpriteSheet spawn_level = new SpriteSheet("/textures/sheets/spawn_lvldark.png", 48);
	public static SpriteSheet projectile_wizard = new SpriteSheet("/textures/sheets/projectiles/wizard.png", 48);
	
	//Animationssprites från player.
	public static SpriteSheet player = new SpriteSheet("/textures/sheets/new_player.png", 96, 128);
	public static SpriteSheet player_down = new SpriteSheet(player, 0, 0, 3, 1, 32);
	public static SpriteSheet player_up = new SpriteSheet(player, 0, 3, 3, 1, 32);
	public static SpriteSheet player_left = new SpriteSheet(player, 0, 1, 3, 1, 32);
	public static SpriteSheet player_right = new SpriteSheet(player, 0, 2, 3, 1, 32);
	
	//Animationssprites från en annan gubbbe.
	public static SpriteSheet dummy = new SpriteSheet("/textures/sheets/dummy.png", 128, 96);
	public static SpriteSheet dummy_down = new SpriteSheet(dummy, 1, 0, 1, 3, 32);
	public static SpriteSheet dummy_up = new SpriteSheet(dummy, 2, 0, 1, 3, 32);
	public static SpriteSheet dummy_left = new SpriteSheet(dummy, 0, 0, 1, 3, 32);
	public static SpriteSheet dummy_right = new SpriteSheet(dummy, 3, 0, 1, 3, 32);
	
	//Animationssprites för itachi.
	public static SpriteSheet itachi = new SpriteSheet("/textures/sheets/itachi_rpg.png", 96, 128);
	public static SpriteSheet itachi_down = new SpriteSheet(itachi, 0, 0, 3, 1, 32);
	public static SpriteSheet itachi_up = new SpriteSheet(itachi, 0, 3, 3, 1, 32);
	public static SpriteSheet itachi_left = new SpriteSheet(itachi, 0, 1, 3, 1, 32);
	public static SpriteSheet itachi_right = new SpriteSheet(itachi, 0, 2, 3, 1, 32);
		
	//Animationssprites för orochimaru.
	public static SpriteSheet orochimaru = new SpriteSheet("/textures/sheets/orochimaru_rpg.png", 96, 128);
	public static SpriteSheet orochimaru_down = new SpriteSheet(orochimaru, 0, 0, 3, 1, 32);
	public static SpriteSheet orochimaru_up = new SpriteSheet(orochimaru, 0, 3, 3, 1, 32);
	public static SpriteSheet orochimaru_left = new SpriteSheet(orochimaru, 0, 1, 3, 1, 32);
	public static SpriteSheet orochimaru_right = new SpriteSheet(orochimaru, 0, 2, 3, 1, 32);
	
	//Animationssprites för deidara.
	public static SpriteSheet deidara = new SpriteSheet("/textures/sheets/deidara_rpg.png", 96, 128);
	public static SpriteSheet deidara_down = new SpriteSheet(deidara, 0, 0, 3, 1, 32);
	public static SpriteSheet deidara_up = new SpriteSheet(deidara, 0, 3, 3, 1, 32);
	public static SpriteSheet deidara_left = new SpriteSheet(deidara, 0, 1, 3, 1, 32);
	public static SpriteSheet deidara_right = new SpriteSheet(deidara, 0, 2, 3, 1, 32);
	
	//Animationssprites för hiruko.
	public static SpriteSheet hiruko = new SpriteSheet("/textures/sheets/hiruko_rpg.png", 96, 128);
	public static SpriteSheet hiruko_down = new SpriteSheet(hiruko, 0, 0, 3, 1, 32);
	public static SpriteSheet hiruko_up = new SpriteSheet(hiruko, 0, 3, 3, 1, 32);
	public static SpriteSheet hiruko_left = new SpriteSheet(hiruko, 0, 1, 3, 1, 32);
	public static SpriteSheet hiruko_right = new SpriteSheet(hiruko, 0, 2, 3, 1, 32);
	
	//Animationssprites för kisune.
	public static SpriteSheet kisune = new SpriteSheet("/textures/sheets/kisune_rpg.png", 96, 128);
	public static SpriteSheet kisune_down = new SpriteSheet(kisune, 0, 0, 3, 1, 32);
	public static SpriteSheet kisune_up = new SpriteSheet(kisune, 0, 3, 3, 1, 32);
	public static SpriteSheet kisune_left = new SpriteSheet(kisune, 0, 1, 3, 1, 32);
	public static SpriteSheet kisune_right = new SpriteSheet(kisune, 0, 2, 3, 1, 32);

	private Sprite[] sprites;
	
	/**
	 * Denna funktion tar storlek på ruta och bild som ska ritas upp för att sedan rita upp pixlarna på spelfönstret.
	 */
	public SpriteSheet(SpriteSheet sheet, int x, int y, int width, int height, int spriteSize){
		int xx = x * spriteSize;
		int yy = y * spriteSize;
		int w = width * spriteSize;
		int h = height * spriteSize;
		if(width == height) SIZE = width;
		else SIZE = -1;
		SPRITE_WIDTH = w;
		SPRITE_HEIGHT = h;
		pixels = new int[w * h];
		for(int y0 = 0; y0 < h ; y0++){
			int yp = yy + y0;
			for(int x0 = 0 ;x0 < w; x0++){
				int xp = xx + x0;
				pixels[x0 + y0 * w] = sheet.pixels[xp + yp * sheet.SPRITE_WIDTH];
			}
		}
		int frame = 0;
		sprites = new Sprite[width * height];
		for(int ya = 0; ya < height ; ya++){
			for (int xa = 0; xa < width ; xa++){
				int[] spritePixels = new int[spriteSize * spriteSize];
				for( int y0 =0; y0 < spriteSize; y0++){
					for( int x0=0; x0 < spriteSize; x0++){
						spritePixels[x0 + y0 * spriteSize] = pixels[(x0 + xa * spriteSize) + (y0 + ya * spriteSize) * SPRITE_WIDTH];
					}
				}
				Sprite sprite = new Sprite(spritePixels, spriteSize, spriteSize);
				sprites[frame++] = sprite;
			}
		}
	}
	
	/**
	 * Tar specifik "path" och storleken på bilden man vill rita upp för att sedan rita ut direkt på spelfönstret.
	 * annan verision från spritesheet-funktionen över.
	 */
	public SpriteSheet (String path, int size){
		this.path=path;
		this.SIZE=size;
		SPRITE_WIDTH = size;
		SPRITE_HEIGHT = size;
		pixels = new int[SIZE*SIZE];
		load();
	}
	
	/**
	 * Annan veraion av spritesheet-funktion över denna.
	 */
	public SpriteSheet(String path, int width, int height) {
		this.path = path;
		SIZE = -1;
		SPRITE_WIDTH = width;
		SPRITE_HEIGHT = height;
		pixels = new int [SPRITE_WIDTH * SPRITE_HEIGHT];
		load();
	}
	
	/**
	 * Getters för sprites, storlekar och pixelarray.
	 */
	public Sprite[] getSprites(){
		return sprites;
	}

	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
	}
	
	public int[] getPixels(){
		return pixels;
	}
	
	/**
	 * Funktion för ladda upp bild till spelfönstret.
	 */
	private void load(){
		try {
			System.out.print("Trying load: " + path + "...");
			BufferedImage image = ImageIO.read(SpriteSheet.class.getResource(path));
			System.out.println(" succeeded!");
			width = image.getWidth();
			height = image.getHeight();
			pixels = new int[width*height];
			image.getRGB(0, 0, width, height, pixels, 0, width);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println(" failed!");
		} catch (Exception e){
			System.out.println(" failed!");
		}
	}

}
