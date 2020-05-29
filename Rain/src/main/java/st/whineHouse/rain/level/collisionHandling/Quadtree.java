package whineHouse.rain.level.collisionHandling;

import java.awt.Rectangle;
import java.util.*;

import st.whineHouse.rain.entity.Entity;
import st.whineHouse.rain.level.SpawnLevel;

/**
 * Denna klass används ej! Under arbete
 * Används för att reducera processor jobb och kolla kollision nära händelsen och inte på hela kartan.
 * @author winston jones
 *
 */
public class Quadtree{
	
	private int MAX_OBJECTS = 10;
	private int MAX_LEVELs = 5;
	
	private int level;
	
// liste på object uttryckta som rektanglar
	private ArrayList<Entity> objects;
	
// lista som hämtar object uttryckta som rektanglar
	private ArrayList<Entity> retrieveList;
	
// rektangel kanter
	private Rectangle bounds;
	
// grenar av trädet
	private Quadtree[] nodes; 
	
	/**
     * Skapar en QuadTree med allmän värde. Kör för hela fönstret.
     */
	public Quadtree() {
        this(0, SpawnLevel.getVisibleRect());
    }
	
	/**
	 * Constructorn tar en int : pLevel för att säga vilken
	 * level är på och en Rectangle : pBounds för varje bounds/corners av denna träd.
	 */
	private Quadtree(int pLevel, Rectangle pBounds) {
		this.level = pLevel;
		this.bounds = pBounds;
		this.objects = new ArrayList<Entity>();
		this.retrieveList = new ArrayList<Entity>();
		nodes = new Quadtree[4];
	}
	
	/**
     * Sätter gränserna för valda träd.
     */
    public void setBounds(int x, int y, int width, int height){
        bounds.x = x;
        bounds.y = y;
        bounds.width = width;
        bounds.height = height;
        clear();
        split();
    }
	
	/**
	 * Tar bort alla barn noder rekursivt. Rensa alla under-träd;
	 */
	public void clear() {
		objects.clear();
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] != null) {
				nodes[i].clear();
				nodes[i] = null;
			}
		}
	}
	
	/**
	 * Delar upp noden ti 4 under-noder.
	 */
	private void split() {
		int subwidth 	= (int) (bounds.getWidth()/2);
		int subHeight 	= (int) (bounds.getHeight()/2);
		int x = (int) bounds.getX();
		int y = (int) bounds.getY();
		
		int subLevel = level + 1;
		nodes[0] = new Quadtree(subLevel, new Rectangle(x+subwidth, y, subwidth, subHeight));
		nodes[1] = new Quadtree(subLevel, new Rectangle(x, y, subwidth, subHeight));
		nodes[2] = new Quadtree(subLevel, new Rectangle(x, y + subHeight, subwidth, subHeight));
		nodes[3] = new Quadtree(subLevel, new Rectangle(x + subwidth, y + subHeight, subwidth, subHeight));
	}
	
	/**
	 * Returnerar noden av objektet som får plats i trädet
	 * 
	 * Returnerar -1 om objektet inte får plats i barnnoden och som är en del av föräldernoden.
	 */
	private int getIndex(Rectangle pRect) {
		int index = -1;
		
		double verticalMidpoint =  (bounds.getX() + bounds.getWidth()/2);
		double horizontalMidpoint =  (bounds.getY() + bounds.getHeight()/2);
		
		boolean topQuadrant = (pRect.getY() < horizontalMidpoint && (pRect.getY() + pRect.getHeight()) < verticalMidpoint);
		boolean bottomQuadrant = (pRect.getY() > horizontalMidpoint);
		
		if ( pRect.getX() < verticalMidpoint && (pRect.getX() + pRect.getWidth()) < verticalMidpoint) {
			if (topQuadrant) {
				index = 1;
			} else if (bottomQuadrant) {
				index = 2;
			}
		} else if (pRect.getX() > verticalMidpoint) {
			if (topQuadrant) {
				index = 0;
			} else if (bottomQuadrant) {
				index = 3;
			}
		}
		return index;
	}
	
	// Hämta index på entitet
    private int getIndex(Entity entity){
        int index = -1;
        double verticalMidpoint = bounds.getX() + (bounds.getWidth() / 2);
        double horizontalMidpoint = bounds.getY() + (bounds.getHeight() / 2);
        boolean topQuadrant = (entity.getY() < horizontalMidpoint && entity.getY() + entity.getHeight() < horizontalMidpoint);
        boolean bottomQuadrant = (entity.getY() > horizontalMidpoint);
        if (entity.getX() < verticalMidpoint && entity.getX() + entity.getWidth() < verticalMidpoint){
            if (topQuadrant){
                index = 1;
            } else if (bottomQuadrant){
                index = 2;
            }
        } else if (entity.getX() > verticalMidpoint){
            if (topQuadrant){
                index = 0;
            } else if (bottomQuadrant){
                index = 3;
            }
        }
        return index;
    }
    
   
	/**
     * Sätt in objekt i trädet
     */
	public void insert(Entity entity) {
		if (nodes[0] != null) {
			int index = getIndex(entity);
			
			if (index != -1) {
				nodes[index].insert(entity);
				
				return;
			}
		}
		objects.add(entity);
		int i = 0;
		while (i < objects.size()) {
			int index = getIndex(objects.get(i));
			if (index != -1) {
				nodes[index].insert(objects.remove(i));
			} else {
				i++;
			}
		}
	}
	
	/**
     * Sätt in arraylista med objekt i en quadtree.
     */
	public void insert(ArrayList<Entity> entity){
        for (int i=0; i<entity.size(); i++){
            insert(entity.get(i));
        }
    }
	
	/**
     * Returnerar lista med kollisionbara entiteter med givna objekt.
     */
	
	public ArrayList<Entity> retrieve(Entity e){
        retrieveList.clear();
        int index = getIndex(e);
        if (index != -1 && nodes[0] != null){
            retrieveList = nodes[index].retrieve(e);
        }
        retrieveList.addAll(objects);
        return retrieveList;
    }
	
	/**
     * Returnerar alla kollisionbara objekt med en given rektangel
     */
	public ArrayList retrieve(ArrayList returnObjects, Rectangle pRect) {
		int index = getIndex(pRect);
		
		if (index != -1 && nodes[0] != null) {
			nodes[index].retrieve(returnObjects, pRect);
		}
		
		returnObjects.addAll(objects);
		
		return returnObjects;
	}

}
