package st.whineHouse.rain.gx.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import st.whineHouse.rain.utilities.Vector2i;

/**
 * UI-label-klassen är en UIcomponent-klass
 * Används som text på våran UI-meny.
 * 
 * @author Winston Jones
 *
 */
public class UILabel extends UIComponent{
	
	public String text;
	private Font font;
	public boolean dropShadow;
	public int dropShadowOffset = 2;
	
	public UILabel(Vector2i position, String text) {
		super(position);
		font = new Font("Helvetica", Font.PLAIN, 32);
		this.text = text;
		color = new Color(0xff00ff);
	}
	
	public UILabel setFont(Font font){
		this.font = font;
		return this;
	}
	
	public void render(Graphics g){
		if(dropShadow){
			g.setColor(Color.BLACK);
			g.setFont(font);
			g.drawString(text, position.x + offset.x+dropShadowOffset, position.y + offset.y+dropShadowOffset);
		}
		
		g.setColor(color);
		g.setFont(font);
		g.drawString(text, position.x + offset.x, position.y + offset.y);
	}

}
