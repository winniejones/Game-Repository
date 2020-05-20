package st.whineHouse.rain;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import st.whineHouse.rain.entity.mob.player.Player;
import st.whineHouse.rain.events.Event;
import st.whineHouse.rain.events.EventListener;
import st.whineHouse.rain.gx.Font;
import st.whineHouse.rain.gx.Screen;
import st.whineHouse.rain.gx.layers.Layer;
import st.whineHouse.rain.gx.ui.UIManager;
import st.whineHouse.rain.input.Keyboard;
import st.whineHouse.rain.input.Mouse;
import st.whineHouse.rain.level.Level;
import st.whineHouse.rain.level.TileCoordinate;
import st.whineHouse.rain.net.Client;
import st.whineHouse.rain.net.player.NetPlayer;
import st.whineHouse.raincloud.net.packet.LoginPacket;
import st.whineHouse.raincloud.serialization.RCDatabase;
import st.whineHouse.raincloud.serialization.RCField;
import st.whineHouse.raincloud.serialization.RCObject;
//import st.whineHouse.rain.level.collisionHandling.Quadtree;

	/**
	 * 
	 * @author Winston Jones
	 *	Denna klass är mainklassen.
	 *	Här skapar jag ett Game och allting som behövs för att game ska kunna köras.
	 *	Spelet har 8 pix/ruta (8bit), som en längdenhet. 
	 */

public class Game extends Canvas implements Runnable, EventListener {
	private static final long serialVersionUID = 1L;
	public static Game game;
	public static final int mobPixelSize = 32;

	public static int panelSize = 80;			//Storlek på panelen till sidan av spelet
	public static int width = 900-panelSize;	//Bredd på själva applikationsfönstret
	public static int height = 500;			//Höjden
	public static int scale = 2;				//Scalan/zoomning
	public static String title="Game Test";
	
	private Thread gameThread;					//Skapa en tråd för hantering av spel
	private JFrame frame;						//Skapa en java ruta
	private Keyboard key;						//Skapa en tangentbordsobjekt
	public Level level, level1;						//Skapa level där spelet ska utspela sig på.
	private NetPlayer player;						//Skapa spalre för den som körspelet.
	private boolean running = false;			//Kollar om spelet är igång.
	public boolean isApplet = false;			//Kollar om spelet är igång.
	public Client client;			//Klient.

	private static UIManager uiManager;			//Skapar en menyhanterare
	private Screen screen;						//Skapar en screen som ska in i javarutan.
	protected Font font;						//Egen klass font som laddas in.
	private BufferedImage image;
	private int[] pixels;
	private List<Layer> layerStack = new ArrayList<>();
	
	/**
	 * Här skapas allt som ska styras av Game-klassen
	 */
	public Game(){
		game = this;
		setSize();
		screen = new Screen(width, height);
		uiManager = new UIManager();
		frame = new JFrame();
		key = new Keyboard();

		// TODO: Connect to server here
		client = new Client(this, "localhost", 8192);
		client.connect();// TODO: We didn't connect

		level = Level.spawn;
		//level1 = Level.upper;
		addLayer(level);
		TileCoordinate playerSpawn = new TileCoordinate(17,42); 		//Koordinat där spelare spawnar
		player = new NetPlayer(generateString(),playerSpawn.x(),playerSpawn.y(),key, null, -1);
		level.add(player);


		LoginPacket loginPacket = new LoginPacket(player.getName(), player.x, player.y);
		loginPacket.writeData(client);

		font = new Font();
		
		addKeyListener(key);
		Mouse mouse = new Mouse(this);
		addMouseListener(mouse);
		addMouseMotionListener(mouse);

		saveScreen();
	}

	private String generateString(){
		Random random = new Random();
		StringBuilder buffer = new StringBuilder(10);
		for(int i = 0; i < 10; i++){
			int randomLimitedInt = 97 +
					(int)(random.nextFloat() * (122 - 97 + 1));
			buffer.append((char) randomLimitedInt);
		}

		return buffer.toString();
	}

	private void setSize() {
//		RCDatabase db = RCDatabase.DeserializeFromFile("res/data/screen.bin");
//		if(db != null) {
//			RCObject obj = db.findObject("Resolution");
//			width = obj.findField("width").getInt();
//			height = obj.findField("height").getInt();
//			scale = obj.findField("scale").getInt();
//			panelSize = obj.findField("panelSize").getInt();
//		}

		Dimension size = new Dimension(width*scale + panelSize * scale, height*scale);
		setPreferredSize(size);

		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
	}

	private void saveScreen() {
		RCDatabase db = new RCDatabase("Screen");
		RCObject obj = new RCObject("Resolution");
		obj.addField(RCField.Integer("width", width));
		obj.addField(RCField.Integer("height", height));
		obj.addField(RCField.Integer("scale", scale));
		obj.addField(RCField.Integer("panelSize", panelSize));
		db.addObject(obj);

		db.serializeToFile("res/data/screen.bin");
	}

	private void load() {

	}
	
	/**
	 * Getters för spelfönstrets storlek.
	 */
	public static int getWindowWidth(){
		return width * scale;
	}
	
	public static int getWindowHeight(){
		return height * scale;
	}
		
	public static UIManager getUIManager(){
		return uiManager;
	}
	
	public void addLayer(Layer layer){
		layerStack.add(layer);
	}
	
	/**
	 * Köra trådar synkroniserat så de körs i rätt ordning
	 * och när det ska avslutas
	 */
	public synchronized void start(){
		running = true;
		gameThread = new Thread(this, "Display");
		gameThread.start();
	}
	public synchronized void stop(){
		running = false;
		try{
			gameThread.join();
		} catch (InterruptedException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Run funktionen finns för att implementera tid, timer, uppdateringar för rendering och frames.
	 */
	public void run(){
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0;
		double delta = 0;
		int frames = 0;
		int updates = 0;
		requestFocus();
		while(running){
			long now = System.nanoTime();
			delta += (now-lastTime)/ns;
			lastTime = now;
			while(delta >= 1){
				update();
				updates++;
				delta--;
			}
			render();
			frames++;
			
			if(System.currentTimeMillis()-timer > 1000){
				timer += 1000;
				frame.setTitle(title + "    |    "+updates + " ups, "+ frames + " fps" );
				updates =0;
				frames=0;
			}
		}
		stop();
	}
	
	public void onEvent(Event event){
		for(int i = layerStack.size() - 1; i >= 0;i-- ){
			layerStack.get(i).onEvent(event);
		}
	}
	
	/**
	 * När uppdatering blir kallad så vill vi uppdatera varje tangentbordsnedtryck, min level och min meny.
	 */
	public void update(){
		key.update();
		uiManager.update();
		
		//Update Layer is here!
		for(int i = 0; i < layerStack.size(); i++){
			layerStack.get(i).update();
		}
	}
	
	/**
	 * Här så rendererar vi allt i spelet.
	 */
	public void render(){
		BufferStrategy bs = getBufferStrategy();
		if (bs == null){
			createBufferStrategy(3);
			return;
		}
		screen.clear();
		double xScroll = player.getX() - screen.width /2;
		double yScroll = player.getY() - screen.height /2;
		level.setScroll((int)xScroll, (int)yScroll);
		
		//Render Layers here! Sköter nu alla renderingar
		for(int i = 0; i < layerStack.size(); i++){
			layerStack.get(i).render(screen);
		}
		
		//font.render(50,50, -6,0,"Eat shit\nand die",screen);
		//screen.renderSheet(40, 40, SpriteSheet.player_down, false);
		for (int i =0; i < pixels.length; i++){
			pixels[i]=screen.pixels[i];
		}

		Graphics g = bs.getDrawGraphics();
		g.setColor(new Color(0xff00ff));
		g.drawRect(0, 0, getWidth(), getHeight());
		//g.drawString("test",player.x,player.y);
		g.drawImage(image, 0, 0, width * scale, height * scale, null);
		uiManager.render(g);
		//g.fillRect(Mouse.getX()-32, Mouse.getY()-32, 64, 64);
		//if(Mouse.getButton() != -1) g.drawString("Button: "+Mouse.getButton(), 80, 80);
		g.dispose();
		bs.show();
	}
	
	/**
	 * Main funktionen.
	 */
	public static void main(String[] arg){
		Game game = new Game();
		game.frame.setResizable(false);
		game.frame.setTitle(title);
		game.frame.add(game);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);
		
		game.start();
		
		
	}
	
}
