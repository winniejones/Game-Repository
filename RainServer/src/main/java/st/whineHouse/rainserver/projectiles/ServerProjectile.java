package st.whineHouse.rainserver.projectiles;

import st.whineHouse.raincloud.graphics.Sprite;
import st.whineHouse.raincloud.shared.MobType;
import st.whineHouse.raincloud.shared.ProjectileType;
import st.whineHouse.rainserver.Rainserver;
import st.whineHouse.rainserver.entity.ServerEntity;
import st.whineHouse.rainserver.entity.ServerMob;
import st.whineHouse.rainserver.user.ServerPlayer;

import java.util.List;
import java.util.Random;

import static st.whineHouse.raincloud.shared.ProjectileType.WIZARDARROW;
import static st.whineHouse.raincloud.shared.ProjectileType.WIZARDPROJECTILE;

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
		this.fireRate = 2;
	}

	public void update(){
		if(level.tileCollision((int)(x + nx),(int) (y+ ny), 7 , 5, 4)){
			//logIsRemoved();
			remove();
		}
		move();
		if(WIZARDPROJECTILE.equals(type)){
			hitMob();
		} else {
			hitPlayer();
		}
	}


	private void hitPlayer(){
		List<ServerPlayer> players = Rainserver.rainserver.level.players;
		for (int i = 0; i < players.size(); i++) {
			if (x < players.get(i).x + 10
					&& x > players.get(i).x - 10// creates a 32x32 boundary, change it if your mobs are not 32x32
					&& y < players.get(i).y + 17
					&& y > players.get(i).y - 17
			) {
				remove();
				players.get(i).health -= 50; //only if your entities have health
			}
		}
	}

	private void hitMob() {
		List<ServerMob> mobs = Rainserver.rainserver.level.getMobs();
		for (int i = 0; i < mobs.size(); i++) {
			if (x < mobs.get(i).x + 17
					&& x > mobs.get(i).x - 17// creates a 32x32 boundary, change it if your mobs are not 32x32
					&& y < mobs.get(i).y + 17
					&& y > mobs.get(i).y - 17
			) {
				remove();
				System.out.println(mobs.get(i).getClass().getSimpleName() + " has been shot");
				mobs.get(i).health -= 100; //only if your entities have health
			}
		}
	}

	private void logIsRemoved() {
		System.out.println(
				type + " is removed on ("
				+ (x + nx) + ","
				+ (y+ ny) + "): (x,y))"
		);
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

	public ProjectileType getType(){
		return type;
	}

	public int getFireRate(){
		return fireRate;
	}
}
