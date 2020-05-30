package st.whineHouse.rain.level;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import st.whineHouse.rain.entity.Entity;
import st.whineHouse.rain.entity.mob.Mob;
import st.whineHouse.rain.entity.mob.player.Player;
import st.whineHouse.rain.entity.particle.Particle;
import st.whineHouse.rain.entity.projectile.NinjaBlade;
import st.whineHouse.rain.entity.projectile.Projectile;
import st.whineHouse.rain.entity.projectile.WizardProjectile;
import st.whineHouse.rain.entity.projectile.WizzardArrow;
import st.whineHouse.rain.events.Event;
import st.whineHouse.rain.gx.Screen;
import st.whineHouse.rain.gx.layers.Layer;
import st.whineHouse.raincloud.tile.Tile;
import st.whineHouse.rain.net.player.NetPlayer;
import st.whineHouse.raincloud.utility.Node;
import st.whineHouse.raincloud.utility.RayCastingResult;
import st.whineHouse.raincloud.utility.Vector2i;
import st.whineHouse.raincloud.net.packet.ProjectilePacket;

/**
 * Level klassen.
 * Här skapas alla listor som håller kolla på entiteter som ska finnas på samma level.
 * Håller även koll på alla rutor(tiles) och spelare.
 * @author Winston Jones
 *
 */
public class Level extends Layer{
	protected int width, height;
	protected int[] tilesInt;
	protected int[] tiles;
	protected int tile_size;

	private int xScroll, yScroll;

	public static boolean USE_PIXELPERFECT_COLLISION = false;
	private List<Entity> entities = new ArrayList<>();
	private List<Projectile> projectiles = new ArrayList<>();
	private List<Particle> particles = new ArrayList<>();
	public static List<Mob> mobs = new ArrayList<>();
	public static List<Mob> players = new ArrayList<>();


	/**
	 * nodeSorter används för A* algoritmen. Den kollar förvarje nod och lägger till i cost,
	 *  för att senare kunna beräkna vilken väg som har minst kostnad för jägaren.
	 */
	private Comparator<Node> nodeSorter = new Comparator<Node>(){
		public int compare(Node n0, Node n1){
			if(n1.fCost < n0.fCost) return +1;
			if(n1.fCost > n0.fCost) return -1;
			return 0;
		}
	};

	/**
	 * Här skapar vi en level klass som har förbestämda attributer.
	 * Laddar in en map som sedan rendereras efter hur färgkoden på mappen ser ut.
	 */
	public static Level spawn = new SpawnLevel("/levels/spawnLevelMap.png");
	//public static Level upper = new SpawnLevel("/levels/spawnLevelMap.png");

	public static int entitySize;
	public Level(int width,int height){
		this.width=width;
		this.height = height;
		tilesInt = new int[width*height];

		generateLevel();
	}

	/**
	 * Dessa är till för alla barnklasser till lvl.
	 */
	public Level(String path){
		loadLevel (path);
		generateLevel();
	}

	protected void loadLevel(String path) {
	}

	protected void generateLevel() {
	}

	/**
	 * Updatering av listor
	 */
	public void update(){
		for( int i = 0; i < entities.size(); i++){
			entities.get(i).update();
		}
		for( int i = 0; i < projectiles.size(); i++){
			projectiles.get(i).update();
		}
		for( int i = 0; i < particles.size(); i++){
			particles.get(i).update();
		}
		for( int i = 0; i < players.size(); i++){
			players.get(i).update();
		}
		for( int i = 0; i < mobs.size(); i++){
			mobs.get(i).update();
		}

		entitySize = allEntetieSize(entities,projectiles,players);
		remove();
	}

	private void removeProjectiles() {
		projectiles.removeIf(Entity::isRemoved);
	}

	public void onEvent(Event event){
			getClientPlayer().onEvent(event);
		}

	/**
	 * Tar bort "döda" entiteter från listor. Projectiler som har skjutits behöver tas bort m.m.
	 */
	private void remove(){
		for( int i = 0; i < entities.size(); i++){
			if(entities.get(i).isRemoved()) entities.remove(i);
		}
		removeProjectiles();
		for( int i = 0; i < particles.size(); i++){
			if(particles.get(i).isRemoved()) particles.remove(i);
		}
		for( int i = 0; i < players.size(); i++){
			if(players.get(i).isRemoved()) players.remove(i);
		}
		for( int i = 0; i < mobs.size(); i++){
			if(mobs.get(i).isRemoved()) mobs.remove(i);
		}
	}

	/**
	 * Lista på alla entiteter
	 */
	private int allEntetieSize(List<Entity> e, List<Projectile> p, List<Mob> player){
		return e.size() + p.size() + player.size();
	}


	public List<Projectile> getProjectiles(){
		return projectiles;
	}

	/**
	 * time() kommer användas senare för att införa ändringar efter tid
	 */
	private void time(){
	}

	/**
	 * Kollisions hanterare.
	 */
	public boolean tileCollision(int x, int y, int size,int xOffset, int yOffset){
		boolean solid = false;
		for(int c = 0; c <4; c++){
			int xt = (x - c % 2 * size + xOffset) >> 4;
			int yt = (y - c / 2 * size + yOffset) >> 4;
			if(getTile(xt,yt).solid()) solid = true;
		}
		return solid;
	}

	public void collideTest(){
		List<Rectangle> mobSquares = new ArrayList<Rectangle>();
		for( int i = 0; i < mobs.size(); i++){
			mobSquares.set(i, mobs.get(i).getBounds());
		}

		if(mobSquares != null){
			for(int y = 0; y < mobs.size();y++){
				for(int x = 1; x < mobs.size(); x++){
					mobSquares.get(y).intersection(mobSquares.get(x));
				}
			}
		}
	}


	public void setScroll(int xScroll, int yScroll){
		this.xScroll = xScroll;
		this.yScroll = yScroll;
	}

	/**
	 * Denna renderfunktion rendererar, efter gubbens position, allting som ska finnas på fönstret.
	 *
	 */
	public void render(Screen screen){
		screen.setOffset(xScroll, yScroll);
		int x0 = xScroll >> 4;  //delar var 16e pixel.
		int x1 = (xScroll + screen.width+16) >> 4 ;
		int y0 = yScroll >> 4;
		int y1 = (yScroll + screen.height+16) >> 4 ;

		for(int y = y0; y < y1 ; y++){
			for(int x = x0; x < x1; x++){
				getTile(x,y).render(x, y, screen); //Vi har render i class Tile för att vi måste "renda" här oavsett, men vi vet bara inte vilken tile.
			}
		}
		for( int i = 0; i < entities.size(); i++){
			entities.get(i).render(screen);
		}
		for( int i = 0; i < projectiles.size(); i++){
			projectiles.get(i).render(screen);
		}
		for( int i = 0; i < particles.size(); i++){
			particles.get(i).render(screen);
		}
		for( int i = 0; i < players.size(); i++){
			players.get(i).render(screen);
		}
		for( int i = 0; i < mobs.size(); i++){
			mobs.get(i).render(screen);
		}
	}

	/**
	 * Funktion för att lägga till entiteter på leveln.
	 */
	public synchronized void add(Entity e){
		e.init(this);
		if(e instanceof Particle){
			particles.add((Particle)e);
		}else if(e instanceof Projectile){
			projectiles.add((Projectile)e);
		}
		else if(e instanceof Player){
			players.add((Player) e);
		}
		else if(e instanceof Mob){
			mobs.add((Mob) e);
			System.out.println("added " + ((Mob)e).getClass().getSimpleName() +" mob on: ("+ ((Mob)e).x + ", "+ ((Mob)e).y +")");
		}else{
			entities.add(e);
		}
	}

	public synchronized void addProjectile(ProjectilePacket projectilePacket) {
		Projectile projectile = null;
		switch (projectilePacket.getProjectileType()){
			case 1:
				projectile = new WizzardArrow(
						projectilePacket.getX(),
						projectilePacket.getY(),
						projectilePacket.getDir()
				);
				break;
			case 2:
				projectile = new NinjaBlade(
						projectilePacket.getX(),
						projectilePacket.getY(),
						projectilePacket.getDir()
				);
				break;
			case 3:
				projectile = new WizardProjectile(
						projectilePacket.getX(),
						projectilePacket.getY(),
						projectilePacket.getDir()
				);
				break;
		}
		if(projectile != null) {
			projectiles.add(projectile);
		}
	}

	public synchronized void addPlayer(Mob player) {
		players.add(player);
	}
	public synchronized void addMob(Mob mob) {
		mobs.add(mob);
		System.out.println("added " + mob.getClass().getSimpleName() +" mob on: ("+ mob.x + ", "+ mob.y +")");
	}
	
	/**
	 * Get-listor för att kunna hämta från andra klasser.
	 */
	public List<Mob> getPlayers(){
		return players;
	}
	
	public List<Mob> getMobs(){
		return mobs;
	}
	public boolean mobExists(int id){
		return mobs.stream().anyMatch(mob -> mob.getId() == id);
	}

	public Player getClientPlayer() {
		return (Player) players.get(0);
	}

/**
 * Hela den här metoden är A* algoritmen. 
 * Den tar hjälp av klassen Node och Vector2i för att beräkna vilken väg som är bäst att ta från en position till en annan.
 */
	
	public List<Node> findPath(Vector2i start, Vector2i goal){								
		List<Node> openList = new ArrayList<Node>();
		List<Node> closedList = new ArrayList<Node>();
		Node current = new Node(start, null, 0, getDistance(start,goal));
		openList.add(current);
		while(openList.size() > 0){
			Collections.sort(openList, nodeSorter);
			current = openList.get(0);
			if(current.tile.equals(goal)){
				List<Node> path = new ArrayList<Node>();
				while(current.parent != null){
					path.add(current);
					current = current.parent;
				}
				openList.clear();
				closedList.clear();
				return path;
			}
			openList.remove(current);
			closedList.add(current);
			for(int i =0; i < 9; i++){
				if(i == 4) continue;
				int x = current.tile.getX();
				int y = current.tile.getY();
				int xi = (i % 3) - 1;
				int yi = (i / 3) - 1;
				Tile at = getTile(x + xi, y + yi);
				if(at == null) continue;
				if(at.solid()) continue;
				Vector2i a = new Vector2i(x+xi,y+yi);
				double gCost = current.gCost + (getDistance(current.tile, a) ==1 ?1 :0.95);
				double hCost = getDistance(a, goal);
				Node node = new Node(a, current, gCost, hCost);
				if(vecInList(closedList, a) && gCost >= node.gCost) continue;
				if(!vecInList(openList, a) || gCost < node.gCost) openList.add(node);
			}
		}
		closedList.clear();
		return null;
	}
	/**
	 * Bresenhams linje algoritm för att returnera pixelkoordinater för varje x, y punkt på en linje
	 */	
	public static List<Vector2i> BresenhamLine(int x1, int y1, int x2, int y2){
		List<Vector2i> result = new ArrayList<>();
		int dy = y2 - y1;
		int dx = x2 - x1;
		int xs, ys;
		
		if(dy < 0) {dy = -dy; ys = -1;} else { ys = 1;}
		if(dx < 0) {dx = -dx; xs = -1;} else { xs = 1;}
		dy <<= 1;
		dx <<= 1;
		
		result.add(new Vector2i(x1, y1));
		if(dx > dy){
			int fraction = dy - (dx >> 1);
			while(x1 != x2){
				if(fraction >= 0){
		            y1 += ys;
		            fraction -= dx;
		         }
		         x1 += xs;
		         fraction += dy;
		         result.add(new Vector2i(x1, y1));
		      }
		   }else{
		      int fraction = dx - (dy >> 1);
		      while(y1 != y2){
		         if(fraction >= 0){
		            x1 += xs;
		            fraction -= dy;
		         }
		         y1 += ys;
		         fraction += dx;
		         result.add(new Vector2i(x1, y1));
		      }
		   }
		   return result;
	}
	
	/**
	 * RayCast metoden intitierar en ny instans av RayCastingResult (Den som håller koll på entitet kollisioner).
	 * Sen kollar den om rayLength är större eller lika med noll, om den är det så kollar den positionen solid och sendan returnerar.
	 * 
	 * Har inte implimenterats än.
	 */
	public RayCastingResult RayCast(Vector2i pos, double angle, float rayLength){
		   RayCastingResult result = new RayCastingResult();
		   result.setCollided(false);
		   if(rayLength <= 0){
		      result.setCollided(this.getTile(pos.getX()>>4, pos.getY()>>4).solid());
		      result.setPosition(pos);
		      return result;
		   }
		   double adjacent = pos.getX()+rayLength*Math.cos(angle);
		   double opposite = pos.getY()+rayLength*Math.sin(angle);
		   List<Vector2i> rayLine = BresenhamLine(pos.getX(), pos.getY(), (int)adjacent, (int)opposite);
		   if(!rayLine.isEmpty()){
		      for(int rayVectorIndex = 0;rayVectorIndex < rayLine.size();rayVectorIndex++){
		         Vector2i rayVector = rayLine.get(rayVectorIndex);
		         if(this.getTile(rayVector.getX()>>4, rayVector.getY()>>4).solid()){
		            result.setPosition(rayVector);
		            result.setCollided(true);
		            break;
		         }
		      }
		   }
		   return result;
	}
	
	/**
	 * Kollar om en vektor finns i nod listan angiven.
	 */
	private boolean vecInList(List<Node> list, Vector2i vector){
		for(Node n : list){
			if(n.tile.equals(vector)) return true;
		}
		return false;
	}
	
	/**
	 * Beräknar längd mellan två vektorer.
	 */
	private double getDistance(Vector2i tile, Vector2i goal){
		double dx = tile.getX() - goal.getX();
		double dy = tile.getY() - goal.getY();
		double distance =Math.sqrt((dx*dx) + (dy*dy));
			return distance;// == 1 ? 1:0.95; 
	}
	
	/**
	 * Getter för att hämta lista på entiteter på leveln.
	 */
	public synchronized List<Entity> getEntities(){
		List<Entity> result = new ArrayList<Entity>();
		for(int i =0; i < entities.size(); i++){
			Entity entity = entities.get(i);
			result.add(entity);
		}
		return result;
	}
	
	/**
	 * Getter för att hämta entiteter inom en radie.
	 * 
	 * Ej implementerad.
	 */
	public synchronized List<Entity> getEntities(Entity e, int radius){
		List<Entity> result = new ArrayList<Entity>();
		int ex = (int)e.getX();
		int ey = (int)e.getY();
		for(int i =0; i < entities.size(); i++){
			Entity entity = entities.get(i);
			if(entity.equals(e)) continue;
			int x = (int)entity.getX();
			int y = (int)entity.getY();
			int dx = Math.abs(x - ex);
			int dy = Math.abs(y - ey);
			double distance = Math.sqrt((dx*dx)+(dy*dy));
			if(distance <= radius) result.add(entity);
		}
		return result;
	}
	
	
	/**
	 * Getter för lista med mobs.
	 * Ej implementerad.
	 */
	public List<Mob> getMobs(Entity e, int radius){
		List<Mob> result = new ArrayList<Mob>();
		int ex = (int)e.getX();
		int ey = (int)e.getY();
		for(int i =0; i< mobs.size(); i++){
			Mob mob = mobs.get(i);
			int x = (int)mob.getX();
			int y = (int)mob.getY();
			int dx = Math.abs(x - ex);
			int dy = Math.abs(y - ey);
			double distance = Math.sqrt((dx*dx)+(dy*dy));
			if(distance <= radius) result.add(mob);
		}
		return result;
	}
	
	/**
	 * Getter för en lista med alla spelare.
	 * 
	 * Ej implementerad.
	 */
	public synchronized List<Mob> getPlayers(Entity e, int radius){
		List<Mob> result = new ArrayList<>();
		int ex = e.getX();
		int ey = e.getY();
		for(int i =0; i< players.size(); i++){
			Mob player = players.get(i);
			int x = player.getX();
			int y = player.getY();
			int dx = Math.abs(x - ex);
			int dy = Math.abs(y - ey);
			double distance = Math.sqrt((dx*dx)+(dy*dy));
			if(distance <= radius) result.add(player);
		}
		return result;
	}
	
	/**
	 * Kollar varje ruta på map för att sedan skicka ut rätt tile-sprite till varje ruta.
	 */
	public Tile getTile(int x, int y){
		if(x<0 || y<0 || x >=width || y >= height) return Tile.voidTile;
		if(tiles[x+y*width]==Tile.col_spawn_floor) return Tile.spawn_floor;
		if(tiles[x+y*width]==Tile.col_spawn_grass) return Tile.spawn_grass;
		if(tiles[x+y*width]==Tile.col_spawn_wall1) return Tile.spawn_wall1;
		if(tiles[x+y*width]==Tile.col_spawn_wall2) return Tile.spawn_wall2;
		if(tiles[x+y*width]==Tile.col_spawn_hedge) return Tile.spawn_hedge;
		if(tiles[x+y*width]==Tile.col_spawn_water) return Tile.spawn_water;
		return Tile.voidTile;
	}

	public synchronized void movePlayer(String username, int x, int y, int speed, boolean walking) {
		players.forEach(player -> {
			if(((NetPlayer) player).getName().equalsIgnoreCase(username)){
				player.x = x;
				player.y = y;
				((Player) player).speed = speed;
				player.walking = walking;
			}
		});
	}

	public synchronized void moveMob(int id, int x, int y, boolean walking) {
		mobs.forEach(mob -> {
			if( mob.getId() == id){
				mob.x = x;
				mob.y = y;
				mob.walking = walking;
			}
		});
	}

	public synchronized void removePlayer(String username) {
		players.removeIf(player -> ((NetPlayer) player).getName().equalsIgnoreCase(username));
	}

}
