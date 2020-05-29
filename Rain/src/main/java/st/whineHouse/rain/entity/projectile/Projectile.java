package whineHouse.rain.entity.projectile;

import java.util.Random;

import st.whineHouse.rain.entity.Entity;
import st.whineHouse.rain.gx.Sprite;

/**
 * Projectile-klassen är en entitets-klass
 * Används som en förälderklass för alla projektiler.
 * Innehåller sprite, vinkel(mellan vektor från och destination), position, hastighet och damage(som ej används än).
 * 
 * @author Winston Jones
 *
 */
public abstract class Projectile extends Entity {
	
	final protected double xOrigin, yOrigin;
	protected double angle;
	protected Sprite sprite;
	protected double x, y, nx, ny;
	protected int amount, life; // till partiklarna
	protected double distance;
	protected double speed, range, damage;
	protected final Random random = new Random();
	protected Sprite particle;
	protected int fireRate;
	
//TEST
	/*protected enum ProjectileType{
		ARROW, WIZARD, NINJABLADE;
	}
	protected int projectileID;
	protected ProjectileType protype;
	public Projectile(Projectile p){
		if(p = )
	}*/
//TEST
	public Projectile(double x, double y, double dir){
		xOrigin = x;
		yOrigin = y;
		angle = dir;
		this.x = x;
		this.y = y;
	}
	
	
	
	public Sprite getSprite(){
		return sprite;
	}
	
	public int getSpriteSize(){
		return sprite.SIZE;
	}

}
