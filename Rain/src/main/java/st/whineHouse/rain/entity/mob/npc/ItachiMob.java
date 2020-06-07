package st.whineHouse.rain.entity.mob.npc;

import st.whineHouse.rain.entity.Entity;
import st.whineHouse.rain.entity.mob.Mob;
import st.whineHouse.rain.entity.projectile.WizzardArrow;
import st.whineHouse.rain.entity.spawner.ParticleSpawner;
import st.whineHouse.raincloud.graphics.AnimatedSprite;
import st.whineHouse.rain.gx.Screen;
import st.whineHouse.raincloud.graphics.Sprite;
import st.whineHouse.raincloud.graphics.SpriteSheet;
import st.whineHouse.raincloud.utility.RayCastingResult;
import st.whineHouse.raincloud.utility.Vector2i;

import java.util.List;

public class ItachiMob extends Mob {
	
	private AnimatedSprite down = new AnimatedSprite(SpriteSheet.itachi_down, 32, 32, 3);
	private AnimatedSprite up = new AnimatedSprite(SpriteSheet.itachi_up, 32, 32, 3);
	private AnimatedSprite left = new AnimatedSprite(SpriteSheet.itachi_left, 32, 32, 3);
	private AnimatedSprite right = new AnimatedSprite(SpriteSheet.itachi_right, 32, 32, 3);
	private AnimatedSprite animSprite = down;
	
	//private int time=0;
	private int xa = 0, ya = 0;
	private Entity rand = null;
	
	public ItachiMob(int x, int y){
		this.x = x;
		this.y = y;
		sprite = Sprite.itachi;
		weaponID = 2;
		health = 500;
		this.id = aint.incrementAndGet();
	}

	public ItachiMob(int x, int y, int id){
		this.x = x;
		this.y = y;
		sprite = Sprite.itachi;
		weaponID = 2;
		health = 500;
		this.id = id;
	}
	
	public synchronized void update() {
		time++;
		//if(level.isServer()){
		//	mobMoving(time);
		//}
		//shootClosest();
		//shootRandom();
		
		if(health<=0){
			level.add(new ParticleSpawner(x, y, 300, 700, level, Sprite.particle_blood));
			remove();
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
			double dx = rand.x-x;
			double dy = rand.y-y;
			double dir = Math.atan2(dy, dx);
			// shoot(x,y,dir,weaponID);
		}
		
	}
	
/**
 * Shoot closest
 */

	public void render(Screen screen) {
		//screen.renderSprite(17*16, 53*16,new Sprite(80, 80, 0xff0000),true);
		
		sprite = animSprite.getSprite();
		//Debug.drawRect(screen, 50, 50, 16, 16, false);
		screen.renderMob(x-16, y-16, this);
	}

}