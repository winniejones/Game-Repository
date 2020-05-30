package st.whineHouse.rain.gx;

import java.util.List;
import java.util.Random;

import st.whineHouse.rain.Game;
import st.whineHouse.rain.entity.mob.npc.Chaser;
import st.whineHouse.rain.entity.mob.Mob;
import st.whineHouse.rain.entity.mob.npc.Star;
import st.whineHouse.rain.entity.projectile.Projectile;
import st.whineHouse.raincloud.graphics.DomainScreen;
import st.whineHouse.raincloud.graphics.Sprite;
import st.whineHouse.raincloud.graphics.SpriteSheet;
import st.whineHouse.raincloud.tile.Tile;
import st.whineHouse.raincloud.utility.Vector2i;

/**
 * Screen-klassen
 * Används för att hålla koll på spelfönstret och vad som rendereras.
 * Pixlar, Map, Text-fonter, bilder från map och entiteter.
 * 
 * @author Winston Jones
 */
public class Screen extends DomainScreen {
	
	public int[] tiles = new int [MAP_SIZE * MAP_SIZE];
	
	private Random random = new Random();

	public Screen(int width, int height){
		this.width = width;
		this.height = height;
		pixels = new int[width*height];
		
		for (int i =0; i<MAP_SIZE*MAP_SIZE;i++){
			tiles [i] = random.nextInt(0xffffff);
			tiles [0] = 0;
		}
	}
	
	public void clear(){
		for (int i = 0; i< pixels.length; i++){
			pixels[i]=0;
		}
	}
	public void renderSheet(int xp, int yp, SpriteSheet sheet, boolean fixed){
		if( fixed){
			xp -= xOffset;
			yp -= yOffset;
		}
		for(int y=0; y< sheet.SPRITE_HEIGHT;y++){
			int ya = y + yp;
			for(int x=0; x< sheet.SPRITE_WIDTH;x++){
				int xa = x + xp;
				if(xa < 0 || xa >= width || ya<0 || ya>= height) continue;
				pixels[xa+ya*width]=sheet.pixels[x+y*sheet.SPRITE_WIDTH];
			}
		}
	}
	
	public void renderTextCharacter(int xp, int yp, Sprite sprite, int color, boolean fixed){
		if( fixed){
			xp -= xOffset;
			yp -= yOffset;
		}
		for(int y=0; y< sprite.getHeight();y++){
			int ya = y + yp;
			for(int x=0; x< sprite.getWidth();x++){
				int xa = x + xp;
				if(xa < 0 || xa >= width || ya<0 || ya>= height) continue;
				int col = sprite.pixels[x+y*sprite.getWidth()];
				if(col != ALPHA_COL && col != 0xff7f007f) pixels[xa+ya*width] = color;
			}
		}
	}
	
	public void renderSprite(int xp, int yp, Sprite sprite, boolean fixed){
		if( fixed){
			xp -= xOffset;
			yp -= yOffset;
		}
		for(int y=0; y< sprite.getHeight();y++){
			int ya = y + yp;
			for(int x=0; x< sprite.getWidth();x++){
				int xa = x + xp;
				if(xa < 0 || xa >= width || ya<0 || ya>= height) continue;
				int col = sprite.pixels[x+y*sprite.getWidth()];
				if(col != ALPHA_COL && col != 0xff7f007f) pixels[xa+ya*width] = col;
			}
		}
	}

	public void renderProjectile(int xp, int yp, Projectile p){
		xp -= xOffset;
		yp -= yOffset;
		for(int y =0; y< p.getSprite().SIZE; y++){
			int ya = y + yp;
			for(int x =0; x < p.getSprite().SIZE; x++){
				int xa = x + xp;
				if(xa < - p.getSprite().SIZE || xa >= width || ya < 0 || ya >= height) break;
				if (xa < 0) xa = 0;
				int col = p.getSprite().pixels[x + y * p.getSprite().SIZE];
				if(col != ALPHA_COL) pixels[xa + ya*width]= col;
			}
		}
	}
	
	public void renderMob(int xp, int yp, Mob mob){
		xp -= xOffset;
		yp -= yOffset;
		int pixelSize = Game.mobPixelSize;
		for(int y =0; y< pixelSize; y++){
			int ya = y + yp;
			int ys = y;
			for(int x =0; x < pixelSize; x++){
				int xa = x + xp;
				int xs = x;
				if(xa < -pixelSize || xa >= width || ya < 0 || ya >= height) break;
				if (xa < 0) xa = 0;
				int col = mob.getSprite().pixels[xs+ys*pixelSize];
				if((mob instanceof Star) && col == 0xff472bbf) col = 0xffba0015;
				if((mob instanceof Star) && col == 0xfffdff60) col = 0xff33332e;
				if((mob instanceof Chaser) && col == 0xff472bbf) col = 0xff33332e; // CHASER COLORING
				if(col != ALPHA_COL && col != 0xff78c380) pixels[xa + ya*width]= col;
			}
		}
	}
	
	public void renderMob(int xp, int yp, Sprite sprite, int flip){
		xp -= xOffset;
		yp -= yOffset;
		int pixelSize = Game.mobPixelSize;
		for(int y =0; y< pixelSize; y++){
			int ya = y + yp;
			int ys = y;
			if(flip==2||flip==3) ys = 31-y;
			for(int x =0; x < pixelSize; x++){
				int xa = x + xp;
				int xs = x;
				if(flip==1||flip==3) xs = 31-x;
				if(xa < -pixelSize || xa >= width || ya < 0 || ya >= height) break;
				if (xa < 0) xa = 0;
				int col = sprite.pixels[xs+ys*pixelSize];
				if(col != ALPHA_COL && col != 0xff78c380) pixels[xa + ya*width]= col;
			}
		}
	}
	
	public void drawRect(int xp, int yp, int width, int height, int color, boolean fixed){
		if(fixed){
			xp -= xOffset;
			yp -= yOffset;
		}
		for(int x = xp; x < xp + width; x++){
			if (yp >= this.height || x < 0 | x >= this.width) continue;
			if (yp > 0) pixels[x + yp * this.width] = color;
			if (yp + height >= this.height) continue;
			if (yp + height > 0) pixels[x + (yp+ height) * this.width] = color;
		}
		
		for(int y = yp; y <= yp + height; y++){
			if (xp >= this.width || y < 0 || y >= this.height) continue;
			if (xp > 0) pixels[xp + y * this.width] = color;
			if (xp+width >= this.width) continue;
			if (xp + width > 0) pixels[(xp+ width) + y * this.width] = color;
		}
	}

	public void fillRect(int xp, int yp, int width, int height, int color, boolean fixed) {
		if (fixed) {
			xp -= xOffset;
			yp -= yOffset;
		}

		for (int y = 0; y < height; y++) {
			int yo = yp + y;
			if (yo < 0 || yo >= this.height)
				continue;
			for (int x = 0; x < width; x++) {
				int xo = xp + x;
				if (xo < 0 || xo >= this.width)
					continue;
				pixels[xo + yo * this.width] = color;
			}
		}
	}
	
	public void drawVectors(List<Vector2i> list, int colour, boolean fixed){
		   for(Vector2i vec : list){
		      int xPixel = vec.getX();
		      int yPixel = vec.getY();
		      if(fixed){
		         xPixel -= xOffset;
		         yPixel -= yOffset;
		      }
		      if(xPixel < 0 || xPixel >= this.width || yPixel < 0 || yPixel >= this.height) continue;
		      pixels[xPixel + yPixel * this.width] = colour;
		   }
		}
	
	public void setOffset(int xOffset,int yOffset){
		this.xOffset=xOffset;
		this.yOffset=yOffset;
	}
}
