package whineHouse.rainserver.entity;

import st.whineHouse.rain.gx.AnimatedSprite;
import st.whineHouse.rain.gx.Screen;
import st.whineHouse.rain.gx.Sprite;
import st.whineHouse.rain.level.Level;
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
	// The animation object
    private Rectangle bounds;

	//TODO: Fixa detta
	protected int xOff, yOff;

	//Constructor
	public ServerEntity(){

	}

	public ServerEntity(int x , int y, Rectangle bounds){
		this.x = x;
		this.y = y;
		this.bounds = bounds;
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
