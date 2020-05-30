package st.whineHouse.rain.gx.ui;

import java.awt.Color;
import java.awt.Graphics;

import org.w3c.dom.ranges.RangeException;

import st.whineHouse.raincloud.utility.Vector2i;

/**
 * UIProgressBar-klass som är en UIcomponent-klass
 * Används för att registrera status visuelt på vårat spelfönster.
 * Används för tillfället som liv på våran UI-meny på spelfönstret. 
 * Planeras användas framöver till allt där en status-bar krävs.
 * 
 * @author Winston Jones
 *
 */

public class UIProgressBar extends UIComponent{
	private double progress; //0.0 till 1.0
	private Vector2i size;
	private Color foregroundColor;
	
	public UIProgressBar(Vector2i position, Vector2i size) {
		super(position);
		this.size = size;
		foregroundColor = new Color(0xff00ff);
	}
	
	public void setProgress(double progress){
		if(progress < 0.0 || progress > 1.0)
			throw new RangeException(RangeException.BAD_BOUNDARYPOINTS_ERR,"Progress bar måste hålla sig till mellan 0.0 och 1.0");
			
		this.progress = progress;	
	}
	
	public void setForegroundColor(int foregroundColor){
		this.foregroundColor = new Color(foregroundColor);
	}
	
	public double getProgress(){
		return progress;
	}

	@Override
	public void update() {
	}

	@Override
	public void render(Graphics g) {
		g.setColor(color);
		g.fillRect(position.x + offset.x, position.y + offset.y, size.x, size.y);
		
		g.setColor(foregroundColor);
		g.fillRect(position.x + offset.x, position.y + offset.y, (int)(progress *size.x), size.y);
	}
	
	

}
