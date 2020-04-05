package st.whineHouse.rain.utilities;

/**
 * RayCastResult-klassen används för att kolla kollision.
 * Ej implimenterat än.
 *
 * @author Winston Jones
 */
public class RayCastingResult {
	   
	   private boolean collided;
	   private Vector2i position;
	   
	   public boolean hasCollided() {
	      return collided;
	   }
	   public void setCollided(boolean collided) {
	      this.collided = collided;
	   }
	   public Vector2i getPosition() {
	      return position;
	   }
	   public void setPosition(Vector2i position) {
	      this.position = position;
	   }
	   
	}
