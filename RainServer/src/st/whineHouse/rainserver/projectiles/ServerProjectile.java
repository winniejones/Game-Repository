package st.whineHouse.rainserver.projectiles;

import st.whineHouse.rain.entity.Entity;
import st.whineHouse.rain.entity.spawner.ParticleSpawner;
import st.whineHouse.rain.gx.Sprite;
import st.whineHouse.raincloud.shared.ProjectileType;
import st.whineHouse.rainserver.entity.ServerEntity;

import java.util.Random;

/**
 * Projectile-klassen är en entitets-klass
 * Används som en förälderklass för alla projektiler.
 * Innehåller sprite, vinkel(mellan vektor från och destination), position, hastighet och damage(som ej används än).
 * 
 * @author Winston Jones
 *
 */
public class ServerProjectile extends ServerEntity {

	protected final Random random = new Random();
	final private double xOrigin, yOrigin;
	private double angle;
	private double x, y, nx, ny;
	private double distance;
	private double speed, range, damage;
	private ProjectileType type;
	private int fireRate, life;

	public ServerProjectile(double x, double y, double dir, ProjectileType type){
		xOrigin = x;
		yOrigin = y;
		angle = dir;
		this.x = x;
		this.y = y;
		initFromType(type);
	}

	private void initFromType(ProjectileType type){
		this.type = type;
		switch (type) {
			case WIZARDPROJECTILE:
				setValues(200,4,20,44);
				break;
			case WIZARDARROW:
				setValues(150,3,15,100);
				break;
			case NINJABLADE:
				setValues(400,5,20,44);
				break;
			default:
				break;
		}
	}
	private void setValues(int range,int speed, int damage, int life){
		this.range = range;
		this.speed = speed;
		this.damage = damage;
		this.life = life;
		this.nx = speed * Math.cos(angle);
		this.ny = speed * Math.sin(angle);
	}

	public void update(){
		if(level.tileCollision((int)(x + nx),(int) (y+ ny), 7 , 5, 4)){
			remove();
		}
		move();
	}

	private void move(){
		x += nx;
		y += ny;
		if(distance() > range) remove();
	}

	private double distance() {
		double dist = 0;
		dist = Math.sqrt(Math.abs((xOrigin - x) * (xOrigin - x) + (yOrigin - y) * (yOrigin - y)));
		return dist;
	}

}
