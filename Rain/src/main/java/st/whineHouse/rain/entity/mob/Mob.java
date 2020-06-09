package st.whineHouse.rain.entity.mob;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import st.whineHouse.rain.Game;
import st.whineHouse.rain.entity.Entity;
import st.whineHouse.rain.entity.projectile.*;
import st.whineHouse.rain.gx.Screen;
import st.whineHouse.rain.gx.ui.UILabel;
import st.whineHouse.raincloud.net.packet.ProjectilePacket;
import st.whineHouse.raincloud.utility.RayCastingResult;
import st.whineHouse.raincloud.utility.Vector2i;

/**
 * Mob-klassen som är en Entity-klass
 * Används som en entitet som har en attribut som en gubbe eller spelare m.m.
 * 
 * @author Winston Jones
 *
 */
public abstract class Mob extends Entity{
	
	protected int id;
	protected AtomicInteger aint = new AtomicInteger();
	public boolean walking = false;
	protected int numOfSteps = 0;
	public int health;
	protected Projectile projectile;
	protected int weaponID;
	protected Boolean isColliding = false;
	protected int time=0;
	private Entity randtarget = null;
	protected UILabel hpLabel;
	protected UILabel screenNameLabel;
	protected int positionOffset = Game.mobPixelSize/2;
	
	protected enum Direction{
		UP,DOWN,LEFT,RIGHT
	}

	protected Direction dir;
	
	public void move(double xa, double ya){
		if( xa != 0 && ya != 0){
			move(xa,0);
			move(0,ya);
			numOfSteps--;
			return;
		}
		numOfSteps++;
		if(xa > 0) dir=Direction.RIGHT;
		if(xa < 0) dir=Direction.LEFT;
		if(ya > 0) dir=Direction.DOWN;
		if(ya < 0) dir=Direction.UP;
		
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
	
	public abstract void update();
	
	protected void shoot(double x, double y , double dir,int weaponID){
		
		//this.projectile = projectile;
		//Projectile p = new WizzardArrow(x, y, dir);
		if(weaponID == 2) projectile = new WizzardArrow(x, y, dir);
		if(weaponID == 1) projectile = new NinjaBlade(x, y, dir);
		if(weaponID == 3) projectile = new WizardProjectile(x, y, dir);
		/*
		 * if(weaponID == 1) projectile = new WizzardArrow(x, y, dir);
		 * if(weaponID == 2) projectile = new NinjaBlade(x, y, dir);
		 * if(weaponID == 3) projectile = new WizzardProjectile(x, y, dir);
		 * 
		 * */

		// TODO: Send to server before adding
		ProjectilePacket projectilePacket = new ProjectilePacket(weaponID,x,y,dir);
		projectilePacket.writeData(Game.game.client);
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

	protected void shootClosest(){

		Mob closest = null;
		double min = 0;
		if(!level.getPlayers().isEmpty()){
			List<Mob> players = level.getPlayers(this, 100);
			// Nedanstående kod gör att gubben skjuter på närmaste entitet.
			players.add(level.getClientPlayer());
			for (int i = 0; i < players.size(); i++) {
				Mob p = players.get(i);
				double distance = Vector2i.getDistance(new Vector2i(x, y), new Vector2i(p.x, p.y));
				if (i == 0 || distance < min) {
					min = distance;
					closest = p;
				}
			}
		}
		if(closest != null){
			double dx = closest.x-x;
			double dy = closest.y-y;
			double dir = Math.atan2(dy, dx);
			RayCastingResult raycast = level.RayCast(new Vector2i(x,y), dir, (int)min);
			if(!raycast.hasCollided()){
				if(time % WizzardArrow.FIRE_RATE == 0){
					shoot(x, y, dir,weaponID);
				}
			}
		}
	}

	
	public int getId() {
		return id;
	}
	
	public abstract void render(Screen screen);
}
