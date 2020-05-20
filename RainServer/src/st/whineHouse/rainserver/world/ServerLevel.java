package st.whineHouse.rainserver.world;

import st.whineHouse.rain.entity.Entity;
import st.whineHouse.rain.events.Event;
import st.whineHouse.rain.gx.layers.Layer;
import st.whineHouse.rain.level.Node;
import st.whineHouse.rain.level.SpawnLevel;
import st.whineHouse.rain.level.tile.Tile;
import st.whineHouse.rain.utilities.RayCastingResult;
import st.whineHouse.rain.utilities.Vector2i;
import st.whineHouse.raincloud.net.packet.ProjectilePacket;
import st.whineHouse.raincloud.shared.ProjectileType;
import st.whineHouse.rainserver.Rainserver;
import st.whineHouse.rainserver.entity.ServerEntity;
import st.whineHouse.rainserver.entity.ServerMob;
import st.whineHouse.rainserver.projectiles.ServerProjectile;
import st.whineHouse.rainserver.user.ServerPlayer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Level klassen.
 * Här skapas alla listor som håller kolla på entiteter som ska finnas på samma level.
 * Håller även koll på alla rutor(tiles) och spelare.
 * @author Winston Jones
 *
 */
public class ServerLevel {
	protected int width, height;
	protected int[] tilesInt;
	protected int[] tiles;
	protected int tile_size;


	private List<ServerProjectile> projectiles = new ArrayList<>();
	public static List<ServerMob> mobs = new ArrayList<>();
	public static List<ServerPlayer> players = new ArrayList<>();


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
	public static ServerSpawnLevel spawn = new ServerSpawnLevel("/levels/spawnLevelMap.png");
	//public static Level upper =

	public ServerLevel(int width, int height){
		this.width=width;
		this.height = height;
		tilesInt = new int[width*height];

		generateLevel();
	}

	/**
	 * Dessa är till för alla barnklasser till lvl.
	 */
	public ServerLevel(String path){
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
		for( int i = 0; i < projectiles.size(); i++){
			projectiles.get(i).update();
		}
		for( int i = 0; i < players.size(); i++){
			players.get(i).update();
		}
		for( int i = 0; i < mobs.size(); i++){
			mobs.get(i).update();
		}

		remove();
	}

	public void onEvent(Event event){

	}

	/**
	 * Tar bort "döda" entiteter från listor. Projectiler som har skjutits behöver tas bort m.m.
	 */
	private void remove(){
		for (int i = 0; i < projectiles.size(); i++) {
			ServerProjectile p = projectiles.get(i);
			if (p.isRemoved()){
				projectiles.remove(i);
			}
		}
		for( int i = 0; i < players.size(); i++){
			if(players.get(i).isRemoved()){
				players.remove(i);
			}
		}
		for( int i = 0; i < mobs.size(); i++){
			if(mobs.get(i).isRemoved()){
				mobs.remove(i);
			}
		}
	}

	public synchronized void removeInServer() {
		for( int i = 0; i < mobs.size(); i++){
			if(mobs.get(i).isRemoved()) mobs.remove(i);
		}
	}

	public List<ServerMob> getAllMobEnteties(){
		List<ServerMob> allmobs = new ArrayList<>(mobs);
		allmobs.addAll(players);
		return allmobs;
	}


	public List<ServerProjectile> getProjectiles(){
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

	/**
	 * Funktion för att lägga till entiteter på leveln.
	 */
	public synchronized void add(ServerEntity e){
		//e.init(this);
		if(e instanceof ServerProjectile){
			projectiles.add((ServerProjectile)e);
		}
		else if(e instanceof ServerPlayer){
			players.add((ServerPlayer) e);
		}
		else if(e instanceof ServerMob){
			mobs.add((ServerMob) e);
			System.out.println("added " + ((ServerMob)e).getClass().getSimpleName() +" mob on: ("+ ((ServerMob)e).x + ", "+ ((ServerMob)e).y +")");
		}
	}

	public synchronized void addProjectile(ProjectilePacket projectilePacket) {
		ServerProjectile projectile = null;
		try {
			projectile = new ServerProjectile(
					projectilePacket.getX(),
					projectilePacket.getY(),
					projectilePacket.getDir(),
					ProjectileType.getProjectileType(projectilePacket.getProjectileType())
			);
			projectiles.add(projectile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(projectile != null) {

		}
	}

	public synchronized void addProjectile(ServerProjectile projectile) {
		projectiles.add(projectile);
	}

	public synchronized void addPlayer(ServerPlayer player) {
		if(Rainserver.rainserver !=null)
			//player.init(this, Rainserver.rainserver.getServer());

		players.add(player);
	}
	public synchronized void addMob(ServerMob mob) {
		//mob.init(this, Rainserver.rainserver.getServer());
		mobs.add(mob);
		System.out.println("added " + mob.getClass().getSimpleName() +" mob on: ("+ mob.x + ", "+ mob.y +")");
	}
	
	
	/**
	 * Get-listor för att kunna hämta från andra klasser.
	 */
	public List<ServerPlayer> getAllPlayers(){
		return players;
	}
	
	public List<ServerMob> getMobs(){
		return mobs;
	}
	public boolean mobExists(int id){
		return mobs.stream().anyMatch(mob -> mob.getId() == id);
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
	 * Getter för lista med mobs.
	 * Ej implementerad.
	 */
	public List<ServerMob> getMobs(Entity e, int radius){
		List<ServerMob> result = new ArrayList<>();
		int ex = (int)e.getX();
		int ey = (int)e.getY();
		for(int i =0; i< mobs.size(); i++){
			ServerMob mob = mobs.get(i);
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
	public synchronized List<ServerMob> getPlayers(Entity e, int radius){
		List<ServerMob> result = new ArrayList<>();
		int ex = e.getX();
		int ey = e.getY();
		for(int i =0; i< players.size(); i++){
			ServerMob player = players.get(i);
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
			if(player.username.equalsIgnoreCase(username)){
				player.x = x;
				player.y = y;
				((ServerPlayer) player).speed = speed;
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
		players.removeIf(player -> player.username.equalsIgnoreCase(username));
	}

}
