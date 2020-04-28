package st.whineHouse.rain.entity.mob.npc;

import java.util.List;

import st.whineHouse.rain.entity.mob.Mob;
import st.whineHouse.rain.entity.spawner.ParticleSpawner;
import st.whineHouse.rain.gx.AnimatedSprite;
import st.whineHouse.rain.gx.Screen;
import st.whineHouse.rain.gx.Sprite;
import st.whineHouse.rain.gx.SpriteSheet;

/**
 * Chaser-klassen är en Mob-klass
 * Används som en gubbe som jagar spelaren om man går tillräckligt nära.
 * Notera att denna klass använder sig inte av någon algoritm och jagar som en zombie.
 * Det innebär att chaser kommer försöka springa genom väggar.
 * 
 * @author Winston Jones
 *
 */
public class KisuneMob extends Mob {
	
	private AnimatedSprite down = new AnimatedSprite(SpriteSheet.kisune_down, 32, 32, 3);
	private AnimatedSprite up = new AnimatedSprite(SpriteSheet.kisune_up, 32, 32, 3);
	private AnimatedSprite left = new AnimatedSprite(SpriteSheet.kisune_left, 32, 32, 3);
	private AnimatedSprite right = new AnimatedSprite(SpriteSheet.kisune_right, 32, 32, 3);
	
	private AnimatedSprite animSprite = down;
	
	private double xa = 0, ya = 0;
	private double speed = 0.8;

	public KisuneMob ( int x, int y){
		this.x = x;
		this.y = y;
		sprite = Sprite.kisune;
		health = 300;
		this.id = aint.incrementAndGet();
	}

	public KisuneMob ( int x, int y, int id){
		this.x = x;
		this.y = y;
		sprite = Sprite.kisune;
		health = 300;
		this.id = id;
	}
	
	private void move(){
		xa = 0;
		ya = 0;
		List<Mob> players = level.getPlayers(this, 100);
		if(players.size() > 0){
			Mob player = players.get(0);
		
			if((int)x < player.getX()) xa += speed;
			if((int)x > player.getX()) xa -= speed;
			if((int)y < player.getY()) ya += speed;
			if((int)y > player.getY()) ya -= speed;
		}
		if(xa !=0 || ya !=0){
			move(xa,ya);
			walking = true;
		}else{
			walking = false;
		}
	}
	
	public synchronized void update() {
		if(level.isServer()) {
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
			System.out.println("Kisune Died");
			level.add(new ParticleSpawner((int)x, (int)y, 300, 700, level, Sprite.particle_blood));
			remove();
		}
	}

	public void render(Screen screen) {
		sprite = animSprite.getSprite();
		screen.renderMob((int)(x - 16), (int)(y - 16), this);

	}

}
