package st.whineHouse.rain.level.tile;

import st.whineHouse.rain.gx.Screen;
import st.whineHouse.rain.gx.Sprite;
import st.whineHouse.rain.level.tile.spawn_level.SpawnGrassTile;
import st.whineHouse.rain.level.tile.spawn_level.SpawnWaterTile;
import st.whineHouse.rain.level.tile.spawn_level.SpawnFloorTile;
import st.whineHouse.rain.level.tile.spawn_level.SpawnHedgeTile;
import st.whineHouse.rain.level.tile.spawn_level.SpawnWallTile;

/**
 * Tile-klass
 * Används för att hålla koll på innehållet av varje ruta.
 * Tile håller koll på alla andra tile-klasser. 
 * Varje tile-klass har en egen sprite som finns denna klass håller koll på.
 * När en level-klass översätter en map, som ska ritas upp, så sköter Tiles vad för sprite som ska ritas.
 * Hanterar även om en ruta är "solid" eller inte.
 * @author Winston Jones
 *
 */
public class Tile {
	
	public int x, y;
	public Sprite sprite;
	
	public static Tile grass = new GrassTile(Sprite.grass);
	public static Tile flower = new FlowerTile(Sprite.flower);
	public static Tile rock = new RockTile(Sprite.rock);
	public static Tile voidTile = new VoidTile(Sprite.voidSprite);
	
	public static Tile spawn_grass = new SpawnGrassTile(Sprite.spawn_grass);
	public static Tile spawn_hedge = new SpawnHedgeTile(Sprite.spawn_hedge);
	public static Tile spawn_water = new SpawnWaterTile(Sprite.spawn_water);
	public static Tile spawn_wall1 = new SpawnWallTile(Sprite.spawn_wall1);
	public static Tile spawn_wall2 = new SpawnWallTile(Sprite.spawn_wall2);
	public static Tile spawn_floor = new SpawnFloorTile(Sprite.spawn_floor);
	
	public static final int col_spawn_grass = 0xff00ff00;
	public static final int col_spawn_hedge = 0; //unused
	public static final int col_spawn_water = 0; //unused
	public static final int col_spawn_wall1 = 0xffa0a0a0;
	public static final int col_spawn_wall2 = 0xff535353;
	public static final int col_spawn_floor = 0xff966a39;
	
	public Tile(Sprite sprite){
		this.sprite = sprite;
	}
	
	public void render(int x, int y, Screen screen){
		
	}
	
	public boolean solid(){
		return false;
	}

}
