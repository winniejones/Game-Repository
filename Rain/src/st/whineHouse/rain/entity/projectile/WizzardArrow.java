package st.whineHouse.rain.entity.projectile;

import st.whineHouse.rain.entity.mob.Mob;
import st.whineHouse.rain.entity.spawner.ParticleSpawner;
import st.whineHouse.rain.gx.Screen;
import st.whineHouse.rain.gx.Sprite;
import st.whineHouse.rain.level.Level;

/**
 * Wizzardarrow-klass är en projectile-klass
 * Används som en projektil. innehåller en specifik sprite och är tänkt att ha fler attributer senare.
 * 
 * @author winston jones
 *
 */
public class WizzardArrow extends Projectile {
	
	public static final int FIRE_RATE = 60; // Higher number = slower rate

	public WizzardArrow(double x, double y, double dir) {
		super(x, y, dir);
		range = 400;
		speed = 5;
		damage = 20;
		particle = Sprite.particle_arrow;
		amount = 5;
		life = 20;
		
		sprite = Sprite.rotate(Sprite.projectile_arrow,angle); // Sprite som anv�nds roteras
		nx = speed * Math.cos(angle);
		ny = speed * Math.sin(angle);
	}
	
	//private int time = 0;
	
	public void update(){
		if(level.tileCollision((int)(x + nx),(int) (y+ ny), 7 , 5, 4)){
			level.add(new ParticleSpawner((int)x, (int)y, life, amount, level, particle));
			remove();
		}
		/*time++;
		if(time % 6 ==0){
			sprite = Sprite.rotate(sprite, Math.PI / 20.0);
		}*/
		projectileCollision();

		move();
	}

	private void projectileCollision(){
		for (int i = 0; i < level.mobs.size(); i++) {
			if (x < level.mobs.get(i).getX() + 17
					&& x > level.mobs.get(i).getX() - 17// creates a 32x32 boundary, change it if your mobs are not 32x32
					&& y < level.mobs.get(i).getY() + 17
					&& y > level.mobs.get(i).getY() - 17
			) {
				remove();
				level.add(new ParticleSpawner((int) x, (int) y, life, amount, level, Sprite.particle_blood));
				level.mobs.get(i).health -= 100; //only if your entities have health

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
