package whineHouse.rain.level;
import st.whineHouse.rain.utilities.Vector2i;

/**
 * Node-klassen
 * Används för A* algoritmen för att hålla koll på värdet av varje nod.
 * 
 * @author Winston Jones
 *
 */
public class Node {

	public Vector2i tile;
	public Node parent;
	public double fCost, gCost, hCost;
	
	public Node(Vector2i tile, Node parent, double gCost, double hCost){
		this.tile = tile;
		this.parent = parent;
		this.gCost = gCost;
		this.hCost = hCost;
		this.fCost = this.gCost + this.hCost;
	}
	
}
