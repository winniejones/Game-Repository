package st.whineHouse.rain.entity;

import java.awt.Rectangle;
import java.util.Random;

import st.whineHouse.rain.gx.AnimatedSprite;
import st.whineHouse.rain.gx.Screen;
import st.whineHouse.rain.gx.Sprite;
import st.whineHouse.rain.level.Level;

/**
 * Entity-klass
 * Används för att skapa entiteter som: Player, Mob(onda gubbar), partiklar, projektiler och annat som kan tänkas.
 * Själva klassen innehåller: 
 * 							*Positionen på entiteten. 
 * 							*Vilken level den ska tillhöra.
 * 							*Bild(sprite) som entiteten har.
 * 							*Position och gränser på sprite.
 * 
 * @author Winston Jones
 *
 */
public class Entity {
	
	public int x, y;
	private boolean removed = false;
	protected Level level;
	protected final Random random = new Random();
	protected Sprite sprite;
	// The animation object
    private AnimatedSprite anim;
    private Rectangle bounds;
    
	//TODO: Fixa detta
	protected int xBound, yBound, xOff, yOff;

	//Constructor
	public Entity(){
		
	}

	public Entity(int x , int y, Sprite sprite){
		this.x = x;
		this.y = y;
		this.sprite = sprite;
		this.bounds = new Rectangle((int) x - (sprite.getWidth() >> 1), (int) y - (sprite.getHeight() >> 1), sprite.getWidth(), sprite.getHeight());
	}
	
	public void update(){
		
	}
	
	public void render(Screen screen){
		if(sprite != null) screen.renderSprite((int)x, (int)y, sprite, true);
	}
	
	public void remove(){
		//remove from level
		removed = true;
	}
	
	public int getX(){
		return (int)x;
	}
	
	public int getY(){
		return (int)y;
	}
	
	public Sprite getSprite(){
		return sprite;
	}
	
	public boolean isRemoved(){
		return removed;
	}
	
	public void init(Level level){
		this.level=level;
		
	}
	
	
	
	public double getoX(){
		return x - xOff;
	}
	   
	public double getoY(){
		return y - yOff;
	}
	   
	public double getoXB(){
		return x + xBound - xOff;
	}
	   
	public double getoYB(){
		return y + yBound - yOff;
	}
	   
	public boolean isColliding(){
        return true;
    }

	
	public int getTop(){
		int top = sprite.getY();
		return top;
	}
	public int getBottom(){
		int bottom = sprite.getY()+ sprite.getHeight();
		return bottom;
	}
	public int getLeft(){
		int left = sprite.getX();
		return left;
	}
	public int getRight(){
		int right = sprite.getX() + sprite.getWidth();
		return right;
	}
	public Rectangle getBounds(){
		return bounds;
	}
	
	
	
	/**
     * Gets you the width of this object
     * 
     * @return The width of this object in pixels
     */
    public int getWidth(){
        return sprite.getWidth();
    }

    /**
     * Get's you the height of this object
     * 
     * @return The height of this object in pixels
     */
    public int getHeight(){
        return sprite.getHeight();
    }
}
