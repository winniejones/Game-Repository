package st.whineHouse.rain.entity.spawner;

import st.whineHouse.rain.entity.Entity;
import st.whineHouse.rain.level.Level;

/**
 * Spawner-klassen är en entity klass.
 * Används för att skapa entiteter på anvisad level.
 * 
 * @author Winston Jones
 *
 */
public abstract class Spawner extends Entity{
	
	
	public enum Type {
		MOB, PARTICLE, FLAME;
	}
	
	protected Type type;
	
	public Spawner(int x, int y , Type type, int amount, Level level){
		init(level);
		this.y=y;
		this.x=x;
		this.type = type;
		
	}

}
