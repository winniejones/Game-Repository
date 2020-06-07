package st.whineHouse.rain.entity.mob.npc;

import java.util.List;

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
		// mobMoving(time);
		//shootClosest();
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


	public void render(Screen screen) {
		//screen.renderSprite(17*16, 53*16,new Sprite(80, 80, 0xff0000),true);
		
		sprite = animSprite.getSprite();
		//Debug.drawRect(screen, 50, 50, 16, 16, false);
		screen.renderMob((int)x-16, (int)y-16, this);
	}

}
