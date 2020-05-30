package st.whineHouse.rain.entity.projectile;

import st.whineHouse.rain.entity.spawner.ParticleSpawner;
import st.whineHouse.rain.gx.Screen;
import st.whineHouse.raincloud.graphics.Sprite;

/**
 * NinjaBlade-klass är en projectile-klass.
 * Används som en projektil. Har sin egna sprite.
 * 
 * @author Winston Jones
 */
public class NinjaBlade extends Projectile {
	
	public static final int FIRE_RATE = 20; // Higher number = slower rate

	public NinjaBlade(double x, double y, double dir) {
		super(x, y, dir);
		range = 150;
		speed = 3;
		damage = 15;
		particle = Sprite.particle_normal;
		amount=50;
		life=100;
		
		sprite = Sprite.rotate(Sprite.projectile_n_blade,angle);
		nx = speed * Math.cos(angle);
		ny = speed * Math.sin(angle);
	}
	
	private int time = 0;
	
	public void update(){
		level.add(new ParticleSpawner((int)x, (int)y, life, amount, level,particle));
		remove();
		time++;
		if(time % 6 ==0){
			sprite = Sprite.rotate(sprite, Math.PI / 20.0);  //spriten roterar krig sin egen axel
		}
		projectileCollision();
		
		move();
		
		
	}
	
	// Check if projectile is colliding
	private void projectileCollision(){
		for (int i = 0; i < level.players.size(); i++) {
			if (x < level.players.get(i).getX() + 10
					&& x > level.players.get(i).getX() - 10// creates a 32x32 boundary, change it if your mobs are not 32x32
					&& y < level.players.get(i).getY() + 17
					&& y > level.players.get(i).getY() - 17
			) {
				level.add(new ParticleSpawner((int) x, (int) y, life, amount, level, Sprite.particle_blood));
				remove();
				level.players.get(i).health -= 50; //only if your entities have health
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
