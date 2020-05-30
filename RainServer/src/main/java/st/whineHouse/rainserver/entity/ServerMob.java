package st.whineHouse.rainserver.entity;

import st.whineHouse.raincloud.net.packet.ProjectilePacket;
import st.whineHouse.raincloud.shared.MobType;
import st.whineHouse.raincloud.shared.ProjectileType;
import st.whineHouse.rainserver.Rainserver;
import st.whineHouse.rainserver.projectiles.ServerProjectile;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Mob-klassen som är en Entity-klass
 * Används som en entitet som har en attribut som en gubbe eller spelare m.m.
 * 
 * @author Winston Jones
 *
 */
public class ServerMob extends ServerEntity{
	
	private int id;
	private AtomicInteger aint = new AtomicInteger();
	public boolean walking = false;
	private int numOfSteps = 0;
	public int health;
	private ServerProjectile projectile;
	private int weaponID;
	protected Boolean isColliding = false;
	private MobType type;

	public MobType getType() {
		return type;
	}

	protected enum Direction{
		UP,DOWN,LEFT,RIGHT
	}

	protected Direction dir;

	public ServerMob(int x, int y, int id,MobType type) {
		super(x,y, new Rectangle(32 >> 1, 32 >> 1, 3,1));
		this.id = id;
		this.type = type;
	}
	
	public void move(double xa, double ya){
		if( xa != 0 && ya != 0){
			move(xa,0);
			move(0,ya);
			numOfSteps--;
			return;
		}
		numOfSteps++;
		if(xa > 0) dir= Direction.RIGHT;
		if(xa < 0) dir= Direction.LEFT;
		if(ya > 0) dir= Direction.DOWN;
		if(ya < 0) dir= Direction.UP;
		
		while(xa != 0){
			if(Math.abs(xa) > 1){
				if(!collision(abs(xa), ya)){
					this.x += abs(xa);			
				}
				xa -=abs(xa);
			} else{
				if(!collision(abs(xa), ya)){
					this.x += xa;			
				}
				xa=0;
			}
		}
		while(ya != 0){
			if(Math.abs(ya) >1){
				if(!collision(xa, abs(ya))){
					this.y += abs(ya);
				} 
				ya -=abs(ya);
			} else{
				if(!collision(xa, abs(ya))){
					this.y += ya;
				}
				ya=0;
			}
		}
	}
	
	private int abs(double value){
		if(value < 0) return -1;
		return 1;
	}
	
	public void update(){
		super.update();
	}
	
	protected void shoot(double x, double y , double dir,int weaponID) throws Exception {
		
		//this.projectile = projectile;
		//Projectile p = new WizzardArrow(x, y, dir);
		projectile = new ServerProjectile(x,y,dir, ProjectileType.getProjectileType(weaponID));
		/*
		 * if(weaponID == 1) projectile = new WizzardArrow(x, y, dir);
		 * if(weaponID == 2) projectile = new NinjaBlade(x, y, dir);
		 * if(weaponID == 3) projectile = new WizzardProjectile(x, y, dir);
		 * 
		 * */

		// TODO: Send to server before adding
		ProjectilePacket projectilePacket = new ProjectilePacket(weaponID,x,y,dir);
		projectilePacket.writeData(Rainserver.rainserver.getServer());
		//level.add(projectile);
	}
	
	
	
	private boolean collision(double xa,double ya){
		boolean solid = false;
		for(int c = 0; c < 4; c++){
			double xt = ((x+xa) -c % 2 * 16) / 16;
			double yt = ((y+ya) -c / 2 * 16+12) / 16;
			int ix = (int) Math.ceil(xt);
			int iy = (int) Math.ceil(yt);
			if(c % 2 == 0) ix = (int) Math.floor(xt);
			if(c / 2 == 0) iy = (int) Math.floor(yt);
			if(level.getTile(ix,iy).solid()) solid = true;
			
		}
		return solid;
	}
	
	public int getId() {
		return id;
	}
}
