package st.whineHouse.rain.gx.ui;

import java.awt.Color;
import java.awt.Graphics;

import st.whineHouse.rain.utilities.Vector2i;

/**
 * UIComponent-klass
 * Används för att hålla koll på allt som ritas upp från våran ui-meny på fönstret.
 * Använder vector2i klassen för att beräkna positioner från spelfönstret för att placera komonenterna.
 * 
 * @author Winston Jones
 *
 */
public class UIComponent {
	
	public Vector2i position, size;
	protected Vector2i offset;
	public Color color;
	protected UIPanel panel;
	
	public boolean active = true;
	
	public UIComponent(Vector2i position){
		this.position = position;
		offset = new Vector2i();
	}
	
	public UIComponent(Vector2i position, Vector2i size){
		this.position = position;
		offset = new Vector2i();
		this.size = size;
	}
	
	void init(UIPanel panel){
		this.panel = panel;
	}

	public UIComponent setColor(int color){
		this.color = new Color(color);
		return this;
	}
	
	public void update(){
		
	}
	
	public void render(Graphics g){
		
	}
	
	public Vector2i getAbsolutePosition(){
		return new Vector2i(position).add(offset);
	}
	
	void setOffset(Vector2i offset){
		this.offset = offset;
	}
	
}
