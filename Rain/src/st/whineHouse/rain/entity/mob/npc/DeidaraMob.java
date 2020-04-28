package st.whineHouse.rain.entity.mob.npc;

import java.util.List;

import st.whineHouse.rain.entity.mob.Mob;
import st.whineHouse.rain.entity.spawner.ParticleSpawner;
import st.whineHouse.rain.gx.AnimatedSprite;
import st.whineHouse.rain.gx.Screen;
import st.whineHouse.rain.gx.Sprite;
import st.whineHouse.rain.gx.SpriteSheet;
import st.whineHouse.rainserver.ServerClient;

/**
 * Chaser-klassen är en Mob-klass
 * Används som en gubbe som jagar spelaren om man går tillräckligt nära.
 * Notera att denna klass använder sig inte av någon algoritm och jagar som en zombie.
 * Det innebär att chaser kommer försöka springa genom väggar.
 * 
 * @author Winston Jones
 *
 */
public class DeidaraMob extends Mob {
	
	private AnimatedSprite down = new AnimatedSprite(SpriteSheet.deidara_down, 32, 32, 3);
	private AnimatedSprite up = new AnimatedSprite(SpriteSheet.deidara_up, 32, 32, 3);
	private AnimatedSprite left = new AnimatedSprite(SpriteSheet.deidara_left, 32, 32, 3);
	private AnimatedSprite right = new AnimatedSprite(SpriteSheet.deidara_right, 32, 32, 3);
	
	private AnimatedSprite animSprite = down;
	
	private double xa = 0, ya = 0;
	private double speed = 0.8;

	public DeidaraMob ( int x, int y){
		this.x = x;
		this.y = y;
		sprite = Sprite.deidara;
		health = 300;
		this.id = aint.incrementAndGet();
	}

	public DeidaraMob ( int x, int y, int id){
		this.x = x;
		this.y = y;
		sprite = Sprite.deidara;
		health = 300;
		this.id = id;
	}
	
	private void move(){
		xa = 0;
		ya = 0;
		if(level != null){
			List<Mob> players = level.getPlayers(this, 100);
			if (players.size() > 0) {
				Mob player = players.get(0);

				if (x < player.getX()) xa += speed;
				if (x > player.getX()) xa -= speed;
				if (y < player.getY()) ya += speed;
				if (y > player.getY()) ya -= speed;
			}
		}
		//else if (server != null) {
		//	List<ServerClient> players = server.getPlayers(this, 100);
		//	if (players.size() > 0) {
		//		ServerClient player = players.get(0);
//
		//		if (x < player.x) xa += speed;
		//		if (x > player.x) xa -= speed;
		//		if (y < player.y) ya += speed;
		//		if (y > player.y) ya -= speed;
		//	}
		//}
		if(xa !=0 || ya !=0){
			move(xa,ya);
			walking = true;
		}else{
			walking = false;
		}
	}
	
	public synchronized void update() {
		if(level.isServer()){
			move();
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
		
		if(health<=0){
			if(level.isClient()){
				level.add(new ParticleSpawner(x, y, 300, 700, level, Sprite.particle_blood));
			}
			remove();
		}
	}

	public void render(Screen screen) {
		sprite = animSprite.getSprite();
		screen.renderMob((x - 16), (y - 16), this);

	}

}
