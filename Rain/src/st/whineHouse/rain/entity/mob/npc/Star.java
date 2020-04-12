package st.whineHouse.rain.entity.mob.npc;

import java.util.List;

import st.whineHouse.rain.entity.mob.Mob;
import st.whineHouse.rain.entity.spawner.ParticleSpawner;
import st.whineHouse.rain.gx.AnimatedSprite;
import st.whineHouse.rain.gx.Screen;
import st.whineHouse.rain.gx.Sprite;
import st.whineHouse.rain.gx.SpriteSheet;
import st.whineHouse.rain.level.Node;
import st.whineHouse.rain.utilities.Vector2i;

/**
 * Star-klassen är en Mob-klass
 * Används som en gubbe som innehåller A*-algoritmen,
 *  vilket gör att den följer efter spelaren när man kommer innanför gubbens range.
 * 
 * @author winston_8
 *
 */
public class Star extends Mob {
	private AnimatedSprite down = new AnimatedSprite(SpriteSheet.hiruko_down, 32, 32, 3);
	private AnimatedSprite up = new AnimatedSprite(SpriteSheet.hiruko_up, 32, 32, 3);
	private AnimatedSprite left = new AnimatedSprite(SpriteSheet.hiruko_left, 32, 32, 3);
	private AnimatedSprite right = new AnimatedSprite(SpriteSheet.hiruko_right, 32, 32, 3);
	
	private AnimatedSprite animSprite = down;
	
	private double xa = 0, ya = 0;
	private double speed = 0.95;
	private List<Node> path = null;
	private int time=0;
	
	public Star(int x, int y){
		this.x = x << 4;
		this.y = y << 4;
		sprite = Sprite.hiruko;
		health = 70;
	}
	
	private void move(){
		xa = 0;
		ya = 0;
		int px = level.getClientPlayer().getX();
		int py = level.getClientPlayer().getY();
		Vector2i start = new Vector2i(getX() >> 4, getY() >> 4);
		Vector2i destination = new Vector2i(px >> 4, py >> 4);
		if(time % 3 == 0) path = level.findPath(start, destination);
		if(path != null){
			if(path.size()>0){
				Vector2i vec = path.get(path.size() - 1).tile;
				if((int)x < vec.getX() << 4) xa+=speed;
				if((int)x > vec.getX() << 4) xa-=speed;
				if((int)y < vec.getY() << 4) ya+=speed;
				if((int)y > vec.getY() << 4) ya-=speed;
			}
		}
		if(xa !=0 || ya !=0){
			move(xa,ya);
			walking = true;
		}else{
			walking = false;
		}
	}
	
	public void update() {
		move();
		time++;
		mobMoving(time);
		
		if(health<=0){
			level.add(new ParticleSpawner((int)x, (int)y, 300, 700, level, Sprite.particle_blood));
			remove();
		}
	}
	
	private void mobMoving(int time){
//		if(time % (random.nextInt(50)+30) == 0){
//			xa = random.nextInt(3)-1;
//			ya = random.nextInt(3)-1;
//			if(random.nextInt(3)==0){
//				xa = 0;
//				ya = 0;
//			}
//		}
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
		screen.renderMob((int)(x - 16), (int)(y - 16), this);

	}

}
