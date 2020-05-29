package whineHouse.rain.entity.projectile;

import st.whineHouse.rain.entity.spawner.ParticleSpawner;
import st.whineHouse.rain.gx.Screen;
import st.whineHouse.rain.gx.Sprite;
import st.whineHouse.rain.level.Level;

/**
 * WizardProjectile-klassen är en Projectile-klass.
 * Används som projektil. Har en egen specifik projektil sprite.
 * @author Winston Jones
 *
 */
public class WizardProjectile extends Projectile {
	
	public static final int FIRE_RATE = 10; // Higher number = slower rate
	

	public WizardProjectile(double x, double y, double dir) {
		super(x, y, dir);
		range = 200;
		speed = 4;
		damage = 20;
		particle = Sprite.particle_normal;
		amount = 50;
		life = 44;
//NYHETER
		/*
		 * protype = ProjectileType.WIZARD;
		 * projectileID = 1;
		 */
//TEST
		sprite = Sprite.projectile_wizard; // Vilken typ av sprite som anv�nds
		
		nx = speed * Math.cos(angle);
		ny = speed * Math.sin(angle);
	}
	
	
	public void update(){
		if(level.tileCollision((int)(x + nx),(int) (y+ ny), 7 , 5, 4)){
			level.add(new ParticleSpawner((int)x, (int)y, life, amount, level, particle));
			remove();
		}
		move();
		
		for (int i = 0; i < level.players.size(); i++) {
	         if (x < level.players.get(i).getX() +13
	            && x > level.players.get(i).getX() -13// creates a 32x32 boundary, change it if your mobs are not 32x32
	            && y <  level.players.get(i).getY() +17
	            && y >  level.players.get(i).getY() -17
	            ) {
	            remove();
	            level.add(new ParticleSpawner((int) x, (int) y, life, amount, level, Sprite.particle_blood));
	            level.players.get(i).health -= 1; //only if your entities have health

	         }
		}
	}
	
	
	
	protected void move(){
		x += nx;
		y += ny;	
		if(distance() > range) remove();
	}
	
	private double distance() {
		double dist = 0;
		dist = Math.sqrt(Math.abs((xOrigin - x) * (xOrigin - x) + (yOrigin - y) * (yOrigin - y)));
		return dist;
	}

	public void render(Screen screen){
		screen.renderProjectile((int) x - 11,(int) y - 2, this);
	}

}
