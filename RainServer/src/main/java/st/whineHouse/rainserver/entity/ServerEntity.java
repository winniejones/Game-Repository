package st.whineHouse.rainserver.entity;

import st.whineHouse.raincloud.graphics.Sprite;
import st.whineHouse.rainserver.Server;
import st.whineHouse.rainserver.world.ServerLevel;

import java.awt.*;
import java.util.Random;

public class ServerEntity {

	public int x, y;
	private boolean removed = false;
	protected ServerLevel level;
	protected Server server;
	protected final Random random = new Random();
	protected Sprite sprite;
	// The animation object
    private Rectangle bounds;

	//TODO: Fixa detta
	protected int xBound, yBound, xOff, yOff;

	//Constructor
	public ServerEntity(){

	}

	public ServerEntity(int x , int y, Rectangle bounds){
		this.x = x;
		this.y = y;
		this.bounds = bounds;
	}

	public ServerEntity(int x , int y){
		this.x = x;
		this.y = y;
		this.sprite = new Sprite(32, 0xAAAAAA);
		this.bounds = new Rectangle((int) x - (sprite.getWidth() >> 1), (int) y - (sprite.getHeight() >> 1), sprite.getWidth(), sprite.getHeight());
	}
	
	public void update(){
		
	}
	
	public void remove(){
		//remove from level
		removed = true;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public boolean isRemoved(){
		return removed;
	}

	public Sprite getSprite(){
		return sprite;
	}
	
	public void init(ServerLevel level){
		this.level=level;
	}
	
	public double getoX(){
		return x - xOff;
	}
	   
	public double getoY(){
		return y - yOff;
	}
	   
	public double getoXB(){
		return x + bounds.x - xOff;
	}
	   
	public double getoYB(){
		return y + bounds.y - yOff;
	}
	   
	public boolean isColliding(){
        return true;
    }

	public Rectangle getBounds(){
		return bounds;
	}

}
