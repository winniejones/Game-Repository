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
	private double speed = 0.85;
	private List<Node> path = null;
	private int time=0;
	
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
	
	//private void move(int time){
	//	xa = 0;
	//	ya = 0;
	//	if(level != null){
	//		List<Mob> players = level.getPlayers(this, 70);
	//		if (players.size() > 0) {
	//			int px = level.getClientPlayer().getX();
	//			int py = level.getClientPlayer().getY();
	//			Vector2i start = new Vector2i(getX() >> 4, getY() >> 4);
	//			Vector2i destination = new Vector2i(px >> 4, py >> 4);
	//			if (time % 3 == 0) path = level.findPath(start, destination);
	//			if (path != null) {
	//				if (path.size() > 0) {
	//					Vector2i vec = path.get(path.size() - 1).tile;
	//					if (x < vec.getX() << 4) xa += speed;
	//					if (x > vec.getX() << 4) xa -= speed;
	//					if (y < vec.getY() << 4) ya += speed;
	//					if (y > vec.getY() << 4) ya -= speed;
	//				}
	//			}
	//		} else if (players.size() == 0) {
	//			if (time % (random.nextInt(50) + 30) == 0) {
	//				xa = random.nextInt(3) - 1;
	//				ya = random.nextInt(3) - 1;
	//				if (random.nextInt(3) == 0) {
	//					xa = 0;
	//					ya = 0;
	//				}
	//			}
	//		}
	//	}
	//	//else if(server != null){
	//	//	List<ServerClient> players = server.getPlayers(this, 70);
	//	//	if (players.size() > 0) {
	//	//		int px = players.get(0).x;
	//	//		int py = players.get(0).y;
	//	//		Vector2i start = new Vector2i(getX() >> 4, getY() >> 4);
	//	//		Vector2i destination = new Vector2i(px >> 4, py >> 4);
	//	//		if (time % 3 == 0) path = level.findPath(start, destination);
	//	//		if (path != null) {
	//	//			if (path.size() > 0) {
	//	//				Vector2i vec = path.get(path.size() - 1).tile;
	//	//				if (x < vec.getX() << 4) xa += speed;
	//	//				if (x > vec.getX() << 4) xa -= speed;
	//	//				if (y < vec.getY() << 4) ya += speed;
	//	//				if (y > vec.getY() << 4) ya -= speed;
	//	//			}
	//	//		}
	//	//	} else if (players.size() == 0) {
	//	//		if (time % (random.nextInt(50) + 30) == 0) {
	//	//			xa = random.nextInt(3) - 1;
	//	//			ya = random.nextInt(3) - 1;
	//	//			if (random.nextInt(3) == 0) {
	//	//				xa = 0;
	//	//				ya = 0;
	//	//			}
	//	//		}
	//	//	}
	//	//}
	//	if(xa !=0 || ya !=0){
//	//		move(xa,ya);
	//		walking = true;
	//	}else{
	//		walking = false;
	//	}
	//}
	
	public synchronized void update() {
		//move(time);
		time++;
		//if(level.isServer()){
		//	mobMoving(time);
		//}
		
		if(health<=0){
			level.add(new ParticleSpawner(x, y, 300, 700, level, Sprite.particle_blood));
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

