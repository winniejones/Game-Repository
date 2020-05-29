package whineHouse.rain.level.collisionHandling;

import java.util.ArrayList;
import java.util.List;

import st.whineHouse.rain.entity.Entity;

/**
 * Klassen används ej! Ett alternativ till QuadTree.
 * Kollar entitetskollision och för att underlätta processorn och inte kolla kollision över hela kartan.
 * @author winston_8
 *
 */
public class Grid {
	
	// The actual grid array
    private List<Entity>[][] grid;

    // The rows and columns in the grid
    private int rows, cols;

    // The cell size
    private int cellSize;
    
    private List<Entity> retrieveList = new ArrayList<Entity>();


    public Grid ( int mapWidth, int mapHeight, int cellSize)
    {
        this.cellSize = cellSize;
        // Calculate rows and cols
        rows = (mapHeight + cellSize - 1) / cellSize;
        cols = (mapWidth + cellSize - 1) / cellSize;
        // Create the grid
        grid = new ArrayList[cols][rows];
    }

    public void clear()
    {
        for (int x=0; x<cols; x++)
        {
            for (int y=0; y<rows; y++)
            {
                grid[x][y].clear();
            }
        }
    }

    public void addEntity(Entity entity)
    {
    	int topLeftX = Math.max(0,entity.getX() / cellSize);
    	int topLeftY = Math.max(0,entity.getY() / cellSize);
    	int bottomRightX = Math.min(cols-1, (entity.getX() + entity.getWidth() - 1) / cellSize);
    	int bottomRightY = Math.min(rows-1, (entity.getY() + entity.getHeight() - 1) / cellSize);
    	
    	for (int x = topLeftX; x <= bottomRightX; x++)
    	{
    	    for (int y = topLeftY; y <= bottomRightY; y++)
    	    {
    	        grid[x][y].add(entity);
    	    }
    	}
    }

    public List<Entity> retrieve(Entity e)
    {
    	retrieveList.clear();
        // Calculate the positions again
        int topLeftX = Math.max(0, e.getX() / cellSize);
        int topLeftY = Math.max(0, e.getY() / cellSize);
        int bottomRightX = Math.min(cols-1, (e.getX() + e.getWidth() -1) / cellSize);
        int bottomRightY = Math.min(rows-1, (e.getY() + e.getHeight() -1) / cellSize);
        for (int x = topLeftX; x <= bottomRightX; x++)
        {
            for (int y = topLeftY; y <= bottomRightY; y++)
            {
                List<Entity> cell = grid[x][y];
                // Add every entity in the cell to the list
                for (int i=0; i<cell.size(); i++)
                {
                    Entity retrieved = cell.get(i);
                    // Avoid duplicate entries
                    if (!retrieveList.contains(retrieved))
                        retrieveList.add(retrieved);
                }
            }
        }
        return retrieveList;
    }

    public Entity getNearest(Entity e)
    {
    	 // For comparisons
        Entity nearest = null;
        long distance = Long.MAX_VALUE;
        // Retrieve the entities
        List<Entity> collidables = retrieve(e);
        // Iterate and find the nearest
        for (int i=0; i<collidables.size(); i++)
        {
            Entity toCheck = collidables.get(i);
            // Check the distance
            long dist = (toCheck.getX()-e.getX())*(toCheck.getX()-e.getX()) + (toCheck.getY()-e.getY())*(toCheck.getY()-e.getY());
            if (dist < distance)
            {
                nearest = toCheck;
                distance = dist;
            }
        }
        return nearest;
    }
	
}
