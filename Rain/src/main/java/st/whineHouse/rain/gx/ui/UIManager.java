package whineHouse.rain.gx.ui;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

/**
 * UIManager-klass
 * Används som en hanterare av paneler som finns i UI-meny.
 * 
 * @author winston_8
 *
 */
public class UIManager {
	
	private List<UIPanel> panels = new ArrayList<UIPanel>();
	
	
	public UIManager(){
		
	}
	
	public void addPanel(UIPanel panel){
		panels.add(panel);
	}
	
	public void update(){
		for(UIPanel panel : panels){
			panel.update();
		}
	}
	
	public void render(Graphics g){
		for(UIPanel panel : panels){
			panel.render(g);
		}
	}

}
