package st.whineHouse.rain.entity.mob;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import st.whineHouse.rain.Game;
import st.whineHouse.rain.entity.projectile.Projectile;
import st.whineHouse.rain.entity.projectile.WizardProjectile;
import st.whineHouse.rain.entity.projectile.WizzardArrow;
import st.whineHouse.rain.entity.spawner.ParticleSpawner;
import st.whineHouse.rain.events.Event;
import st.whineHouse.rain.events.EventDispatcher;
import st.whineHouse.rain.events.EventListener;
import st.whineHouse.rain.events.types.MousePressedEvent;
import st.whineHouse.rain.events.types.MouseReleasedEvent;
import st.whineHouse.rain.gx.AnimatedSprite;
import st.whineHouse.rain.gx.Screen;
import st.whineHouse.rain.gx.Sprite;
import st.whineHouse.rain.gx.SpriteSheet;
import st.whineHouse.rain.gx.ui.UIActionListener;
import st.whineHouse.rain.gx.ui.UIButton;
import st.whineHouse.rain.gx.ui.UIButtonListener;
import st.whineHouse.rain.gx.ui.UILabel;
import st.whineHouse.rain.gx.ui.UIManager;
import st.whineHouse.rain.gx.ui.UIPanel;
import st.whineHouse.rain.gx.ui.UIProgressBar;
import st.whineHouse.rain.input.Keyboard;
import st.whineHouse.rain.input.Mouse;
import st.whineHouse.rain.level.Level;
import st.whineHouse.rain.utilities.ImageUtils;
import st.whineHouse.rain.utilities.Vector2i;

/**
 * Player-klassen är en mob-klass
 * Används av en som spelar.
 * Innehåller allt som en mob har.
 * Hanterar input, animering av sprite, UImeny som används på spelfönstret och projektil skjutning.
 * 
 * @author Winston Jones
 *
 */
public class Player extends Mob implements EventListener{
	
	private String name;
	private Keyboard input;
	private Sprite sprite;
	private boolean walking;
	private AnimatedSprite down = new AnimatedSprite(SpriteSheet.player_down, 32, 32, 3);
	private AnimatedSprite up = new AnimatedSprite(SpriteSheet.player_up, 32, 32, 3);
	private AnimatedSprite left = new AnimatedSprite(SpriteSheet.player_left, 32, 32, 3);
	private AnimatedSprite right = new AnimatedSprite(SpriteSheet.player_right, 32, 32, 3);
	private AnimatedSprite animSprite = down;
	private int gameScreenBoundry;

	//TODO: This should be a class for weapons and change to one property 'private Weapon equippedWeapon'
	private int weaponID =1;  // weaponID om 1 = arrow, 2 = wizard, 3 = ninjablade
	Projectile p;
	private int fireRate = 0;
	
	//UI Saker
	private UIManager ui;
	private UIProgressBar uiHealthBar;
	private UIButton button;
	
	private BufferedImage image;
	
	private boolean shooting =false;
	
	@Deprecated
	public Player(String name, Keyboard input){
		gameScreenBoundry = (Game.width*Game.scale - Game.panelSize);
		this.name = name;
		this.input = input;
		sprite = Sprite.player_up;
	}
	
	public Player(String name,int x, int y, Keyboard input){
		gameScreenBoundry = (Game.width*Game.scale - Game.panelSize);
		this.name = name;
		this.x = x;
		this.y = y;
		this.input = input;
		isColliding = false;
		xBound=1;
		yBound=1;
		//TODO Fixa s� den inte �r beroende av just projectile fire rate.
		fireRate = WizardProjectile.FIRE_RATE;
		
//sj�lva panelen, grunden
		ui = Game.getUIManager();
		UIPanel panel = (UIPanel) new UIPanel(
				new Vector2i(Game.width*Game.scale,0),
				new Vector2i(Game.panelSize*Game.scale, Game.height*Game.scale)
		).setColor(0x4f4f4f); // TODO: Move this out to a personal preference file
		ui.addPanel(panel);
		
// namn label på panelen
		UILabel nameLabel = new UILabel(new Vector2i(40,200), name);		//namn label (position och size)
		nameLabel.setColor(0xbbbbbb);
		nameLabel.setFont(new Font("Verdana", Font.PLAIN,20));
		nameLabel.dropShadow = true;
		panel.addComponent(nameLabel);
		
// healthbar placering och f�rgl�ggning
		uiHealthBar = new UIProgressBar(new Vector2i(10,210),new Vector2i(Game.panelSize*Game.scale-20,15));		// helthbar (position (x,y), size (x,y))
		uiHealthBar.setColor(0x6a6a6a);
		uiHealthBar.setForegroundColor(0x6add6a);
		panel.addComponent(uiHealthBar);
		
// HP textning i panelen
		UILabel hpLabel = new UILabel(new Vector2i(uiHealthBar.position).add(new Vector2i(2,12)),"HP");
		hpLabel.setColor(0xffffff);
		hpLabel.setFont(new Font("verdana", Font.PLAIN, 14));
		panel.addComponent(hpLabel);
		
		//Player default attributes
		health = 100;
		
		//create a button then overide with the function we want. (exit on pressed on that button)
		button = new UIButton(new Vector2i(10,260), new Vector2i(120,40), new UIActionListener() {
			public void perform(){
				System.exit(0);
			}
		});
		
		//overide the button we created with the function we want. (perform action when pressed)
//		button.setButtonListener(new UIButtonListener(){
//			public void pressed(UIButton button) {
//				super.pressed(button);;
//				button.performAction();
//				button.ignoreNextPress();
//			}
//		});
		button.setText("Exit");
		panel.addComponent(button);
		
		/**
		 * Skapar en knapp med vald bild vald genom ImageiO.
		 * Vi override UIActionListener med vald funktion och även Override attribut efter button exited, pressed, entered och released.
		 */
//		try {
//			image = ImageIO.read(new File("res/textures/sheets/kakashi111.png"));
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//
//
//		UIButton imageButton = new UIButton(new Vector2i(10,360),image, new UIActionListener() {
//			public void perform(){
//				System.exit(0);
//			}
//		});
//
//		imageButton.setButtonListener(new UIButtonListener(){
//			public void entered(UIButton button){
//				button.setImage(ImageUtils.changeBrightness(image, 100));
//			}
//			public void exited(UIButton button) {
//				button.setImage(image);
//			}
//
//			public void pressed(UIButton button) {
//				button.setImage(ImageUtils.changeBrightness(image, -70));
//			}
//			public void released(UIButton button) {
//				button.setImage(image);
//			}
//		});
//		panel.addComponent(imageButton);
	}
	
	public String getName(){
		return name;
	}
	
	public void onEvent(Event event){
		EventDispatcher dispatcher = new EventDispatcher(event);
		dispatcher.dispatch(Event.Type.MOUSE_PRESSED, (Event e) -> onMousePressed((MousePressedEvent)e) );
		dispatcher.dispatch(Event.Type.MOUSE_RELEASED, (Event e) -> onMouseReleased((MouseReleasedEvent)e) );
	}
	
	public void update(){
		if(walking)animSprite.update();
		else animSprite.setFrame(0);
		if(fireRate > 0) fireRate--;
		double xa=0, ya=0;
		double speed = 2.0;
		
		if(input.up){
			ya -= speed;
			animSprite = up;
		}else if(input.down) {
			ya += speed;
			animSprite = down;
		}
		if(input.left) {
			xa -= speed;
			animSprite = left;
		}else if(input.right){ 
			xa += speed;
			animSprite = right;
		}
		
		if((xa != 0 || ya != 0)){
			move(xa,ya);
			walking = true;
		}else{
			walking = false;
		}
		
		clear();
		updateShooting();
		entityCollision(x,y);
		if(health >=0)
			uiHealthBar.setProgress(health/100.0);
	}
	
	private void entityCollision(int x,int y){
		for (int i = 0; i < level.mobs.size(); i++) {
	         if (x < level.mobs.get(i).getX() +10
	            && x > level.mobs.get(i).getX() -10// creates a 32x32 boundary, change it if your mobs are not 32x32
	            && y <  level.mobs.get(i).getY() +17
	            && y >  level.mobs.get(i).getY() -17) 
	         {
	        	System.out.println("krock");
	            isColliding = true;
	         }else isColliding = false;
		}
	}
	
	private void updateShooting(){
		if(!shooting || fireRate > 0)
			return;
		
		double dx = Mouse.getX() - (Game.getWindowWidth()/2);
		double dy = Mouse.getY() - (Game.getWindowHeight()/2);
		double dir = Math.atan2(dy, dx);
		shoot(x , y, dir,weaponID);
		fireRate = WizardProjectile.FIRE_RATE;
	}
	
	public boolean onMousePressed(MousePressedEvent e){
		System.out.println(Mouse.getX());
		if(Mouse.getX() > gameScreenBoundry)
			return false;
		if(e.getButton()== MouseEvent.BUTTON1){
			shooting = true;
			return true;
		}
		return false;
	}
	
	public boolean onMouseReleased(MouseReleasedEvent e){
		if(e.getButton()== MouseEvent.BUTTON1){
			shooting = false;
			return true;
		}
		return false;
	}
	
	private void clear() {
		for(int i = 0; i < level.getProjectiles().size(); i++){
			Projectile p = level.getProjectiles().get(i);
			if(p.isRemoved()) level.getProjectiles().remove(i);
		}
	}


	public void render(Screen screen){
		int flip =0;
		sprite = animSprite.getSprite();
		screen.renderMob((x - 16),(y - 16), sprite, flip);
//		screen.fillRect((x - 16),(y - 16),32,32,0,false);
	}

}
