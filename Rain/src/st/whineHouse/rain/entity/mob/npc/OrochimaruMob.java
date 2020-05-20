package st.whineHouse.rain.entity.mob.npc;

import st.whineHouse.rain.entity.mob.Mob;
import st.whineHouse.rain.entity.spawner.ParticleSpawner;
import st.whineHouse.rain.gx.AnimatedSprite;
import st.whineHouse.rain.gx.Screen;
import st.whineHouse.rain.gx.Sprite;
import st.whineHouse.rain.gx.SpriteSheet;

/**
 * Dummy-klassen som är en Mob-klass.
 * Används som en testgubbe för att helt enklat fylla ut en level.
 * Tänkt att skapa en npc som helt enkelt går om kring i helt random riktningar.
 * 
 * @author Winston Jones
 *
 */
public class OrochimaruMob extends Mob {
	
	private AnimatedSprite down = new AnimatedSprite(SpriteSheet.orochimaru_down, 32, 32, 3);
	private AnimatedSprite up = new AnimatedSprite(SpriteSheet.orochimaru_up, 32, 32, 3);
	private AnimatedSprite left = new AnimatedSprite(SpriteSheet.orochimaru_left, 32, 32, 3);
	private AnimatedSprite right = new AnimatedSprite(SpriteSheet.orochimaru_right, 32, 32, 3);
	
	private AnimatedSprite animSprite = down;
	
	private int time=0;

	private int xa = 0, ya = 0;
	
	public OrochimaruMob(int x,int y){
		this.x = x;
		this.y = y;
		this.xBound = xBound;
		this.yBound = yBound;
		yOff = 1;
		xOff = 1;
		sprite = Sprite.orochimaru;
		health = 100;
		this.id = aint.incrementAndGet();
	}

	public OrochimaruMob(int x,int y, int id){
		this.x = x;
		this.y = y;
		this.xBound = xBound;
		this.yBound = yBound;
		yOff = 1;
		xOff = 1;
		sprite = Sprite.orochimaru;
		health = 100;
		this.id = id;
	}
	
	public synchronized void update() {
		//if(level.isServer()) {
		//	time++;
		//	mobMoving(time);
		//}
		
		if(health<=0 && !isRemoved()){
			level.add(new ParticleSpawner(x, y, 300, 700, level, Sprite.particle_blood));
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

	public void render(Screen screen) {
		sprite = animSprite.getSprite();
		screen.renderMob((int)(x - 16), (int)(y - 16), sprite, 0);
	}

}
