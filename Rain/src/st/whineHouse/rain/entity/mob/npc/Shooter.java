package st.whineHouse.rain.entity.mob.npc;

import java.util.List;

import st.whineHouse.rain.entity.Entity;
import st.whineHouse.rain.entity.mob.Mob;
import st.whineHouse.rain.entity.particle.Particle;
import st.whineHouse.rain.entity.projectile.NinjaBlade;
import st.whineHouse.rain.entity.projectile.WizzardArrow;
import st.whineHouse.rain.entity.spawner.ParticleSpawner;
import st.whineHouse.rain.gx.AnimatedSprite;
import st.whineHouse.rain.gx.Screen;
import st.whineHouse.rain.gx.Sprite;
import st.whineHouse.rain.gx.SpriteSheet;
import st.whineHouse.rain.utilities.Debug;
import st.whineHouse.rain.utilities.RayCastingResult;
import st.whineHouse.rain.utilities.Vector2i;
/**
 * Shooter-klass är en mob-klass
 * Används som testgubbe som skjuter projektiler.
 * Den testar att rätt projektil skjuts iväg, den siktar på närmaste entitet(behöver förbättas).
 * 
 * @author Winston Jones
 *
 */
public class Shooter extends Mob {
	
	private AnimatedSprite down = new AnimatedSprite(SpriteSheet.itachi_down, 32, 32, 3);
	private AnimatedSprite up = new AnimatedSprite(SpriteSheet.itachi_up, 32, 32, 3);
	private AnimatedSprite left = new AnimatedSprite(SpriteSheet.itachi_left, 32, 32, 3);
	private AnimatedSprite right = new AnimatedSprite(SpriteSheet.itachi_right, 32, 32, 3);
	private AnimatedSprite animSprite = down;
	
	private int time=0;
	private int xa = 0, ya = 0;
	private Entity rand = null;
	
	public Shooter(int x, int y){
		this.x = x;
		this.y = y;
		sprite = Sprite.itachi;
		weaponID = 2;
		health = 50;
		this.id = aint.incrementAndGet();
	}

	public Shooter(int x, int y, int id){
		this.x = x;
		this.y = y;
		sprite = Sprite.itachi;
		weaponID = 2;
		health = 50;
		this.id = id;
	}
	
	public synchronized void update() {
		time++;
		mobMoving(time);
		shootClosest();
		//shootRandom();
		if(health<=0){
			level.add(new ParticleSpawner((int)x, (int)y, 300, 700, level, Sprite.particle_blood));
			remove();
		}
	}
	
	private void mobMoving(int time){
		if(time % (random.nextInt(50)+30) == 0){
			xa = random.nextInt(3)-1;
			ya = random.nextInt(3)-1;
			if(random.nextInt(3)==0){
				xa = 0;
				ya = 0;
			}
		}
		if(walking) animSprite.update();
		else animSprite.setFrame(0);
		
		if(ya<0){
			animSprite = up;
			dir = Direction.UP;
		}else if(ya>0) {
			animSprite = down;
			dir = Direction.DOWN;
		}
		if(xa<0) {
			animSprite = left;
			dir=Direction.LEFT;
		}else if(xa>0){
			animSprite = right;
			dir=Direction.RIGHT;
		}
		
		if(xa !=0 || ya !=0){
			move(xa,ya);
			walking = true;
		}else{
			walking = false;
		}
	}
	
/**
 * Shoots at random
 */
	private void shootRandom() {
		List<Entity> entities = level.getEntities(this, 500);
		entities.add(level.getClientPlayer());
		if(time % (30 + random.nextInt(91))==0){
			int index = random.nextInt(entities.size());
			rand = entities.get(index);
			//Collections.shuffle(entities);
		}	
		
		if(rand != null){
			double dx = rand.getX()-x;
			double dy = rand.getY()-y;
			double dir = Math.atan2(dy, dx);
			shoot(x,y,dir,weaponID);
		}
		
	}
	
/**
 * Shoot closest
 */
	private void shootClosest(){
		Mob closest = null;
		double min = 0;
		if(!level.getPlayers().isEmpty()){
			List<Mob> players = level.getPlayers(this, 100);
			// Nedanstående kod gör att gubben skjuter på närmaste entitet.
			players.add(level.getClientPlayer());
			for (int i = 0; i < players.size(); i++) {
				Mob p = players.get(i);
				double distance = Vector2i.getDistance(new Vector2i(x, y), new Vector2i(p.getX(), p.getY()));
				if (i == 0 || distance < min) {
					min = distance;
					closest = p;
				}
			}
		}
		if(closest != null){
			double dx = closest.getX()-x;
			double dy = closest.getY()-y;
			double dir = Math.atan2(dy, dx);
			RayCastingResult raycast = level.RayCast(new Vector2i(x,y), dir, (int)min);
			if(!raycast.hasCollided()){
				if(time % WizzardArrow.FIRE_RATE == 0){
					shoot(x, y, dir,weaponID);
				}
			}
		}
	}
	

	public void render(Screen screen) {
		//screen.renderSprite(17*16, 53*16,new Sprite(80, 80, 0xff0000),true);
		
		sprite = animSprite.getSprite();
		//Debug.drawRect(screen, 50, 50, 16, 16, false);
		screen.renderMob((int)x-16, (int)y-16, this);
	}

}
