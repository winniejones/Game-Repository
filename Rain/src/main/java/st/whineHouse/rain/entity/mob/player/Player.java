package st.whineHouse.rain.entity.mob.player;

import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import st.whineHouse.rain.Game;
import st.whineHouse.rain.entity.mob.Mob;
import st.whineHouse.rain.entity.projectile.Projectile;
import st.whineHouse.rain.entity.projectile.WizardProjectile;
import st.whineHouse.rain.events.Event;
import st.whineHouse.rain.events.EventDispatcher;
import st.whineHouse.rain.events.EventListener;
import st.whineHouse.rain.events.types.MousePressedEvent;
import st.whineHouse.rain.events.types.MouseReleasedEvent;
import st.whineHouse.raincloud.graphics.AnimatedSprite;
import st.whineHouse.rain.gx.Screen;
import st.whineHouse.raincloud.graphics.Sprite;
import st.whineHouse.raincloud.graphics.SpriteSheet;
import st.whineHouse.rain.gx.ui.UIActionListener;
import st.whineHouse.rain.gx.ui.UIButton;
import st.whineHouse.rain.gx.ui.UILabel;
import st.whineHouse.rain.gx.ui.UIManager;
import st.whineHouse.rain.gx.ui.UIPanel;
import st.whineHouse.rain.gx.ui.UIProgressBar;
import st.whineHouse.rain.input.Keyboard;
import st.whineHouse.rain.input.Mouse;
import st.whineHouse.raincloud.utility.Vector2i;
import st.whineHouse.raincloud.net.packet.LogoutPacket;
import st.whineHouse.raincloud.net.packet.MovePacket;

/**
 * Player-klassen är en mob-klass
 * Används av en som spelar.
 * Innehåller allt som en mob har.
 * Hanterar input, animering av sprite, UImeny som används på spelfönstret och projektil skjutning.
 *
 * @author Winston Jones
 */
public class Player extends Mob implements EventListener {

    private String name;
    private Keyboard input;
    private Sprite sprite;
    private AnimatedSprite down = new AnimatedSprite(SpriteSheet.player_down, 32, 32, 3);
    private AnimatedSprite up = new AnimatedSprite(SpriteSheet.player_up, 32, 32, 3);
    private AnimatedSprite left = new AnimatedSprite(SpriteSheet.player_left, 32, 32, 3);
    private AnimatedSprite right = new AnimatedSprite(SpriteSheet.player_right, 32, 32, 3);
    private AnimatedSprite animSprite = down;
    private int gameScreenBoundry;

    //TODO: This should be a class for weapons and change to one property 'private Weapon equippedWeapon'
    private int weaponID = 1;  // weaponID om 1 = arrow, 2 = wizard, 3 = ninjablade
    Projectile p;
    private int fireRate = 0;

    //UI Saker
    private UIManager ui;
    private UIProgressBar uiHealthBar;
    private UILabel hpLabel;
    private UIButton button;
    private UILabel screenNameLabel;

    private BufferedImage image;

    private boolean shooting = false;
    public double speed;
    private int positionOffset = Game.mobPixelSize/2;

    @Deprecated
    public Player(String name, Keyboard input) {
        gameScreenBoundry = (Game.width * Game.scale - Game.panelSize);
        this.name = name;
        this.input = input;
        sprite = Sprite.player_up;
    }

    public Player(String name, int x, int y, Keyboard input) {
        gameScreenBoundry = (Game.width * Game.scale - Game.panelSize);
        this.name = name;
        this.x = x;
        this.y = y;
        this.input = input;
        isColliding = false;
        xBound = 1;
        yBound = 1;
        //TODO Fixa s� den inte �r beroende av just projectile fire rate.
        fireRate = WizardProjectile.FIRE_RATE;

//sj�lva panelen, grunden
        if(Game.game != null){
            ui = Game.getUIManager();
            UIPanel panel = (UIPanel) new UIPanel(
                    new Vector2i(Game.width * Game.scale, 0),
                    new Vector2i(Game.panelSize * Game.scale, Game.height * Game.scale)
            ).setColor(0x4f4f4f); // TODO: Move this out to a personal preference file
            ui.addPanel(panel);

// namn label på panelen
            UILabel nameLabel = new UILabel(new Vector2i(40, 200), name);        //namn label (position och size)
            nameLabel.setColor(0xbbbbbb);
            nameLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
            nameLabel.dropShadow = true;
            panel.addComponent(nameLabel);

            screenNameLabel = new UILabel(new Vector2i(x - positionOffset, y - (positionOffset+4)), name);        //namn label (position och size)
            nameLabel.setColor(0xbbbbbb);
            nameLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
            nameLabel.dropShadow = true;
            panel.addComponent(nameLabel);

// healthbar placering och f�rgl�ggning
            uiHealthBar = new UIProgressBar(new Vector2i(10, 210), new Vector2i(Game.panelSize * Game.scale - 20, 15));        // helthbar (position (x,y), size (x,y))
            uiHealthBar.setColor(0x6a6a6a);
            uiHealthBar.setForegroundColor(0x6add6a);
            panel.addComponent(uiHealthBar);

// HP textning i panelen
            hpLabel = new UILabel(new Vector2i(uiHealthBar.position).add(new Vector2i(2, 12)), "HP");
            hpLabel.setColor(0xffffff);
            hpLabel.setFont(new Font("verdana", Font.PLAIN, 14));
            panel.addComponent(hpLabel);

            //create a button then overide with the function we want. (exit on pressed on that button)
            button = new UIButton(new Vector2i(10, 260), new Vector2i(120, 40), new UIActionListener() {
                public void perform() {
                    LogoutPacket logoutPacket = new LogoutPacket(name,x,y);
                    logoutPacket.writeData(Game.game.client);
                    System.exit(0);
                }
            });

            button.setText("Exit");
            panel.addComponent(button);
        }

        //Player default attributes
        health = 100;


        //overide the button we created with the function we want. (perform action when pressed)
//		button.setButtonListener(new UIButtonListener(){
//			public void pressed(UIButton button) {
//				super.pressed(button);;
//				button.performAction();
//				button.ignoreNextPress();
//			}
//		});

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

    public String getName() {
        return name;
    }

    public void onEvent(Event event) {
        EventDispatcher dispatcher = new EventDispatcher(event);
        dispatcher.dispatch(Event.Type.MOUSE_PRESSED, (Event e) -> onMousePressed((MousePressedEvent) e));
        dispatcher.dispatch(Event.Type.MOUSE_RELEASED, (Event e) -> onMouseReleased((MouseReleasedEvent) e));
    }

    public void update() {
        if (walking) animSprite.update();
        else animSprite.setFrame(0);
        if (fireRate > 0) fireRate--;
        double xa = 0, ya = 0;
        speed = 2.0;

        if (input != null) {
            if (input.up) {
                ya -= speed;
                animSprite = up;
            } else if (input.down) {
                ya += speed;
                animSprite = down;
            }
            if (input.left) {
                xa -= speed;
                animSprite = left;
            } else if (input.right) {
                xa += speed;
                animSprite = right;
            }
        }

        if (xa != 0 || ya != 0) {
            move(xa, ya);
            walking = true;
            MovePacket movePacket = new MovePacket(name, x, y, (int) speed, walking);
            movePacket.writeData(Game.game.client);
        } else {
            walking = false;
        }

        updateShooting();
        //entityCollision(x, y);
        if (health >= 0 && uiHealthBar != null)
            uiHealthBar.setProgress(health / 100.0);
    }

    //private void entityCollision(int x, int y) {
    //    for (int i = 0; i < level.mobs.size(); i++) {
    //        if (x < level.mobs.get(i).getX() + 10
    //                && x > level.mobs.get(i).getX() - 10// creates a 32x32 boundary, change it if your mobs are not 32x32
    //                && y < level.mobs.get(i).getY() + 17
    //                && y > level.mobs.get(i).getY() - 17) {
    //            System.out.println("krock");
    //            isColliding = true;
    //        } else isColliding = false;
    //    }
    //}

    private void updateShooting() {
        if (!shooting || fireRate > 0)
            return;

        double dx = Mouse.getX() - (Game.getWindowWidth() / 2);
        double dy = Mouse.getY() - (Game.getWindowHeight() / 2);
        double dir = Math.atan2(dy, dx);
        shoot(x, y, dir, weaponID);
        fireRate = WizardProjectile.FIRE_RATE;
    }

    public boolean onMousePressed(MousePressedEvent e) {
        if (Mouse.getX() > gameScreenBoundry)
            return false;
        if (e.getButton() == MouseEvent.BUTTON1) {
            shooting = true;
            return true;
        }
        return false;
    }

    public boolean onMouseReleased(MouseReleasedEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            shooting = false;
            return true;
        }
        return false;
    }

    public void render(Screen screen) {
        int flip = 0;
        sprite = animSprite.getSprite();
        screen.renderMob((x - positionOffset), (y - positionOffset), sprite, flip);
        screenNameLabel.setPosition(new Vector2i(x - positionOffset, y - positionOffset));
		//screen.fillRect((x - 16),(y - 16),32,32,0,false);
    }

//    public void TestRender(Screen screen) {
//        int xTile = 0;
//        int yTile = 28;
//        int walkingSpeed = 4;
//        int flipTop = (numSteps >> walkingSpeed) & 1;
//        int flipBottom = (numSteps >> walkingSpeed) & 1;
//
//        if (movingDir == 1) {
//            xTile += 2;
//        } else if (movingDir > 1) {
//            xTile += 4 + ((numSteps >> walkingSpeed) & 1) * 2;
//            flipTop = (movingDir - 1) % 2;
//        }
//
//        int modifier = 8 * scale;
//        int xOffset = x - modifier / 2;
//        int yOffset = y - modifier / 2 - 4;
//        if (isSwimming) {
//            int waterColour = 0;
//            yOffset += 4;
//            if (tickCount % 60 < 15) {
//                waterColour = Colours.get(-1, -1, 225, -1);
//            } else if (15 <= tickCount % 60 && tickCount % 60 < 30) {
//                yOffset -= 1;
//                waterColour = Colours.get(-1, 225, 115, -1);
//            } else if (30 <= tickCount % 60 && tickCount % 60 < 45) {
//                waterColour = Colours.get(-1, 115, -1, 225);
//            } else {
//                yOffset -= 1;
//                waterColour = Colours.get(-1, 225, 115, -1);
//            }
//            screen.render(xOffset, yOffset + 3, 0 + 27 * 32, waterColour, 0x00, 1);
//            screen.render(xOffset + 8, yOffset + 3, 0 + 27 * 32, waterColour, 0x01, 1);
//        }
//        screen.render(xOffset + (modifier * flipTop), yOffset, xTile + yTile * 32, colour, flipTop, scale);
//        screen.render(xOffset + modifier - (modifier * flipTop), yOffset, (xTile + 1) + yTile * 32, colour, flipTop,
//                scale);
//
//        if (!isSwimming) {
//            screen.render(xOffset + (modifier * flipBottom), yOffset + modifier, xTile + (yTile + 1) * 32, colour,
//                    flipBottom, scale);
//            screen.render(xOffset + modifier - (modifier * flipBottom), yOffset + modifier, (xTile + 1) + (yTile + 1)
//                    * 32, colour, flipBottom, scale);
//        }
//        if (username != null) {
//            Font.render(username, screen, xOffset - ((username.length() - 1) / 2 * 8), yOffset - 10,
//                    Colours.get(-1, -1, -1, 555), 1);
//        }
//    }
}
