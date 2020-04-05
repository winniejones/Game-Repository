package st.whineHouse.rain.utilities;

/**
 * Vectorklass för att hålla koll på alla rutor fixt till levelns alla tiles.
 * @author Winston Jones
 */
public class Vector2i {
	
	public int x, y;
	
	public Vector2i(){
		set(0,0);
	}
	
	public Vector2i(Vector2i vector){
		set(vector.x, vector.y);
	}
	
	public Vector2i(double x2, double y2){
		set((int)x2,(int)y2);
	}
	
	public void set(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Kalla på denna funktion för att testa
	 */
	protected void test(){
		Vector2i player_position = new Vector2i(80,40).setX(15);
		Vector2i mob_postion = new Vector2i(player_position).add(player_position);
		
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public Vector2i add(Vector2i vector){
		this.x += vector.x;
		this.y += vector.y;
		return this;
	}
	
	public Vector2i add(int value){
		this.x += value;
		this.y += value;
		return this;
	}
	
	public Vector2i sub(Vector2i vector){
		this.x -= vector.x;
		this.y -= vector.y;
		return this;
	}
	
	public Vector2i setX(int x){
		this.x=x;
		return this;
	}
	public Vector2i setY(int y){
		this.y=y;
		return this;
	}
	
	public static double getDistance(Vector2i v0, Vector2i v1){
		double x = v0.getX() - v1.getX();
		double y = v0.getY() - v1.getY();
		return Math.sqrt(x*x + y*y);
	}
	
	public boolean equals(Object object){
		if(!(object instanceof Vector2i)) return false;
		Vector2i vec = (Vector2i) object;
		if(vec.getX() == this.getX() && vec.getY() == this.getY()) return true;
		return false;
	}
	
	
}
