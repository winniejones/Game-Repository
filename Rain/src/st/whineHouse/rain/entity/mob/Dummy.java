package st.whineHouse.rain.entity.mob;

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
public class Dummy extends Mob {
	
	private AnimatedSprite down = new AnimatedSprite(SpriteSheet.dummy_down, 32, 32, 3);
	private AnimatedSprite up = new AnimatedSprite(SpriteSheet.dummy_up, 32, 32, 3);
	private AnimatedSprite left = new AnimatedSprite(SpriteSheet.dummy_left, 32, 32, 3);
	private AnimatedSprite right = new AnimatedSprite(SpriteSheet.dummy_right, 32, 32, 3);
	
	private AnimatedSprite animSprite = down;
	
	private int time=0;

	private int xa = 0, ya = 0;
	
	public Dummy(int x,int y){
		this.x = x <<4;
		this.y = y <<4;
		this.xBound = xBound;
		this.yBound = yBound;
		yOff = 1;
		xOff = 1;
		sprite = Sprite.dummy;
	}
	
	public void update() {
		time++;
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
