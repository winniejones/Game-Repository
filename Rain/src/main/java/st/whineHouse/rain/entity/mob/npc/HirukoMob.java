package st.whineHouse.rain.entity.mob.npc;

import java.util.List;

import st.whineHouse.rain.entity.mob.Mob;
import st.whineHouse.rain.entity.spawner.ParticleSpawner;
import st.whineHouse.raincloud.graphics.AnimatedSprite;
import st.whineHouse.rain.gx.Screen;
import st.whineHouse.raincloud.graphics.Sprite;
import st.whineHouse.raincloud.graphics.SpriteSheet;
import st.whineHouse.raincloud.utility.Node;

/**
 * Star-klassen är en Mob-klass
 * Används som en gubbe som innehåller A*-algoritmen,
 *  vilket gör att den följer efter spelaren när man kommer innanför gubbens range.
 * 
 * @author winston_8
 *
 */
public class HirukoMob extends Mob {
	private AnimatedSprite down = new AnimatedSprite(SpriteSheet.hiruko_down, 32, 32, 3);
	private AnimatedSprite up = new AnimatedSprite(SpriteSheet.hiruko_up, 32, 32, 3);
	private AnimatedSprite left = new AnimatedSprite(SpriteSheet.hiruko_left, 32, 32, 3);
	private AnimatedSprite right = new AnimatedSprite(SpriteSheet.hiruko_right, 32, 32, 3);
	
	private AnimatedSprite animSprite = down;
	
	private double xa = 0, ya = 0;
	private List<Node> path = null;
	
	public HirukoMob(int x, int y){
		this.x = x;
		this.y = y;
		sprite = Sprite.hiruko;
		health = 70;
		this.id = aint.incrementAndGet();
	}

	public HirukoMob(int x, int y, int id){
		this.x = x;
		this.y = y;
		sprite = Sprite.hiruko;
		health = 70;
		this.id = id;
	}
	
	public synchronized void update() {
		time++;
		
		if(health<=0){
			level.add(new ParticleSpawner(x, y, 300, 700, level, Sprite.particle_blood));
			remove();
		}
	}

	public void render(Screen screen) {
		sprite = animSprite.getSprite();
		screen.renderMob((int)(x - 16), (int)(y - 16), this);
		if(health > 0)
			screen.drawRect(x - positionOffset,y - (positionOffset+2),(int)(0.32*(health+30)),1,0x6add6a, true);
	}

}

