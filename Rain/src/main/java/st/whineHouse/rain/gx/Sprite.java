package whineHouse.rain.gx;

/**
 * Sprite-klassen
 * Denna klass innehåller alla bilder som ska ritas upp för entiteterna på spelfönstret.
 * 
 * @author Winston Jones
 *
 */
public class Sprite {
	
	public final int SIZE;
	private int x, y;
	private int width, height;
	public int[] pixels;
	protected SpriteSheet sheet;
	
	public static Sprite grass = new Sprite(16, 0, 0, SpriteSheet.tiles);
	public static Sprite flower = new Sprite(16, 1, 0, SpriteSheet.tiles);
	public static Sprite rock = new Sprite(16, 2, 0, SpriteSheet.tiles);
	public static Sprite voidSprite = new Sprite(16,0x1B87E0);
	
	//Spawn Level sprites here:
	public static Sprite spawn_grass = new Sprite(16, 0, 0, SpriteSheet.spawn_level);
	public static Sprite spawn_hedge = new Sprite(16, 1, 0, SpriteSheet.spawn_level);
	public static Sprite spawn_water = new Sprite(16, 2, 0, SpriteSheet.spawn_level);
	public static Sprite spawn_wall1 = new Sprite(16, 0, 1, SpriteSheet.spawn_level);
	public static Sprite spawn_wall2 = new Sprite(16, 1, 1, SpriteSheet.spawn_level);
	public static Sprite spawn_floor = new Sprite(16, 0, 2, SpriteSheet.spawn_level);
	
	//Player Sprites here:
	
	public static Sprite player_up = new Sprite(32, 0, 5, SpriteSheet.tiles);
	public static Sprite player_down = new Sprite(32, 2, 5, SpriteSheet.tiles);
	public static Sprite player_side = new Sprite(32, 1, 5, SpriteSheet.tiles);
	
	public static Sprite player_up_1 = new Sprite(32, 0, 6, SpriteSheet.tiles);
	public static Sprite player_up_2 = new Sprite(32, 0, 7, SpriteSheet.tiles);
	
	public static Sprite player_side_1 = new Sprite(32, 1, 6, SpriteSheet.tiles);
	public static Sprite player_side_2 = new Sprite(32, 1, 7, SpriteSheet.tiles);
	
	public static Sprite player_down_1 = new Sprite(32, 2, 6, SpriteSheet.tiles);
	public static Sprite player_down_2 = new Sprite(32, 2, 7, SpriteSheet.tiles);
	
	public static Sprite dummy = new Sprite(32,0,0, SpriteSheet.dummy_down);
	public static Sprite itachi = new Sprite(32,0,0, SpriteSheet.itachi_down);
	public static Sprite deidara = new Sprite(32,0,0, SpriteSheet.deidara_down);
	public static Sprite orochimaru = new Sprite(32,0,0, SpriteSheet.orochimaru_down);
	public static Sprite hiruko = new Sprite(32,0,0, SpriteSheet.hiruko_down);
	public static Sprite kisune = new Sprite(32,0,0, SpriteSheet.kisune_down);
	
	//Projectile Sprites here:
	public static Sprite projectile_wizard = new Sprite(16, 0, 0, SpriteSheet.projectile_wizard);
	public static Sprite projectile_arrow = new Sprite(16, 1, 0, SpriteSheet.projectile_wizard);
	public static Sprite projectile_n_blade = new Sprite(16, 2, 0, SpriteSheet.projectile_wizard);
	
	
	// Particles
	public static Sprite particle_normal = new Sprite(3,0xaaaaaa);
	public static Sprite particle_blood = new Sprite(2,0xcc2222);
	public static Sprite particle_arrow = new Sprite(3,0x7D3920);
	
	
	protected Sprite(SpriteSheet sheet, int width, int height){
		SIZE = (width == height) ? width : -1;
		this.width = width;
		this.height = height;
		this.sheet = sheet;
	}
	
	public Sprite(int size, int x, int y, SpriteSheet sheet){
		SIZE=size;
		this.width = size;
		this.height = size;
		pixels = new int[SIZE*SIZE];
		this.x = x*size;
		this.y = y*size;
		this.sheet = sheet;
		load();
	}
	
	public Sprite(int width, int height, int colour){
		SIZE = -1;
		this.width=width;
		this.height = height;
		pixels = new int[width*height];
		setColour(colour);
	}
	
	public Sprite(int size, int colour){
		SIZE = size;
		this.width = size;
		this.height = size;
		pixels = new int[SIZE*SIZE];
		setColour(colour);
	}
	
	public Sprite(int[] pixels, int width, int height){
		SIZE = (width == height) ? width : -1;
		this.width = width;
		this.height = height;
		this.pixels = new int[pixels.length];      
		//Om inte jag hade det här kunde jag fått en nullpointer exception. Här initierar jag en ny pixelarray
		
		//System.arraycopy(pixels, 0, this.pixels, 0, pixels.length); 
		//annat sätt att kopiera arrayen på in i for loopen.. detta används till fonterna
		
		for(int i = 0; i < pixels.length; i++){
			this.pixels[i] = pixels[i];	
		}	
	}
	
	//Rotera Sprite Enkel version
	public static Sprite rotate(Sprite sprite, double angle){
		return new Sprite(rotate(sprite.pixels, sprite.width, sprite.height, angle), sprite.width, sprite.height);
	}
	
	//Rotera sprite annan version
	private static int[] rotate(int[] pixels, int width, int height, double angle){
		int[] result = new int[width * height];
		
		double nx_x = rot_x(-angle, 1.0, 0.0);
		double nx_y = rot_y(-angle, 1.0, 0.0);
		double ny_x = rot_x(-angle, 0.0, 1.0);
		double ny_y = rot_y(-angle, 0.0, 1.0);
		
		double x0 = rot_x(-angle, -width/2.0, -height/2.0) + width/2.0;
		double y0 = rot_y(-angle, -width/2.0, -height/2.0) + height/2.0;
		
		for(int y =0; y < height; y++){
			double x1 = x0;
			double y1 = y0;
			for(int x = 0; x < width; x++){
				int xx = (int)x1;
				int yy = (int)y1;
				int col =0;
				if(xx < 0 || xx >= width || yy <0 || yy >= height) col = 0xffff00ff;
				else col = pixels[xx + yy * width];
				result[x + y * width] = col;
				x1 += nx_x;
				y1 += nx_y;
			}
			x0 += ny_x;
			y0 += ny_y;
			
		}
		
		return result;
	}
	
	//Rotera med avseende på x-axel
	private static double rot_x(double angle, double x, double y){
		double cos = Math.cos(angle - Math.PI/2);
		double sin = Math.sin(angle - Math.PI/2);
		return x * cos + y * -sin;
	}
	
	//Rotera med avseende på y-axel
	private static double rot_y(double angle, double x, double y){
		double cos = Math.cos(angle - Math.PI/2);
		double sin = Math.sin(angle - Math.PI/2);
		return x* sin + y * cos;
	}
	
	//Delar på en sprite i bitar. används t.ex till partiklar.
	public static Sprite[] split(SpriteSheet sheet) {
	    int amount = (sheet.getWidth() * sheet.getHeight()) /     (sheet.SPRITE_WIDTH * sheet.SPRITE_HEIGHT);

	    Sprite[] sprites = new Sprite[amount];
	    int current = 0;
	    int[] pixels = new int[sheet.SPRITE_WIDTH *    sheet.SPRITE_HEIGHT];
	    for (int yp = 0; yp < sheet.getHeight() / sheet.SPRITE_HEIGHT; yp++) {
	        for (int xp = 0; xp < sheet.getWidth() /     sheet.SPRITE_WIDTH; xp++) {

	            for (int y = 0; y < sheet.SPRITE_HEIGHT; y++) {
	                for (int x = 0; x < sheet.SPRITE_WIDTH; x++) {
	                    int x0 = x + xp * sheet.SPRITE_WIDTH;
	                    int y0 = y + yp * sheet.SPRITE_HEIGHT;
	                    pixels[x + y * sheet.SPRITE_WIDTH] = sheet.getPixels()[x0 + y0 * sheet.getWidth()];
	                }
	            }
	            
	            sprites[current++] = new Sprite(pixels, sheet.SPRITE_WIDTH, sheet.SPRITE_HEIGHT);
	        }
	    }
	    return sprites;
	}
	
	//Lägga till en sprite genom en pixelarray och storlek på sprite.
	public Sprite(int[] pixels, int size){
		SIZE = width = height = size;
		this.pixels = pixels;
	}
	
	//Fyll sprite med en färg.
	private void setColour(int colour) {
		for( int i =0; i< width*height;i++){
			pixels[i] = colour;
		}
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public int getY(){
		return y;
	}
	
	public int getX(){
		return x;
	}
	

	//Hämta ladda upp pixlar för sprite.
	private void load(){
		for(int y =0; y< height; y++){
			for(int x=0; x<width;x++){
				pixels[x+y*width] = sheet.pixels[(x+this.x) + (y+this.y)*sheet.SPRITE_WIDTH];
			}
		}
	}

}
